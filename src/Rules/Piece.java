package Rules;


public abstract class Piece {
	private char name, color;
	private int x, y;	//x => rank, y => file
	private boolean moved = false;

	public Piece() {}
	
	public char getName() {
		return name;
	}
	
	public void setName(char name) {
		this.name = name;
	}
	
	public char getColor() {
		return color;
	}
	
	public void setColor(char color) {
		this.color = color;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean moved() {
		return moved;
	}
	
	public void move() {
		moved = true;
	}
	
	public String toString() {
		if (color != 'e')
			return color + "" + name;
		else
			return "00";
	}
	
	public abstract boolean moveTo(int x, int y, Piece[][] chessboard);
	
	public abstract boolean reachable(int x, int y, Piece[][] chessboard);
	
	public abstract Piece clone();
}
