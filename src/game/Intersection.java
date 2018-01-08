package game;

import vehicles.Vehicle;

import java.util.LinkedList;

/**
 * Created by alexanderm on 06/01/2018.
 */
public class Intersection {
    private Tuple position;
    private RoadQueue northEntrance = new RoadQueue(true);
    private RoadQueue southEntrance = new RoadQueue(true);
    private RoadQueue eastEntrance = new RoadQueue(true);
    private RoadQueue westEntrance = new RoadQueue(true);

    public Intersection(Tuple position) {
        this.position = position;
    }

    public Tuple getPosition() {
        return position;
    }

    public RoadQueue getNorthEntrance() {
        return northEntrance;
    }

    public RoadQueue getSouthEntrance() {
        return southEntrance;
    }

    public RoadQueue getEastEntrance() {
        return eastEntrance;
    }

    public RoadQueue getWestEntrance() {
        return westEntrance;
    }


}
