// Copyright 2019, Benedikt Strobel, All rights reserved.

package bensbasicgameengine.Physic;

import java.awt.*;
import java.awt.geom.*;

public class PhysicsRectangle extends PhysicsObject{
    private int height,width;

    public PhysicsRectangle(Point2D position, double mass, int height, int width)
    {
        super(position, mass);
        this.height = height;
        this.width = width;
        updateShape();
    }

    public PhysicsRectangle(Point2D position, double mass, int height, int width, int textureid)
    {
        super(position, mass);
        this.height = height;
        this.width = width;
        this.textureid = textureid;
        updateShape();
    }

    @Override
    public String getTransmissionData(char delimiter){
        return super.getTransmissionData(delimiter) + delimiter + "r" + delimiter + width + delimiter + height;
    }

    public void updateShape(){
        setShape(new Rectangle2D.Double(position.getX(), position.getY(), width, height));
    }

    @Override
    public Shape getShape()
    {
        return rotate(getOrientation());
    }

    @Override
    public Point2D getCenterPosition() {
        Point2D p = (Point2D) getPosition().clone();
        return new Point2D.Double(p.getX()+width/2,p.getY()+height/2);
    }

    public Shape getunrotatedShape(){return shape;}

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Shape rotate(double angle){
        AffineTransform t = new AffineTransform();
        t.rotate(Math.toRadians(angle),shape.getBounds2D().getX()+shape.getBounds2D().getWidth()/2,shape.getBounds2D().getY() + shape.getBounds2D().getHeight()/2);
        Path2D.Double p = (Path2D.Double) t.createTransformedShape(shape);
        return p;
    }

    @Override
    public boolean detectCollision(PhysicsObject object) {
        Line2D line = new Line2D.Double(0,0,10,10);
        return detectCollisionGeneral(this,object);
        //if(object instanceof PhysicsCircle){return PhysicsObject.detectCollision((PhysicsCircle)object, this);}
        //if(object instanceof PhysicsRectangle){return PhysicsObject.detectCollision(this, (PhysicsRectangle) object);}
        //return false;
    }
}