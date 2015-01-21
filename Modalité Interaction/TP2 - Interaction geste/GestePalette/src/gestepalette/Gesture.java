/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestepalette;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author ladoucar
 */
public class Gesture {
    
    private ArrayList<Point> points;
    private ArrayList<Point> step1points;
    private ArrayList<Point> step2points;
    private ArrayList<Point> step3points;
    private ArrayList<Point> step4points;
    
    private ArrayList<Point> circle;
    private ArrayList<Point> rectangle;
    private ArrayList<Point> triangle;
    
    private static int N = 64;
    private static float SquareSize = (float) 250.0;
    
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

    public ArrayList<Point> getPoints() {
        return points;
    }

    public ArrayList<Point> getStep1points() {
        return step1points;
    }

    public ArrayList<Point> getStep2points() {
        return step2points;
    }

    public ArrayList<Point> getStep3points() {
        return step3points;
    }

    public ArrayList<Point> getStep4points() {
        return step4points;
    }
    
    
    
    public void collectNewPoint(Point p) {
        points.add(p);
    }

    void finishCollection() {
        step1points.clear();
        step2points.clear();
        step3points.clear();
        step4points.clear();
        applyStep1();
        applyStep2();
        applyStep3();
        applyStep4();
        
        forme = "circle";
        int r = 0;
        int tmp = 0;
        
        System.out.println("SIZE = " + step1points.size() + " - " + step2points.size() + " - " + step3points.size() + " - " + step4points.size());
        for (int i=0; i<N; ++i) {
            Point p1 = circle.get(i);
            Point p2 = step4points.get(i);
            r += p1.distance(p2);
        }
        for (int i=0; i<N; ++i) {
            tmp += rectangle.get(i).distance(step4points.get(i));
        }
        if (tmp < r) {
            forme = "rectangle";
            r = tmp;

        }
        System.out.println("distance : " + r);
        /*tmp = 0;
        for (int i=0; i<N; ++i) {
            tmp += triangle.get(i).distance(step4points.get(i));
        }*/
        /*if (tmp < r) {
            forme = "triangle";
            r = tmp;
            System.out.println("pourcent : " + r);
        }*/
    }
    
    public float getdistance(Point p1, Point p2) {
        return (float) Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y));
    }

    private void applyStep1() {
        
        float dt = 0;
        for (int i=0; i<points.size()-1; i++) {
            dt += getdistance(points.get(i), points.get(i+1));
        }
        float I = (float) (dt / ( (float)N -1.0 ));
        
        Stack<Point> stack = new Stack();
        for( int i=0; i < points.size(); i++)
        {
          stack.push( points.get(points.size()-1-i));
        }      
        
        float D = (float) 0.0;
        while( !stack.empty())
        {
            Point pt1 = (Point) stack.pop();
            if( stack.empty())
            {
              step1points.add(pt1);
              continue;
            }
            Point pt2 = (Point)stack.peek(); 
            float d = (float) pt1.distance(pt2);
            if( (D + d) >= I)
            {
               float qx = pt1.x + (( I - D ) / d ) * (pt2.x - pt1.x);
               float qy = pt1.y + (( I - D ) / d ) * (pt2.y - pt1.y);
               Point q = new Point((int)qx, (int)qy);
               step1points.add(q);
               stack.push( q );
               D = (float) 0.0;
            } else {
              D += d;
            }
        }
        if( step1points.size() == (N -1) )
        {
          step1points.add(points.get(points.size()-1));
        }
    }

    private void applyStep2() {
        Point c = Centroid(step1points);
        float theta = (float) Math.atan2( c.y - step1points.get(0).y, c.x - step1points.get(0).x);
        RotateBy( c, -theta);
    }

    private void applyStep3() {
        Rectangle B = BoundingBox();
        System.out.println("Rectangle : " + B.height + " / " + B.width + "/");
        for(Point p : step2points)
        {
           float qx = (float) (p.x * (SquareSize / B.getWidth()));
           float qy = (float) (p.y * (SquareSize / B.getHeight()));
           step3points.add(new Point((int)qx, (int)qy));
        }
    }

    private void applyStep4() {
        Point c = Centroid(step3points);
        System.out.println("Step4 : centre = " + c.x + " : " + c.y);
        for(Point p : step3points)
        {
          float qx = p.x - c.x + 150;
          float qy = p.y - c.y + 150;
          step4points.add(new Point((int)qx, (int)qy));
        }
    }

    private Point Centroid(ArrayList<Point> p) {
        Point centriod = new Point(0, 0);
        for( int i = 1; i < p.size(); i++)
        {
            centriod.x += p.get(i).x;
            centriod.y += p.get(i).y;
        }
        centriod.x /= p.size();
        centriod.y /= p.size();
        return centriod;
    }

    private void RotateBy(Point c, float f) {
        float Cos = (float) Math.cos( f );
        float Sin = (float) Math.sin( f );

        for(Point p : step1points)
        {
          float qx = (p.x - c.x) * Cos - (p.y - c.y) * Sin + c.x;
          float qy = (p.x - c.x) * Sin + (p.y - c.y) * Cos + c.y;
          step2points.add(new Point((int)qx, (int)qy));
        }
    }

    private Rectangle BoundingBox() {
        float minX = (float) 1e9;
        float maxX = (float) -1e9;
        float minY = (float) 1e9;
        float maxY = (float) -1e9;

        for(Point p : step2points)
        {
          minX = Math.min( p.x, minX);
            maxX = Math.max( p.x, maxX);
            minY = Math.min( p.y, minY);
            maxY = Math.max( p.y, maxY);
        }
        return new Rectangle((int) minX, (int)minY,(int)(maxX - minX), (int)(maxY - minY));
    }

    private void initShapeModel() {
        circle = new ArrayList<>();
        rectangle = new ArrayList<>();
        triangle = new ArrayList<>();
        
        circle.add( new Point(30, 158)); circle.add( new Point(26, 170)); circle.add( new Point(28, 183)); circle.add( new Point(32, 194)); circle.add( new Point(35, 207)); circle.add( new Point(39, 220)); circle.add( new Point(47, 231)); circle.add( new Point(55, 242)); circle.add( new Point(65, 251)); circle.add( new Point(75, 259)); circle.add( new Point(87, 265)); circle.add( new Point(98, 269)); circle.add( new Point(111, 273)); circle.add( new Point(123, 276)); circle.add( new Point(135, 275)); circle.add( new Point(148, 276)); circle.add( new Point(160, 278)); circle.add( new Point(172, 275)); circle.add( new Point(184, 273)); circle.add( new Point(195, 271)); circle.add( new Point(206, 265)); circle.add( new Point(215, 258)); circle.add( new Point(227, 251)); circle.add( new Point(237, 242)); circle.add( new Point(245, 234)); circle.add( new Point(253, 224)); circle.add( new Point(263, 215)); circle.add( new Point(266, 205)); circle.add( new Point(272, 193)); circle.add( new Point(275, 183)); circle.add( new Point(276, 171)); circle.add( new Point(275, 159)); circle.add( new Point(274, 148)); circle.add( new Point(272, 137)); circle.add( new Point(273, 125)); circle.add( new Point(271, 113)); circle.add( new Point(265, 103)); circle.add( new Point(259, 96)); circle.add( new Point(251, 87)); circle.add( new Point(242, 79)); circle.add( new Point(234, 70)); circle.add( new Point(224, 62)); circle.add( new Point(216, 53)); circle.add( new Point(206, 48)); circle.add( new Point(198, 39)); circle.add( new Point(186, 35)); circle.add( new Point(176, 30)); circle.add( new Point(164, 28)); circle.add( new Point(153, 28)); circle.add( new Point(140, 28)); circle.add( new Point(127, 31)); circle.add( new Point(117, 32)); circle.add( new Point(106, 38)); circle.add( new Point(95, 42)); circle.add( new Point(86, 48)); circle.add( new Point(76, 56)); circle.add( new Point(67, 63)); circle.add( new Point(60, 75)); circle.add( new Point(55, 86)); circle.add( new Point(47, 96)); circle.add( new Point(40, 105)); circle.add( new Point(35, 117)); circle.add( new Point(30, 130)); circle.add( new Point(29, 141)); circle.add( new Point(30, 143)); 
        rectangle.add(new Point(28,155));rectangle.add(new Point(37,164));rectangle.add(new Point(44,173));rectangle.add(new Point(52,182));rectangle.add(new Point(58,191));rectangle.add(new Point(65,200));rectangle.add(new Point(73,209));rectangle.add(new Point(81,218));rectangle.add(new Point(88,227));rectangle.add(new Point(96,236));rectangle.add(new Point(103,245));rectangle.add(new Point(112,254));rectangle.add(new Point(119,263));rectangle.add(new Point(127,272));rectangle.add(new Point(134,278));rectangle.add(new Point(142,272));rectangle.add(new Point(151,264));rectangle.add(new Point(160,258));rectangle.add(new Point(169,251));rectangle.add(new Point(178,244));rectangle.add(new Point(186,237));rectangle.add(new Point(195,229));rectangle.add(new Point(203,223));rectangle.add(new Point(212,215));rectangle.add(new Point(220,209));rectangle.add(new Point(229,202));rectangle.add(new Point(238,195));rectangle.add(new Point(247,188));rectangle.add(new Point(256,180));rectangle.add(new Point(264,174));rectangle.add(new Point(273,166));rectangle.add(new Point(273,158));rectangle.add(new Point(268,149));rectangle.add(new Point(262,139));rectangle.add(new Point(254,131));rectangle.add(new Point(247,123));rectangle.add(new Point(240,115));rectangle.add(new Point(232,106));rectangle.add(new Point(226,98));rectangle.add(new Point(218,89));rectangle.add(new Point(211,81));rectangle.add(new Point(205,71));rectangle.add(new Point(197,63));rectangle.add(new Point(191,55));rectangle.add(new Point(186,47));rectangle.add(new Point(178,38));rectangle.add(new Point(172,29));rectangle.add(new Point(163,31));rectangle.add(new Point(154,40));rectangle.add(new Point(145,45));rectangle.add(new Point(135,50));rectangle.add(new Point(125,58));rectangle.add(new Point(116,65));rectangle.add(new Point(107,73));rectangle.add(new Point(97,79));rectangle.add(new Point(87,86));rectangle.add(new Point(78,93));rectangle.add(new Point(69,101));rectangle.add(new Point(59,108));rectangle.add(new Point(52,116));rectangle.add(new Point(43,124));rectangle.add(new Point(35,132));rectangle.add(new Point(26,138));rectangle.add(new Point(24,141));
        triangle.add(new Point(15,150));triangle.add(new Point(23,156));triangle.add(new Point(33,161));triangle.add(new Point(42,167));triangle.add(new Point(52,172));triangle.add(new Point(62,176));triangle.add(new Point(72,183));triangle.add(new Point(84,185));triangle.add(new Point(94,188));triangle.add(new Point(104,192));triangle.add(new Point(114,199));triangle.add(new Point(122,205));triangle.add(new Point(132,212));triangle.add(new Point(142,219));triangle.add(new Point(152,225));triangle.add(new Point(161,232));triangle.add(new Point(171,239));triangle.add(new Point(183,241));triangle.add(new Point(193,246));triangle.add(new Point(203,252));triangle.add(new Point(213,257));triangle.add(new Point(221,263));triangle.add(new Point(231,270));triangle.add(new Point(239,275));triangle.add(new Point(249,279));triangle.add(new Point(257,281));triangle.add(new Point(257,266));triangle.add(new Point(253,252));triangle.add(new Point(253,237));triangle.add(new Point(251,221));triangle.add(new Point(247,208));triangle.add(new Point(245,192));triangle.add(new Point(245,176));triangle.add(new Point(243,161));triangle.add(new Point(241,145));triangle.add(new Point(239,132));triangle.add(new Point(237,116));triangle.add(new Point(233,103));triangle.add(new Point(233,87));triangle.add(new Point(231,71));triangle.add(new Point(227,58));triangle.add(new Point(225,45));triangle.add(new Point(223,31));triangle.add(new Point(213,33));triangle.add(new Point(201,40));triangle.add(new Point(191,47));triangle.add(new Point(181,54));triangle.add(new Point(171,60));triangle.add(new Point(159,67));triangle.add(new Point(150,74));triangle.add(new Point(138,78));triangle.add(new Point(126,85));triangle.add(new Point(114,89));triangle.add(new Point(104,96));triangle.add(new Point(90,98));triangle.add(new Point(80,105));triangle.add(new Point(68,109));triangle.add(new Point(58,114));triangle.add(new Point(50,121));triangle.add(new Point(40,125));triangle.add(new Point(29,129));triangle.add(new Point(19,136));triangle.add(new Point(9,141));triangle.add(new Point(7,147));
    }
    
}
