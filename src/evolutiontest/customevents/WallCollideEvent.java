package evolutiontest.customevents;

import bensbasicgameengine.GameLogic.Events.LogicEvent;
import bensbasicgameengine.GameLogic.GameObject;
import bensbasicgameengine.GameLogic.Logic;
import bensbasicgameengine.Physic.PhysicsObject;
import evolutiontest.LongWrapper;

import java.awt.geom.Point2D;

public class WallCollideEvent extends LogicEvent {

    private GameObject player;
    private Logic logic;
    private Point2D abort;

    public WallCollideEvent(GameObject player, Logic logic, Point2D abort){
        this.player = player;
        this.logic = logic;
        this.abort = abort;
    }

    @Override
    public String getTransmissionData() {
        return ">";
    }

    @Override
    public void eventmethod() {
        abort.setLocation(player.getPhysicsObject().getPosition());
    }

    @Override
    public boolean eventstate() {
        for(PhysicsObject obj : player.getPhysicsObject().getCollides()){
            if(obj.getFlag().equals("wall")){
                return true;
            }
        }
        return false;
    }
}
