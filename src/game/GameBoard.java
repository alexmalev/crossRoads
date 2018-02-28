package game;

import vehicles.Vehicle;

import java.awt.*;
import java.io.IOException;
import java.util.*;

public class GameBoard {
    private static Random rand = new Random();

    private Map<Tuple, Tile> boardMap = new HashMap<>();
    private int horizontalTiles = 20;
    private int verticalTiles = 15;

    private Intersection intersection;
    private RoadQueue southExit;
    private RoadQueue northExit;
    private RoadQueue eastExit;
    private RoadQueue westExit;


    GameBoard() throws IOException {
        generateBoard();
    }

    private void generateBoard() throws IOException {
        Tuple intersectionPosition = insertIntersection();
        this.intersection = new Intersection(intersectionPosition);
        this.southExit = new RoadQueue(Color.GREEN);
        this.northExit = new RoadQueue(Color.GREEN);
        this.eastExit = new RoadQueue(Color.GREEN);
        this.westExit = new RoadQueue(Color.GREEN);
        insertRoads(intersectionPosition);
        insertGrass();
    }

    private Tuple insertIntersection() throws IOException {
        int intersectionX = horizontalTiles / 2;
        int intersectionY = verticalTiles / 2;
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
        for (int j = 0; j < verticalTiles; j++) {
            Tuple tilePosition = new Tuple(intersectionPosition.getX(), j);
            if (boardMap.containsKey(tilePosition))
                continue;
            boardMap.put(tilePosition, new Tile(TileType.VERTICAL));
        }
    }
    private void insertGrass() throws IOException {
        for (int i = 0; i < horizontalTiles; i++) {
            for (int j = 0; j < verticalTiles; j++) {
                Tuple tilePosition = new Tuple(i, j);
                if (!boardMap.containsKey(tilePosition))
                    boardMap.put(tilePosition, new Tile(TileType.GRASS));
            }
        }
    }

    void draw(Graphics g) {
        for (Tuple tuple : boardMap.keySet()) {
            boardMap.get(tuple).draw(tuple, g);
        }
        g.drawImage(intersection.getTrafficLightImage(Direction.NORTH), (intersection.getPosition().getX() - 1) * 40, (intersection.getPosition().getY() - 1) * 40, null);
        g.drawImage(intersection.getTrafficLightImage(Direction.EAST), (intersection.getPosition().getX() + 1) * 40, (intersection.getPosition().getY() - 1) * 40, null);
        g.drawImage(intersection.getTrafficLightImage(Direction.SOUTH), (intersection.getPosition().getX() + 1) * 40, (intersection.getPosition().getY() + 1) * 40, null);
        g.drawImage(intersection.getTrafficLightImage(Direction.WEST), (intersection.getPosition().getX() - 1) * 40, (intersection.getPosition().getY() + 1) * 40, null);
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


    int eastTurn = 0;
    int westTurn = 0;
    int horizontalMin = 30;
    int horizontalMax = 31;
    int nextEast = getRandomInt(horizontalMin, horizontalMax);
    int nextWest = getRandomInt(horizontalMin, horizontalMax);

    int northTurn = 0;
    int southTurn = 0;
    int verticalMin = 30;
    int verticalMax = 31;
    int nextNorth = getRandomInt(verticalMin, verticalMax);
    int nextSouth = getRandomInt(verticalMin, verticalMax);

    int getRandomInt(int min, int max) {
        return rand.nextInt((max + 1) - min) + min;
    }

    void updateGameBoard() throws IOException {
        eastTurn++;
        westTurn++;
        northTurn++;
        southTurn++;
        if (eastTurn == nextEast) {
            intersection.getEntrance(Direction.EAST).getQueue().add(new Vehicle(new Tuple(800, 280), Direction.WEST));
            eastTurn = 0;
            nextEast = getRandomInt(horizontalMin, horizontalMax);
        }
        if (westTurn == nextWest) {
            intersection.getEntrance(Direction.WEST).getQueue().add(new Vehicle(new Tuple(-40, 300), Direction.EAST));
            westTurn = 0;
            nextWest = getRandomInt(horizontalMin, horizontalMax);
        }
        if (northTurn == nextNorth) {
            intersection.getEntrance(Direction.NORTH).getQueue().add(new Vehicle(new Tuple(400, -40), Direction.SOUTH));
            northTurn = 0;
            nextNorth = getRandomInt(verticalMin, verticalMax);
        }
        if (southTurn == nextSouth) {
            intersection.getEntrance(Direction.SOUTH).getQueue().add(new Vehicle(new Tuple(420, 600), Direction.NORTH));
            southTurn = 0;
            nextSouth = getRandomInt(verticalMin, verticalMax);
        }

        controlVehicles();
    }

    private void controlVehicles() {
        LinkedList<Vehicle> southExit = this.southExit.getQueue();
        LinkedList<Vehicle> northExit = this.northExit.getQueue();
        LinkedList<Vehicle> westExit = this.westExit.getQueue();
        LinkedList<Vehicle> eastExit = this.eastExit.getQueue();

        controlExit(southExit);
        controlExit(northExit);
        controlExit(westExit);
        controlExit(eastExit);

        LinkedList<Vehicle> northEntrance = intersection.getEntrance(Direction.NORTH).getQueue();
        LinkedList<Vehicle> southEntrance = intersection.getEntrance(Direction.SOUTH).getQueue();
        LinkedList<Vehicle> eastEntrance = intersection.getEntrance(Direction.EAST).getQueue();
        LinkedList<Vehicle> westEntrance = intersection.getEntrance(Direction.WEST).getQueue();

        controlVehiclesInQueue(northEntrance);
        controlVehiclesInQueue(southEntrance);
        controlVehiclesInQueue(eastEntrance);
        controlVehiclesInQueue(westEntrance);

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
                if (isFirstNotYetInIntersection(currentVehicle)) {
                    currentVehicle.drive(true);
                    if (isFirstVehicleBeforeIntersection(currentVehicle)) {
                        intersection.getWaitingList(currentVehicle.getDirection()).add(currentVehicle);
                    }

                } else {
                    if (isGreenLight(currentVehicle.getDirection())) {
                        currentVehicle.drive(true);
                        passVehicleToNextQueue(currentVehicle);
                        iterator.remove();
                    } else {
                        currentVehicle.drive(false);
                    }
                }

            } else {
                iterator.previous();
                Vehicle vehicleInFront = iterator.previous();
                if (hasSpaceToMove(currentVehicle, vehicleInFront)) {
                    currentVehicle.drive(true);
                } else {
                    currentVehicle.drive(false);
                    intersection.getWaitingList(currentVehicle.getDirection()).add(currentVehicle);
                }
                iterator.next();
                iterator.next();
            }
        }
    }

    private boolean isGreenLight(Direction direction) {
        return intersection.getEntrance(direction).isCanPass();
    }

    private boolean hasSpaceToMove(Vehicle currentVehicle, Vehicle vehicleInFront) {
        switch (currentVehicle.getDirection()) {
            case SOUTH:
                return currentVehicle.getPosition().getY() <= vehicleInFront.getPosition().getY() - 40;
            case NORTH:
                return currentVehicle.getPosition().getY() >= vehicleInFront.getPosition().getY() + 40;
            case EAST:
                return currentVehicle.getPosition().getX() <= vehicleInFront.getPosition().getX() - 40;
            case WEST:
                return currentVehicle.getPosition().getX() >= vehicleInFront.getPosition().getX() + 40;
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
        intersection.getWaitingList(vehicle.getDirection()).remove(vehicle);
    }

    private boolean isFirstVehicleBeforeIntersection(Vehicle vehicle) {
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

    public boolean isSidePassing() {

        return isVehicleInIntersection(eastExit.getQueue()) || isVehicleInIntersection(westExit.getQueue());

    }

    public boolean isMainPassing() {

        return isVehicleInIntersection(northExit.getQueue()) || isVehicleInIntersection(southExit.getQueue());

    }

    private boolean isVehicleInIntersection(LinkedList<Vehicle> queue) {
        if (queue.size() == 0)
            return false;
        Vehicle vehicle = queue.getLast();
        switch (vehicle.getDirection()) {
            case SOUTH:
                return vehicle.getPosition().getY() < intersection.getPosition().getY() * 40 + 35;
            case NORTH:
                return vehicle.getPosition().getY() > intersection.getPosition().getY() * 40 - 45;
            case EAST:
                return vehicle.getPosition().getX() < intersection.getPosition().getX() * 40 + 35;
            case WEST:
                return vehicle.getPosition().getX() > intersection.getPosition().getX() * 40 - 45;
        }
        return false;
    }


    private boolean isFirstNotYetInIntersection(Vehicle vehicle) {
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

    public Intersection getIntersection() {
        return intersection;
    }

}
