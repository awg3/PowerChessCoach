package Rules;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Stack;

import android.util.Log;

import parser.Move;
import parser.Parser;


public abstract class ChessGame {
	public static final String LESSON = "lesson";
	private Piece[][] chessboard;
	private char turn = 'w'; //'w' => white, 'b' => black
	private Stack<MoveHistory> moveHistory;
	
	private boolean check;
	
	//new
	private Parser parser;
	private Move moveList;
	private char player;
	
	/*
	public ChessGame(String str, String board, char turn) {
		if (str.compareTo(ChessGame.REGULAR_GAME) == 0) {
			historyMove = new Stack<Piece[][]>();
			this.regularGame();
		}else if (str.compareTo(ChessGame.CUSTOMIZED_GAME) == 0) {
			if (board == null || board.length() != 128 || (turn != 'w' && turn != 'b')) {
				
			}else {
				historyMove = new Stack<Piece[][]>();
				this.customizedGame(board, turn);
			}
		}
	}*/
	
	/**
	 * new
	 * @param str
	 * @param board
	 * @param turn
	 * @param moveList
	 * @param cMessage
	 * @param wMessage
	 */
	public ChessGame(String str, String board, char turn, String moveList, String[][] cMessage, String[][] wMessage) {
		
		
		
		if (str.compareTo(ChessGame.LESSON) == 0) {
			if (board == null || board.length() != 128 || (turn != 'w' && turn != 'b')) {
				
			}else {
				moveHistory = new Stack<MoveHistory>();
				parser = new Parser(moveList, cMessage, wMessage);
				this.moveList = parser.getMoveList();
				this.customizedGame(board, turn);
				player = turn;
			}
		}
	}
	
	private void regularGame() {
		chessboard = new Piece[8][8];
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (i == 7) 
					switch(j) {
						case 0:case 7:
							chessboard[i][j] = new WhiteRook(i, j);
							break;
						case 1:case 6:
							chessboard[i][j] = new WhiteKnight(i, j);
							break;
						case 2:case 5:
							chessboard[i][j] = new WhiteBishop(i, j);
							break;
						case 3:
							chessboard[i][j] = new WhiteQueen(i, j);
							break;
						default:
							chessboard[i][j] = new WhiteKing(i, j);	
						}
				else if (i == 6) 
					chessboard[i][j] = new WhitePawn(i, j);
				else if (i == 0) 
					switch(j) {
						case 0:case 7:
							chessboard[i][j] = new BlackRook(i, j);
							break;
						case 1:case 6:
							chessboard[i][j] = new BlackKnight(i, j);
							break;
						case 2:case 5:
							chessboard[i][j] = new BlackBishop(i, j);
							break;
						case 3:
							chessboard[i][j] = new BlackQueen(i, j);
							break;
						default:
							chessboard[i][j] = new BlackKing(i, j);	
					}
				else if (i == 1) 
					chessboard[i][j] = new BlackPawn(i, j);
				else
					chessboard[i][j] = new Empty(i, j);
			}
	}
	
	public String getHistoryMove() {
		if(moveHistory.peek()==null)
			return "empty";
		MoveHistory lastMove = moveHistory.pop();
		Piece[][] tmp = lastMove.getChessboard();
		moveList = lastMove.getMove();
		turn = lastMove.getTurn();
		check = lastMove.getCheck();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				chessboard[i][j] = tmp[i][j];
			}
		}
		return this.getChessBoard();
	}
	
	/**
	 * changed
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public boolean move(int x1, int y1, int x2, int y2) {
		Log.d("vals",x1+" "+y1+" "+x2+" "+y2);
		if (turn == player) {
			moveHistory.push(new MoveHistory(chessboard, moveList, check, turn));
			
			boolean result = chessboard[x1][y1].reachable(x2, y2, chessboard);
			if (!result) {
				moveHistory.pop();
				return false;
			}
			
			result = false;
			
			String move;
			Move tmp = null;
			for (int i = 0; i < moveList.getNextMove().size(); i++) {
				tmp = moveList.getNextMove().get(i);
				if (player == 'w')
					move = tmp.wMove;
				else
					move = tmp.bMove;
				char p = move.charAt(0);
				int cx = 56 - move.charAt(move.length() - 1);
				int cy = move.charAt(move.length() - 2) - 97;
				Log.d("move", tmp.toString());
				Log.d("x", cx+"");
				Log.d("y", cy+"");
				if (cx == x2 && cy == y2 && chessboard[x1][y1].getName() == p && chessboard[x1][y1].getColor() == player) {
					result = true;
					break;
				}
			}
			
			if (!result) {
				tmp = moveList.getNextMove().get(0);
				moveHistory.pop();
			}else {
				chessboard[x1][y1].moveTo(x2, y2, chessboard);
				moveList = tmp;
			}
			Log.d("moveList",moveList.toString());
			
			this.showMessage(tmp.cMessage, tmp.wMessage, result);
			if (result)
				if (turn == 'w') {
					turn = 'b';
					this.blackRound();
				}else {
					turn = 'w';
					this.whiteRound();
				}
			return result;
		}
		
		if (chessboard[x1][y1].getColor() != turn) {
			return false;
		}else {
			boolean result = chessboard[x1][y1].moveTo(x2, y2, chessboard);
			if (result) {
				if (turn == 'w') {
					turn = 'b';
					this.blackRound();
				}else {
					turn = 'w';
					this.whiteRound();
				}
			}
			return result;
		}
	}
	
	/**
	 * changed
	 */
	private void blackRound() {
		check = false;
		boolean promotion = false;
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (chessboard[i][j].getColor() == 'b' && chessboard[i][j].getName() == 'P') {
					((BlackPawn) chessboard[i][j]).setInitialMove(false);
				}else if (chessboard[i][j].getColor() == 'w' && chessboard[i][j].getName() == 'P' && i == 0) {
					promotion(i, j, 'w');
					promotion = true;
				}
		if (!promotion) {
			for (int i = 0; i < 8; i++)
				for (int j = 0; j < 8; j++)
					if (chessboard[i][j].getName() == 'K' && chessboard[i][j].getColor() == 'b') {
						check = check(i, j, chessboard, 'b');
					}
			if (check)
				this.checking('b');
		}
//		this.nextRound();
	}
	
	/**
	 * changed
	 */
	private void whiteRound() {
		check = false;
		boolean promotion = false;
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (chessboard[i][j].getColor() == 'w' && chessboard[i][j].getName() == 'P') {
					((WhitePawn) chessboard[i][j]).setInitialMove(false);
				}else if (chessboard[i][j].getColor() == 'b' && chessboard[i][j].getName() == 'P' && i == 7) {
					promotion(i, j, 'b');
					promotion = true;
				}
		if (!promotion) {
			for (int i = 0; i < 8; i++)
				for (int j = 0; j < 8; j++)
					if (chessboard[i][j].getName() == 'K' && chessboard[i][j].getColor() == 'w') {
						check = ChessGame.check(i, j, chessboard, 'w');
					}
			if (check)
				this.checking('w');
		}
//		this.nextRound();
	}
	
	public void print() {
		for (int i = 7; i >= 0; i--) {
			System.out.print((i + 1) + " |");
			for (int j = 0; j < 8; j++)
				System.out.print(chessboard[i][j].toString() + " ");
			System.out.println();
		}
		System.out.print("  ------------------------\n   ");
		for (int i = 0; i < 8; i++) {
			System.out.print((char) (i + 65) + "  ");
		}
		System.out.println("\n*****************************************");
	}
	
	public static boolean check(int x, int y, Piece[][] chessboard, char color) {
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (chessboard[i][j].getColor() != color && chessboard[i][j].getName() != 'P') {
					if (chessboard[i][j].reachable(x, y, chessboard))
						return true;
				}else if (chessboard[i][j].getName() == 'P' && chessboard[i][j].getColor() != color) {
					if (chessboard[i][j].reachable(x, y, chessboard) && Math.abs(j - y) == 1)
						return true;
				}
		return false;
	}	
	public char whoseTurn() {
		return turn;
	}
	
	public String getChessBoard() {
		String str = "";
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				str += chessboard[i][j].toString().toLowerCase(Locale.US); // wP bK, " ." empty
		return str;
	}
	
	public void promotion(char name, char color, int x, int y) {
		check = false;
		Piece piece = this.newPiece(name, color, x, y);
		
		chessboard[x][y] = piece;
		
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (chessboard[i][j].getName() == 'K' && chessboard[i][j].getColor() != color) {
					check = check(i, j, chessboard, chessboard[i][j].getColor());
				}
		if (check)
			if (color == 'w') {
				this.checking('b');
			}else {
				this.checking('w');
			}
	}
	
	private Piece newPiece(char name, char color, int x, int y) {
		Piece piece = null;
		if (color == 'w') {
			switch (name) {
			case 'Q':
				piece = new WhiteQueen(x, y);
				break;
			case 'B':
				piece = new WhiteBishop(x, y);
				break;
			case 'N':
				piece = new WhiteKnight(x, y);
				break;
			case 'R':
				piece = new WhiteRook(x, y);
				break;
			case 'K':
				piece = new WhiteKing(x, y);
				break;
			default:
				piece = new WhitePawn(x, y);	
			}
		}else if (color == 'b'){
			switch (name) {
			case 'Q':
				piece = new BlackQueen(x, y);
				break;
			case 'B':
				piece = new BlackBishop(x, y);
				break;
			case 'N':
				piece = new BlackKnight(x, y);
				break;
			case 'R':
				piece = new BlackRook(x, y);
				break;
			case 'K':
				piece = new BlackKing(x, y);
				break;
			default:
				piece = new BlackPawn(x, y);
			}
		}else
			piece = new Empty(x, y);
		return piece;
	}
	
	private void customizedGame(String board, char turn) {
		
		chessboard = new Piece[8][8];

		Piece piece;
		char name, color;
		
		this.turn = turn;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				color = board.charAt((i * 8 + j) * 2);
				name = board.charAt((i * 8 + j) * 2 + 1);
				piece = this.newPiece(name, color, i, j);
				if (name == 'P' && color == 'b' && i != 1) {
					piece.move();
				}else if (name == 'P' && color == 'w' && i != 6) {
					
					Log.d("chessGame","i:"+i+"j:"+j);
					piece.move();
				}else if (name == 'R' && color == 'w' && (j != 0 || j != 7 || i != 7)) {
					piece.move();
				}else if (name == 'R' && color == 'b' && (j != 0 || j != 7 || i != 0)) {
					piece.move();
				}else if (name == 'K' && color == 'b' && (j != 4 || i != 0)) {
					piece.move();
				}else if (name == 'K' && color == 'w' && (j != 4 || i != 7)) {
					piece.move();
				}
				chessboard[i][j] = piece;				
			}
		}
	}
	
	/**
	 * new
	 */
	public int[] appMove() {
		if (turn != player) {
			if (player == 'w') {
				if (moveList.bMove == null)
					return null;
				else
					return this.move(moveList.bMove, turn);
			}else {
				if (moveList.getNextMove().size() == 0)
					return null;
				else
					return this.move(moveList.getNextMove().get(0).wMove, turn);
			}
		}else
			return null;
	}
	
	/**
	 * new
	 * @param move
	 * @param color
	 */
	private int[] move(String move, char color) {
		
		Log.d("move", move);
		char Piece = move.charAt(0);
		int x1 = -1, y1 = -1;
		int x2 = 56 - move.charAt(move.length() - 1);
		int y2 = move.charAt(move.length() - 2) - 97;
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (chessboard[i][j].getName() == Piece && chessboard[i][j].getColor() == color 
						&& chessboard[i][j].reachable(x2, y2, chessboard)) {
					x1 = i;
					y1 = j;
					break;
				}
			}
		}
		
		if (x1 != -1) {
			this.move(x1, y1, x2, y2);
			return new int[] {x1, y1, x2, y2};
		}else
			return null;
	}
	
	public boolean inCheck(){
		return check;
	}
	
	/**
	 * return the all legal move
	 * @param x
	 * @param y
	 * @return
	 */
	public ArrayList<int[]> legalPosition(int x, int y) {
		ArrayList<int[]> p = new ArrayList<int[]>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (chessboard[x][y].reachable(i, j, chessboard)) {
					p.add(new int[] {i, j});
				}
			}
		}
		return p;
	}
	
	/**
	 * 
	 * @return
	 */
	public int[] nextMove() {
		if (moveList.getNextMove().size() == 0) {
			return null;
		}
		
		String next;
		
		if (player == 'w') {
			next = moveList.getNextMove().get(0).wMove;
		}else {
			next = moveList.getNextMove().get(0).bMove;
		}
		if(next==null)
			return null;
		int x1 = -1, y1 = -1;
		int x2 = 56 - next.charAt(next.length() - 1);
		int y2 = next.charAt(next.length() - 2) - 97;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (chessboard[i][j].getName() == next.charAt(0) && chessboard[i][j].getColor() == player 
						&& chessboard[i][j].reachable(x2, y2, chessboard)) {
					x1 = i;
					y1 = j;
					break;
				}
			}
		}
		
		int[] move = new int[] {x1, y1, x2, y2};
		return move;
	}
	
	public abstract void promotion(int x, int y, char color);
	
	public abstract void checking(char color);
	
	public abstract void showMessage(String cMessage, String wMessage, boolean b);
}
