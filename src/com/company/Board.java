package com.company;

import javax.swing.*;
import java.awt.event.*;

public class Board extends JFrame implements MouseListener, MouseMotionListener, KeyListener {
    private final Cell[][] cells; // Makes a 2d array or grid of cells
    private final JLabel[][] labels; // Makes a 2d array of labels which display the state of its corresponding cell
    private final int xLen; // The width of the board
    private final int yLen; // The height of the board
    private final int size = 10; // The size of the label components
    private boolean cont = true; // Whether or not to update (change to the next generation)
    private boolean shouldRun; // Whether or not to keep running the simulation
    private final double weight = 0.5; // The larger the wight the more cells are alive when pressing G (generating a random small arrangement)
    private final double radius = 4; // The max distance the affected cells can be when pressing G

    public Board(int xLen, int yLen) {
        super("Game of Life");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(29 + (xLen * size), 51 + (yLen * size));
        setResizable(false);
        setLayout(null);
        // Sets up the frame (window)

        this.xLen = xLen;
        this.yLen = yLen;
        cells = new Cell[xLen][yLen];
        labels = new JLabel[xLen][yLen];
        for (int x = 0; x < xLen; x++) {
            for (int y = 0; y < yLen; y++) {
                cells[x][y] = new Cell(x, y);
                labels[x][y] = new JLabel("");
                labels[x][y].setBounds(10 + (x * size), 10 + (y * size), size, size);
                add(labels[x][y]);
            }
        }
        // Initialises the labels (components) and cells

        setLocationRelativeTo(null); // Makes the frame appear in the center of the screen
        addMouseListener(this); // Allows the program to recognise and act when the mouse is clicked, pressed etc
        addMouseMotionListener(this); // Allows the program to recognise and act when the mouse is dragged
        addKeyListener(this); // Allows the program to recognize and act when a key is pressed, typed etc

        setVisible(true); // Displays the frame
    }

    public void run() {
        String msg = """
                Welcome to the Game of Life
                Left click or drag to colour cells
                Right click or drag to remove cells
                Space to pause (still can edit)
                Enter to clear the screen
                R to randomise the screen
                G to generate a random arrangement
                Q for the rules
                Have fun""";
        // Start message
        JOptionPane.showMessageDialog(null, msg); // Displays the message
        shouldRun = true;
        while (shouldRun) { // The loop which performs all the task while its running
            display(); // Sets each label to its cells state, O if alive or nothing if dead
            calc(); // Calculates each cells next state (the next generation)
            if (cont) inact(); // If the simulation is not paused move to the next generation
            try {
                Thread.sleep(200); // Wait 0.2 seconds between generations
            } catch (InterruptedException e) {
                System.out.println("Timing error");
            }
        }
    }

    public void stop() {
        shouldRun = false;
    } // If the simulation is to be stopped from within the code

    public void display() {
        for (int x = 0; x < xLen; x++) {
            for (int y = 0; y < yLen; y++) {
                labels[x][y].setText(cells[x][y].show()); // Gets each cells state and displays that on its corresponding label
            }
        }
    }

    public void calc() {
        for (int x = 0; x < xLen; x++) {
            for (int y = 0; y < yLen; y++) {
                int aliveCount = 0; // How many neighbours are alive
                int nx;
                int ny;

                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        nx = x + i;
                        ny = y + j;
                        if (nx < 0 || nx >= xLen || ny < 0 || ny >= yLen) continue;
                        if (cells[nx][ny].alive) aliveCount++;
                    }
                }
                // Checks all 8 surrounding cells (neighbours) and if they are alive increments the alive counter (aliveCount)

                cells[x][y].next(false); // By default sets the cell to be dead next generation
                if ((cells[x][y].alive && (aliveCount == 2 || aliveCount == 3))
                        || (!cells[x][y].alive && aliveCount == 3)) cells[x][y].next(true);
                // If the cell reaches the requirements to be alive fro the next generation
            }
        }
    }

    public void inact() {
        for (int x = 0; x < xLen; x++) {
            for (int y = 0; y < yLen; y++) {
                cells[x][y].act(); // Makes each cell take the state of the next generation
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = (e.getX() - 15) / size; // Calculates X of the cell clicked on
        int y = (e.getY() - 40) / size; // Calculates y of the cell clicked on
        if (x < 0 || x >= xLen || y < 0 || y >= yLen) return; // Makes sure the x, y are within range
        if (SwingUtilities.isLeftMouseButton(e))
            cells[x][y].next(true); // If the user left clicked turn the cell on
        else
            cells[x][y].next(false); // If the user right clicked turn the cell on
        cells[x][y].act(); // Makes the cell assume its new state outlined above
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = (e.getX() - 15) / size;
        int y = (e.getY() - 40) / size;
        if (x < 0 || x >= xLen || y < 0 || y >= yLen) return;
        if (SwingUtilities.isLeftMouseButton(e))
            cells[x][y].next(true);
        else
            cells[x][y].next(false);
        cells[x][y].act();
        // Does the exact same thing as the mouseClicked method but for each coord dragged over
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE -> cont = !cont; // If space pressed unpauses the simulation if running and vice versa
            case KeyEvent.VK_ENTER -> { // Resets the simulation
                for (int x = 0; x < xLen; x++) {
                    for (int y = 0; y < yLen; y++) {
                        cells[x][y].next(false);
                        cells[x][y].act();
                    }
                }
            }
            case KeyEvent.VK_R -> { // Randomises each cell
                for (int x = 0; x < xLen; x++) {
                    for (int y = 0; y < yLen; y++) {
                        cells[x][y].next(new java.util.Random().nextInt(10) < 5);
                        cells[x][y].act();
                    }
                }
            }
            case KeyEvent.VK_G -> { // Generates a small random arrangement where the moses is
                if (getMousePosition() == null) break;
                int a = (getMousePosition().x - 15) / size;
                int b = (getMousePosition().y - 40) / size;
                if (a < 0 || a >= xLen || b < 0 || b >= yLen) break;
                double hyp;
                for (int x = 0; x < xLen; x++) {
                    for (int y = 0; y < yLen; y++) {
                        hyp = Math.hypot((a - x), (b - y));
                        if (hyp > radius) continue;
                        cells[x][y].next((Math.random() * hyp) < weight);
                        cells[x][y].act();
                    }
                }
            }
            case KeyEvent.VK_Q -> { // Displays a pop up with the rules to the Game of Life
                String msg = """
                        Rules:
                        1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
                        2. Any live cell with two or three live neighbours lives on to the next generation.
                        3. Any live cell with more than three live neighbours dies, as if by overpopulation.
                        4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.""";
                JOptionPane.showMessageDialog(null, msg);
            }
            case KeyEvent.VK_ESCAPE -> {
                stop(); // Allows the user to stop the simulation
                dispose(); // Closes the frame
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    // Other methods included in the listeners

    private static class Cell {
        final int x;
        final int y;
        boolean alive;
        boolean next;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
            alive = false;
        }

        public void next(boolean next) { this.next = next; } // Sets its next state (generation)
        public void act() { alive = next; } // Moves the cell to the next generation

        public String show() {
            if (alive) return "O";
            return "";
        }
    }
}

//import java.awt.Font;
//                labels[x][y].setFont(new Font("Verdana", Font.PLAIN, 15));
// When using ● for the alive cell character