package evolutiontest.customevents;

import bensbasicgameengine.GameLogic.Events.LogicEvent;
import bensbasicgameengine.GameLogic.GameObject;
import bensbasicgameengine.Lib.Tools;

import java.awt.geom.Point2D;

public class SensorMoveEvent extends LogicEvent {

    private GameObject [] fl, f, fr;
    private GameObject player;

    public SensorMoveEvent(GameObject player, GameObject [] fl, GameObject [] f, GameObject [] fr){
        this.fl = fl;
        this.f = f;
        this.fr = fr;
        this.player = player;
    }

    @Override
    public String getTransmissionData() {
        return null;
    }

    @Override
    public void eventmethod() {
        double fldeg = player.getOrientation() - 45, frdeg = player.getOrientation() + 45;
        if(fldeg < 0){fldeg = 360 + fldeg;}
        if(frdeg > 360){frdeg = frdeg - 360;}
        Point2D flvec = Tools.getRealMiddle(player.getPhysicsObject()), frvec = Tools.getRealMiddle(player.getPhysicsObject()), fvec = Tools.getRealMiddle(player.getPhysicsObject());
        Point2D flvecaddr = Tools.mulVector(Tools.normVector(Tools.getVectorFromAngle(fldeg)), 40);
        Point2D fvecaddr = Tools.mulVector(Tools.normVector(Tools.getVectorFromAngle(player.getOrientation())), 40);
        Point2D frvecaddr = Tools.mulVector(Tools.normVector(Tools.getVectorFromAngle(frdeg)), 40);
        flvec = Tools.addVector(Tools.addVector(flvec,flvecaddr),flvecaddr);
        fvec = Tools.addVector(Tools.addVector(fvec,fvecaddr),fvecaddr);
        frvec = Tools.addVector(Tools.addVector(frvec,frvecaddr),frvecaddr);
        for(GameObject sensor : fl){
            double deg = fldeg - 90;
            if(deg < 0){deg = 360 + deg;}
            sensor.rotate(deg);
            sensor.getPhysicsObject().setPosition(flvec);
            flvec = Tools.addVector(flvec,flvecaddr);
        }
        for(GameObject sensor : f){
            double deg = player.getOrientation() - 90;
            if(deg < 0){deg = 360 + deg;}
            sensor.rotate(deg);
            sensor.getPhysicsObject().setPosition(fvec);
            fvec = Tools.addVector(fvec,fvecaddr);
        }
        for(GameObject sensor : fr){
            double deg = frdeg - 90;
            if(deg < 0){deg = 360 + deg;}
            sensor.rotate(deg);
            sensor.getPhysicsObject().setPosition(frvec);
            frvec = Tools.addVector(frvec,frvecaddr);
        }
    }

    @Override
    public boolean eventstate() {
        return true;
    }
}
