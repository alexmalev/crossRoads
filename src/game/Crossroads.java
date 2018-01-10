package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by alexanderm on 05/01/2018.
 */
public class Crossroads extends JComponent implements ActionListener {
    GameBoard gameBoard;

    public Crossroads() throws IOException {
        gameBoard = new GameBoard(1);
    }

    public static void main(String[] args) throws IOException {
        JFrame window = new JFrame("Crossroads");
        Crossroads crossroadsGame = new Crossroads();
        window.add(crossroadsGame);
        window.pack();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        Timer timer = new Timer(30, crossroadsGame);
        timer.start();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    @Override
    protected void paintComponent(Graphics g) {
        gameBoard.draw(g);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        gameBoard.updateState();

        repaint();
    }


}
