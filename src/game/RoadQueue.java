package game;

import vehicles.Vehicle;

import java.util.LinkedList;

/**
 * Created by alexanderm on 08/01/2018.
 */
public class RoadQueue {
    private LinkedList<Vehicle> queue = new LinkedList<>();
    private boolean canPass;

    public RoadQueue(boolean canPass) {
        this.canPass = canPass;
    }

    public LinkedList<Vehicle> getQueue() {
        return queue;
    }

    public boolean isCanPass() {
        return canPass;
    }

    public void setCanPass(boolean canPass) {
        this.canPass = canPass;
    }


}
