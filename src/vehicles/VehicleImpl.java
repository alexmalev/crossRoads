package vehicles;

import game.Direction;
import game.Intersection;
import game.Tuple;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by alexanderm on 02/01/2018.
 */
public class VehicleImpl implements Vehicle {
    private Tuple position;
    private Direction direction;
    private BufferedImage carImage;

    public VehicleImpl(Tuple position, Direction direction) throws IOException {
        this.position = position;
        this.direction = direction;
        this.carImage = createImage(direction);
                
    }


    private BufferedImage createImage(Direction direction) throws IOException {
        String fileName = "";
        switch (direction){
            case SOUTH:
                fileName = "car_south.png";
                break;
            case NORTH:
                fileName = "car_north.png";
                break;
            case EAST:
                fileName = "car_east.png";
                break;
            case WEST:
                fileName = "car_west.png";
                break;
        }

        BufferedImage carImage = ImageIO.read(getClass().getResource(fileName));
        return carImage;
    }


    @Override
    public void drive(boolean state) {
        if (state)
            performMove(position);
    }

    @Override
    public BufferedImage getCarImage() {
        return carImage;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    private void performMove(Tuple position) {
        switch (direction) {
            case EAST:
                position.moveX(2);
                break;
            case WEST:
                position.moveX(-2);
                break;
            case NORTH:
                position.moveY(-2);
                break;
            case SOUTH:
                position.moveY(2);
                break;
        }

    }

    @Override
    public Tuple getPosition() {
        return new Tuple(position.getX(), position.getY());
    }


    @Override
    public void draw(Graphics g) {
        g.drawImage(carImage, position.getX(), position.getY(), null);
    }
}
