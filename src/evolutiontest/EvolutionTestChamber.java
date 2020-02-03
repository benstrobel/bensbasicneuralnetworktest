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
import bensbasicgameengine.Lib.Tools;
import bensbasicgameengine.Physic.Physics;
import bensbasicgameengine.Physic.PhysicsObject;
import bensbasicgameengine.Physic.PhysicsRectangle;
import evolutiontest.bensbasicneuralnetwork.Evolution;
import evolutiontest.bensbasicneuralnetwork.Network;
import evolutiontest.customevents.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class EvolutionTestChamber {

    private Graphic graphic = new Graphic();
    private Physics physics = new Physics();
    private KeyListener keyListener = new KeyListener();
    private Mouse_Listener mouse_listener = new Mouse_Listener();
    private MouseMove_Listener mouseMove_listener = new MouseMove_Listener();
    private WindowFocusListener windowFocusListener = new WindowFocusListener();
    private Logic logic = new Logic(graphic,physics,null,keyListener,mouse_listener,mouseMove_listener);

    private String texturepaths [] = {};
    private BufferedImage textures [];
    private GameObject player;

    HudObject testmenu;
    Random random = new Random();
    Point2D abort = new Point2D.Double(-1,-1);


    public static void main(String[] args) {
        new Evolution(20,30);
    }

    public EvolutionTestChamber(){
        setupGraphics();
        setupHUD();
        setupPlayer();
        setupEvents();
        setupWindow();
        //logic.setShowhitbox(true);
        logic.forcecamfollow(player.getPhysicsObject());
    }

    private void createStep(int x, int y, int height, int width, BoolWrapper boolWrapper){
        PhysicsRectangle r0 = new PhysicsRectangle(new Point2D.Double(x,y),1, height, width);
        GameObject g0 = new GameObject(logic.getNextID(),r0,Color.YELLOW,true);
        r0.setParent(g0);
        g0.registerLogicEvent(new StepEvent(g0,boolWrapper));
        logic.addGameObject(g0);
    }

    public long simulate(BoolWrapper [] in, BoolWrapper [] out, Network network){
        abort.setLocation(-1,-1);
        BoolWrapper [] steps = new BoolWrapper[10];
        BoolWrapper goalbool = new BoolWrapper();
        Point2D [] points = new Point2D[7];
        for(int i = 0; i < steps.length; i++){
            steps [i] = new BoolWrapper();
        }
        //------------------Steps-----------------------
        createStep(0,300,30,300,steps[0]);
        createStep(300,0,300,30,steps[1]);
        createStep(300,300,30,300,steps[2]);
        createStep(300,670,30,300,steps[3]);
        createStep(600,670,300,30,steps[4]);
        createStep(600,670,30,300,steps[5]);
        createStep(600,300,30,300,steps[6]);
        //-----------------Points-----------------------
        points[0] = new Point2D.Double(150,315);
        points[1] = new Point2D.Double(315,150);
        points[2] = new Point2D.Double(450,315);
        points[3] = new Point2D.Double(315,785);
        points[4] = new Point2D.Double(615,820);
        points[5] = new Point2D.Double(750,685);
        points[6] = new Point2D.Double(750,315);
        //------------------End of Setup-----------------------
        LogicEvent hitwall = new WallCollideEvent(player,logic,abort);
        logic.registerLogicEvent(hitwall);
        LogicEvent inputEvent = new InputEvent(out[0],out[1],player);
        logic.registerLogicEvent(inputEvent);
        PhysicsObject goalrectangle = new PhysicsRectangle(new Point2D.Double(930,30), 1, 300, 100);
        GameObject goal = new GameObject(logic.getNextID(),goalrectangle,Color.YELLOW,true);
        goal.registerLogicEvent(new HitGoalEvent(goal,goalbool));
        goalrectangle.setParent(goal);
        goal.setGraphiclayerid(0);
        goal.setFlag("goal");
        logic.addGameObject(goal);
        setupSensors(in);
        boolean realtime = false;
        //if(random.nextInt(100) < 1){realtime = true;}else{realtime = false;}
        while(abort.getX() == -1 && abort.getY() == -1 && !goalbool.getState()) {
            if (logic.getTickcounter() % 3 == 0) {
                double[] values = new double[3];
                values[0] = 100;
                values[1] = 100;
                values[2] = 100;
                if (in[0].getState()) {
                    values[0] = 1;
                }
                if (in[1].getState()) {
                    values[0] = 2;
                }
                if (in[2].getState()) {
                    values[0] = 3;
                }
                if (in[3].getState()) {
                    values[0] = 4;
                }
                if (in[4].getState()) {
                    values[0] = 5;
                }
                if (in[5].getState()) {
                    values[1] = 1;
                }
                if (in[6].getState()) {
                    values[1] = 2;
                }
                if (in[7].getState()) {
                    values[1] = 3;
                }
                if (in[8].getState()) {
                    values[1] = 4;
                }
                if (in[9].getState()) {
                    values[1] = 5;
                }
                if (in[10].getState()) {
                    values[2] = 1;
                }
                if (in[11].getState()) {
                    values[2] = 2;
                }
                if (in[12].getState()) {
                    values[2] = 3;
                }
                if (in[13].getState()) {
                    values[2] = 4;
                }
                if (in[14].getState()) {
                    values[2] = 5;
                }
                double[] output = network.simulate(values);
                //System.out.println("In: " + values[0] + " " + values[1] + " " + values[2] + " Out: " + output[0]);
                if (output[0] > 0) {
                    out[0].setState(true);
                    out[1].setState(false);
                } else if (output[0] == 0){
                    out[0].setState(false);
                    out[1].setState(false);
                }else {
                    out[0].setState(false);
                    out[1].setState(true);
                }
                /*if (output[1] >= 0) {
                    out[1].setState(true);
                } else {
                    out[1].setState(false);
                }*/
            }else{
                out[0].setState(false);
                out[1].setState(false);
            }


            if (realtime) {
                logic.tick();
                Tools.threadsleep(10);
            } else {
                if(keyListener.getKeysAndReset()[KeyListener.SPACE]){
                    logic.clearhudObjects();
                    paintNetwork(network);
                    logic.tick();
                    Tools.threadsleep(10);
                }else{
                    logic.clearhudObjects();
                    logic.tick();
                    //logic.tickphyonly();
                }
            }
        }
        long stepsum = 0;
        long distancetopoint = 0;
        for(BoolWrapper bw : steps){
            if(bw.getState()){stepsum += 1000000;}
        }
        if(goalbool.getState()){stepsum = Long.MAX_VALUE;}
        distancetopoint = Tools.getDistance(points[0],abort);
        if(steps[0].getState()){distancetopoint = Tools.getDistance(points[1],abort);}
        if(steps[1].getState()){distancetopoint = Tools.getDistance(points[2],abort);}
        if(steps[2].getState()){distancetopoint = Tools.getDistance(points[3],abort);}
        if(steps[3].getState()){distancetopoint = Tools.getDistance(points[4],abort);}
        if(steps[4].getState()){distancetopoint = Tools.getDistance(points[5],abort);}
        if(steps[5].getState()){distancetopoint = Tools.getDistance(points[6],abort);}
        reset(in);
        return stepsum-distancetopoint;
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
        player.rotate(260);
        player.setFlag("player");
        logic.addGameObject(player);
        logic.addWall(0,0,1000,30);
        logic.addWall(0,970, 30, 1000);
        logic.addWall(0,0,30,1000);
        logic.addWall(300,300,700,30);
        logic.addWall(600,0,700,30);
        logic.addWall(900,300,700,30);
    }

    private void paintNetwork(Network network){
        for(int i = 0; i < network.getInputlayer().getNeurons().size(); i++){
            Color c = null;
            if(network.getInputlayer().getNeurons().get(i).getValue() >= 0){c = Color.GREEN;}else{c = Color.RED;}
            HudObject hudobj = new HudObject(50,50+(i*50),25,25, new GraphicShape(new Ellipse2D.Double(50,50+(i*50),25,25), c, true, 0, true)) {
                @Override
                public void activationMethod() {
                }
            };
            hudobj.setEnabled(true);
            logic.addHudObject(hudobj);
        }
        for(int x = 0; x < network.getHiddenlayers().size(); x++){
            for(int i = 0; i < network.getHiddenlayers().get(x).getNeurons().size(); i++){
                Color c = null;
                if(network.getHiddenlayers().get(x).getNeurons().get(i).getValue() >= 0){c = Color.GREEN;}else{c = Color.RED;}
                HudObject hudobj = new HudObject(50+((x+1)*60),50+(i*50),25,25, new GraphicShape(new Ellipse2D.Double(50+((x+1)*60),50+(i*50),25,25), c, true, 0, true)) {
                    @Override
                    public void activationMethod() {
                    }
                };
                hudobj.setEnabled(true);
                logic.addHudObject(hudobj);
            }
        }
        for(int i = 0; i < network.getOutputlayer().getNeurons().size(); i++){
            Color c = null;
            if(network.getOutputlayer().getNeurons().get(i).getValue() >= 0){c = Color.GREEN;}else{c = Color.RED;}
            HudObject hudobj = new HudObject(50+((network.getHiddenlayers().size()+1)*60),50+(i*50),25,25, new GraphicShape(new Ellipse2D.Double(50+((network.getHiddenlayers().size()+1)*60),50+(i*50),25,25), c, true, 0, true)) {
                @Override
                public void activationMethod() {
                }
            };
            hudobj.setEnabled(true);
            logic.addHudObject(hudobj);
        }
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
            f[i].registerLogicEvent(new SensorEvent(f[i],player,in[5+i]));
            logic.addGameObject(f[i]);
        }
        for(int i = 0; i < 5; i++){
            PhysicsObject obj = new PhysicsRectangle(new Point2D.Double(0,0),1, 40, 20);
            fr [i] = new GameObject(logic.getNextID(),obj,null);
            obj.setParent(fr[i]);
            fr[i].registerLogicEvent(new SensorEvent(fr[i],player,in[10+i]));
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
