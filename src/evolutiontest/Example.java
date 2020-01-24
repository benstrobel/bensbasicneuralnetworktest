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
import evolutiontest.bensbasicneuralnetwork.Network;

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

    private String texturepaths [] = {"dude.png"};
    private BufferedImage textures [];
    private GameObject player;

    HudObject testmenu;


    public static void main(String[] args) {
        new Example();
    }

    public Example(){
        Network network = new Network(5,2,3);
        double inputlayervalues [] = {1,1,1,1,1};
        Network networkcloned = network.cloneNetwork();
        network.simulateandoutput(inputlayervalues);
        networkcloned.simulateandoutput(inputlayervalues);
        /*setupGraphics();
        setupHUD();
        setupPlayer();
        setupDeadZones();
        setupEvents();
        setupWindow();
        logic.setShowhitbox(true);
        logic.forcecamfollow(player.getPhysicsObject());
        logic.startloop();*/
    }

    private void setupWindow(){
        JFrame frame = new JFrame("Bens Basic Game Engine - Example");
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
        LogicEvent mouseEvent = new MouseEvent(mouse_listener,logic,player,logic.getCamlocation());
        logic.registerLogicEvent(mouseEvent);
        LogicEvent windowFocusEvent = new WindowFocusEvent(windowFocusListener,keyListener);
        logic.registerLogicEvent(windowFocusEvent);
        LogicEvent hudclick0 = new HudClickEvent(testmenu,mouse_listener);
        logic.registerLogicEvent(hudclick0);

    }

    private void setupPlayer(){
        PhysicsObject playerrectangle = new PhysicsRectangle(new Point2D.Double(100,100), 1, 80, 60);
        player = new GameObject(logic.getNextID(),playerrectangle,textures[0]);
        playerrectangle.setParent(player);
        player.setGraphiclayerid(0);
        logic.addGameObject(player);
        PhysicsObject targetrectangle = new PhysicsRectangle(new Point2D.Double(300,300), 1, 50, 50);
        GameObject target = new GameObject(logic.getNextID(),targetrectangle,null);
        targetrectangle.setParent(target);
        logic.addGameObject(target);
        logic.addWall(0,0,1000,30);
        logic.addWall(0,970, 30, 1000);
        logic.addWall(0,0,30,1000);
        logic.addWall(970,0,1000,30);
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

    private void setupDeadZones(){
        logic.addDeadZone(-100,-100,50,1200);
        logic.addDeadZone(-100,-100,1200,50);
        logic.addDeadZone(-100,1050,50,1200);
        logic.addDeadZone(1050,-100, 1200, 50);
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
