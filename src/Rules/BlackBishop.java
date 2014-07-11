package Rules;


public class BlackBishop extends Piece{

	public BlackBishop(int x, int y) {
		super.setX(x);
		super.setY(y);
		super.setName('B');
		super.setColor('b');
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
		}else  if (Math.abs(x - super.getX()) != Math.abs(y - super.getY())) {
			return false;
		}else {
			int stepX = (x - super.getX()) / Math.abs(x - super.getX());
			int stepY = (y - super.getY()) / Math.abs(y - super.getY());
			boolean result = true;
			int i = super.getX() + stepX, j = super.getY() + stepY;
			while (i != x) {
				if (chessboard[i][j].getColor() != 'e') {
					result = false;
					break;
				}
				i += stepX;
				j += stepY;
			}
			if (!result) {
				return false;
			}else {
				return true;
			}
		}
	}

	public Piece clone() {
		return new BlackBishop(this.getX(), this.getY());
	}
}
