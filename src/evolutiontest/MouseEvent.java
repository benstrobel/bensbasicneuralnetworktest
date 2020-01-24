package evolutiontest;

import bensbasicgameengine.GameLogic.Events.DeleteProjectilesEvent;
import bensbasicgameengine.GameLogic.GameObject;
import bensbasicgameengine.GameLogic.Logic;
import bensbasicgameengine.GameLogic.Events.LogicEvent;
import bensbasicgameengine.Input.Mouse_Listener;
import bensbasicgameengine.Lib.Tools;
import bensbasicgameengine.Physic.PhysicsObject;
import bensbasicgameengine.Physic.PhysicsRectangle;

import java.awt.*;
import java.awt.geom.Point2D;

public class MouseEvent extends LogicEvent {

    private Mouse_Listener mouse_listener;
    private Logic logic;
    private GameObject player;
    private Point2D camlocation;

    public MouseEvent(Mouse_Listener mouse_listener, Logic logic, GameObject player, Point2D camlocation){
        this.mouse_listener = mouse_listener;
        this.logic = logic;
        this.player = player;
        this.camlocation = camlocation;
    }

    @Override
    public void eventmethod() {
        PhysicsObject projectilerectangle = new PhysicsRectangle(Tools.getMiddle(player.getPhysicsObject()), 1, 10, 5);
        GameObject projectile = new GameObject(logic.getNextID(),projectilerectangle, Color.black, true);
        projectilerectangle.setParent(projectile);
        Point2D mousePos = (Point2D) mouse_listener.getPos().clone();
        mousePos.setLocation(mousePos.getX()+camlocation.getX(),mousePos.getY()+camlocation.getY());
        Point2D direction = Tools.calculateDirection(Tools.getMiddle(projectilerectangle),mousePos,20);
        Point2D adddirection = Tools.calculateDirection(Tools.getMiddle(projectilerectangle),mousePos,60);
        projectilerectangle.getPosition().setLocation(Tools.addVector(projectilerectangle.getPosition(),adddirection));
        projectile.getPhysicsObject().updateShape();
        projectilerectangle.setVelocityX(direction.getX());
        projectilerectangle.setVelocityY(direction.getY());
        projectilerectangle.setOrientation(Tools.getDegree(direction));
        projectile.registerLogicEvent(new DeleteProjectilesEvent(projectile));
        logic.addGameObject(projectile);
    }

    @Override
    public boolean eventstate() {
        return mouse_listener.isLeftMousePressed();
    }

    @Override
    public String getTransmissionData() {
        return "-";
    }
}
