package com.company;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Minesweeper implements ActionListener {
    static final int boarder = 10;//10
    static final int distBetween = 0;//0
    static final int size = 25;//25
    static int rows = 15;
    static int columns = 10;
    static int difficultyLevel = 30;
//    static String src = "src/";
    static String src = "";

    private final JFrame jFrame;
    private final Tile[][] tiles;
    private final JButton jButton;


    private boolean isDead;
    private boolean flagMode;

    public Minesweeper() {
        jFrame = new JFrame("Minesweeper");
        tiles = new Tile[rows][columns];

        jFrame.setLayout(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setResizable(false);
        int xSize = 35 + ((size + distBetween) * tiles.length);
        int ySize = 105 + ((size + distBetween) * tiles[0].length);
        jFrame.setBounds(300, 100, xSize, ySize);

        jButton = new JButton("<html><font face=\"Arial Rounded MT\">Flag mode : " + flagMode + " (Enter)</font></html>");
        jButton.setBounds(boarder, ySize - 85, xSize - (boarder * 4) + 5, 40);
        jButton.addActionListener(e -> {
            flagMode = !flagMode;
            jButton.setText("<html><font face=\"Arial Rounded MT\">Flag mode : " + flagMode + " (Enter)</font></html>");
        });
        jButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                flagMode = !flagMode;
                jButton.setText("<html><font face=\"Arial Rounded MT\">Flag mode : " + flagMode + " (Enter)</font></html>");
            }
        });
        jFrame.add(jButton);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                tiles[i][j] = new Tile(i, j);
                tiles[i][j].setActionListener(this);
                tiles[i][j].setKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        flagMode = !flagMode;
                        jButton.setText("<html><font face=\"Arial Rounded MT\">Flag mode : " + flagMode + " (Enter)</font></html>");
                    }
                });
                jFrame.add(tiles[i][j].getButton());
            }
        }

        //GENERATING THE MAP
        var pairs = new ArrayList<int[]>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                pairs.add(new int[]{i, j});
            }
        }
        var minePairs = new ArrayList<int[]>();
        for (int i = 0; i < difficultyLevel; i++) {
            int key = new Random().nextInt(pairs.size());
            minePairs.add(pairs.get(key));
            pairs.remove(key);
        }
        for (int i = 0; i < difficultyLevel; i++) {
            tiles[minePairs.get(i)[0]][minePairs.get(i)[1]].setMine(true);
        }
        int count;
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                count = 0;
                if (tiles[x][y].isMine()) continue;
                try {
                    if (tiles[x - 1][y - 1].isMine()) count++;
                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    if (tiles[x][y - 1].isMine()) count++;
                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    if (tiles[x + 1][y - 1].isMine()) count++;
                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    if (tiles[x - 1][y].isMine()) count++;
                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    if (tiles[x + 1][y].isMine()) count++;
                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    if (tiles[x - 1][y + 1].isMine()) count++;
                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    if (tiles[x][y + 1].isMine()) count++;
                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    if (tiles[x + 1][y + 1].isMine()) count++;
                } catch (IndexOutOfBoundsException ignored) {
                }
                tiles[x][y].setValue(count);
            }
        }
        //-----------------

        jFrame.setVisible(true);
    }

    public int[] getTileXYFromAction(ActionEvent e) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (tiles[i][j].getButton() == e.getSource()) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{};
    }

    public void debug(ActionEvent e) {
        int x = getTileXYFromAction(e)[0];
        int y = getTileXYFromAction(e)[1];
        tiles[x][y].showInfo();
    }

    public void check() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                tiles[i][j].updateButtonIcon();
            }
        }

        if (isDead) {
            JOptionPane.showMessageDialog(null, "Oops");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    tiles[i][j].disable(this);
                    tiles[i][j].setRevealed(true);
                    tiles[i][j].setFlagged(false);
                    tiles[i][j].updateButtonIcon();
                }
            }
        }
        int points = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (tiles[i][j].isMine()) {
                    if (tiles[i][j].isFlagged()) points++;
                } else {
                    if (!tiles[i][j].isFlagged() && tiles[i][j].isRevealed()) points++;
                }
            }
        }
        if (points == rows * columns) {
            JOptionPane.showMessageDialog(null, "Yay");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    tiles[i][j].disable(this);
                }
            }
        }
    }

    public boolean contains(ArrayList<int[]> set, int[] value) {
        for (int i = 0; i < set.size(); i++) {
            if (set.get(i)[0] == value[0] && set.get(i)[1] == value[1]) return true;
        }
        return false;
    }

    public void growZeros(int[] xy) {
        ArrayList<int[]> next = new ArrayList<>();
        next.add(xy);
        ArrayList<int[]> banned = new ArrayList<>();
        ArrayList<int[]> current = new ArrayList<>();
        while (next.size() != 0) {
            current.clear();
            for (int i = 0; i < next.size(); i++) {
                banned.add(next.get(i));
                current.add(next.get(i));
            }
            next.clear();
            for (int i = 0; i < current.size(); i++) {
                int x = current.get(i)[0];
                int y = current.get(i)[1];
                try {
                    if (!tiles[x][y - 1].isMine()
                            && !tiles[x][y - 1].isFlagged()
                            && tiles[x][y - 1].getValue() == 0
                            && !contains(banned, new int[]{x, y - 1})) {
                        tiles[x][y - 1].setRevealed(true);
                        next.add(new int[]{x, y - 1});
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    if (!tiles[x - 1][y].isMine()
                            && !tiles[x - 1][y].isFlagged()
                            && tiles[x - 1][y].getValue() == 0
                            && !contains(banned, new int[]{x - 1, y})) {
                        tiles[x - 1][y].setRevealed(true);
                        next.add(new int[]{x - 1, y});
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    if (!tiles[x + 1][y].isMine()
                            && !tiles[x + 1][y].isFlagged()
                            && tiles[x + 1][y].getValue() == 0
                            && !contains(banned, new int[]{x + 1, y})) {
                        tiles[x + 1][y].setRevealed(true);
                        next.add(new int[]{x + 1, y});
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    if (!tiles[x][y + 1].isMine()
                            && !tiles[x][y + 1].isFlagged()
                            && tiles[x][y + 1].getValue() == 0
                            && !contains(banned, new int[]{x, y + 1})) {
                        tiles[x][y + 1].setRevealed(true);
                        next.add(new int[]{x, y + 1});
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }
        ArrayList<int[]> check = new ArrayList<>();
        check.add(xy);
        ArrayList<int[]> left = new ArrayList<>();
        for (int i = 0; i < banned.size(); i++) {
            if (!contains(left, banned.get(i))
                    && !contains(check, banned.get(i))) {
                left.add(banned.get(i));
            }
        }
        if (tiles[xy[0]][xy[1]].getValue() == 0) left.add(xy);

        for (int i = 0; i < left.size(); i++) {
            int x = left.get(i)[0];
            int y = left.get(i)[1];
            try {
                if (!tiles[x][y - 1].isMine()
                        && !tiles[x][y - 1].isFlagged()) {
                    tiles[x][y - 1].setRevealed(true);
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
            try {
                if (!tiles[x - 1][y].isMine()
                        && !tiles[x - 1][y].isFlagged()) {
                    tiles[x - 1][y].setRevealed(true);
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
            try {
                if (!tiles[x + 1][y].isMine()
                        && !tiles[x + 1][y].isFlagged()) {
                    tiles[x + 1][y].setRevealed(true);
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
            try {
                if (!tiles[x][y + 1].isMine()
                        && !tiles[x][y + 1].isFlagged()) {
                    tiles[x][y + 1].setRevealed(true);
                }
            } catch (IndexOutOfBoundsException ignored) {
            }

            try {
                if (!tiles[x - 1][y - 1].isMine()
                        && !tiles[x - 1][y - 1].isFlagged()) {
                    tiles[x - 1][y - 1].setRevealed(true);
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
            try {
                if (!tiles[x + 1][y - 1].isMine()
                        && !tiles[x + 1][y - 1].isFlagged()) {
                    tiles[x + 1][y - 1].setRevealed(true);
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
            try {
                if (!tiles[x - 1][y + 1].isMine()
                        && !tiles[x - 1][y + 1].isFlagged()) {
                    tiles[x - 1][y + 1].setRevealed(true);
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
            try {
                if (!tiles[x + 1][y + 1].isMine()
                        && !tiles[x + 1][y + 1].isFlagged()) {
                    tiles[x + 1][y + 1].setRevealed(true);
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int x = getTileXYFromAction(e)[0];
        int y = getTileXYFromAction(e)[1];
        //debug(e);
        if (tiles[x][y].isRevealed()) {
            if (tiles[x][y].isFlagged()) {
                tiles[x][y].setFlagged(false);
                tiles[x][y].setRevealed(false);
            } else {
                int val = 0;
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        try {
                            var tile = tiles[x + i][y + j];
                            if (tile.isFlagged()) val++;
                        } catch (IndexOutOfBoundsException ignored) { }
                    }
                }
                if (val == tiles[x][y].getValue()) {
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            try {
                                tiles[x + i][y + j].setRevealed(true);
                                if (tiles[x + i][y + j].isMine() && !tiles[x + i][y + j].isFlagged()) isDead = true;
                            } catch (IndexOutOfBoundsException ignored) { }
                        }
                    }
                }
            }
        } else {
            if (flagMode) {
                tiles[x][y].setFlagged(true);
                tiles[x][y].setRevealed(true);
            } else {
                if (tiles[x][y].isMine()) {
                    tiles[x][y].setRevealed(true);
                    isDead = true;
                } else {
                    tiles[x][y].setRevealed(true);
                    growZeros(new int[]{x, y});
                }
            }
        }

        check();
    }

    public static class Tile {
        private final int x;
        private final int y;
        private int value;
        private boolean isMine;
        private boolean isFlagged;
        private boolean isRevealed;
        private JButton button;

        public Tile(int x, int y) {
            this.x = x;
            this.y = y;
            this.value = 0;
            this.isFlagged = false;
            this.isRevealed = false;
            setButton();
        }

        public void showInfo() {
            System.out.println("x\t\t\t" + x);
            System.out.println("y\t\t\t" + y);
            System.out.println("value\t\t" + value);
            System.out.println("isMine\t\t" + isMine);
            System.out.println("isFlagged\t" + isFlagged);
            System.out.println("isReveled\t" + isRevealed);
            System.out.println();
        }

        public JButton setButton() {
            button = new JButton();
            Border border = BorderFactory.createEmptyBorder();
            button.setBorder(border);
            button.setBounds(boarder + (x * (size + distBetween)),
                    boarder + (y * (size + distBetween)),
                    size,
                    size);
            updateButtonIcon();
            return button;
        }

        public void updateButtonIcon() {
            if (isFlagged) {
                button.setIcon(new ImageIcon(((new ImageIcon(
                        src + "resources/f.png").getImage()
                        .getScaledInstance(size, size,
                                java.awt.Image.SCALE_SMOOTH)))));
            } else if (isRevealed && isMine) {
                button.setIcon(new ImageIcon(((new ImageIcon(
                        src + "resources/m.png").getImage()
                        .getScaledInstance(size, size,
                                java.awt.Image.SCALE_SMOOTH)))));
            } else if (!isRevealed) {
                button.setIcon(new ImageIcon(((new ImageIcon(
                        src + "resources/n.png").getImage()
                        .getScaledInstance(size, size,
                                java.awt.Image.SCALE_SMOOTH)))));
            } else if (value == 0) {
                button.setIcon(new ImageIcon(((new ImageIcon(
                        src + "resources/rn.png").getImage()
                        .getScaledInstance(size, size,
                                java.awt.Image.SCALE_SMOOTH)))));
            } else if (value > 0) {
                button.setIcon(new ImageIcon(((new ImageIcon(
                        src + "resources/" + value + ".png").getImage()
                        .getScaledInstance(size, size,
                                java.awt.Image.SCALE_SMOOTH)))));
            }
        }

        public void disable(ActionListener actionListener) {
            button.removeActionListener(actionListener);
        }

        public void setActionListener(ActionListener actionListener) {
            button.addActionListener(actionListener);
        }

        public void setKeyListener(KeyAdapter keyAdapter) {
            button.addKeyListener(keyAdapter);
        }

        public JButton getButton() {
            return button;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public boolean isMine() {
            return isMine;
        }

        public boolean isFlagged() {
            return isFlagged;
        }

        public void setFlagged(boolean flagged) {
            isFlagged = flagged;
        }

        public boolean isRevealed() {
            return isRevealed;
        }

        public void setRevealed(boolean revealed) {
            isRevealed = revealed;
        }

        public void setMine(boolean mine) {
            isMine = mine;
        }
    }
}
