/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestepalette;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author ladoucar
 */
public class Gesture {

    public final static String SHAPERECTANGLE = "shaperectangle";
    public final static String SHAPECIRCLE = "shapecircle";

    private ArrayList<PointGeste> points;
    private ArrayList<PointGeste> step1points;
    private ArrayList<PointGeste> step2points;
    private ArrayList<PointGeste> step3points;
    private ArrayList<PointGeste> step4points;

    public ArrayList<PointGeste> circle;
    public ArrayList<PointGeste> rectangle;

    private static int N = 64;
    private static double SquareSize = (double) 250.0;

    public String forme;

    public Gesture() {
        forme = "";
        points = new ArrayList<>();
        step1points = new ArrayList<>();
        step2points = new ArrayList<>();
        step3points = new ArrayList<>();
        step4points = new ArrayList<>();

        initShapeModel();
    }

    public ArrayList<PointGeste> getPoints() {
        return points;
    }

    public ArrayList<PointGeste> getStep1points() {
        return step1points;
    }

    public ArrayList<PointGeste> getStep2points() {
        return step2points;
    }

    public ArrayList<PointGeste> getStep3points() {
        return step3points;
    }

    public ArrayList<PointGeste> getStep4points() {
        return step4points;
    }

    public void collectNewPoint(PointGeste p) {
        points.add(p);
    }

    void finishCollection() {
        if (points.size() == 0) {
            return;
        }
        
        step1points.clear();
        step2points.clear();
        step3points.clear();
        step4points.clear();

        applyStep1();
        applyStep2();
        applyStep3();
        applyStep4();

        forme = "";
        int distC = 0;
        int distR = 0;
        int lenght = Math.min(N, step4points.size());
        System.out.println("SIZE = " + points.size() + " | " + step1points.size() + " - " + step2points.size() + " - " + step3points.size() + " - " + step4points.size());
        for (int i = 0; i < lenght; ++i) {
            PointGeste pc = circle.get(i);
            PointGeste pr = rectangle.get(i);
            PointGeste p2 = step4points.get(i);
            distC += pc.distance(p2);
            distR += pr.distance(p2);
        }
        System.out.println("Distance : c=" + distC + " / r=" + distR);
        if (distC < distR && distC < 5000) {
            forme = SHAPECIRCLE;
        } else if (distR < distC && distR < 5000) {
            forme = SHAPERECTANGLE;
        }

        if (forme == "") {
            distC = 0;
            distR = 0;
            for (int i = 0; i < lenght; ++i) {
                PointGeste pc = circle.get(i);
                PointGeste pr = rectangle.get(i);
                PointGeste p2 = step4points.get(step4points.size()-1-i);
                distC += pc.distance(p2);
                distR += pr.distance(p2);
            }
            System.out.println("Distance : c=" + distC + " / r=" + distR);
            if (distC < distR && distC < 5000) {
                forme = SHAPECIRCLE;
            } else if (distR < distC && distR < 5000) {
                forme = SHAPERECTANGLE;
            }
        }

    }

    public double getdistance(PointGeste p1, PointGeste p2) {
        return (double) Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
    }

    private void applyStep1() {

        double dt = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            dt += getdistance(points.get(i), points.get(i + 1));
        }
        double I = (double) (dt / ((double) N - 1.0));
        //int I = (int) (dt / ( N -1 ));

        Stack<PointGeste> stack = new Stack();
        for (int i = 0; i < points.size(); i++) {
            stack.push(points.get(points.size() - 1 - i));
        }

        double D = (double) 0.0;
        while (!stack.empty()) {
            PointGeste pt1 = (PointGeste) stack.pop();
            if (stack.empty()) {
                step1points.add(pt1);
                continue;
            }
            PointGeste pt2 = (PointGeste) stack.peek();
            double d = (double) pt1.distance(pt2);
            if ((D + d) >= I) {
                double qx = (double) (pt1.getX() + ((I - D) / d) * (pt2.getX() - pt1.getX()));
                double qy = (double) (pt1.getY() + ((I - D) / d) * (pt2.getY() - pt1.getY()));
                PointGeste q = new PointGeste((double) qx, (double) qy) {
                };
                step1points.add(q);
                stack.push(q);
                D = (double) 0.0;
            } else {
                D += d;
            }
        }
        if (step1points.size() == (N - 1)) {
            step1points.add(points.get(points.size() - 1));
        }
    }

    private void applyStep2() {
        PointGeste c = Centroid(step1points);
        double theta = (double) Math.atan2(c.y - step1points.get(0).y, c.x - step1points.get(0).x);
        RotateBy(c, -theta);
    }

    private void applyStep3() {
        Rectangle B = BoundingBox();
        System.out.println("Rectangle : " + B.height + " / " + B.width + "/");
        for (PointGeste p : step2points) {
            double qx = (double) (p.x * (SquareSize / B.getWidth()));
            double qy = (double) (p.y * (SquareSize / B.getHeight()));
            step3points.add(new PointGeste((int) qx, (int) qy));
        }
    }

    private void applyStep4() {
        PointGeste c = Centroid(step3points);
        System.out.println("Step4 : centre = " + c.x + " : " + c.y);
        for (PointGeste p : step3points) {
            double qx = (double) (p.x - c.x + 150);
            double qy = (double) (p.y - c.y + 150);
            step4points.add(new PointGeste((int) qx, (int) qy));
        }
    }

    private PointGeste Centroid(ArrayList<PointGeste> p) {
        PointGeste centriod = new PointGeste(0, 0);
        for (int i = 1; i < p.size(); i++) {
            centriod.x += p.get(i).x;
            centriod.y += p.get(i).y;
        }
        centriod.x /= p.size();
        centriod.y /= p.size();
        return centriod;
    }

    private void RotateBy(PointGeste c, double f) {
        double Cos = (double) Math.cos(f);
        double Sin = (double) Math.sin(f);

        for (PointGeste p : step1points) {
            double qx = (double) ((p.x - c.x) * Cos - (p.y - c.y) * Sin + c.x);
            double qy = (double) ((p.x - c.x) * Sin + (p.y - c.y) * Cos + c.y);
            step2points.add(new PointGeste((int) qx, (int) qy));
        }
    }

    private Rectangle BoundingBox() {
        double minX = (double) 1e9;
        double maxX = (double) -1e9;
        double minY = (double) 1e9;
        double maxY = (double) -1e9;
        for (PointGeste p : step2points) {
            minX = (double) Math.min(p.x, minX);
            maxX = (double) Math.max(p.x, maxX);
            minY = (double) Math.min(p.y, minY);
            maxY = (double) Math.max(p.y, maxY);
        }
        return new Rectangle((int) minX, (int) minY, (int) (maxX - minX), (int) (maxY - minY));
    }

    private void initShapeModel() {
        circle = new ArrayList<>();
        rectangle = new ArrayList<>();

        circle.add(new PointGeste(30, 158));
        circle.add(new PointGeste(26, 170));
        circle.add(new PointGeste(28, 183));
        circle.add(new PointGeste(32, 194));
        circle.add(new PointGeste(35, 207));
        circle.add(new PointGeste(39, 220));
        circle.add(new PointGeste(47, 231));
        circle.add(new PointGeste(55, 242));
        circle.add(new PointGeste(65, 251));
        circle.add(new PointGeste(75, 259));
        circle.add(new PointGeste(87, 265));
        circle.add(new PointGeste(98, 269));
        circle.add(new PointGeste(111, 273));
        circle.add(new PointGeste(123, 276));
        circle.add(new PointGeste(135, 275));
        circle.add(new PointGeste(148, 276));
        circle.add(new PointGeste(160, 278));
        circle.add(new PointGeste(172, 275));
        circle.add(new PointGeste(184, 273));
        circle.add(new PointGeste(195, 271));
        circle.add(new PointGeste(206, 265));
        circle.add(new PointGeste(215, 258));
        circle.add(new PointGeste(227, 251));
        circle.add(new PointGeste(237, 242));
        circle.add(new PointGeste(245, 234));
        circle.add(new PointGeste(253, 224));
        circle.add(new PointGeste(263, 215));
        circle.add(new PointGeste(266, 205));
        circle.add(new PointGeste(272, 193));
        circle.add(new PointGeste(275, 183));
        circle.add(new PointGeste(276, 171));
        circle.add(new PointGeste(275, 159));
        circle.add(new PointGeste(274, 148));
        circle.add(new PointGeste(272, 137));
        circle.add(new PointGeste(273, 125));
        circle.add(new PointGeste(271, 113));
        circle.add(new PointGeste(265, 103));
        circle.add(new PointGeste(259, 96));
        circle.add(new PointGeste(251, 87));
        circle.add(new PointGeste(242, 79));
        circle.add(new PointGeste(234, 70));
        circle.add(new PointGeste(224, 62));
        circle.add(new PointGeste(216, 53));
        circle.add(new PointGeste(206, 48));
        circle.add(new PointGeste(198, 39));
        circle.add(new PointGeste(186, 35));
        circle.add(new PointGeste(176, 30));
        circle.add(new PointGeste(164, 28));
        circle.add(new PointGeste(153, 28));
        circle.add(new PointGeste(140, 28));
        circle.add(new PointGeste(127, 31));
        circle.add(new PointGeste(117, 32));
        circle.add(new PointGeste(106, 38));
        circle.add(new PointGeste(95, 42));
        circle.add(new PointGeste(86, 48));
        circle.add(new PointGeste(76, 56));
        circle.add(new PointGeste(67, 63));
        circle.add(new PointGeste(60, 75));
        circle.add(new PointGeste(55, 86));
        circle.add(new PointGeste(47, 96));
        circle.add(new PointGeste(40, 105));
        circle.add(new PointGeste(35, 117));
        circle.add(new PointGeste(30, 130));
        circle.add(new PointGeste(29, 141));
        circle.add(new PointGeste(30, 143));
        rectangle.add(new PointGeste(28, 155));
        rectangle.add(new PointGeste(37, 164));
        rectangle.add(new PointGeste(44, 173));
        rectangle.add(new PointGeste(52, 182));
        rectangle.add(new PointGeste(58, 191));
        rectangle.add(new PointGeste(65, 200));
        rectangle.add(new PointGeste(73, 209));
        rectangle.add(new PointGeste(81, 218));
        rectangle.add(new PointGeste(88, 227));
        rectangle.add(new PointGeste(96, 236));
        rectangle.add(new PointGeste(103, 245));
        rectangle.add(new PointGeste(112, 254));
        rectangle.add(new PointGeste(119, 263));
        rectangle.add(new PointGeste(127, 272));
        rectangle.add(new PointGeste(134, 278));
        rectangle.add(new PointGeste(142, 272));
        rectangle.add(new PointGeste(151, 264));
        rectangle.add(new PointGeste(160, 258));
        rectangle.add(new PointGeste(169, 251));
        rectangle.add(new PointGeste(178, 244));
        rectangle.add(new PointGeste(186, 237));
        rectangle.add(new PointGeste(195, 229));
        rectangle.add(new PointGeste(203, 223));
        rectangle.add(new PointGeste(212, 215));
        rectangle.add(new PointGeste(220, 209));
        rectangle.add(new PointGeste(229, 202));
        rectangle.add(new PointGeste(238, 195));
        rectangle.add(new PointGeste(247, 188));
        rectangle.add(new PointGeste(256, 180));
        rectangle.add(new PointGeste(264, 174));
        rectangle.add(new PointGeste(273, 166));
        rectangle.add(new PointGeste(273, 158));
        rectangle.add(new PointGeste(268, 149));
        rectangle.add(new PointGeste(262, 139));
        rectangle.add(new PointGeste(254, 131));
        rectangle.add(new PointGeste(247, 123));
        rectangle.add(new PointGeste(240, 115));
        rectangle.add(new PointGeste(232, 106));
        rectangle.add(new PointGeste(226, 98));
        rectangle.add(new PointGeste(218, 89));
        rectangle.add(new PointGeste(211, 81));
        rectangle.add(new PointGeste(205, 71));
        rectangle.add(new PointGeste(197, 63));
        rectangle.add(new PointGeste(191, 55));
        rectangle.add(new PointGeste(186, 47));
        rectangle.add(new PointGeste(178, 38));
        rectangle.add(new PointGeste(172, 29));
        rectangle.add(new PointGeste(163, 31));
        rectangle.add(new PointGeste(154, 40));
        rectangle.add(new PointGeste(145, 45));
        rectangle.add(new PointGeste(135, 50));
        rectangle.add(new PointGeste(125, 58));
        rectangle.add(new PointGeste(116, 65));
        rectangle.add(new PointGeste(107, 73));
        rectangle.add(new PointGeste(97, 79));
        rectangle.add(new PointGeste(87, 86));
        rectangle.add(new PointGeste(78, 93));
        rectangle.add(new PointGeste(69, 101));
        rectangle.add(new PointGeste(59, 108));
        rectangle.add(new PointGeste(52, 116));
        rectangle.add(new PointGeste(43, 124));
        rectangle.add(new PointGeste(35, 132));
        rectangle.add(new PointGeste(26, 138));
        rectangle.add(new PointGeste(24, 141));

    }

}
