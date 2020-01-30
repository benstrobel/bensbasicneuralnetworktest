package evolutiontest;

import bensbasicgameengine.GameLogic.Events.LogicEvent;
import bensbasicgameengine.GameLogic.GameObject;
import bensbasicgameengine.Physic.PhysicsObject;

public class HitGoal extends LogicEvent {

    private GameObject goal;
    private LongWrapper abort;

    public HitGoal(GameObject goal, LongWrapper abort){
        this.goal = goal;
        this.abort = abort;
    }

    @Override
    public String getTransmissionData() {
        return null;
    }

    @Override
    public void eventmethod() {
        abort.setState(999999999);
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
