package evolutiontest.customevents;

import bensbasicgameengine.GameLogic.Events.LogicEvent;
import bensbasicgameengine.GameLogic.GameObject;
import bensbasicgameengine.Lib.Tools;

import java.awt.geom.Point2D;

public class VelocityEvent extends LogicEvent {

    private GameObject player;

    public VelocityEvent(GameObject player){
        this.player = player;
    }

    @Override
    public String getTransmissionData() {
        return "x";
    }

    @Override
    public void eventmethod() {
        Point2D velcevtor = Tools.mulVector(Tools.normVector(Tools.getVectorFromAngle(player.getOrientation())), 5);
        player.getPhysicsObject().setVelocityX(velcevtor.getX());
        player.getPhysicsObject().setVelocityY(velcevtor.getY());
    }

    @Override
    public boolean eventstate() {
        return true;
    }

}
