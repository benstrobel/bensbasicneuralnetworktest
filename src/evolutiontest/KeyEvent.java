// Copyright 2019, Benedikt Strobel, All rights reserved.
package evolutiontest;

import bensbasicgameengine.GameLogic.GameObject;
import bensbasicgameengine.GameLogic.Events.LogicEvent;
import bensbasicgameengine.GameLogic.HudObject;
import bensbasicgameengine.Graphic.Graphic;
import bensbasicgameengine.Input.KeyListener;

import java.awt.geom.Point2D;

public class KeyEvent extends LogicEvent {

    KeyListener keyListener;
    GameObject player;
    Graphic graphic;
    HudObject testmenu;

    public KeyEvent(KeyListener keyListener, GameObject player, Graphic graphic, HudObject testmenu){
        this.keyListener = keyListener;
        this.player = player;
        this.graphic = graphic;
        this.testmenu = testmenu;
    }

    @Override
    public void eventmethod() {
        boolean keys [] = keyListener.getKeysAndReset();
        if(keys[KeyListener.W]){
            if(keys[KeyListener.S]){
                player.getPhysicsObject().setVelocityY(0);
            }else{
                player.getPhysicsObject().setVelocityY(-5);
            }
        }else {
            if(keys[KeyListener.S]){
                player.getPhysicsObject().setVelocityY(5);
            }else{
                player.getPhysicsObject().setVelocityY(0);
            }
        }
        if(keys[KeyListener.A]){
            if(keys[KeyListener.D]){
                player.getPhysicsObject().setVelocityX(0);
            }else{
                player.getPhysicsObject().setVelocityX(-5);
            }
        }else{
            if(keys[KeyListener.D]){
                player.getPhysicsObject().setVelocityX(5);
            }else{
                player.getPhysicsObject().setVelocityX(0);
            }
        }
        if(keys[KeyListener.Q]){
            if(!keys[KeyListener.E]){
                player.rotate(player.getOrientation()-5);
            }
        }else{
            if(keys[KeyListener.E]){
                player.rotate(player.getOrientation()+5);
            }
        }
        if(keys[KeyListener.LEFT]){
            if(!keys[KeyListener.RIGHT]){
                Point2D g = Graphic.getCameralocation();
                graphic.setCameralocation(new Point2D.Double(g.getX()+5,g.getY()));
            }
        }
        if(keys[KeyListener.RIGHT]){
            if(!keys[KeyListener.LEFT]){
                Point2D g = Graphic.getCameralocation();
                graphic.setCameralocation(new Point2D.Double(g.getX()-5,g.getY()));
            }
        }
        if(keys[KeyListener.UP]){
            if(!keys[KeyListener.DOWN]){
                Point2D g = Graphic.getCameralocation();
                graphic.setCameralocation(new Point2D.Double(g.getX(),g.getY()+5));
            }
        }
        if(keys[KeyListener.DOWN]){
            if(!keys[KeyListener.UP]){
                Point2D g = Graphic.getCameralocation();
                graphic.setCameralocation(new Point2D.Double(g.getX(),g.getY()-5));
            }
        }
        if(keys[KeyListener.ESC]){
            testmenu.setEnabled(!testmenu.isEnabled());
        }
    }

    @Override
    public boolean eventstate() {
        return keyListener.isKeyupdate();
    }

    @Override
    public String getTransmissionData() {
        return "-";
    }
}
