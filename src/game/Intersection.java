package game;

import vehicles.Vehicle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by alexanderm on 06/01/2018.
 */
public class Intersection {
    private Tuple position;
    private Set<Vehicle> waitingNorth = new HashSet<>();
    private Set<Vehicle> waitingSouth = new HashSet<>();
    private Set<Vehicle> waitingWest = new HashSet<>();
    private Set<Vehicle> waitingEast = new HashSet<>();
    private RoadQueue northEntrance = new RoadQueue(true);
    private RoadQueue southEntrance = new RoadQueue(true);
    private RoadQueue eastEntrance = new RoadQueue(false);
    private RoadQueue westEntrance = new RoadQueue(false);
    private BufferedImage greenLightNorth = ImageIO.read(getClass().getResource("greenLightNorth.png"));
    private BufferedImage redLightNorth = ImageIO.read(getClass().getResource("redLightNorth.png"));
    private BufferedImage greenLightEast = ImageIO.read(getClass().getResource("greenLightEast.png"));
    private BufferedImage redLightEast = ImageIO.read(getClass().getResource("redLightEast.png"));
    private BufferedImage greenLightSouth = ImageIO.read(getClass().getResource("greenLightSouth.png"));
    private BufferedImage redLightSouth = ImageIO.read(getClass().getResource("redLightSouth.png"));
    private BufferedImage greenLightWest = ImageIO.read(getClass().getResource("greenLightWest.png"));
    private BufferedImage redLightWest = ImageIO.read(getClass().getResource("redLightWest.png"));

    public Intersection(Tuple position) throws IOException {
        this.position = position;
    }

    public Tuple getPosition() {
        return position;
    }

    public RoadQueue getEntrance(Direction direction) {
        switch (direction){
            case EAST:
                return westEntrance;
            case SOUTH:
                return northEntrance;
            case WEST:
                return eastEntrance;
            case NORTH:
                return southEntrance;
        }
        return null;
    }
    public Set<Vehicle> getWaitingList(Direction direction){
        switch (direction){
            case EAST:
                return waitingWest;
            case SOUTH:
                return waitingNorth;
            case WEST:
                return waitingEast;
            case NORTH:
                return waitingSouth;
        }
        return null;
    }

    public BufferedImage getTrafficLightImage(Direction direction){
        switch (direction){
            case NORTH:
                return northEntrance.isCanPass() ? greenLightNorth : redLightNorth;
            case WEST:
                return westEntrance.isCanPass() ? greenLightWest : redLightWest;
            case SOUTH:
                return southEntrance.isCanPass() ? greenLightSouth : redLightSouth;
            case EAST:
                return eastEntrance.isCanPass() ? greenLightEast : redLightEast;
        }
        return null;
    }

}
