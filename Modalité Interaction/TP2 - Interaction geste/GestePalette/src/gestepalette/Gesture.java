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
    
    private static int N = 64;
    private static float SquareSize = (float) 250.0;
    
    public Gesture() {
        points = new ArrayList<>();
        step1points = new ArrayList<>();
        step2points = new ArrayList<>();
        step3points = new ArrayList<>();
        step4points = new ArrayList<>();
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
    
}
