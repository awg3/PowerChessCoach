package Rules;


public class WhiteKnight extends Piece{

	public WhiteKnight(int x, int y) {
		super.setX(x);
		super.setY(y);
		super.setName('N');
		super.setColor('w');
	}

	public boolean moveTo(int x, int y, Piece[][] chessboard) {
		boolean result = this.reachable(x, y, chessboard);
		if (result) {
			Piece tmp = chessboard[x][y];
			if (tmp.getColor() != 'e') {
				tmp = new Empty(x, y);
			}
			chessboard[x][y] = chessboard[super.getX()][super.getY()];
			chessboard[super.getX()][super.getY()] = tmp;
			super.setX(x);
			super.setY(y);
			super.move();
		}
		return result;
	}

	public boolean reachable(int x, int y, Piece[][] chessboard) {
		if (chessboard[x][y].getColor() == super.getColor()) {
			return false;
		}else  if ((Math.abs(x - super.getX()) == 1 && Math.abs(y - super.getY()) == 2) || (Math.abs(x - super.getX()) == 2 && Math.abs(y - super.getY()) == 1)) {
			return true;
		}
		return false;
	}

	public Piece clone() {
		return new WhiteKnight(this.getX(), this.getY());
	}
}
