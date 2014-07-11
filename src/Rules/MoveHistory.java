package Rules;

import parser.Move;

public class MoveHistory {
	private Piece[][] chessboard;
	private Move currentMove;
	private boolean check;
	private char turn;
	
	public MoveHistory(Piece[][] chessboard, Move currentMove, boolean check, char turn) {
		this.chessboard = new Piece[8][8];
		for (int i = 0; i < 8; i ++) {
			for (int j = 0; j < 8; j++) {
				this.chessboard[i][j] = chessboard[i][j].clone();
			}
		}
		this.currentMove = currentMove;
		this.check = check;
		this.turn = turn;
	}
	
	public Piece[][] getChessboard() {
		return chessboard;
	}
	
	public Move getMove() {
		return currentMove;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public char getTurn() {
		return turn;
	}
}
