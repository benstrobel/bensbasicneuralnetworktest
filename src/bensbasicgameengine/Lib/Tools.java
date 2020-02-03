// Copyright 2019, Benedikt Strobel, All rights reserved.

package bensbasicgameengine.Lib;

import bensbasicgameengine.Physic.PhysicsObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class Tools {

    public static int getDistance(Point2D a, Point2D b){
        int distance = 0;
        distance += differance(a.getX(),b.getX());
        distance += differance(a.getY(),b.getY());
        return distance;
    }

    public static Point2D getVectorFromAngle(double orientation){
        double rad = Math.toRadians(orientation);
        return new Point2D.Double(Math.cos(rad),Math.sin(rad));
    }

    public static void threadsleep(long ms){
        if(ms < 1){return;}
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Point2D calculateDirection(Point2D from, Point2D to, int vectorlength){
        return mulVector(normVector(new Point2D.Double(to.getX()-from.getX(),to.getY()-from.getY())),vectorlength);    //Get vector on length 5
    }

    public static Point2D calculateDirection(PhysicsObject from, PhysicsObject to, int vectorlength){
        Point2D source = getMiddle(from);
        Point2D target = (Point2D) to.getPosition().clone();
        return  calculateDirection(source,target,vectorlength);
    }

    public static Point2D getMiddle(PhysicsObject from){
        Point2D source = (Point2D) from.getPosition().clone();
        source.setLocation(source.getX()+from.getShape().getBounds2D().getWidth()/2,source.getY()+from.getShape().getBounds2D().getHeight()/2);
        return source;
    }

    public static Point2D getRealMiddle(PhysicsObject from){
        return from.getCenterPosition();
    }

    public static double getDegree(Point2D m){
        double x = m.getX(), y = m.getY();
        double d = Math.abs(Math.toDegrees(Math.atan(m.getX()/m.getY())));
        if(x >= 0){
            //left
            if(y >= 0){
                //lower
                return 180-d;
            }else{
                //upper
                return d;
            }
        }else{
            //right
            if(y >= 0){
                //lower
                return 180+d;
            }else{
                //upper
                return 360-d;
            }
        }
    }

    public static Point2D addVector(Point2D v1, Point2D v2){
        return new Point2D.Double(v1.getX()+v2.getX(),v1.getY()+v2.getY());
    }

    public static Point2D mulVector(Point2D vector, double factor){
        return new Point2D.Double(vector.getX()*factor,vector.getY()*factor);
    }

    public static Point2D normVector(Point2D vector){
        double factor = 1/Math.sqrt(Math.pow(vector.getX(),2)+Math.pow(vector.getY(),2));
        return new Point2D.Double(vector.getX()*factor,vector.getY()*factor);
    }

    public static int differance(int a, int b){
        if(a > b){
            return Math.abs(a-b);
        }else{
            return Math.abs(b-a);
        }
    }

    public static double differance(double a, double b){
        if(a > b){
            return Math.abs(a-b);
        }else{
            return Math.abs(b-a);
        }
    }

    public static Shape rotateShape(double angle, Shape shape){
        AffineTransform t = new AffineTransform();
        t.rotate(Math.toRadians(angle),shape.getBounds2D().getX()+shape.getBounds2D().getWidth()/2,shape.getBounds2D().getY() + shape.getBounds2D().getHeight()/2);
        Path2D.Double p = (Path2D.Double) t.createTransformedShape(shape);
        return p;
    }
}
