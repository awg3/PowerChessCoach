package Rules;


public class Empty extends Piece{

	public Empty(int x, int y) {
		super.setX(x);
		super.setY(y);
		super.setColor('e');
	}
	
	public boolean moveTo(int x, int y, Piece[][] chessboard) {
		return false;
	}

	public boolean reachable(int x, int y, Piece[][] chessboard) {
		return false;
	}

	public Piece clone() {
		return new Empty(this.getX(), this.getY());
	}

}
