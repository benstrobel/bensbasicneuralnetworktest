// Copyright 2019, Benedikt Strobel, All rights reserved.
package evolutiontest;

import bensbasicgameengine.GameLogic.Events.HudClickEvent;
import bensbasicgameengine.GameLogic.GameObject;
import bensbasicgameengine.GameLogic.HudObject;
import bensbasicgameengine.GameLogic.Logic;
import bensbasicgameengine.GameLogic.Events.LogicEvent;
import bensbasicgameengine.Graphic.Graphic;
import bensbasicgameengine.Graphic.GraphicShape;
import bensbasicgameengine.Input.KeyListener;
import bensbasicgameengine.Input.MouseMove_Listener;
import bensbasicgameengine.Input.Mouse_Listener;
import bensbasicgameengine.Input.WindowFocusListener;
import bensbasicgameengine.Physic.Physics;
import bensbasicgameengine.Physic.PhysicsObject;
import bensbasicgameengine.Physic.PhysicsRectangle;
import evolutiontest.bensbasicneuralnetwork.EvolutionTest;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Example {

    private Graphic graphic = new Graphic();
    private Physics physics = new Physics();
    //private SoundManager soundManager = new SoundManager();
    private KeyListener keyListener = new KeyListener();
    private Mouse_Listener mouse_listener = new Mouse_Listener();
    private MouseMove_Listener mouseMove_listener = new MouseMove_Listener();
    private WindowFocusListener windowFocusListener = new WindowFocusListener();
    private Logic logic = new Logic(graphic,physics,null,keyListener,mouse_listener,mouseMove_listener);

    private String texturepaths [] = {};
    private BufferedImage textures [];
    private GameObject player;

    HudObject testmenu;

    LongWrapper abort = new LongWrapper();


    public static void main(String[] args) {
        new EvolutionTest(20,30);
    }

    public Example(){
        setupGraphics();
        setupHUD();
        setupPlayer();
        setupEvents();
        setupWindow();
        logic.setShowhitbox(true);
        logic.forcecamfollow(player.getPhysicsObject());
    }

    public long simulate(BoolWrapper [] in, BoolWrapper [] out){
        abort.setState(0);
        LogicEvent hitwall = new WallCollideEvent(player,logic,abort);
        logic.registerLogicEvent(hitwall);
        LogicEvent inputEvent = new InputEvent(out[0],out[1],player);
        logic.registerLogicEvent(inputEvent);
        setupSensors(in);
        while(abort.getState() == 0){
            logic.tick();
        }
        reset(in);
        return abort.getState();
    }

    public void reset(BoolWrapper [] in){
        logic.reset();
        setupPlayer();
        setupSensors(in);
        setupEvents();
        logic.forcecamfollow(player.getPhysicsObject());
    }

    private void setupWindow(){
        JFrame frame = new JFrame("Bens Basic Evolution Testchamber");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800,800);
        frame.add(graphic.getPanel());
        frame.addKeyListener(keyListener);
        frame.setResizable(false);
        frame.addWindowFocusListener(windowFocusListener);
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        graphic.setFrame(frame);
    }

    private void setupEvents(){
        LogicEvent keyEvent = new KeyEvent(keyListener,player,graphic,testmenu);
        logic.registerLogicEvent(keyEvent);
        LogicEvent velocityEvent = new VelocityEvent(player);
        logic.registerLogicEvent(velocityEvent);
        LogicEvent windowFocusEvent = new WindowFocusEvent(windowFocusListener,keyListener);
        logic.registerLogicEvent(windowFocusEvent);
        LogicEvent hudclick0 = new HudClickEvent(testmenu,mouse_listener);
        logic.registerLogicEvent(hudclick0);

    }

    private void setupPlayer(){
        PhysicsObject playerrectangle = new PhysicsRectangle(new Point2D.Double(100,800), 1, 60, 80);
        player = new GameObject(logic.getNextID(),playerrectangle,Color.BLUE,true);
        playerrectangle.setParent(player);
        player.setGraphiclayerid(0);
        player.rotate(270);
        player.setFlag("player");
        PhysicsObject goalrectangle = new PhysicsRectangle(new Point2D.Double(930,30), 1, 300, 100);
        GameObject goal = new GameObject(logic.getNextID(),goalrectangle,Color.YELLOW,true);
        goal.registerLogicEvent(new HitGoal(goal,abort));
        goalrectangle.setParent(goal);
        goal.setGraphiclayerid(0);
        goal.setFlag("goal");
        logic.addGameObject(goal);
        logic.addGameObject(player);
        logic.addWall(0,0,1000,30);
        logic.addWall(0,970, 30, 1000);
        logic.addWall(0,0,30,1000);
        logic.addWall(300,300,700,30);
        logic.addWall(600,0,700,30);
        logic.addWall(900,300,700,30);
    }

    private void setupHUD(){
        testmenu = new HudObject(300,300,300,100, new GraphicShape(new Rectangle2D.Double(300,300,300,100), Color.black, true, 0, true)) {
            @Override
            public void activationMethod() {
                System.out.println("Hud Object Clicked");
            }
        };
        logic.addHudObject(testmenu);
    }

    private void setupSensors(BoolWrapper [] in){
        GameObject [] fl = new GameObject[5], f = new GameObject[5], fr = new GameObject[5];
        for(int i = 0; i < 5; i++){
            PhysicsObject obj = new PhysicsRectangle(new Point2D.Double(0,0),1, 40, 20);
            fl [i] = new GameObject(logic.getNextID(),obj,null);
            obj.setParent(fl[i]);
            fl[i].registerLogicEvent(new SensorEvent(fl[i],player,in[i]));
            logic.addGameObject(fl[i]);
        }
        for(int i = 0; i < 5; i++){
            PhysicsObject obj = new PhysicsRectangle(new Point2D.Double(0,0),1, 40, 20);
            f [i] = new GameObject(logic.getNextID(),obj,null);
            obj.setParent(f[i]);
            fl[i].registerLogicEvent(new SensorEvent(fl[i],player,in[5+i]));
            logic.addGameObject(f[i]);
        }
        for(int i = 0; i < 5; i++){
            PhysicsObject obj = new PhysicsRectangle(new Point2D.Double(0,0),1, 40, 20);
            fr [i] = new GameObject(logic.getNextID(),obj,null);
            obj.setParent(fr[i]);
            fl[i].registerLogicEvent(new SensorEvent(fl[i],player,in[10+i]));
            logic.addGameObject(fr[i]);
        }
        LogicEvent movesensor = new SensorMoveEvent(player,fl,f,fr);
        logic.registerLogicEvent(movesensor);
    }

    private void setupGraphics(){
        textures = new BufferedImage[texturepaths.length];
        URL toload;
        for(int i = 0; i < texturepaths.length; i++){
            toload = this.getClass().getResource(texturepaths[i]);
            if(toload != null){
                try {
                    textures [i] = ImageIO.read(toload);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logic.addGraphicLayer();
    }
}
