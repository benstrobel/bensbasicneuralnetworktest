package evolutiontest;

public class BoolWrapper {
    private boolean state = false;

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }

    public String toString(){
        return ""+getState();
    }
}
