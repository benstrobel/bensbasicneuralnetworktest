// Copyright 2019, Benedikt Strobel, All rights reserved.
package bensbasicgameengine.GameLogic;

import bensbasicgameengine.Graphic.GraphicObject;

public abstract class HudObject {

    private int x,y,width,height;
    private boolean enabled = false;
    private GraphicObject graphicObject;

    public HudObject(int x, int y, int width, int height, GraphicObject graphicObject){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.graphicObject = graphicObject;
    }

    public HudObject(int x, int y, GraphicObject graphicObject){
        this.x = x;
        this.y = y;
        this.graphicObject = graphicObject;
    }

    public abstract void activationMethod();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GraphicObject getGraphicObject() {
        return graphicObject;
    }
}
