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
    private boolean canMove;
    private Direction direction;
    private BufferedImage carImage;

    public VehicleImpl(Tuple position, Direction direction) throws IOException {
        this.position = position;
        this.direction = direction;
        this.canMove = true;
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
    public void controlMovement(Vehicle prevVehicle, Intersection intersection) {
        canMove = !(atIntersection(intersection) || behindStoppedCar(prevVehicle));
    }

    @Override
    public BufferedImage getCarImage() {
        return carImage;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public Tuple drive() {
        if (!canMove)
            return position;
        performMove(position);
        return position;
    }
    private boolean behindStoppedCar(Vehicle prevVehicle) {
        if (prevVehicle == null || prevVehicle.canMove())
            return false;
        Tuple prevVehiclePosition = prevVehicle.getPosition();
        return position.getY() / 40 == prevVehiclePosition.getY() - 1;
    }


    private boolean atIntersection(Intersection intersection) {
        return position.getX()/40 == intersection.getPosition().getX() &&
                position.getY() == (intersection.getPosition().getY() - 1) * 40 &&
                !intersection.getNorthEntrance().isCanPass();
    }
    private void performMove(Tuple position) {
        switch (direction) {
            case EAST:
                position.moveX(1);
                break;
            case WEST:
                position.moveX(-1);
                break;
            case NORTH:
                position.moveY(-1);
                break;
            case SOUTH:
                position.moveY(1);
                break;
        }

    }
    

    @Override
    public boolean canMove() {
        return canMove;
    }

    @Override
    public Tuple getPosition() {
        return new Tuple(position.getX() / 40, position.getY() / 40);
    }


    @Override
    public void draw(Graphics g) {
        g.drawImage(carImage, position.getX(), position.getY(), null);
    }
}
