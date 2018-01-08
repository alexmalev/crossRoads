package game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexanderm on 06/01/2018.
 */
public class Tile {

    private BufferedImage tileImage;
    private TileType type;
    private Map<Direction, Boolean> taken;
    boolean takenNorth = false;
    boolean takenSouth = false;
    boolean takenEast = false;
    boolean takenWest = false;
    private int tileSize = 40;

    public Tile(TileType type) throws IOException {
        switch (type) {
            case GRASS:
                break;
            case VERTICAL:
                this.tileImage = ImageIO.read(getClass().getResource("vertical_road.png"));
                break;
            case HORIZONTAL:
                this.tileImage = ImageIO.read(getClass().getResource("horizontal_road.png"));
                break;
            case INTERSECTION:
                this.tileImage = ImageIO.read(getClass().getResource("intersection.png"));
                break;
        }
        this.type = type;
        this.taken = new HashMap<>(4);
        this.taken.replaceAll((k, v) -> false);
    }

    public TileType getType() {
        return type;
    }

    public BufferedImage getTileImage() {
        return tileImage;
    }

    public void setTaken(Direction direction, boolean taken) {
        this.taken.put(direction, taken);
    }

    public boolean isTaken(Direction direction) {
        return this.taken.get(direction);
    }

    public void draw(Tuple position, Graphics g) {
        g.drawImage(tileImage, position.getX() * tileSize, position.getY() * tileSize, null);
    }
}
