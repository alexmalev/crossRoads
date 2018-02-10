package game;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDVarSet;
import tau.smlab.syntech.games.controller.symbolic.SymbolicController;
import tau.smlab.syntech.games.controller.symbolic.SymbolicControllerReaderWriter;
import tau.smlab.syntech.jtlv.BDDPackage;
import tau.smlab.syntech.jtlv.Env;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;

/**
 * Created by alexanderm on 05/01/2018.
 */
public class Crossroads extends JComponent {
    GameBoard gameBoard;
    private BDD currentState;
    private SymbolicController ctrl;
    private boolean initialState = true;

    public Crossroads() throws IOException {
        gameBoard = new GameBoard(1);
    }

    public void run() throws Exception {
        loadController();
        int i =0;
        while (true) {
            if (i % 20 ==0){
                try {
                    updateState();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            gameBoard.updateState();

            i++;
            repaint();
            Thread.sleep(30);
        }
    }
    private void loadController() {
        BDDPackage.setCurrPackage(BDDPackage.JTLV);

        try {
            ctrl = SymbolicControllerReaderWriter.readSymbolicController("out/");
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentState = ctrl.initial().id();
        initialState = true;
    }
    private void updateState() throws InterruptedException {
        // compute next BDD
        if (initialState) {
            BDD one = currentState.satOne(Env.globalUnprimeVars());
            currentState.free();
            currentState = one;
            initialState = false;
        } else {
            BDD succs = ctrl.succ(currentState);
            BDD succsWithVehicles = setVehiclesState(succs); //TODO
            succs.free();
            java.util.List<BDD> choices = new ArrayList<BDD>();
            BDD.BDDIterator it = new BDD.BDDIterator(succsWithVehicles, Env.globalUnprimeVars());
            while (it.hasNext()) {
                choices.add(it.next());
            }
//            if(choices.size()==1){
//                System.out.println("------------------------");
//                System.out.println("choices are, "+choices);
//                System.out.println("------------------------");
//            }
            int pick = (int) Math.floor(Math.random() * choices.size());
            currentState = choices.get(pick).id();
            Env.free(choices);
            succsWithVehicles.free();
        }
        System.out.println(Env.toNiceSignleLineString(currentState));

        // set values of class variables according to BDD
        String state = currentState.toStringWithDomains(Env.stringer);
        String[] stateVals = state.replace("<", "").replace(">", "").replace(" ", "").split(",");
        boolean goMain = false;
        boolean goSide = false;
        for (String s : stateVals) {
            String[] val = s.split(":");
            if ("goMain".equals(val[0])) {
                goMain = Boolean.parseBoolean(val[1]);
            }
            if ("goSide".equals(val[0])) {
                goSide = Boolean.parseBoolean(val[1]);
            }
        }
        gameBoard.getIntersection().getEntrance(Direction.NORTH).setCanPass(goMain);
        gameBoard.getIntersection().getEntrance(Direction.SOUTH).setCanPass(goMain);
        gameBoard.getIntersection().getEntrance(Direction.EAST).setCanPass(goSide);
        gameBoard.getIntersection().getEntrance(Direction.WEST).setCanPass(goSide);

    }

    private BDD setVehiclesState(BDD succs) {
        BDDVarSet vehiclesVars = Env.getVar("carMain").support()
                .union(Env.getVar("carSide").support())
                .union(Env.getVar("carMainCrossing").support())
                .union(Env.getVar("carSideCrossing").support());
        BDD.BDDIterator it = new BDD.BDDIterator(succs, vehiclesVars);

        Set<String> posPos = new HashSet<>();
        while(it.hasNext()){
            BDD vehicleCounts = it.next();
            String state = vehicleCounts.toStringWithDomains(Env.stringer);
            if (!posPos.contains(state)) {
                posPos.add(state);
            }
        }

        if (posPos.size() > 1) {
// TODO            waitForBanana = true;

            int waitingNorh = gameBoard.getIntersection().getWaitingList(Direction.SOUTH).size();
//            if(waitingNorh > 0) {
//                System.out.println("waiting north");
//            }
            int waitingEast = gameBoard.getIntersection().getWaitingList(Direction.WEST).size();
//            if(waitingEast > 0) {
//                System.out.println("waiting east");
//            }
            String carMainCrossing = String.valueOf(gameBoard.isMainPassing());
            String carSideCrossing = String.valueOf(gameBoard.isSidePassing());
//            System.out.println("successing for: waitingNorth "+ waitingNorh + " waitingEast " + waitingEast + " mainCrossing " + carMainCrossing + " sideCrossing " + carSideCrossing);
            BDD succWithVehicles = succs.and(Env.getBDDValue("carMain", waitingNorh))
                    .and(Env.getBDDValue("carSide", waitingEast))
                    .and(Env.getBDDValue("carMainCrossing", carMainCrossing))
                    .and(Env.getBDDValue("carSideCrossing", carSideCrossing)); //TODO
            return succWithVehicles;
        } else {
            return succs.id();
        }
    }

    public static void main(String[] args) throws Exception {
        JFrame window = new JFrame("Crossroads");
        Crossroads crossroadsGame = new Crossroads();
        window.add(crossroadsGame);
        window.pack();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        crossroadsGame.run();
//        Timer timer = new Timer(30, crossroadsGame);
//        timer.start();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    @Override
    protected void paintComponent(Graphics g) {
        gameBoard.draw(g);
    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (gameBoard.isInterfere()){
//            try {
//                updateState();
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }
//        }
//        gameBoard.updateState();
//
//
//        repaint();
//    }


}
