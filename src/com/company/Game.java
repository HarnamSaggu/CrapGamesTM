package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game extends JFrame implements KeyListener {
//    String src = "src/";
    String src = "";
    final int X;
    final int Y;
    final int SIZE = 30;
    JLabel[][] cells;
    ArrayList<Anchor> anchors;
    HashMap<Integer, Integer> eToDir;
    ArrayList<Segment> parts;
    int[] apple;
    boolean isRunning = false;
    int score;
    boolean cont;

    public Game(int X, int Y) {
        super("Snake, score: 0");
        this.X = X;
        this.Y = Y;
        Segment.X = X;
        Segment.Y = Y;
        setSize(15 + (X * SIZE), 37 + (Y * SIZE));
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.black);

        score = 0;

        anchors = new ArrayList<>();

        parts = new ArrayList<>();
        parts.add(new Segment(X / 2, Y / 2, 1));

        addApple();

        cells = new JLabel[X][Y];
        for (int j = 0; j < Y; j++) {
            for (int i = 0; i < X; i++) {
                cells[i][j] = new JLabel(new ImageIcon(src + "resources/bg.png"));
                cells[i][j].setBounds(i * SIZE, j * SIZE, SIZE, SIZE);
                add(cells[i][j]);
            }
        }

        eToDir = new HashMap<>();
        eToDir.put(KeyEvent.VK_UP, 0);
        eToDir.put(KeyEvent.VK_RIGHT, 1);
        eToDir.put(KeyEvent.VK_DOWN, 2);
        eToDir.put(KeyEvent.VK_LEFT, 3);
        eToDir.put(KeyEvent.VK_W, 0);
        eToDir.put(KeyEvent.VK_D, 1);
        eToDir.put(KeyEvent.VK_S, 2);
        eToDir.put(KeyEvent.VK_A, 3);

        addKeyListener(this);
        setVisible(true);

        new SwingWorker() {
            @Override
            protected Object doInBackground() {
                cont = true;
                runSnake();
                return null;
            }
        }.execute();
    }

    public void appendPart() {
        int nx = parts.get(parts.size() - 1).x;
        int ny = parts.get(parts.size() - 1).y;
        int lastDir = parts.get(parts.size() - 1).dir;
        switch (lastDir) {
            case 0 -> ny += 1;
            case 1 -> nx -= 1;
            case 2 -> ny -= 1;
            case 3 -> nx += 1;
        }
        parts.add(new Segment(nx, ny, lastDir));
    }

    public void addApple() {
        int[][] xys = new int[X * Y][2];
        boolean add;
        int linear = 0;
        for (int i = 0; i < Y; i++) {
            for (int j = 0; j < X; j++) {
                add = true;
                for (Segment part : parts) {
                    if (j == part.x && i == part.y) {
                        add = false;
                        break;
                    }
                }
                if (add) xys[linear] = new int[]{j, i};
                linear++;
            }
        }
        int index = new Random().nextInt(xys.length);
        apple = new int[]{xys[index][0], xys[index][1]};
    }

    public void loadBG() {
        for (int j = 0; j < Y; j++) {
            for (int i = 0; i < X; i++) {
                cells[i][j].setIcon(new ImageIcon(src + "resources/bg.png"));
            }
        }
    }

    public void runSnake() {
        while (cont) {
            loadBG();

            cells[apple[0]][apple[1]].setIcon(new ImageIcon(src + "resources/apple.png"));

            for (int i = 0; i < parts.size(); i++) {
                parts.get(i).move();
                cells[parts.get(i).x][parts.get(i).y].setIcon(new ImageIcon(src + "resources/snake.png"));

                for (int j = 0; j < anchors.size(); j++) {
                    if (parts.get(i).x == anchors.get(j).X && parts.get(i).y == anchors.get(j).Y) {
                        parts.get(i).dir = anchors.get(j).DIR;
                        if (i == parts.size() - 1) anchors.remove(j);
                    }
                }
            }

            cells[parts.get(0).x][parts.get(0).y].setIcon(new ImageIcon(src + "resources/head.png"));

            for (int i = 0; i < parts.size(); i++) {
                for (int j = 0; j < parts.size(); j++) {
                    if (parts.get(i).x == parts.get(j).x &&
                            parts.get(i).y == parts.get(j).y) {
                        if (i == j) continue;
                        cont = false;
                    }
                }

                if (parts.get(i).x == apple[0] && parts.get(i).y == apple[1]) {
                    appendPart();
                    addApple();
                    setTitle("Snake, score: " + ++score);
                }
            }

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void turn(KeyEvent e) {
        Integer direction = eToDir.get(e.getKeyCode());
        if (direction != null) {
            if (direction == parts.get(0).dir) return;
            if ((direction == 0 && parts.get(0).dir != 2)
                    || (direction == 2 && parts.get(0).dir != 0)
                    || (direction == 1 && parts.get(0).dir != 3)
                    || (direction == 3 && parts.get(0).dir != 1)) {
                boolean isUnique = true;
                for (Anchor anchor : anchors) {
                    if (parts.get(0).x == anchor.X && parts.get(0).y == anchor.Y) {
                        isUnique = false;
                        break;
                    }
                }
                if (isUnique) {
                    anchors.add(new Anchor(parts.get(0).x, parts.get(0).y, direction));
                    parts.get(0).dir = anchors.get(anchors.size() - 1).DIR;
                    if (parts.size() == 1) anchors.remove(anchors.size() - 1);
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!cont && e.getKeyCode() == KeyEvent.VK_ENTER) dispose(); //System.exit(0);
        if (!isRunning) {
            isRunning = true;
            SwingUtilities.invokeLater(() -> turn(e));
            isRunning = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static class Segment {
        static int X;
        static int Y;
    
        int x;
        int y;
        int dir;
    
        public Segment(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }
    
        public void move() {
            switch (dir) {
                case 0 -> y--;
                case 1 -> x++;
                case 2 -> y++;
                case 3 -> x--;
            }
            if (y < 0) y = Y - 1;
            if (x < 0) x = X - 1;
            if (y >= Y) y = 0;
            if (x >= X) x = 0;
        }
    }

    public static class Anchor {
        final int X;
        final int Y;
        final int DIR;
    
        public Anchor(int x, int y, int dir) {
            this.X = x;
            this.Y = y;
            this.DIR = dir;
        }
    }
}
