package evolutiontest;

import bensbasicgameengine.GameLogic.Events.LogicEvent;
import bensbasicgameengine.GameLogic.GameObject;
import bensbasicgameengine.Physic.PhysicsObject;

public class SensorEvent extends LogicEvent {

    private GameObject sensorobject, player;
    private BoolWrapper sensor;

    public SensorEvent(GameObject sensorobject, GameObject player, BoolWrapper sensor){
        this.sensorobject = sensorobject;
        this.player = player;
        this.sensor = sensor;
    }

    @Override
    public String getTransmissionData() {
        return null;
    }

    @Override
    public void eventmethod() {
        for(PhysicsObject obj : sensorobject.getPhysicsObject().getCollides()){
            if(obj.getFlag().equals("wall")){
                sensor.setState(true);
            }
        }
        sensor.setState(false);
    }

    @Override
    public boolean eventstate() {
        return true;
    }
}
