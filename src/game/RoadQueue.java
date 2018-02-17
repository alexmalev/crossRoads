package game;

import vehicles.Vehicle;

import java.util.LinkedList;

/**
 * Created by alexanderm on 08/01/2018.
 */
public class RoadQueue {
    private LinkedList<Vehicle> queue = new LinkedList<>();

    private Color light;

    public RoadQueue(Color light) {
        this.light = light;
    }

    public LinkedList<Vehicle> getQueue() {
        return queue;
    }

    public boolean isCanPass() {
        return this.light.equals(Color.GREEN) || this.light.equals(Color.OFF);
    }

    public void setLight(Color light) {
        this.light = light;
    }
    public Color getLight() {
        return light;
    }

}
