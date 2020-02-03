package evolutiontest.customevents;

import bensbasicgameengine.GameLogic.Events.LogicEvent;
import bensbasicgameengine.GameLogic.GameObject;
import bensbasicgameengine.Physic.PhysicsObject;
import evolutiontest.BoolWrapper;
import evolutiontest.LongWrapper;

public class StepEvent extends LogicEvent {

    private GameObject step;
    private BoolWrapper boolWrapper;

    public StepEvent(GameObject step, BoolWrapper boolWrapper){
        this.step = step;
        this.boolWrapper = boolWrapper;
    }

    @Override
    public String getTransmissionData() {
        return null;
    }

    @Override
    public void eventmethod() {
        boolWrapper.setState(true);
    }

    @Override
    public boolean eventstate() {
        for(PhysicsObject obj : step.getPhysicsObject().getCollides()){
            if(obj.getFlag().equals("player")){
                return true;
            }
        }
        return false;
    }
}
