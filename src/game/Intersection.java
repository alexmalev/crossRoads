package game;

import vehicles.Vehicle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
    private BufferedImage greenLight = ImageIO.read(getClass().getResource("greenLight.png"));
    private BufferedImage redLight = ImageIO.read(getClass().getResource("redLight.png"));

    public Intersection(Tuple position) throws IOException {
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
    public BufferedImage getTrafficLightImage(){
        return northEntrance.isCanPass() ? greenLight : redLight;
    }

}
