package game;

import vehicles.Vehicle;
import vehicles.VehicleImpl;

import java.awt.*;
import java.io.IOException;
import java.util.*;

/**
 * Created by alexanderm on 06/01/2018.
 */
public class GameBoard {

    private Map<Tuple, Tile> boardMap = new HashMap<>();
    private int horizontalTiles = 20;
    private int verticalTiles = 15;
    private Intersection intersection;
    private RoadQueue southExit;
    private RoadQueue northExit;
    private RoadQueue eastExit;
    private RoadQueue westExit;


    public GameBoard(int numOfIntersections) throws IOException {
        generateBoard(numOfIntersections);
    }

    private void generateBoard(int numOfIntersections) throws IOException {
        Tuple intersectionPosition = insertIntersection(numOfIntersections);
        this.intersection = new Intersection(intersectionPosition);
        this.southExit = new RoadQueue(true);
        this.northExit = new RoadQueue(true);
        this.eastExit = new RoadQueue(true);
        this.westExit = new RoadQueue(true);
        insertRoads(intersectionPosition);
    }

    private Tuple insertIntersection(int numOfIntersections) throws IOException {
        int intersectionX = horizontalTiles / (numOfIntersections + 1);
        int intersectionY = verticalTiles / (numOfIntersections + 1);
        Tuple position = new Tuple(intersectionX, intersectionY);
        boardMap.put(position, new Tile(TileType.INTERSECTION));
        return position;
    }

    private void insertRoads(Tuple intersectionPosition) throws IOException {
        for (int i = 0; i < horizontalTiles; i++) {
            Tuple tilePosition = new Tuple(i, intersectionPosition.getY());
            if (boardMap.containsKey(tilePosition))
                continue;
            boardMap.put(tilePosition, new Tile(TileType.HORIZONTAL));
        }
        for (int j = 0; j < horizontalTiles; j++) {
            Tuple tilePosition = new Tuple(intersectionPosition.getX(), j);
            if (boardMap.containsKey(tilePosition))
                continue;
            boardMap.put(tilePosition, new Tile(TileType.VERTICAL));
        }
    }

    public void draw(Graphics g) {
        g.drawImage(intersection.getTrafficLightImage(Direction.NORTH), (intersection.getPosition().getX() - 1) * 40, (intersection.getPosition().getY() - 1) * 40, null);
        g.drawImage(intersection.getTrafficLightImage(Direction.EAST), (intersection.getPosition().getX() + 1) * 40, (intersection.getPosition().getY() - 1) * 40, null);
        g.drawImage(intersection.getTrafficLightImage(Direction.SOUTH), (intersection.getPosition().getX() + 1) * 40, (intersection.getPosition().getY() + 1) * 40, null);
        g.drawImage(intersection.getTrafficLightImage(Direction.WEST), (intersection.getPosition().getX() - 1) * 40, (intersection.getPosition().getY() + 1) * 40, null);
        for (Tuple tuple : boardMap.keySet()) {
            boardMap.get(tuple).draw(tuple, g);
        }

        for (Vehicle vehicle : intersection.getEntrance(Direction.NORTH).getQueue()) {
            vehicle.draw(g);
        }
        for (Vehicle vehicle : intersection.getEntrance(Direction.EAST).getQueue()) {
            vehicle.draw(g);
        }
        for (Vehicle vehicle : intersection.getEntrance(Direction.SOUTH).getQueue()) {
            vehicle.draw(g);
        }
        for (Vehicle vehicle : intersection.getEntrance(Direction.WEST).getQueue()) {
            vehicle.draw(g);
        }
        for (Vehicle vehicle : southExit.getQueue()) {
            vehicle.draw(g);
        }
        for (Vehicle vehicle : northExit.getQueue()) {
            vehicle.draw(g);
        }
        for (Vehicle vehicle : westExit.getQueue()) {
            vehicle.draw(g);
        }
        for (Vehicle vehicle : eastExit.getQueue()) {
            vehicle.draw(g);
        }
    }


    private int turn = 1;
    private int greenTimer = 500;
    private int delayTimer = 50;
    Direction nextGreen1;
    Direction nextGreen2;
    public void updateState() {
        greenTimer--;

        if (greenTimer == 0){
            if (intersection.getEntrance(Direction.NORTH).isCanPass()){
                intersection.getEntrance(Direction.NORTH).setCanPass(false);
                intersection.getEntrance(Direction.SOUTH).setCanPass(false);
                nextGreen1 = Direction.EAST;
                nextGreen2 = Direction.WEST;
            }
            else {
                intersection.getEntrance(Direction.EAST).setCanPass(false);
                intersection.getEntrance(Direction.WEST).setCanPass(false);
                nextGreen1 = Direction.NORTH;
                nextGreen2 = Direction.SOUTH;
            }
        }
        if (greenTimer == -50){
            intersection.getEntrance(nextGreen1).setCanPass(true);
            intersection.getEntrance(nextGreen2).setCanPass(true);
            greenTimer = 500;
        }

        if (turn % 200 == 1) {
            try {
                intersection.getEntrance(Direction.NORTH).getQueue().add(new VehicleImpl(new Tuple(400, -40), Direction.SOUTH));
                intersection.getEntrance(Direction.SOUTH).getQueue().add(new VehicleImpl(new Tuple(420, 600), Direction.NORTH));
                intersection.getEntrance(Direction.WEST).getQueue().add(new VehicleImpl(new Tuple(-40, 300), Direction.EAST));
                intersection.getEntrance(Direction.EAST).getQueue().add(new VehicleImpl(new Tuple(800, 280), Direction.WEST));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        turn++;
        controlVehicles();
    }

    private void controlVehicles() {
        LinkedList<Vehicle> northEntrance = intersection.getEntrance(Direction.NORTH).getQueue();
        LinkedList<Vehicle> southEntrance = intersection.getEntrance(Direction.SOUTH).getQueue();
        LinkedList<Vehicle> eastEntrance = intersection.getEntrance(Direction.EAST).getQueue();
        LinkedList<Vehicle> westEntrance = intersection.getEntrance(Direction.WEST).getQueue();

        controlVehiclesInQueue(northEntrance);
        controlVehiclesInQueue(southEntrance);
        controlVehiclesInQueue(eastEntrance);
        controlVehiclesInQueue(westEntrance);
        LinkedList<Vehicle> southExit = this.southExit.getQueue();
        LinkedList<Vehicle> northExit = this.northExit.getQueue();
        LinkedList<Vehicle> westExit = this.westExit.getQueue();
        LinkedList<Vehicle> eastExit = this.eastExit.getQueue();

        controlExit(southExit);
        controlExit(northExit);
        controlExit(westExit);
        controlExit(eastExit);
    }

    private void controlExit(LinkedList<Vehicle> exit) {
        for (ListIterator<Vehicle> iterator = exit.listIterator(); iterator.hasNext(); ) {
            Vehicle currentVehicle = iterator.next();
            currentVehicle.drive(true);
            if (leftTheScreen(currentVehicle)) {
                iterator.remove();
            }
        }
    }

    private boolean leftTheScreen(Vehicle currentVehicle) {
        switch (currentVehicle.getDirection()) {
            case SOUTH:
                return currentVehicle.getPosition().getY() > 600;
            case NORTH:
                return currentVehicle.getPosition().getY() < -40;
            case EAST:
                return currentVehicle.getPosition().getX() > 800;
            case WEST:
                return currentVehicle.getPosition().getX() < -40;
        }
        return false;
    }

    private void controlVehiclesInQueue(LinkedList<Vehicle> queue) {
        for (ListIterator<Vehicle> iterator = queue.listIterator(); iterator.hasNext(); ) {
            Vehicle currentVehicle = iterator.next();
            if (currentVehicle == queue.getFirst()) {
                if (isFirstVehicleFreeToDrive(currentVehicle)) {
                    currentVehicle.drive(true);
                } else if (isFirstVehicleAtIntersection(currentVehicle)) {
                    if (isGreenLight(currentVehicle.getDirection())) {
                        currentVehicle.drive(true);
                    } else {
                        currentVehicle.drive(false);
                    }
                } else {
                    passVehicleToNextQueue(currentVehicle);
                    iterator.remove();
                }

            } else {
                iterator.previous();
                Vehicle vehicleInFront = iterator.previous();
                if (hasSpaceToMove(currentVehicle, vehicleInFront)) {
                    currentVehicle.drive(true);
                } else {
                    currentVehicle.drive(false);
                }
                iterator.next();
                iterator.next();
            }
        }
    }

    private boolean isGreenLight(Direction direction) {
        switch (direction) {
            case SOUTH:
                return intersection.getEntrance(Direction.NORTH).isCanPass();
            case NORTH:
                return intersection.getEntrance(Direction.SOUTH).isCanPass();
            case WEST:
                return intersection.getEntrance(Direction.EAST).isCanPass();
            case EAST:
                return intersection.getEntrance(Direction.WEST).isCanPass();
        }
        return false;

    }

    private boolean hasSpaceToMove(Vehicle currentVehicle, Vehicle vehicleInFront) {
        switch (currentVehicle.getDirection()) {
            case SOUTH:
                return currentVehicle.getPosition().getY() < vehicleInFront.getPosition().getY() - 40;
            case NORTH:
                return currentVehicle.getPosition().getY() > vehicleInFront.getPosition().getY() + 40;
            case EAST:
                return currentVehicle.getPosition().getX() < vehicleInFront.getPosition().getX() - 40;
            case WEST:
                return currentVehicle.getPosition().getX() > vehicleInFront.getPosition().getX() + 40;
        }
        return false;
    }

    private void passVehicleToNextQueue(Vehicle vehicle) {
        switch (vehicle.getDirection()) {
            case SOUTH:
                southExit.getQueue().add(vehicle);
                break;
            case NORTH:
                northExit.getQueue().add(vehicle);
                break;
            case EAST:
                eastExit.getQueue().add(vehicle);
                break;
            case WEST:
                westExit.getQueue().add(vehicle);
                break;
        }
    }

    private boolean isFirstVehicleAtIntersection(Vehicle vehicle) {
        switch (vehicle.getDirection()) {
            case SOUTH:
                return vehicle.getPosition().getY() == intersection.getPosition().getY() * 40 - 40;
            case NORTH:
                return vehicle.getPosition().getY() == intersection.getPosition().getY() * 40 + 40;
            case EAST:
                return vehicle.getPosition().getX() == intersection.getPosition().getX() * 40 - 40;
            case WEST:
                return vehicle.getPosition().getX() == intersection.getPosition().getX() * 40 + 40;
        }
        return false;

    }

    private boolean isFirstVehicleFreeToDrive(Vehicle vehicle) {
        switch (vehicle.getDirection()) {
            case SOUTH:
                return vehicle.getPosition().getY() < intersection.getPosition().getY() * 40 - 40;
            case NORTH:
                return vehicle.getPosition().getY() > intersection.getPosition().getY() * 40 + 40;
            case EAST:
                return vehicle.getPosition().getX() < intersection.getPosition().getX() * 40 - 40;
            case WEST:
                return vehicle.getPosition().getX() > intersection.getPosition().getX() * 40 + 40;

        }
        return true;

    }

}
