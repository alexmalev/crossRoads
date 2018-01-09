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


    public GameBoard(int numOfIntersections) throws IOException {
        generateBoard(numOfIntersections);
    }

    private void generateBoard(int numOfIntersections) throws IOException {
        Tuple intersectionPosition = insertIntersection(numOfIntersections);
        this.intersection = new Intersection(intersectionPosition);
        this.southExit = new RoadQueue(true);
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
        g.drawImage(intersection.getTrafficLightImage(), (intersection.getPosition().getX() - 1) * 40, (intersection.getPosition().getY() -1) * 40, null);
        for (Tuple tuple : boardMap.keySet()) {
            boardMap.get(tuple).draw(tuple, g);
        }

        for (Vehicle vehicle : intersection.getNorthEntrance().getQueue()) {
            vehicle.draw(g);
        }
        for (Vehicle vehicle : southExit.getQueue()) {
            vehicle.draw(g);
        }
    }


    public Queue<Vehicle> getVehicles() {
        return intersection.getNorthEntrance().getQueue();
    }
    private int turn = 1;
    public void updateState() {
        if (turn % 510 == 0){
            intersection.getNorthEntrance().setCanPass(true);
        }

        if (turn % 250 ==0 || turn % 620 ==0 ){
            intersection.getNorthEntrance().setCanPass(false);
        }
        if (turn % 100 == 0){
            try {
                getVehicles().add(new VehicleImpl(new Tuple(400,-160), Direction.SOUTH));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        turn ++;
        controlVehicles();
    }
    private void controlVehicles() {
        LinkedList<Vehicle> queue = intersection.getNorthEntrance().getQueue();
        for (ListIterator<Vehicle> iterator = queue.listIterator(); iterator.hasNext(); ) {
            Vehicle currentVehicle = iterator.next();
            Tuple currentVehiclePosition = currentVehicle.getPosition();
            if (currentVehicle == queue.getFirst()) {
                if (currentVehiclePosition.getY() < intersection.getPosition().getY() * 40 -40){
                    currentVehicle.drive(true);
                }
                else if (currentVehiclePosition.getY() == intersection.getPosition().getY() * 40 -40){
                    if (intersection.getNorthEntrance().isCanPass()){
                        currentVehicle.drive(true);
                    }
                    else {
                        currentVehicle.drive(false);
                    }
                }
                else {
                    southExit.getQueue().add(currentVehicle);
                    iterator.remove();
                }

            } else {
                iterator.previous();
                Vehicle vehicleInFront = iterator.previous();
                Tuple vehicleInFrontPosition = vehicleInFront.getPosition();
                if (currentVehiclePosition.getY() < vehicleInFrontPosition.getY() - 40){
                    currentVehicle.drive(true);
                }
                else {
                    currentVehicle.drive(false);
                }
                iterator.next();
                iterator.next();
            }
        }
        LinkedList<Vehicle> exit = southExit.getQueue();
        for (ListIterator<Vehicle> iterator = exit.listIterator(); iterator.hasNext(); ) {
            Vehicle currentVehicle = iterator.next();
            currentVehicle.drive(true);
            if (currentVehicle.getPosition().getY() > 600) {
                iterator.remove();
            }
        }
    }

}
