package evolutiontest.customevents;

import bensbasicgameengine.GameLogic.Events.LogicEvent;
import bensbasicgameengine.GameLogic.GameObject;
import bensbasicgameengine.Physic.PhysicsObject;
import evolutiontest.BoolWrapper;
import evolutiontest.LongWrapper;

public class HitGoalEvent extends LogicEvent {

    private GameObject goal;
    private BoolWrapper abort;

    public HitGoalEvent(GameObject goal, BoolWrapper abort){
        this.goal = goal;
        this.abort = abort;
    }

    @Override
    public String getTransmissionData() {
        return null;
    }

    @Override
    public void eventmethod() {
        abort.setState(true);
    }

    @Override
    public boolean eventstate() {
        for(PhysicsObject obj : goal.getPhysicsObject().getCollides()){
            if(obj.getFlag().equals("player")){
                return true;
            }
        }
        return false;
    }
}
