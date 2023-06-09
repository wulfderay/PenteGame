import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Container;
import javax.swing.JFrame;
/**
The Board for the Pente game. 
It will display the game being played by drawing a set of buttons grouped in a BOARD_SIZE x BOARD_SIZE square.  It will support history, as well as warnings.
@author Mark Lee 100123607
*/
public class PenteBoard extends JPanel implements ActionListener
{

	// constants	
	public static final int BOARD_SIZE = 19;
	
	// for referring to data in each square.
	public static final char PENTE_EMPTY = 0;
	public static final char PENTE_PLAYER_1 = 1;
	public static final char PENTE_PLAYER_2 = 2;
	
	ImageIcon crossIcon = new ImageIcon("cross.gif");
	ImageIcon topIcon = new ImageIcon("top.gif");
	ImageIcon bottomIcon = new ImageIcon("bottom.gif");
	ImageIcon leftIcon = new ImageIcon("left.gif");
	ImageIcon rightIcon = new ImageIcon("right.gif");

	// the corner icons
	ImageIcon bottomLeftIcon = new ImageIcon("bottomleft.gif");
	ImageIcon topLeftIcon = new ImageIcon("topleft.gif");
	ImageIcon bottomRightIcon = new ImageIcon("bottomright.gif");
	ImageIcon topRightIcon = new ImageIcon("topright.gif");
	
	ImageIcon playerOneIcon = new ImageIcon("player1.gif");
	ImageIcon playerTwoIcon = new ImageIcon("player2.gif");
	// the visual representation of the board
	private JButton [][] boardButtons = new JButton[BOARD_SIZE +2][BOARD_SIZE +2]; // we need borders
	private char [][] boardData = new char[BOARD_SIZE+2][BOARD_SIZE+2];  // we need to seperate the data from the front end.
	
	private Move [] history = new Move[(BOARD_SIZE+2)*(BOARD_SIZE+2)];
	private int turns = 0;
	private char whoseTurn = PENTE_PLAYER_2; 
	private int playerOnePoints = 0;
	private int playerTwoPoints = 0;
	private int InARow = 0; // how many in a row;
	
	/**
	Creates a pente board, ready to play.  This constructor handles the buttons automatically, so it does not know when to stop, or reset, etc.  use the next constructorr for most games.
	*/
	public PenteBoard()
	{
		setLayout( new GridLayout(BOARD_SIZE+2,BOARD_SIZE+2) );
		// set the playable area up
		for(int row = 0; row < BOARD_SIZE +2; row++)
		{
			for (int col = 0; col < BOARD_SIZE +2; col++)
			{
				boardButtons[row][col] = new JButton();
				boardButtons[row][col].addActionListener(this);
				add(boardButtons[row][col]); // add it to the panel
			}
		}
		reset(); // set all the data to empty, ant the icons to empty, clear the history, and make a first move.
	}
	/**
	Creates a pente board, which needs logic to run.
	This constructor is designed to give more freedom of how the game is to progress, by giving control of the button presses to an outside class. useful when your clients are not human.
	@param theListener the class that will handle the button presses
	*/
	public PenteBoard(ActionListener theListener)
	{
		setLayout( new GridLayout(BOARD_SIZE+2,BOARD_SIZE+2) );
		// set the playable area up
		for(int row = 0; row < BOARD_SIZE +2; row++)
		{
			for (int col = 0; col < BOARD_SIZE +2; col++)
			{
				boardButtons[row][col] = new JButton();
				boardButtons[row][col].addActionListener(theListener);
				add(boardButtons[row][col]); // add it to the panel
			}
		}
		reset(); // set all the data to empty, ant the icons to empty, clear the history, and make a first move.
	}
	/**
	Sets up a fresh pente board. Used at the beginning of any game. Can be hooked up to a reset button.
	*/
	public void reset()
	{
		InARow = 0;
		turns = 0;
		playerOnePoints = 0;
		playerTwoPoints = 0;
		whoseTurn = PENTE_PLAYER_2;
		for(int row = 0; row < BOARD_SIZE +2; row++)
		{
			for (int col = 0; col < BOARD_SIZE +2; col++)
			{
				// this area should happen for all buttons
				boardButtons[row][col].setBorderPainted(false);
				boardData[row][col] = PENTE_EMPTY;
				if (row == 0) // on top row
				{
					if (col == 0) // top left
				 	{
				 		boardButtons[row][col].setIcon(topLeftIcon);
				 	}
				 	else if (col == BOARD_SIZE +1) // top right
				 	{
				 		boardButtons[row][col].setIcon(topRightIcon);
				 	}
				 	else // somewhere on the top
				 	{
				 		boardButtons[row][col].setIcon(topIcon);
				 	}
				}
				else if (row == BOARD_SIZE +1) // bottom of board
				{
					if (col == 0) // bottom left
				 	{
				 		boardButtons[row][col].setIcon(bottomLeftIcon);
				 	}
				 	else if (col == BOARD_SIZE +1) // bottom right
				 	{
				 		boardButtons[row][col].setIcon(bottomRightIcon);
				 	}
				 	else // somewhere on the bottom
				 	{
				 		boardButtons[row][col].setIcon(bottomIcon);
				 	}
				}
				else // we are in the middle of the board
				{
					if (col == 0) // left
				 	{
				 		boardButtons[row][col].setIcon(leftIcon);
				 	}
				 	else if (col == BOARD_SIZE +1) // right
				 	{
				 		boardButtons[row][col].setIcon(rightIcon);
				 	}
				 	else // somewhere in the middle
				 	{
				 		boardButtons[row][col].setIcon(crossIcon);
				 	}
				}
				history[row * col] = new Move(); // clear the history;
			}
		} 
		boardButtons[(BOARD_SIZE+1)/2][(BOARD_SIZE+1)/2].setIcon(playerOneIcon);
		boardData[(BOARD_SIZE+1)/2][(BOARD_SIZE+1)/2] = PENTE_PLAYER_1; 
		history[turns] = new Move((BOARD_SIZE+1)/2,(BOARD_SIZE+1)/2);
		turns++;
	}
	/**
	returns who is occupying a square on the board, or PENTE_EMPTY if noone is.
	@param row the y coordinate of the square
	@param column the x coordinate of the square.
	@return PENTE_PLAYER_1, PENTE_PLAYER_2, or PENTE_EMPTY.
	*/
	public char getSquare(int row, int column)
	{
		if (row > 0  && row <= BOARD_SIZE && column > 0 && column <= BOARD_SIZE)
			return boardData[row][column];
		//Else
		return PENTE_EMPTY;
	}
	
	/**
	sets a square on the board to PENTE_EMPTY
	@param row the y coordinate of the square
	@param column the x coordinate of the square.
	*/
	private void clearSquare(int row, int column)
	{
		boardButtons[row][column].setIcon(crossIcon);
		boardData[row][column] = PENTE_EMPTY;
	}
	
	/**
	checks if an event was triggered by a particular button
	@param row the y coordinate of the button
	@param column the x coordinate of the button
	@return whether or not the button was the culprit
	*/
	public boolean checkButton(int row,int col, ActionEvent theEvent)
	{
		return (boardButtons[row][col] == theEvent.getSource());
	}
	/**
	returns the number of captures made by a player this game
	@param thePlayer may be PENTE_PLAYER_1 or PENTE_PLAYER_2
	@return the number of points
	*/
	public int getPoints(int thePlayer)
	{
		if (thePlayer == PENTE_PLAYER_1)
			return playerOnePoints;
		if (thePlayer == PENTE_PLAYER_2)
			return playerTwoPoints;
		return -1;
	}
	
	/**
	return the highest number of stones in a row made by the last player, last turns
	@return the number of In-a-rows
	**/
	public int getInARows()
	{
		return InARow;
	}
	/**
	If the game is over, this will return the correct player.  asking whose turn will give the wrong one.
	@return the winner of the game.
	*/
	public int getWinner() // who won? 
	{
		if (whoseTurn == PENTE_PLAYER_1)
			return PENTE_PLAYER_2;
		return PENTE_PLAYER_1;  // it looks wierd, but because of the turn taking logic it works.
	}
	/**
	If the game is over, this will NOT return the correct player.  ask during the game and it will return the correct answer.
	@return the player whose turn it is..
	*/
	public int whoseTurn()
	{
		return whoseTurn;
	}
	// checks to see if there are any 3's 4's, 5's or captures as a result of the last move
	// note: this is the very worst method I have ever programmed.  May I be forgiven.
	/**
	checks to see what happened during the last move, be it captures or in-A-Rows. Should be called at the end of each turn.
	*/
	public void checkMoveEvents()
	{
		// check for in-a-row's
		checkInARows();
		// check for captured pieces
		checkCaptures();
	}
	/** checks to see if there are any 3's 4's, 5's as a result of the last move
	 note: this is one of the very worst methods I have ever programmed.  I could not make this work in any sort of automatic loop, so I hard coded every direction. May I be forgiven.
	*/
	private void checkInARows() // check how many in a row
	{
		int MaxInARow = 0;
		int MaxInACol = 0;
		int Diag2 = 0;
		int Diag1 = 0;
		int theRow = history[turns - 1].getRow();
		int theCol = history[turns - 1].getColumn();
		int thePlayer = getSquare(theRow,theCol);
		
		int ind = 0;
		while("forever" != "loop") // up
		{
			if (getSquare(theRow + ind, theCol) != thePlayer) break;
			MaxInARow++;
			if (ind > 5) break;
			ind++;
		}
		ind = 1;
		while("forever" != "loop") // down
		{	
			if (getSquare(theRow - ind, theCol) != thePlayer) break;
			MaxInARow++;
			if (ind > 4) break;
			ind++;	
		}
		ind = 0;
		while("forever" != "loop") // left
		{
			if (getSquare(theRow, theCol + ind) != thePlayer) break;
			MaxInACol++;
			if (ind > 5) break;
			ind++;
		}
		ind = 1;
		while("forever" != "loop") // right
		{
			if (getSquare(theRow, theCol - ind) != thePlayer) break;
			MaxInACol++;
			if (ind > 4) break;
			ind++;
		}
		//diagonals
		
		ind = 0;
		while("forever" != "loop") // up left
		{
			if (getSquare(theRow + ind, theCol - ind) != thePlayer) break;
			Diag1++;
			if (ind > 5) break;
			ind++;
		}
		ind = 1;
		while("forever" != "loop") // down right
		{	
			if (getSquare(theRow - ind, theCol + ind) != thePlayer) break;
			Diag1++;
			if (ind > 4) break;
			ind++;	
		}
		ind = 0;
		while("forever" != "loop") // down left
		{
			if (getSquare(theRow - ind, theCol - ind) != thePlayer) break;
			Diag2++;
			if (ind > 5) break;
			ind++;
		}
		ind = 1;
		while("forever" != "loop") // up right
		{
			if (getSquare(theRow + ind, theCol + ind) != thePlayer) break;
			Diag2++;
			if (ind > 4) break;
			ind++;
		}
		InARow = MaxInARow;
		if (MaxInACol > InARow) InARow = MaxInACol;
		if (Diag2 > InARow) InARow = Diag2;
		if (Diag1 > InARow) InARow = Diag1;	
	}

	/** checks to see if there are any captures as a result of the last move
	 note: this is one of the very worst methods I have ever programmed. 
	I could not make this work in any sort of automatic loop, so I hard coded every direction.
	 May I be forgiven.
	*/
	private void checkCaptures() // check if the last move made any captures.
	{
		int theRow = history[turns - 1].getRow();
		int theCol = history[turns - 1].getColumn();
		int thePlayer = getSquare(theRow,theCol);
		if (thePlayer == PENTE_PLAYER_2)
		{
			if (getSquare(theRow - 1, theCol) == PENTE_PLAYER_1 && getSquare(theRow -2, theCol) == PENTE_PLAYER_1 && getSquare(theRow - 3, theCol) == PENTE_PLAYER_2) // captured a piece from bottom. 
			{
				playerTwoPoints++;
				clearSquare(theRow -2,theCol);
				clearSquare(theRow - 1, theCol);
				
			}
			if (getSquare(theRow + 1, theCol) == PENTE_PLAYER_1 && getSquare(theRow +2, theCol) == PENTE_PLAYER_1 && getSquare(theRow + 3, theCol) == PENTE_PLAYER_2) // captured a piece from top. 
			{
				playerTwoPoints++;
				clearSquare(theRow +2,theCol);
				clearSquare(theRow + 1, theCol);
				
			}
			if (getSquare(theRow, theCol - 1) == PENTE_PLAYER_1 && getSquare(theRow, theCol - 2) == PENTE_PLAYER_1 && getSquare(theRow, theCol - 3) == PENTE_PLAYER_2) // captured a piece from left. 
			{
				playerTwoPoints++;
				clearSquare(theRow, theCol - 2);
				clearSquare(theRow, theCol - 1);
				
			}
			if (getSquare(theRow, theCol + 1) == PENTE_PLAYER_1 && getSquare(theRow, theCol + 2) == PENTE_PLAYER_1 && getSquare(theRow, theCol + 3) == PENTE_PLAYER_2) // captured a piece from right. 
			{
				playerTwoPoints++;
				clearSquare(theRow, theCol + 2);
				clearSquare(theRow, theCol + 1);
				
			}
			// diagonals
			if (getSquare(theRow - 1, theCol - 1) == PENTE_PLAYER_1 && getSquare(theRow -2, theCol - 2) == PENTE_PLAYER_1 && getSquare(theRow - 3, theCol - 3) == PENTE_PLAYER_2) // captured a piece from bottom left . 
			{
				playerTwoPoints++;
				clearSquare(theRow -2,theCol - 2);
				clearSquare(theRow - 1, theCol - 1);
				
			}
			if (getSquare(theRow + 1, theCol - 1) == PENTE_PLAYER_1 && getSquare(theRow +2, theCol - 2) == PENTE_PLAYER_1 && getSquare(theRow + 3, theCol -3) == PENTE_PLAYER_2) // captured a piece from top left. 
			{
				playerTwoPoints++;
				clearSquare(theRow +2,theCol - 2);
				clearSquare(theRow + 1, theCol - 1);
				
			}
			if (getSquare(theRow + 1, theCol + 1) == PENTE_PLAYER_1 && getSquare(theRow + 2, theCol + 2) == PENTE_PLAYER_1 && getSquare(theRow + 3, theCol + 3) == PENTE_PLAYER_2) // captured a piece from top right. 
			{
				playerTwoPoints++;
				clearSquare(theRow +2, theCol + 2);
				clearSquare(theRow + 1, theCol + 1);
				
			}
			if (getSquare(theRow - 1, theCol + 1) == PENTE_PLAYER_1 && getSquare(theRow - 2, theCol + 2) == PENTE_PLAYER_1 && getSquare(theRow - 3, theCol + 3) == PENTE_PLAYER_2) // captured a piece from bottom right. 
			{
				playerTwoPoints++;
				clearSquare(theRow - 2, theCol + 2);
				clearSquare(theRow - 1, theCol + 1);
				
			}
		}
		else
		{
			if (getSquare(theRow - 1, theCol) == PENTE_PLAYER_2 && getSquare(theRow -2, theCol) == PENTE_PLAYER_2 && getSquare(theRow - 3, theCol) == PENTE_PLAYER_1) // captured a piece from bottom. 
			{
				playerOnePoints++;
				clearSquare(theRow -2,theCol);
				clearSquare(theRow - 1, theCol);
				
			}
			if (getSquare(theRow + 1, theCol) == PENTE_PLAYER_2 && getSquare(theRow +2, theCol) == PENTE_PLAYER_2 && getSquare(theRow + 3, theCol) == PENTE_PLAYER_1) // captured a piece from top. 
			{
				playerOnePoints++;
				clearSquare(theRow +2,theCol);
				clearSquare(theRow + 1, theCol);
				
			}
			if (getSquare(theRow, theCol - 1) == PENTE_PLAYER_2 && getSquare(theRow, theCol - 2) == PENTE_PLAYER_2 && getSquare(theRow, theCol - 3) == PENTE_PLAYER_1) // captured a piece from left. 
			{
				playerOnePoints++;
				clearSquare(theRow, theCol - 2);
				clearSquare(theRow, theCol - 1);
				
			}
			if (getSquare(theRow, theCol + 1) == PENTE_PLAYER_2 && getSquare(theRow, theCol + 2) == PENTE_PLAYER_2 && getSquare(theRow, theCol + 3) == PENTE_PLAYER_1) // captured a piece from right. 
			{
				playerOnePoints++;
				clearSquare(theRow, theCol + 2);
				clearSquare(theRow, theCol + 1);
				
			}
			// diagonals
			
			if (getSquare(theRow - 1, theCol - 1) == PENTE_PLAYER_2 && getSquare(theRow -2, theCol - 2) == PENTE_PLAYER_2 && getSquare(theRow - 3, theCol - 3) == PENTE_PLAYER_1) // captured a piece from bottom left . 
			{
				playerOnePoints++;
				clearSquare(theRow -2,theCol - 2);
				clearSquare(theRow - 1, theCol - 1);
				
			}
			if (getSquare(theRow + 1, theCol - 1) == PENTE_PLAYER_2 && getSquare(theRow +2, theCol - 2) == PENTE_PLAYER_2 && getSquare(theRow + 3, theCol -3) == PENTE_PLAYER_1) // captured a piece from top left. 
			{
				playerOnePoints++;
				clearSquare(theRow +2,theCol - 2);
				clearSquare(theRow + 1, theCol - 1);
				
			}
			if (getSquare(theRow + 1, theCol + 1) == PENTE_PLAYER_2 && getSquare(theRow + 2, theCol + 2) == PENTE_PLAYER_2 && getSquare(theRow + 3, theCol + 3) == PENTE_PLAYER_1) // captured a piece from top right. 
			{
				playerOnePoints++;
				clearSquare(theRow +2, theCol + 2);
				clearSquare(theRow + 1, theCol + 1);
				
			}
			if (getSquare(theRow - 1, theCol + 1) == PENTE_PLAYER_2 && getSquare(theRow - 2, theCol + 2) == PENTE_PLAYER_2 && getSquare(theRow - 3, theCol + 3) == PENTE_PLAYER_1) // captured a piece from bottom right. 
			{
				playerOnePoints++;
				clearSquare(theRow - 2, theCol + 2);
				clearSquare(theRow - 1, theCol + 1);	
			}
		}
	}
	
	/**
	makes a move based on the current player
	@param row the y coordinate of the move
	@param column the x coordinate of the move
	@return whether or not the move was acceptable - at the moment this doesn't check for much, so use with caution.
	*/
	public boolean makeMove(int row, int column)
	{
		if (whoseTurn == PENTE_PLAYER_1)
		{
			boardButtons[row][column].setIcon(playerOneIcon);
			boardData[row][column] = PENTE_PLAYER_1;
			history[turns] = new Move(row,column);
			turns++;
			whoseTurn = PENTE_PLAYER_2;
			return true;	
		}
		else 
		if (whoseTurn == PENTE_PLAYER_2)
		{
			boardButtons[row][column].setIcon(playerTwoIcon);
			boardData[row][column] = PENTE_PLAYER_2;
			history[turns] = new Move(row,column);
			turns++;
			whoseTurn = PENTE_PLAYER_1;
			return true;
		}
		return false;
	}

	/**
	updates the board based on button presses. Not very smart, so use your own actionListener
	@param theEvent the event that was generated
	*/
	public void actionPerformed( ActionEvent theEvent)
	{
		//if (theEvent.getSource() == helpButton)
		for (int row = 1; row <= BOARD_SIZE ; row++)
		{
			for (int col = 1; col <= BOARD_SIZE ; col++)
			{
				if (theEvent.getSource() == boardButtons[row][col])
				{
					if (boardData[row][col] == PENTE_EMPTY)
					{
						makeMove(row,col);
						checkMoveEvents();
					}
				}
			}
		}
	}
	
}