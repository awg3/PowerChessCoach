package parser;

import java.util.ArrayList;

public class Move {
	public String wMove, bMove;
	public String cMessage, wMessage;
	private Move preMove;
	private ArrayList<Move> nextMove;
	
	public Move(String wMove, String bMove, String cMessage, String wMessage) {
		this.nextMove = new ArrayList<Move>();
		if (wMove == null || wMove.equals("..."))
			this.wMove = null;
		else
			this.wMove = wMove;
		this.bMove = bMove;
		this.cMessage = cMessage;
		this.wMessage = wMessage;
	}
	
	public void setNextMove(Move next) {
		this.nextMove.add(next);
		next.setPreMove(this);
	}
	
	public void setPreMove(Move pre) {
		this.preMove = pre;
	}
	
	public ArrayList<Move> getNextMove() {
		return this.nextMove;
	}
	
	public Move getPreMove() {
		return this.preMove;
	}
	
	public String toString() {
		String result = wMove + "\t" + bMove + "\t" + cMessage + "\t" + wMessage;
		return result;
	}
}
