/**
Move is a small class for storing the history of moves for the pente game.
If I have more time I might implement the history function.
The main obstacles are moves that capture and moves that set off warnings.. I could just reset the board and play 
all the moves back - 1.. but that would probably be inefficient.  hmm..
@author Mark Lee 100123607
*/
public class Move
{
	private int row;
	private int col;
	
	/**
	Store a move in the class
	@param theRow the y position of the piece
	@param theCol the x position of the piece
	*/
	public Move(int theRow, int theCol)
	{
		row = theRow;
		col = theCol;
	}
	/**
	Create an empty move.  
	Useful when initializing a history list.
	*/
	public Move() // create an empty move
	{
		row = -1;
		col = -1;
	}
	/**
	yup
	@return the row the move was initialized to.  if the move is empty returns -1.
	*/
	public int getRow()
	{
		return row;
	}
	/**
	yup
	@return the column the move was initialized to.  if the move is empty returns -1.
	*/
	public int getColumn()
	{
		return col;
	}

}
