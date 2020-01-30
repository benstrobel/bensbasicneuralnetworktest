// Copyright 2019, Benedikt Strobel, All rights reserved.

package bensbasicgameengine.GameLogic;

import bensbasicgameengine.GameLogic.Events.CollisionDeleteEvent;
import bensbasicgameengine.GameLogic.Events.LogicEvent;
import bensbasicgameengine.Graphic.Graphic;
import bensbasicgameengine.Graphic.GraphicShape;
import bensbasicgameengine.Input.KeyListener;
import bensbasicgameengine.Input.MouseMove_Listener;
import bensbasicgameengine.Input.Mouse_Listener;
import bensbasicgameengine.Lib.Tools;
import bensbasicgameengine.Physic.Physics;
import bensbasicgameengine.Physic.PhysicsObject;
import bensbasicgameengine.Physic.PhysicsRectangle;
import bensbasicgameengine.Sound.SoundManager;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class Logic {

    private Graphic graphic;
    private Physics physics;
    private SoundManager soundManager;
    private KeyListener keyListener;
    private Mouse_Listener mouse_listener;
    private MouseMove_Listener mouseMove_listener;


    private long tickcounter = 0;
    private final int tickspersecond = 60;
    private final int waittime = 1000/tickspersecond; //in ms
    private boolean run = true, showhitbox = false, pause = false;
    private int graphiclayers = -1;
    private PhysicsObject camfollowobject;
    private Point2D camlocation = new Point2D.Double(0,0);
    private int currentid = 0;

    private ArrayList<LogicEvent> logicEvents;
    private ArrayList<GameObject> gameObjects;
    private ArrayList<HudObject> hudObjects;
    private ArrayList<PhysicsObject> triggerObjects;

    public Logic(Graphic graphic, Physics physics, SoundManager soundManager, KeyListener keyListener, Mouse_Listener mouse_listener, MouseMove_Listener mouseMove_listener){
        this.graphic = graphic;
        graphic.setCameralocation(camlocation);
        this.physics = physics;
        this.soundManager = soundManager;
        this.keyListener = keyListener;
        this.mouse_listener = mouse_listener;
        this.mouseMove_listener = mouseMove_listener;
        logicEvents = new ArrayList<>();
        gameObjects = new ArrayList<>();
        hudObjects = new ArrayList<>();
        triggerObjects = new ArrayList<>();
        if(mouse_listener != null){
            graphic.getPanel().addMouseListener(mouse_listener);
        }
        if(mouseMove_listener != null){
            graphic.getPanel().addMouseMotionListener(mouseMove_listener);
        }
    }

    public void reset(){
        run = false;
        logicEvents.clear();
        gameObjects.clear();
        triggerObjects.clear();
    }

    public int getNextID(){
        return currentid++;
    }

    public void tick(){
        if(graphic != null){graphic.repaint();}
        if(soundManager != null){soundManager.tick();}
        if(!pause){
            logictick();
        }
        graphictick();
    }

    public void setPause(boolean pause){
        this.pause = pause;
    }

    public boolean isPause() {
        return pause;
    }

    public void startloop(){
        run = true;
        loop();
    }

    private void logictick(){
        handleGlobalEvents();
        handleLocalEventsAndCollectGarbage();
        physics.tick();
        if(camfollowobject != null){
            Dimension framedim = graphic.getFramedimension();
            camlocation.setLocation(camfollowobject.getCenterPosition().getX()-framedim.width/2,camfollowobject.getCenterPosition().getY()-framedim.height/2);
            graphic.setCameralocation(new Point2D.Double(-camfollowobject.getCenterPosition().getX()+framedim.width/2,-camfollowobject.getCenterPosition().getY()+framedim.height/2));
        }
        mouse_listener.resetAll();
        tickcounter++;
    }

    private void handleLocalEventsAndCollectGarbage(){
        synchronized (gameObjects){
            for(Iterator i = gameObjects.iterator(); i.hasNext();){
                GameObject gameObject = (GameObject) i.next();
                gameObject.tick();
                if(gameObject.isGarbage()){
                    physics.removeObject(gameObject.getPhysicsObject());
                    i.remove();
                }
            }
        }
    }

    private void graphictick(){
        synchronized (graphic.getObjectlist()){
            graphic.clear();
            addgameObjects();
            addhudObjects();
        }
        graphic.repaint();
    }

    private void addgameObjects(){
        synchronized (gameObjects){
            synchronized (graphic.getObjectlist()){
                for(GameObject gameObject : gameObjects){
                    if(gameObject.getGraphicObject() != null && gameObject.getGraphiclayerid() <= graphiclayers){
                        graphic.add(gameObject.getGraphiclayerid(), gameObject.getGraphicObject());
                    }
                }
            }
        }
    }

    public void addhudObjects(){
        synchronized (gameObjects){
            if(showhitbox){
                synchronized (graphic.getObjectlist()){
                    for(GameObject gameObject : gameObjects){
                        Color c = null;
                        if(gameObject.getPhysicsObject().iscolliding()){c = Color.RED;}else{c = Color.GREEN;}
                        PhysicsObject physicsObject = gameObject.getPhysicsObject();
                        if(physicsObject instanceof PhysicsRectangle){
                            graphic.add(graphiclayers,new GraphicShape(((PhysicsRectangle)physicsObject).getunrotatedShape(), c, gameObject.isFill(),physicsObject.getOrientation()));
                        }else{
                            graphic.add(graphiclayers,new GraphicShape(physicsObject.getShape(), c, gameObject.isFill(),physicsObject.getOrientation()));
                        }
                    }
                }
            }
        }
        synchronized (hudObjects){
            synchronized (graphic.getObjectlist()){
                for(HudObject hudObject : hudObjects){
                    if(hudObject.isEnabled()){
                        graphic.add(graphiclayers,hudObject.getGraphicObject());
                    }
                }
            }
        }
    }

    public void addGraphicLayer(){
        graphiclayers = graphic.addList();
    }

    private void handleGlobalEvents(){
        synchronized (logicEvents){
            for(Iterator<LogicEvent> it = logicEvents.iterator(); it.hasNext();){
                LogicEvent event = it.next();
                if(event.isRemoveFlag()){it.remove();continue;}
                if(event.eventstate()){
                    event.eventmethod();
                }
            }
        }
    }

    private void loop(){
        long time;
        long timepassed;
        while(run){
            time = System.currentTimeMillis();
            tick();
            timepassed = System.currentTimeMillis() - time;
            if(waittime < timepassed){System.out.println("Can't keep up, timediff: " + (waittime-timepassed));}
            Tools.threadsleep(waittime-timepassed);
        }
    }

    public void registerLogicEvent(LogicEvent event){
        synchronized (logicEvents){
            logicEvents.add(event);
        }
    }

    public void removeLogicEvent(LogicEvent event){
        synchronized (logicEvents){
            logicEvents.remove(event);
        }
    }

    public long getTickcounter() {
        return tickcounter;
    }

    public void addGameObject(GameObject gameObject){
        synchronized (gameObjects){
            gameObjects.add(gameObject);
            physics.addObject(gameObject.getPhysicsObject());
        }
    }

    public void removeGameObject(GameObject gameObject){
        synchronized (gameObjects){
            gameObjects.remove(gameObject);
            physics.removeObject(gameObject.getPhysicsObject());
        }
    }

    public void addHudObject(HudObject hudObject){
        hudObjects.add(hudObject);
    }

    public void removeHudObject(HudObject hudObject){
        hudObjects.remove(hudObject);
    }

    public void setGraphiclayers(int graphiclayers){
        this.graphiclayers = graphiclayers;
    }

    public boolean isshowinghitboxes(){
        return showhitbox;
    }

    public void setShowhitbox(boolean showhitbox){
        this.showhitbox = showhitbox;
    }

    public GameObject createGameObject(PhysicsObject physicsObject, BufferedImage bufferedImage){
        GameObject gameObject = new GameObject(getNextID(),physicsObject,bufferedImage);
        physicsObject.setParent(gameObject);
        return gameObject;
    }

    public GameObject createGameObject(PhysicsObject physicsObject, Color color, boolean fill){
        GameObject gameObject = new GameObject(getNextID(),physicsObject,color,fill);
        physicsObject.setParent(gameObject);
        return gameObject;
    }

    public void addDeadZone(double x, double y, int height, int width){
        PhysicsObject deadzonerect = new PhysicsRectangle(new Point2D.Double(x,y), 1, height, width);
        deadzonerect.setUnmoveable(true);
        GameObject deadzone = createGameObject(deadzonerect,null);
        deadzone.registerLogicEvent(new CollisionDeleteEvent(deadzone));
        deadzone.setFill(true);
        deadzone.setFlag("deadzone");
        addGameObject(deadzone);
    }

    public void addWall(double x, double y, int height, int width){
        PhysicsObject wallrect = new PhysicsRectangle(new Point2D.Double(x,y),1, height,width);
        wallrect.setUnmoveable(true);
        GameObject wall = createGameObject(wallrect,Color.GRAY, true);
        //wall.registerLogicEvent(new CollisionBlockMovementEvent(wall));
        wallrect.setSolid(true);
        wall.setFlag("wall");
        addGameObject(wall);
    }

    public void forcecamfollow(PhysicsObject camcenterpos){
        this.camfollowobject = camcenterpos;
    }

    public Point2D getCamlocation(){
        return camlocation;
    }
}
