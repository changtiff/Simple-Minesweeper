//Minesweeper.java

/**
 * This program sets up the introduction to the Minesweeper game. When the game
 * starts, a dialog box asks the user what difficulty level of Minesweeper he/she
 * would like to play. It takes the inputted level and initializes the game.
 *
 * @author	Tiffany Chang
 * @version	05_05_2016
 **/
 
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Minesweeper
{
	public static void main (String [] args)
	{
	    Integer level = 0;
	    boolean levelSet = false;
	    
		do {
		    String levelInput = JOptionPane.showInputDialog(null, "Please enter desired difficulty level: 1=Beginner, 2=Intermediate, 3=Expert");
		    if (levelInput == null)
		    	System.exit(0);
		    try {
		        level = Integer.parseInt(levelInput);
		        if (level < 1 || level > 3) {
		    	    JOptionPane.showMessageDialog(null, "Please enter a valid number!");
		        }
		        else
		        	levelSet = true;
		    } catch (NumberFormatException e) {
		    	JOptionPane.showMessageDialog(null, "Please enter a valid number!");
		    }
		    
		} while (!levelSet);
		
		Minefield gameboard = new Minefield(level);
		gameboard.setBoard();
		gameboard.printMinefield();
		
		gameboard.setVisible(true);
		gameboard.setResizable(false);
        gameboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}