import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
Provides a wrapper for the PenteBoard and provides information to the user.
**/
public class PenteGame extends JFrame implements ActionListener
{
	// global constants
	public static final int VERTICAL_SIZE = 745; // overall window dimentions
	public static final int HORIZONTAL_SIZE = 846; 
	public static final int OUTPUT_SIZE = 30; // size of text fields for output
	
	// gui
	private JButton helpButton = new JButton("Help!");
	private JButton resetButton = new JButton("Reset the Game");
	private JTextField statusField = new JTextField(OUTPUT_SIZE);
	private JTextArea authorField = new JTextArea(OUTPUT_SIZE,OUTPUT_SIZE/2);
	// the board
	private PenteBoard theBoard = new PenteBoard(this);
	private boolean isGameOver = false;
	private int whoseTurn = theBoard.PENTE_PLAYER_2;
	private int [] warningLevel = {0,0,0};
	
	/**
	* Constructor for the PenteGame class
	* Sets up the window 
	**/
	public PenteGame()
	{
		setTitle("Pente");
		setSize(HORIZONTAL_SIZE, VERTICAL_SIZE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setUpDisplay();
		setVisible(true); 
		updateGame();
	}
	
	/**
	* sets up how the window looks, attaches the buttons to their listeners.
	**/
	private void setUpDisplay()
	{
		
		helpButton.addActionListener(this);
		resetButton.addActionListener(this);
		authorField.setText( "Author : Mark Lee \n100123607 \nShouts to: \nPM, Qartis, Dan, Jre");
		authorField.setEditable(false);
		Container container = getContentPane();
		JPanel mainPanel = new JPanel(); // holds everything in it;
		JPanel sideBar = new JPanel();
		sideBar.setLayout( new BorderLayout());
		sideBar.add(resetButton, BorderLayout.NORTH);
		// some stats here..
		sideBar.add(authorField, BorderLayout.CENTER);
		mainPanel.setLayout( new BorderLayout());
		mainPanel.add(helpButton,BorderLayout.NORTH);
		mainPanel.add(sideBar,BorderLayout.EAST);
		mainPanel.add(theBoard,BorderLayout.CENTER);
		statusField.setEditable(false);
		mainPanel.add(statusField, BorderLayout.SOUTH);
		container.add(mainPanel);
		setResizable(false);
		
		
	}
	/**
	this gets called by the at the end of the game.
	*/
	private void gameOver(int thePlayer)
	{
		isGameOver = true;
		JOptionPane.showMessageDialog(null,"Player " + thePlayer + " Won!");	
	}
	/**
	this updates the game variables after each turn, including the text on the screen
	*/
	private void updateGame()
	{
		theBoard.checkMoveEvents();
		authorField.setText( "Author : Mark Lee \n100123607 \nShouts to: \nPM, Qartis, Dan, Jre\n----------------\n\nIt's Player " + theBoard.whoseTurn() + "'s turn.\n----------------" );
		
		authorField.setText(authorField.getText() + "\nPlayer 1 points: " + theBoard.getPoints(theBoard.PENTE_PLAYER_1) + 
			"\n\n\nPlayer 2 points: " + theBoard.getPoints(theBoard.PENTE_PLAYER_2));
			
		statusField.setText("Player 1 points: " + theBoard.getPoints(theBoard.PENTE_PLAYER_1) + 
			" Player 2 points: " + theBoard.getPoints(theBoard.PENTE_PLAYER_2));
		if (theBoard.getInARows() > 2 && theBoard.getInARows() > warningLevel[whoseTurn])
			warningLevel[whoseTurn] = theBoard.getInARows();
		if (warningLevel[theBoard.PENTE_PLAYER_1] > 2)
		{
			statusField.setText( statusField.getText() + "  Warning: Player 1 has a " + warningLevel[theBoard.PENTE_PLAYER_1] + "-in-a-row!");
			authorField.setText(authorField.getText() + "\n\n\nWarning: \nPlayer 1 has a \n" + warningLevel[theBoard.PENTE_PLAYER_1] + "-in-a-row!" );
		}
		if (warningLevel[theBoard.PENTE_PLAYER_2] > 2)
		{	
			authorField.setText(authorField.getText() + "\n\n\nWarning: \nPlayer 2 has a \n" + warningLevel[theBoard.PENTE_PLAYER_2] + "-in-a-row!" );
			statusField.setText( statusField.getText() + "  Warning: Player 2 has a " + warningLevel[theBoard.PENTE_PLAYER_2] + "-in-a-row!");
		}
	}
	/**
	* changes internal settings and triggers events
	* @param theEvent the event that is being dealt with
	**/
	public void actionPerformed( ActionEvent theEvent)
	{
		if (theEvent.getSource() == helpButton)
		{
			JOptionPane.showMessageDialog(null,"Learn to play Pente!");	
		}
		else
		if (theEvent.getSource() == resetButton)
		{
			//JOptionPane.showMessageDialog(null,"Resetting!");	
			theBoard.reset();
			isGameOver = false;
			whoseTurn = theBoard.PENTE_PLAYER_2;
			warningLevel[theBoard.PENTE_PLAYER_1] = 0;
			warningLevel[theBoard.PENTE_PLAYER_2] = 0;
			updateGame();
		}
		else if (!isGameOver) 
		{
			
			for (int row = 1; row <= theBoard.BOARD_SIZE ; row++)
			{
				for (int col = 1; col <= theBoard.BOARD_SIZE ; col++)
				{
					if (theBoard.checkButton(row,col,theEvent) == true)
					{
						if (theBoard.getSquare(row,col) == theBoard.PENTE_EMPTY)
						{
							theBoard.makeMove(row,col);
							updateGame();
							if (theBoard.getPoints(whoseTurn) >=5)
								gameOver(whoseTurn);
							else
							if (theBoard.getInARows() >= 5)
								gameOver(whoseTurn);
							else
								whoseTurn = theBoard.whoseTurn();
						}
					}
				}
			}
		}
	}
	
	/**
	* instantiates a window.
	**/
	public static void main(String [] args)
	{
		PenteGame theGame = new PenteGame();
	
	}
	
}
