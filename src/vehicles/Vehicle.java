package vehicles;

import game.Direction;
import game.Intersection;
import game.Tuple;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by alexanderm on 02/01/2018.
 */
public interface Vehicle {
    BufferedImage getCarImage();
    Direction getDirection();
    Tuple drive();
    boolean canMove();
    Tuple getPosition();
    void draw(Graphics g);

}
