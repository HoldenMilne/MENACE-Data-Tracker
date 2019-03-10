package v5;

public class Point
{
	int x;
	int y;
	Move[] moves;
	String winner;
	public Point(int x, int y, Move[] moves)
	{
		this.moves = moves;
		this.x = x;
		this.y = y;
		if(Main.won == 1)
			winner = "MENACE";
		else if(Main.won == -1)
			winner = "PLAYER";
		else
			winner = "DRAW";
	}
	
}
