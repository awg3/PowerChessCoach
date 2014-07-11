package Rules;


public class BlackKing extends Piece{

	public BlackKing(int x, int y) {
		super.setX(x);
		super.setY(y);
		super.setName('K');
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
		}else if (Math.abs(y - super.getY()) == 2 && x == super.getX()) {
			return castling(x, y, chessboard);
		}
		
		return result;
	}

	private boolean castling(int x, int y, Piece[][] chessboard) {
		if (super.moved()) {
			return false;
		}
		if (ChessGame.check(super.getX(), super.getY(), chessboard, 'b')) {
			return false;
		}
		int i, step;
		if (y > super.getY()) {
			i = 7;
			step = 1;
		}else {
			i = 0;
			step = -1;
		}
		
		if (chessboard[x][i].getName() != 'R' || chessboard[x][i].moved()) {
			return false;
		}
		boolean result = chessboard[x][i].moveTo(x, y - step, chessboard);
		
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
		}else  if (Math.abs(x - super.getX()) > 1 || Math.abs(y - super.getY()) > 1) {
			return false;
		}
		return true;
	}

	public Piece clone() {
		return new BlackKing(this.getX(), this.getY());
	}
}
