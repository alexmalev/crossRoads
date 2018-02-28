package game;

import vehicles.Vehicle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Intersection {
    private Tuple position;
    private Set<Vehicle> waitingNorth = new HashSet<>();
    private Set<Vehicle> waitingSouth = new HashSet<>();
    private Set<Vehicle> waitingWest = new HashSet<>();
    private Set<Vehicle> waitingEast = new HashSet<>();
    private RoadQueue northEntrance = new RoadQueue(Color.GREEN);
    private RoadQueue southEntrance = new RoadQueue(Color.GREEN);
    private RoadQueue eastEntrance = new RoadQueue(Color.RED);
    private RoadQueue westEntrance = new RoadQueue(Color.RED);
    private BufferedImage greenLightNorth = ImageIO.read(getClass().getResource("greenLightNorth.png"));
    private BufferedImage redLightNorth = ImageIO.read(getClass().getResource("redLightNorth.png"));
    private BufferedImage yellowLightNorth = ImageIO.read(getClass().getResource("yellowLightNorth.png"));
    private BufferedImage offLightNorth = ImageIO.read(getClass().getResource("offLightNorth.png"));
    private BufferedImage greenLightEast = ImageIO.read(getClass().getResource("greenLightEast.png"));
    private BufferedImage redLightEast = ImageIO.read(getClass().getResource("redLightEast.png"));
    private BufferedImage yellowLightEast = ImageIO.read(getClass().getResource("yellowLightEast.png"));
    private BufferedImage offLightEast = ImageIO.read(getClass().getResource("offLightEast.png"));
    private BufferedImage greenLightSouth = ImageIO.read(getClass().getResource("greenLightSouth.png"));
    private BufferedImage redLightSouth = ImageIO.read(getClass().getResource("redLightSouth.png"));
    private BufferedImage yellowLightSouth = ImageIO.read(getClass().getResource("yellowLightSouth.png"));
    private BufferedImage offLightSouth = ImageIO.read(getClass().getResource("offLightSouth.png"));
    private BufferedImage greenLightWest = ImageIO.read(getClass().getResource("greenLightWest.png"));
    private BufferedImage redLightWest = ImageIO.read(getClass().getResource("redLightWest.png"));
    private BufferedImage yellowLightWest = ImageIO.read(getClass().getResource("yellowLightWest.png"));
    private BufferedImage offLightWest = ImageIO.read(getClass().getResource("offLightWest.png"));

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
                if (northEntrance.getLight().equals(Color.GREEN))
                    return greenLightNorth;
                if (northEntrance.getLight().equals(Color.RED))
                    return redLightNorth;
                return offLightNorth;
            case WEST:
                if (westEntrance.getLight().equals(Color.GREEN))
                    return greenLightWest;
                if (westEntrance.getLight().equals(Color.RED))
                    return redLightWest;
                return offLightWest;
            case SOUTH:
                if (southEntrance.getLight().equals(Color.GREEN))
                    return greenLightSouth;
                if (southEntrance.getLight().equals(Color.RED))
                    return redLightSouth;
                return offLightSouth;
            case EAST:
                if (eastEntrance.getLight().equals(Color.GREEN))
                    return greenLightEast;
                if (eastEntrance.getLight().equals(Color.RED))
                    return redLightEast;
                return offLightEast;
        }
        return null;
    }

}
