package game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by alexanderm on 06/01/2018.
 */
public class Intersection {
    private Tuple position;
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
                return eastEntrance;
            case SOUTH:
                return southEntrance;
            case WEST:
                return westEntrance;
            case NORTH:
                return northEntrance;
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
