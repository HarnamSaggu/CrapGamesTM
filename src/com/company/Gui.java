package com.company;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class Gui extends JFrame {
    JLabel pic;
//    String path1 = "src/resources/gameFrame";
    String path1 = "resources/gameFrame";
    String path2 = ".png";
    int level = 1;
    JTextField in;
    JTextArea console;
    JScrollPane pane;
    JTextArea banned;
    JScrollPane banPane;
    String banList = "";
    String userWord = "";
    JLabel wordLabel;

    public Gui(String word) {
        super("Hangman");
        setSize(750, 345);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pic = new JLabel(new ImageIcon(path1 + level + path2));
        pic.setBounds(25, 25, 250, 250);
        add(pic);

        in = new JTextField();
        in.setBounds(305, 255, 260, 20);
        add(in);

        console = new JTextArea(" Good luck and have fun\n");
        console.setEditable(false);

        pane = new JScrollPane(console);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setBounds(305, 25, 260, 225);
        add(pane);

        banned = new JTextArea(" Incorrect letters\n");
        banned.setEditable(false);

        banPane = new JScrollPane(banned);
        banPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        banPane.setBounds(590, 25, 125, 250);
        add(banPane);

        for (int i = 0; i < word.length(); i++) {
            userWord += "*";
        }

        wordLabel = new JLabel("So far you have : " + userWord);
        wordLabel.setBounds(25, 275, 500, 25);
        add(wordLabel);

        in.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //System.out.println(e.getKeyChar());
                char guess = e.getKeyChar();
                if (word.contains(String.valueOf(guess)) && "abcdefghijklmnopqrstuvwxyz ".contains(String.valueOf(guess))) {
                    String newWord = "";
                    for (int i = 0; i < word.length(); i++) {
                        if (word.charAt(i) == guess) {
                            newWord += guess;
                        } else {
                            newWord += userWord.charAt(i);
                        }
                    }
                    userWord = newWord;
                    console.setText(console.getText() + " " + newWord + "\n");
                    wordLabel.setText("So far you have : " + userWord);
                    if (!newWord.contains("*")) {
                        //WIN
                        in.setEditable(false);
                        in.removeKeyListener(this);
                        console.setText(console.getText() + " You win\n");
                    }
                } else if ("abcdefghijklmnopqrstuvwxyz".contains(String.valueOf(guess)) && !banList.contains(String.valueOf(guess))) {
                    banned.setText(banned.getText() + " " + guess + "\n");
                    banList += guess;
                    level++;
                    File file = new File(path1 + (level + 1) + path2);
                    if (file.exists()) {
                        pic.setIcon(new ImageIcon(path1 + level + path2));
                        console.setText(console.getText() + " Woopsie, " + guess + " is wrong\n");
                    } else {
                        //GAME OVER
                        pic.setIcon(new ImageIcon(path1 + level + path2));
                        in.setEditable(false);
                        in.removeKeyListener(this);
                        console.setText(console.getText() + " Game over");
                        wordLabel.setText("It was " + word);
                    }
//                    System.out.println(path1 + level + path2);
                }
                in.setText("");
            }
        });
    }
}
