package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Chopsticks implements ActionListener {
    Hands hand1, hand2;
    JFrame jFrame;
    JButton l1, r1, l2, r2;
    JButton split1, split2;
    JList<ArrayList<Integer>> list1;
    JList<ArrayList<Integer>> list2;
    int gameState = 0;
    int chosenHand;
    DefaultListModel<ArrayList<Integer>> l1s;
    DefaultListModel<ArrayList<Integer>> l2s;
    JLabel label;
    //    static String src = "src/";
    static String src = "";

    public Chopsticks() {
        hand1 = new Hands();
        hand2 = new Hands();

        jFrame = new JFrame("Chopsticks");
        jFrame.setLayout(null);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setBounds(50, 50, 460, 325);

        l1 = new JButton(new ImageIcon(hand1.leftPath()));
        r1 = new JButton(new ImageIcon(hand1.rightPath()));
        l2 = new JButton(new ImageIcon(hand2.leftPath()));
        r2 = new JButton(new ImageIcon(hand2.rightPath()));

        l1.setBounds(10, 10, 86, 106);
        r1.setBounds(106, 10, 86, 106);
        l2.setBounds(252, 10, 86, 106);
        r2.setBounds(348, 10, 86, 106);

        l1.addActionListener(this);
        r1.addActionListener(this);
        l2.addActionListener(this);
        r2.addActionListener(this);

        jFrame.add(l1);
        jFrame.add(r1);
        jFrame.add(l2);
        jFrame.add(r2);

        split1 = new JButton("<html><font size=\"4\"><i>Split</i></font><html>");
        split2 = new JButton("<html><font size=\"4\"><i>Split</i></font><html>");

        split1.setBounds(10, 126, 181, 30);
        split2.setBounds(252, 126, 181, 30);

        split1.addActionListener(this);
        split2.addActionListener(this);

        jFrame.add(split1);
        jFrame.add(split2);

        l1s = new DefaultListModel<>();
        l2s = new DefaultListModel<>();

        var p1 = hand1.possibleSplits();
        var p2 = hand2.possibleSplits();

        for (int i = 0; i < p1.size(); i++) {
            var ret = new ArrayList<Integer>();
            ret.add(p1.get(i)[0]);
            ret.add(p1.get(i)[1]);
            l1s.addElement(ret);
        }

        for (int i = 0; i < p2.size(); i++) {
            var ret = new ArrayList<Integer>();
            ret.add(p2.get(i)[0]);
            ret.add(p2.get(i)[1]);
            l2s.addElement(ret);
        }

        list1 = new JList<>(l1s);
        list2 = new JList<>(l2s);

        list1.setBounds(10, 166, 181, 80);
        list2.setBounds(252, 166, 181, 80);

        jFrame.add(list1);
        jFrame.add(list2);

        label = new JLabel("Player 1's turn (choose hand or split)");
        label.setBounds(10, 256, 440, 20);
        jFrame.add(label);

        jFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (gameState % 4) {
            case 0 -> {
                if (e.getSource() == l1 && hand1.getLeftC() != 0) {
                    chosenHand = 0;
                    gameState++;
                }
                if (e.getSource() == r1 && hand1.getRightC() != 0) {
                    chosenHand = 1;
                    gameState++;
                }
                if (e.getSource() == split1 && l1s.size() != 0 && list1.getSelectedValue() != null) {
                    hand1.setHands(new int[]{(list1.getSelectedValue()).get(0), (list1.getSelectedValue()).get(1)});
                    gameState += 2;
                }
            }
            case 1 -> {
                if (e.getSource() == l2 && hand2.getLeftC() != 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    attack(2);
                    gameState++;
                }
                if (e.getSource() == r2 && hand2.getRightC() != 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    attack(3);
                    gameState++;
                }
                if (e.getSource() == l1 && hand1.getLeftC() != 0) {
                    chosenHand = 0;
                }
                if (e.getSource() == r1 && hand1.getRightC() != 0) {
                    chosenHand = 1;
                }
            }
            case 2 -> {
                if (e.getSource() == l2 && hand2.getLeftC() != 0) {
                    chosenHand = 2;
                    gameState++;
                }
                if (e.getSource() == r2 && hand2.getRightC() != 0) {
                    chosenHand = 3;
                    gameState++;
                }
                if (e.getSource() == split2 && l2s.size() != 0 && list2.getSelectedValue() != null) {
                    hand2.setHands(new int[]{(list2.getSelectedValue()).get(0), (list2.getSelectedValue()).get(1)});
                    gameState += 2;
                }
            }
            case 3 -> {
                if (e.getSource() == l1 && hand1.getLeftC() != 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    attack(0);
                    gameState++;
                }
                if (e.getSource() == r1 && hand1.getRightC() != 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    attack(1);
                    gameState++;
                }
                if (e.getSource() == l2 && hand2.getLeftC() != 0) {
                    chosenHand = 2;
                }
                if (e.getSource() == r2 && hand2.getRightC() != 0) {
                    chosenHand = 3;
                }
            }
        }

        checkState();
    }

    public void attack(int otherHand) {
        switch (otherHand) {
            case 0 -> {
                int a = 0;
                if (chosenHand == 2) {
                    a = hand2.getLeftC();
                }
                if (chosenHand == 3) {
                    a = hand2.getRightC();
                }
                int b = hand1.getLeftC();
                hand1.setLeftC(a + b);
            }
            case 1 -> {
                int a = 0;
                if (chosenHand == 2) {
                    a = hand2.getLeftC();
                }
                if (chosenHand == 3) {
                    a = hand2.getRightC();
                }
                int b = hand1.getRightC();
                hand1.setRightC(a + b);
            }
            case 2 -> {
                int a = 0;
                if (chosenHand == 0) {
                    a = hand1.getLeftC();
                }
                if (chosenHand == 1) {
                    a = hand1.getRightC();
                }
                int b = hand2.getLeftC();
                hand2.setLeftC(a + b);
            }
            case 3 -> {
                int a = 0;
                if (chosenHand == 0) {
                    a = hand1.getLeftC();
                }
                if (chosenHand == 1) {
                    a = hand1.getRightC();
                }
                int b = hand2.getRightC();
                hand2.setRightC(a + b);
            }
        }
    }

    public void checkState() {
        switch (gameState % 4) {
            case 0 -> label.setText("Player 1's turn (choose hand or split)");
            case 1 -> label.setText("Player 1's turn (choose hand to attack)");
            case 2 -> label.setText("Player 2's turn (choose hand or split)");
            case 3 -> label.setText("Player 2's turn (choose hand to attack)");
        }

        l1.setIcon(new ImageIcon(hand1.leftPath()));
        r1.setIcon(new ImageIcon(hand1.rightPath()));
        l2.setIcon(new ImageIcon(hand2.leftPath()));
        r2.setIcon(new ImageIcon(hand2.rightPath()));

        l1s.clear();
        l2s.clear();

        var p1 = hand1.possibleSplits();
        var p2 = hand2.possibleSplits();

        for (int i = 0; i < p1.size(); i++) {
            var ret = new ArrayList<Integer>();
            ret.add(p1.get(i)[0]);
            ret.add(p1.get(i)[1]);
            l1s.addElement(ret);
        }

        for (int i = 0; i < p2.size(); i++) {
            var ret = new ArrayList<Integer>();
            ret.add(p2.get(i)[0]);
            ret.add(p2.get(i)[1]);
            l2s.addElement(ret);
        }

        if ((hand1.getRightC() == 0 && hand1.getLeftC() == 0) || (hand2.getRightC() == 0 && hand2.getLeftC() == 0)) {
            if (hand1.getRightC() == 0 && hand1.getLeftC() == 0) JOptionPane.showMessageDialog(jFrame, "Player 2 won");
            else JOptionPane.showMessageDialog(jFrame, "Player 1 won");
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.exit(0);
            jFrame.dispose();
        }
    }

    private static class Hands {
        private int leftC;
        private int rightC;

        public Hands() {
            leftC = 1;
            rightC = 1;
        }

        public void setHands(int[] setup) {
            leftC = setup[0];
            rightC = setup[1];
            reset();
        }

        public void reset() {
            if (leftC > 4) leftC = 0;
            if (rightC > 4) rightC = 0;
        }

        public ArrayList<int[]> possibleSplits() {
            var ret = new ArrayList<int[]>();
            if (leftC == 1 && rightC == 1) {
                ret.add(new int[]{2, 0});
                ret.add(new int[]{0, 2});
                return ret;
            }
            if ((leftC == 1 && rightC == 2) || (leftC == 2 && rightC == 1)) {
                ret.add(new int[]{3, 0});
                ret.add(new int[]{0, 3});
            }
            if (leftC == 2 && rightC == 2) {
                ret.add(new int[]{4, 0});
                ret.add(new int[]{0, 4});
                ret.add(new int[]{3, 1});
                ret.add(new int[]{1, 3});
            }
            if ((leftC == 2 && rightC == 3) || (leftC == 3 && rightC == 2)) {
                ret.add(new int[]{4, 1});
                ret.add(new int[]{1, 4});
            }
            if (leftC == 3 && rightC == 3) {
                ret.add(new int[]{4, 2});
                ret.add(new int[]{2, 4});
            }
            if ((leftC == 1 && rightC == 3) || (leftC == 3 && rightC == 1)) {
                ret.add(new int[]{2, 2});
                ret.add(new int[]{4, 0});
                ret.add(new int[]{0, 4});
            }
            if ((leftC == 1 && rightC == 4) || (leftC == 4 && rightC == 1)) {
                ret.add(new int[]{2, 3});
                ret.add(new int[]{3, 2});
            }
            if ((leftC == 2 && rightC == 4) || (leftC == 4 && rightC == 2)) {
                ret.add(new int[]{3, 3});
            }
            if ((leftC == 2 && rightC == 0) || (leftC == 0 && rightC == 2)) {
                ret.add(new int[]{1, 1});
            }
            if ((leftC == 3 && rightC == 0) || (leftC == 0 && rightC == 3)) {
                ret.add(new int[]{2, 1});
                ret.add(new int[]{1, 2});
            }
            if ((leftC == 4 && rightC == 0) || (leftC == 0 && rightC == 4)) {
                ret.add(new int[]{3, 1});
                ret.add(new int[]{1, 3});
                ret.add(new int[]{2, 2});
                ret.add(new int[]{2, 2});
            }
            return ret;
        }

        public String leftPath() {
            if (leftC == 0 || leftC > 4) return src + "resources/0.png";
            return src + "resources/" + leftC + "l.png";
        }

        public String rightPath() {
            if (rightC == 0 || rightC > 4) return src + "resources/0.png";
            return src + "resources/" + rightC + "r.png";
        }

        public int getLeftC() {
            return leftC;
        }

        public void setLeftC(int leftC) {
            this.leftC = leftC;
            reset();
        }

        public int getRightC() {
            return rightC;
        }

        public void setRightC(int rightC) {
            this.rightC = rightC;
            reset();
        }
    }
}
