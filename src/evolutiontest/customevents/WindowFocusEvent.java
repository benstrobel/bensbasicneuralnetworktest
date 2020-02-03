package evolutiontest.customevents;

import bensbasicgameengine.GameLogic.Events.LogicEvent;
import bensbasicgameengine.Input.KeyListener;
import bensbasicgameengine.Input.WindowFocusListener;

public class WindowFocusEvent extends LogicEvent {

    private WindowFocusListener windowFocusListener;
    private KeyListener keyListener;

    public WindowFocusEvent(WindowFocusListener windowFocusListener, KeyListener keyListener){
        this.windowFocusListener = windowFocusListener;
        this.keyListener = keyListener;
    }

    @Override
    public String getTransmissionData() {
        return "-";
    }

    @Override
    public void eventmethod() {
        keyListener.lostFocus();
    }

    @Override
    public boolean eventstate() {
        if(windowFocusListener.hasLostfocus()){
            windowFocusListener.reset();
            return true;
        }
        return false;
    }
}
