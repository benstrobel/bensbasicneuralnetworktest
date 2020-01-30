package evolutiontest;

import bensbasicgameengine.GameLogic.Events.LogicEvent;
import bensbasicgameengine.GameLogic.GameObject;
import bensbasicgameengine.GameLogic.Logic;
import bensbasicgameengine.Physic.PhysicsObject;

public class WallCollideEvent extends LogicEvent {

    private GameObject player;
    private Logic logic;
    private LongWrapper abort;

    public WallCollideEvent(GameObject player, Logic logic, LongWrapper abort){
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
        abort.setState(logic.getTickcounter());
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
