package game;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDVarSet;
import tau.smlab.syntech.games.controller.symbolic.SymbolicController;
import tau.smlab.syntech.games.controller.symbolic.SymbolicControllerReaderWriter;
import tau.smlab.syntech.jtlv.BDDPackage;
import tau.smlab.syntech.jtlv.Env;

import javax.swing.*;
import java.awt.*;
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
            if (i % 10 ==0){
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
//        System.out.println(Env.toNiceSignleLineString(currentState));

        // set values of class variables according to BDD
        String state = currentState.toStringWithDomains(Env.stringer);
        String[] stateVals = state.replace("<", "").replace(">", "").replace(" ", "").split(",");
        Color goMain = null;
        Color goSide = null;
        int carMain = 0;
        int carSide = 0;
        int blinksMain = 0;
        int blinksSide = 0;
        boolean carMainCrossing= false;
        boolean carSideCrossing = false;
        for (String s : stateVals) {
            String[] val = s.split(":");
            if ("goMain".equals(val[0])) {
                goMain = Color.valueOf(val[1]);
            }
            else if ("goSide".equals(val[0])) {
                goSide = Color.valueOf(val[1]);
            }
            else if ("carMain".equals(val[0])) {
                carMain = Integer.valueOf(val[1]);
            }
            else if ("carSide".equals(val[0])) {
                carSide = Integer.valueOf(val[1]);
            }
            else if ("blinksMain".equals(val[0])){
                blinksMain = Integer.valueOf(val[1]);
            }
            else if ("blinksSide".equals(val[0])){
                blinksSide = Integer.valueOf(val[1]);
            }
            else if ("carMainCrossing".equals(val[0])){
                carMainCrossing = Boolean.valueOf(val[1]);
            }
            else if ("carSideCrossing".equals(val[0])){
                carSideCrossing = Boolean.valueOf(val[1]);
            }
//            if("mainBlinks".equals(val[0]) && Integer.parseInt(val[1])>0) {
//                goMain = false;
//                System.out.println("mainBlinks " + val[1]);
//            }
//            if("sideBlinks".equals(val[0]) && Integer.parseInt(val[1])>0){
//                goSide = false;
//                System.out.println("sideBlinks " + val[1]);
//            }
        }
        System.out.println("goMain: " + goMain + " goSide: " + goSide + " carMain " + carMain + " carSide " + carSide + " blinksMain: " + blinksMain + " blinksSide: " + blinksSide + " carMainCrossing: " + carMainCrossing + " carSideCrosing: " + carSideCrossing );
        gameBoard.getIntersection().getEntrance(Direction.NORTH).setLight(goMain);
        gameBoard.getIntersection().getEntrance(Direction.SOUTH).setLight(goMain);
        gameBoard.getIntersection().getEntrance(Direction.EAST).setLight(goSide);
        gameBoard.getIntersection().getEntrance(Direction.WEST).setLight(goSide);

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
            System.out.println("successing for: waitingNorth "+ waitingNorh + " waitingEast " + waitingEast + " mainCrossing " + carMainCrossing + " sideCrossing " + carSideCrossing);
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
