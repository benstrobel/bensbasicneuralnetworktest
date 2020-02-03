// Copyright 2019, Benedikt Strobel, All rights reserved.

package bensbasicgameengine.Graphic;

import bensbasicgameengine.Physic.PhysicsObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import static bensbasicgameengine.Lib.Tools.differance;

public class GraphicImage extends GraphicObject {

    private BufferedImage image;
    private int x,y;
    private int xoffset, yoffset;
    private double orientation;
    boolean centered = false;
    boolean subimage = false;
    private PhysicsObject physicsObject;
    public GraphicImage(BufferedImage image, Point2D position)
    {
        this.image = image;
        xoffset = image.getWidth()/2;
        yoffset = image.getHeight()/2;
        this.x = (int)position.getX();
        this.y = (int)position.getY();
    }

    public GraphicImage(BufferedImage image, Point2D position, boolean centered)
    {
        this.image = image;
        xoffset = image.getWidth()/2;
        yoffset = image.getHeight()/2;
        this.x = (int)position.getX();
        this.y = (int)position.getY();
        this.centered = centered;
    }

    public GraphicImage(BufferedImage image, PhysicsObject physicsObject)
    {
        this.image = image;
        xoffset = differance(image.getWidth(),(int)(physicsObject.getOriginalWidth()))/2;
        yoffset = differance(image.getHeight(),(int)(physicsObject.getOriginalHeight()))/2;
        x = (int)physicsObject.getPosition().getX();
        y = (int)physicsObject.getPosition().getY();
        this.centered = true;
        this.physicsObject = physicsObject;
        x += Graphic.getCameralocation().getX();
        y += Graphic.getCameralocation().getY();
        return;
    }

    public GraphicImage(BufferedImage image, PhysicsObject physicsObject, boolean isStatic)
    {
        this.image = image;
        xoffset = differance(image.getWidth(),(int)(physicsObject.getOriginalWidth()))/2;
        yoffset = differance(image.getHeight(),(int)(physicsObject.getOriginalHeight()))/2;
        x = (int)physicsObject.getPosition().getX();
        y = (int)physicsObject.getPosition().getY();
        this.centered = true;
        this.physicsObject = physicsObject;
        if(!isStatic){
            x += Graphic.getCameralocation().getX();
            y += Graphic.getCameralocation().getY();
        }
        return;
    }

    public GraphicImage(BufferedImage image, Point2D position, float alpha)
    {
        this.image = image;
        xoffset = image.getWidth()/2;
        yoffset = image.getHeight()/2;
        this.x = (int)position.getX();
        this.y = (int)position.getY();
        this.alpha = alpha;
    }

    public void setSubimage(boolean subimage) {
        this.subimage = subimage;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }

    @Override
    public void paint(Graphics2D g2d) {
        if(getAlpha() != 1.0F){
            g2d.setComposite(AlphaComposite.SrcOver.derive(getAlpha()));
        }
        if(centered){
            if(subimage){
                g2d.drawImage(orientate(image.getSubimage(0,0,image.getWidth(),image.getHeight())), x-xoffset, y-yoffset, null);
            }else{
                g2d.drawImage(orientate(image), x-xoffset, y-yoffset, null);
            }
        }else{
            if(subimage){
                g2d.drawImage(orientate(image.getSubimage(0,0,image.getWidth(),image.getHeight())), x, y,null);
            }else{
                g2d.drawImage(orientate(image), x, y,null);
            }
        }
        g2d.setComposite(AlphaComposite.SrcOver.derive(1.0F));
    }

    private BufferedImage orientate(BufferedImage image){
        AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(orientation), image.getWidth()/2,image.getHeight()/2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image,null);
    }

    public static BufferedImage resize(BufferedImage image, double factor){
        AffineTransform tx = AffineTransform.getScaleInstance(factor,factor);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image,null);
    }
}