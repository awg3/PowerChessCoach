package Rules;



public class BlackPawn extends Piece{
	private boolean initialMove = false;
	
	public BlackPawn(int x, int y) {
		super.setX(x);
		super.setY(y);
		super.setName('P');
		super.setColor('b');
	}

	public boolean moveTo(int x, int y, Piece[][] chessboard) {
		boolean result = this.reachable(x, y, chessboard);
		if (result) {
			if (!super.moved())
				initialMove = true;
			Piece tmp = chessboard[x][y];
			if (tmp.getColor() != 'e') {
				tmp = new Empty(x, y);
			}
			chessboard[x][y] = chessboard[super.getX()][super.getY()];
			chessboard[super.getX()][super.getY()] = tmp;
			super.setX(x);
			super.setY(y);
			super.move();
		}else if (chessboard[x - 1][y].getName() == 'P' && chessboard[x - 1][y].getColor() == 'w' && ((WhitePawn)chessboard[x - 1][y]).initialMove()) {
			this.enPassant(x, y, chessboard);
			return true;
		}
		return result;
	}
	
	private void enPassant(int x, int y, Piece[][] chessboard) {
		Piece tmp = chessboard[x][y];
		chessboard[x - 1][y] = new Empty(x - 1, y);
		chessboard[x][y] = chessboard[super.getX()][super.getY()];
		chessboard[super.getX()][super.getY()] = tmp;
		super.setX(x);
		super.setY(y);
		super.move();
	}

	public boolean initialMove() {
		return initialMove;
	}
	
	public void setInitialMove(boolean b) {
		initialMove = b;
	}

	public boolean reachable(int x, int y, Piece[][] chessboard) {
		if (chessboard[x][y].getColor() == super.getColor()) {
			return false;
		}else if (x == super.getX() + 1 && y == super.getY() && chessboard[x][y].getColor() == 'e') {
			return true;
		}else if (x == super.getX() + 1 && Math.abs(y - super.getY()) == 1) {
			if (chessboard[x][y].getColor() == 'w') {
				return true;
			}
		}else if(x == super.getX() + 2 && y == super.getY() && !super.moved()  && chessboard[x - 1][y].getColor() == 'e') {
			return true;
		}
		return false;
	}

	public Piece clone() {
		BlackPawn tmp = new BlackPawn(this.getX(), this.getY());
		tmp.setInitialMove(this.initialMove());
		return tmp;
	}
}
