//Minefield.java

/**
 * This program is a simple implementation of the standard game of minesweeper
 * that comes by default on most computers. Like the traditional minesweeper, there
 * are 3 preset difficulty levels: beginner, intermediate, and expert. Each 
 * difficulty level has a different board size and a different number of randomly
 * placed mines. To win, a player must activate all the non-mine cells by left-
 * clicking them. If the player would like visual assistance in keeping track of 
 * where he/she thinks the mines are, he/she may right-click a cell to place a 
 * flag. If he/she would like to remove the flag, he/she must simply right-click 
 * that cell again. Each time a flag is placed or removed, the flag counter, which
 * is displayed above the board gets decremented or incremented respectively. The
 * player will not be permitted to place more flags than there are total number 
 * of mines. If the player tries to do so, a dialog box will inform him/her that
 * he/she is not permitted to do that.
 *
 * @author	Tiffany Chang
 * @version	05_05_2016
 **/
 
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Minefield extends JFrame
{
	private int level;
	private char [][] minefield;
	private int boardRows, boardColumns;    //Game board dimensions.
	private int totMines, totNonmines;
	private int nonmineCount;
	private boolean gameOver = false;
	private boolean isWinner = false;
	
	private Container cp;
	private JButton [][] jbuttonfield;
	private Font buttonFont = new Font ("Helvetica", Font.BOLD, 20);
	private JPanel banner = new JPanel();
	private JPanel field = new JPanel();
	private JLabel flagCountdown;
	private int flagCount;
	
	private final int cellHeight = 30;
	private final int cellWidth = 30;
	
	//Constructor.
	public Minefield(int level) 
	{
		//Set values depending on the difficulty level selected by the user.
		if (level == 1) {
			boardRows = 9;
			boardColumns = 9;
			totMines = 10;
		}
		else if (level == 2) {
			boardRows = 16;
			boardColumns = 16;
			totMines = 40;
		}
		else if (level == 3) {
			boardRows = 16;
			boardColumns = 30;
			totMines = 99;
		}
		flagCount = totMines;
		totNonmines = boardRows * boardColumns - totMines;
		nonmineCount = 0;
		
		//Set the board size.
		minefield = new char [boardRows][boardColumns];
		
		//Set the basic structure of the gameboard GUI.
		cp = this.getContentPane();
		setTitle("Minesweeper");
		setSize(boardColumns * cellWidth, boardRows * cellHeight + 40);
		banner.setPreferredSize(new Dimension(boardColumns * cellWidth, 40));
		flagCountdown = new JLabel("Flags remaining: " + flagCount);
		banner.add(flagCountdown);
		add(banner, BorderLayout.NORTH);    //Put the game configuration menu at the top of the JFrame.
	}
	
	/**
	 * This method randomly places the correct number of mines according to
	 * the difficulty level indicated by the user at the start of the game.
	 *
	 */
	public void placeMines()
	{
		int mineCount = 0;
		while (mineCount < totMines)
		{
			//Randomly generate the indices of a mine location.
		    int mineRow = (int)(Math.random() * boardRows);
		    int mineColumn = (int)(Math.random() * boardColumns);
		    
			//If there isn't already a mine in the 2D char minefield, place a mine.
			if (minefield [mineRow][mineColumn] != '*') {
				minefield [mineRow][mineColumn] = '*';
				mineCount++;
			}
		}
	}
	
	/**
	 * This method places numbers in the 2D character minefield array that
	 * indicate the number of mines that are touching each cell that does
	 * not have a mine.
	 *
	 */
	public void placeNumbers()
	{
		for (int i = 0; i < boardRows; i++) {
			for (int j = 0; j < boardColumns; j++) {
				if (minefield [i][j] != '*') {
					minefield [i][j] = (char)(checkSurrounding(i, j) + 48);
                }
			}
		}
	}
	
	/**
	 * This method receives the row and column of the index cell and checks all
	 * cells touching the index cell in the 2D character minefield array
	 * (including diagonal cells). Returns an integer value with the number of 
	 * mines '*' that are touching the index cell.
	 *
	 * @param	indexRow		The row number of the index cell.
	 * @param	indexColumn		The column number of the index cell.
	 * @return				    The number of mines the index cell is touching.
	 */
	public int checkSurrounding(int indexRow, int indexColumn)
	{
		int mineCount = 0;
		for (int di = -1; di < 2; di++) {
			for (int dj = -1; dj < 2; dj++) {
				int checkedRow = indexRow + di;
				int checkedColumn = indexColumn + dj;
				if ( (checkedRow >= 0 && checkedRow < boardRows) &&     //Only check the cells that fall
					 (checkedColumn >= 0 && checkedColumn < boardColumns) ) {    //within the board.
				    if (minefield [checkedRow][checkedColumn] == '*')
				    	mineCount++;
				}
				
			}
		}
		return mineCount;
	}
	
	/**
	 * This method prints out the underlying 2D character minefield array that
	 * will be "covered" by JButtons for actual gameplay. This method is primarily
	 * for testing purposes.
	 *
	 */
	public void printMinefield()
	{
		for (int i = 0; i < boardRows; i++) {
			for (int j = 0; j < boardColumns; j++) {
				System.out.print(minefield [i][j] + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * This method creates the 2D JButton field array that "covers" the underlying
	 * 2D character minefield array.
	 *
	 */
	public void coverMinefield()
	{
		jbuttonfield = new JButton [boardRows][boardColumns];
		for (int i = 0; i < boardRows; i++) {
        	for (int j = 0; j < boardColumns; j++) {
        		if ( jbuttonfield[i][j] == null) {
        	        jbuttonfield[i][j] = new JButton();    //Initialize the buttons.
                    jbuttonfield[i][j].addMouseListener(new ButtonListener());
                    field.add(jbuttonfield[i][j]);    //Add each JButton to the GridLayout, center (field) JPanel.
                }
            }
        }
        field.setLayout( new GridLayout(boardRows, boardColumns) );
        cp.add(field, BorderLayout.CENTER);    //Put the minefield of JButtons in the center of the JFrame.
	}
	
	/**
	 * This method calls on all the appropriate methods to setup the game board.
	 *
	 */
	public void setBoard()
	{
		placeMines();
		placeNumbers();
		coverMinefield();
	}
	
	//Inner class to respond to any button being clicked.
	class ButtonListener extends MouseAdapter //implements ActionListener
    {
    	/**
         * This method determines the appropriate course of action based on the
         * source of the MouseEvent. It distinguishes between right and left mouse
         * clicks.
         *
         */
        public void mousePressed(MouseEvent e) 
	    {
	        if(e.getButton() == MouseEvent.BUTTON1)    //Detected Mouse Left Click.
	        {
	            for (int i = 0; i < boardRows; i++) {
                    for (int j = 0; j < boardColumns; j++) {
                        if (e.getSource() == jbuttonfield[i][j])    //Responds to a tile left mouse click.
                        {
                        	if (minefield[i][j] == '*') {
                        		gameOver = true;
                        		endGame();
                        	}
                        	else {
                                jbuttonfield[i][j].setMargin(new Insets(1,1,1,1));
                            	jbuttonfield[i][j].setFont(buttonFont);
                            	jbuttonfield[i][j].setText("" + minefield[i][j]);
                                jbuttonfield[i][j].setEnabled(false);
                                nonmineCount++;
                                if ( nonmineCount >= totNonmines ) {
                                	gameOver = true;
                                	isWinner = true;
                        		    endGame();
                                }
                            }
                        }
                    }
                }
	        }	    
	        else if(e.getButton() == MouseEvent.BUTTON3)    //Detected Mouse Right Click.
	        {
	        	for (int i = 0; i < boardRows; i++) {
                    for (int j = 0; j < boardColumns; j++) {
                    	jbuttonfield[i][j].setMargin(new Insets(1,1,1,1));
                        jbuttonfield[i][j].setFont(buttonFont);
                        if (e.getSource() == jbuttonfield[i][j])    //Responds to a tile right mouse click.
                        {
                        	if (jbuttonfield[i][j].getText() == "") {
                                flagCount--;
                                if (totMines - flagCount > totMines) {    //Player may not place more flags than total # of mines.
                                	flagCount++;    //Fixes the flag count.
                                	JOptionPane.showMessageDialog(null, "You may not place more flags than total number of mines!");
                                }
                                else {
                        		    jbuttonfield[i][j].setText("\u25BA");
                                    jbuttonfield[i][j].setEnabled(false);
                                }
                        	}
                        	else if (jbuttonfield[i][j].getText() == "\u25BA") {
                                flagCount++;
                        		jbuttonfield[i][j].setText("");
                                jbuttonfield[i][j].setEnabled(true);
                        	}
                        	flagCountdown.setText("Flags remaining: " + flagCount);
                        }
                    }
                }
	        }
	    }
	    
	    /**
	     * This method is called when the conditions that satisfy the end of the
	     * game are met, either: (1) the player has activated all the JButtons 
	     * that cover non-mine characters or (2) the player has clicked on a mine.
	     * In the first case, the player wins, and a congratulatory message is
	     * displayed in a dialog box. In the second case, the player loses, and
	     * a sorry message is displayed in a dialog box. In the event of a loss,
	     * the entire minefield is activated so the player can see the answer to 
	     * the minesweeper puzzle.
	     *
    	 */
	    public void endGame()
	    {
	    	if (isWinner) {
	    		JOptionPane.showMessageDialog(null, "Congratulations, you have won! :)");
	    	}
	    	else {
	    		for (int i = 0; i < boardRows; i++) {
                    for (int j = 0; j < boardColumns; j++) {
                    	jbuttonfield[i][j].setMargin(new Insets(1,1,1,1));
                        jbuttonfield[i][j].setFont(buttonFont);
                        jbuttonfield[i][j].setText("" + minefield [i][j]);
                        jbuttonfield[i][j].setEnabled(false);
                    }
                }
	    		JOptionPane.showMessageDialog(null, "I'm sorry, you have lost! :(");
	    	}
	    }
    }
}