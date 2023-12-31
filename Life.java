/*
* This program is an implementation of Conway's Life simulation
* Author: Ayan Masud
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Life implements MouseListener, ActionListener, Runnable {

    // variables and objects
    boolean[][] cells = new boolean[25][25];
    JFrame frame = new JFrame("Life simulation");
    LifePanel panel = new LifePanel(cells);
    Container south = new Container();
    JButton step = new JButton("Step");
    JButton start = new JButton("Start");
    JButton stop = new JButton("Stop");
    boolean running = false;

    public Life(){ // constructor
        frame.setSize(600,600);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        panel.addMouseListener(this);

        // south container
        south.setLayout(new GridLayout(1,3));
        south.add(step);
        step.addActionListener(this);
        south.add(start);
        start.addActionListener(this);
        south.add(stop);
        stop.addActionListener(this);
        frame.add(south, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args){ // starts the code
        new Life();
    }

    @Override
    public void mouseClicked(MouseEvent event) { // button goes down and up which is basically a click

    }

    @Override
    public void mousePressed(MouseEvent event) { // button goes down over it

    }

    @Override
    public void mouseReleased(MouseEvent event) { // button comes back up over it
        System.out.println(event.getX() + "," + event.getY());
        double width = (double)panel.getWidth() / cells[0].length;
        double height = (double)panel.getHeight() / cells.length;
        int column = Math.min(cells[0].length - 1, (int)(event.getX() / width));
        int row = Math.min(cells.length - 1, (int)(event.getY() / height));
        System.out.println(column + "," + row);
        cells[row][column] = !cells[row][column];
        frame.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent event) { // you've gone into a particular component

    }

    @Override
    public void mouseExited(MouseEvent event) { // opposite of mouseEntered()

    }

    @Override
    public void actionPerformed(ActionEvent event) { // can make something happen when something like a button is pressed
        if (event.getSource().equals(step)){
            Step();
        }
        if (event.getSource().equals(start)){
            if (running == false){
                running = true;
                Thread t = new Thread(this);
                t.start();
            }
        }
        if (event.getSource().equals(stop)){
            running = false;
        }
    }
    @Override
    public void run() {
        while (running == true){
            Step();
            try {
                Thread.sleep(500);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    /*
     * row-1,column-1       row-1,column        row-1,column+1
     * row,column-1         row,column          row,column+1
     * row+1,column-1       row+1,column        row+1,column+1
     */
    public void Step(){
        boolean[][] nextCells = new boolean[cells.length][cells[0].length];
        for (int row = 0; row < cells.length; row++) {
            for (int column = 0; column < cells[0].length; column++) {
                int neighborCount = 0;
                // checks the rules for the steps
                if (row > 0 && column > 0 && cells[row-1][column-1] == true){
                    neighborCount++;
                }
                if (row > 0 && cells[row-1][column] == true){
                    neighborCount++;
                }
                if (row > 0 && column < cells[0].length-1 && cells[row-1][column+1] == true){
                    neighborCount++;
                }
                if (column > 0 && cells[row][column-1] == true){
                    neighborCount++;
                }
                if (column < cells[0].length-1 && cells[row][column+1] == true){
                    neighborCount++;
                }
                if (row < cells.length-1 && column > 0 && cells[row+1][column-1] == true){
                    neighborCount++;
                }
                if (row < cells.length-1 && cells[row+1][column] == true){
                    neighborCount++;
                }
                if (row < cells.length-1 && column < cells[0].length-1 && cells[row+1][column+1] == true){
                    neighborCount++;
                }
                // rules of life
                if (cells[row][column] == true) { // I'm alive
                    if (neighborCount == 2 || neighborCount == 3) {
                        nextCells[row][column] = true; // Alive still
                    }
                    else {
                        nextCells[row][column] = false; // Dead next time
                    }
                }
                else { // dead.
                    if (neighborCount == 3) {
                        nextCells[row][column] = true; // alive again
                    }
                    else {
                        nextCells[row][column] = false; // still dead
                    }
                }
            }
        }
        // redraws the squares after knowing what will happen next
        cells = nextCells;
        panel.setCells(nextCells);
        frame.repaint();
    }
}
