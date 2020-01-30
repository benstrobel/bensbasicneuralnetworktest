package evolutiontest;

import bensbasicgameengine.GameLogic.Events.LogicEvent;
import bensbasicgameengine.GameLogic.GameObject;

public class InputEvent extends LogicEvent {

    private BoolWrapper left, right;
    private GameObject player;

    public InputEvent(BoolWrapper left, BoolWrapper right, GameObject player){
        this.left = left;
        this.right = right;
        this.player = player;
    }

    @Override
    public String getTransmissionData() {
        return null;
    }

    @Override
    public void eventmethod() {
        if(left.getState()){
            player.rotate(player.getOrientation()-5);
        }
        if(right.getState()){
            player.rotate(player.getOrientation()+5);
        }
    }

    @Override
    public boolean eventstate() {
        return true;
    }
}
