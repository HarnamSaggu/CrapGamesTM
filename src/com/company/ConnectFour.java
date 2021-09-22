package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ConnectFour extends JFrame implements ActionListener, KeyListener {
    char[][] cells;
    JLabel[][] slots;
    JButton[] adds;
    int state;
    boolean stillPlaying = true;
    JTextArea log;
    JScrollPane logScroll;
//    String src = "src/";
    String src = "";

    public ConnectFour() {
        setTitle("Connect four");
        setResizable(false);
        setBounds(200, 100, 641, 529);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        adds = new JButton[7];
        for (int i = 0; i < adds.length; i++) {
            adds[i] = new JButton();
            adds[i].setBounds(18 + (64 * i), 10, 48, 48);
            adds[i].addActionListener(this);
            adds[i].setIcon(new ImageIcon(src + "resources/add.png"));
            adds[i].setBorder(null);
            adds[i].addKeyListener(this);
            add(adds[i]);
        }

        cells = new char[7][6];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = 'u';
            }
        }

        slots = new JLabel[7][6];
        for (int i = 0; i < slots.length; i++) {
            for (int j = 0; j < slots[0].length; j++) {
                slots[i][j] = new JLabel(new ImageIcon(src + "resources/" + cells[i][j] + ".png"));
                slots[i][j].setBounds(10 + (64 * i), 30 + (64 * (6 - j)), 64, 64);
                slots[i][j].addKeyListener(this);
                add(slots[i][j]);
            }
        }

        log = new JTextArea("Good luck and have fun\n");
        log.append("Red goes first\n");
        log.setEditable(false);
        log.addKeyListener(this);
        logScroll = new JScrollPane(log);
        logScroll.setBounds(466, 10, 150, 469);
        add(logScroll);

        addKeyListener(this);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (stillPlaying) {
            int addIndex = 0;
            for (int i = 0; i < adds.length; i++) {
                if (e.getSource() == adds[i]) addIndex = i;
            }

            play(addIndex);
        }
    }

    public void play(int addIndex) {
        char[] players = {'r', 'b'};
        for (int i = 0; i < cells[addIndex].length; i++) {
            if (cells[addIndex][i] == 'u') {
                cells[addIndex][i] = players[state % 2];
                slots[addIndex][i].setIcon(new ImageIcon(src + "resources/" + players[state % 2] + ".png"));
                if (players[state % 2] == 'r')
                    log.append("Red placed at (" +  addIndex + ", " + i + ")\n");
                else
                    log.append("Blue placed at (" +  addIndex + ", " + i + ")\n");
                state++;
                break;
            }
        }


        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (cells[i][j] == 'u') continue;

                try {
                    if (cells[i + 1][j] == cells[i][j]
                            && cells[i + 2][j] == cells[i][j]
                            && cells[i + 3][j] == cells[i][j]) {
                        slots[i][j].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i + 1][j].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i + 2][j].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i + 3][j].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        end(cells[i][j]);
                    }
                } catch (Exception ex) {
                }

                try {
                    if (cells[i - 1][j] == cells[i][j]
                            && cells[i - 2][j] == cells[i][j]
                            && cells[i - 3][j] == cells[i][j]) {
                        slots[i][j].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i - 1][j].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i - 2][j].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i - 3][j].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        end(cells[i][j]);
                    }
                } catch (Exception ex) {
                }

                try {
                    if (cells[i + 1][j + 1] == cells[i][j]
                            && cells[i + 2][j + 2] == cells[i][j]
                            && cells[i + 3][j + 3] == cells[i][j]) {
                        slots[i][j].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i + 1][j + 1].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i + 2][j + 2].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i + 3][j + 3].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        end(cells[i][j]);
                    }
                } catch (Exception ex) {
                }

                try {
                    if (cells[i + 1][j - 1] == cells[i][j]
                            && cells[i + 2][j - 2] == cells[i][j]
                            && cells[i + 3][j - 3] == cells[i][j]) {
                        slots[i][j].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i + 1][j - 1].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i + 2][j - 2].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i + 3][j - 3].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        end(cells[i][j]);
                    }
                } catch (Exception ex) {
                }

                try {
                    if (cells[i - 1][j + 1] == cells[i][j]
                            && cells[i - 2][j + 2] == cells[i][j]
                            && cells[i - 3][j + 3] == cells[i][j]) {
                        slots[i][j].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i - 1][j + 1].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i - 2][j + 2].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i - 3][j + 3].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        end(cells[i][j]);
                    }
                } catch (Exception ex) {
                }

                try {
                    if (cells[i - 1][j - 1] == cells[i][j]
                            && cells[i - 2][j - 2] == cells[i][j]
                            && cells[i - 3][j - 3] == cells[i][j]) {
                        slots[i][j].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i - 1][j - 1].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i - 2][j - 2].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i - 3][j - 3].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        end(cells[i][j]);
                    }
                } catch (Exception ex) {
                }

                try {
                    if (cells[i][j + 1] == cells[i][j]
                            && cells[i][j + 2] == cells[i][j]
                            && cells[i][j + 3] == cells[i][j]) {
                        slots[i][j].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i][j + 1].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i][j + 2].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i][j + 3].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        end(cells[i][j]);
                    }
                } catch (Exception ex) {
                }

                try {
                    if (cells[i][j - 1] == cells[i][j]
                            && cells[i][j - 2] == cells[i][j]
                            && cells[i][j - 3] == cells[i][j]) {
                        slots[i][j].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i][j - 1].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i][j - 2].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        slots[i][j - 3].setIcon(new ImageIcon(src + "resources/w" + cells[i][j] + ".png"));
                        end(cells[i][j]);
                    }
                } catch (Exception ex) {
                }
            }
        }
        boolean allFilled = true;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (cells[i][j] == 'u') {
                    allFilled = false;
                    break;
                }
            }
        }
        if (allFilled && stillPlaying) {
            stillPlaying = false;
            JOptionPane.showMessageDialog(null, "Draw");
            log.append("Draw\n");
        }
    }

    public void end(char winner) {
        if (stillPlaying) {
            if (winner == 'r')
                log.append("Red won\n");
            else
                log.append("Blue won\n");
            stillPlaying = false;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (Character.getNumericValue(e.getKeyChar()) > 0
                && Character.getNumericValue(e.getKeyChar()) < 8
                && stillPlaying)
            play(Character.getNumericValue(e.getKeyChar()) - 1);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
