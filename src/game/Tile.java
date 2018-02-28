package game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Tile {
    private BufferedImage tileImage;
    Tile(TileType type) throws IOException {
        switch (type) {
            case GRASS:
                this.tileImage = ImageIO.read(getClass().getResource("grass.png"));
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
    }



    public void draw(Tuple position, Graphics g) {
        int tileSize = 40;
        g.drawImage(tileImage, position.getX() * tileSize, position.getY() * tileSize, null);
    }
}
