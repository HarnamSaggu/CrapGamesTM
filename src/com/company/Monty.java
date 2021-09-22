package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Monty implements ActionListener {
//    String src = "src/";
    String src = "";
    JFrame jFrame;
    String[] paths;
    JButton[] doors;
    int gameState = 0;
    int rightDoor = new Random().nextInt(3);
    int firstDoor;
    int openedDoor;

    public Monty() {
        jFrame = new JFrame("Are you trying to monty hall me?");
        jFrame.setSize(460, 310);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setLayout(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        paths = new String[6];
        paths[0] = src + "resources/0d.png";
        paths[1] = src + "resources/1d.png";
        paths[2] = src + "resources/2d.png";
        paths[3] = src + "resources/l.png";
        paths[4] = src + "resources/w.png";
        paths[5] = src + "resources/z.png";

        doors = new JButton[3];
        for (int i = 0; i < 3; i++) {
            doors[i] = new JButton(new ImageIcon(paths[i]));
            doors[i].setBounds(i * 144 + 10, 10, 134, 249);
            doors[i].addActionListener(this);
            jFrame.add(doors[i]);
        }

        jFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (gameState) {
            case 0 -> {
                for (int i = 0; i < 3; i++) {
                    if (e.getSource() == doors[i]) firstDoor = i;
                    if (i != rightDoor && i != firstDoor) openedDoor = i;
                }
                doors[openedDoor].setIcon(new ImageIcon(paths[5]));
                doors[openedDoor].setEnabled(false);
                gameState++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
            case 1 -> {
                for (int i = 0; i < 3; i++) {
                    if (e.getSource() == doors[i] && i == rightDoor) {
                        doors[i].setIcon(new ImageIcon(paths[4]));
                        try {
                            doors[i + 2].setEnabled(false);
                        } catch (IndexOutOfBoundsException ignored) { }
                        try {
                            doors[i + 1].setEnabled(false);
                        } catch (IndexOutOfBoundsException ignored) { }
                        try {
                            doors[i - 2].setEnabled(false);
                        } catch (IndexOutOfBoundsException ignored) { }
                        try {
                            doors[i - 1].setEnabled(false);
                        } catch (IndexOutOfBoundsException ignored) { }
                    } else if (e.getSource() == doors[i] && i != rightDoor) {
                        doors[i].setIcon(new ImageIcon(paths[5]));
                        doors[rightDoor].setIcon(new ImageIcon(paths[3]));
                        doors[i].setEnabled(false);
                    }
                }
                gameState++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
            case 2 -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
//                System.exit(0);
                jFrame.dispose();
            }
        }
    }
}
