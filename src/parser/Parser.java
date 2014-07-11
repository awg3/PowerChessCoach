package parser;

public class Parser {
	private Move moveList;
	
	public Parser(String moveList, String[][] cMessage, String[][] wMessage) {
		this.moveList = new Move(null, null, null, null);
		this.parseMove(this.moveList, moveList, cMessage, wMessage);
	}
	
	private void parseMove(Move preMove, String moveList, String[][] cMessage, String[][] wMessage) {
		if (moveList.indexOf(";") == -1)
			return;
		
		int braces = 0, delimiter = 0;
		for (int i = 0; i < moveList.length(); i++) {
			if (moveList.charAt(i) == '{')
				braces++;
			else if (moveList.charAt(i) == '}')
				braces--;
			else if (moveList.charAt(i) == ';' && braces == 0) {
				delimiter = i;
				break;
			}
		}
		String subStr = moveList.substring(0, delimiter + 1);
		
		String wMove = null, bMove = null, cM = null, wM = null;
		
		if (subStr.indexOf("{") == -1) {
			if (subStr.indexOf("*") == -1) {
				if (subStr.indexOf(",") != -1) {
					wMove = subStr.substring(0,	subStr.indexOf(","));
					bMove = subStr.substring(subStr.indexOf(",") + 1, subStr.indexOf(";"));
				}else {
					wMove = subStr.substring(0, subStr.indexOf(";"));
				}
				Move move = new Move(wMove, bMove, cM, wM);
				preMove.setNextMove(move);
				this.parseMove(move, moveList.substring(delimiter + 1), cMessage, wMessage);
			}else {
				String tmp = subStr.substring(subStr.indexOf("*") + 1, subStr.lastIndexOf("*"));
				subStr = subStr.substring(0, subStr.indexOf("*") + 1);
				if (subStr.indexOf(",") != -1) {
					wMove = subStr.substring(0,	subStr.indexOf(","));
					bMove = subStr.substring(subStr.indexOf(",") + 1, subStr.indexOf("*"));
				}else {
					wMove = subStr.substring(0, subStr.indexOf("*"));
				}
				
				if (tmp.indexOf(",") == -1) {
					int messageNum = Integer.parseInt(tmp.substring(1));
					if (tmp.charAt(0) == 'C') {
						for (int i = 0; i < cMessage.length; i++) {
							if (cMessage[i][0].compareTo(messageNum + "") == 0) {
								cM = cMessage[i][1];
								break;
							}
						}
					}else {
						for (int i = 0; i < wMessage.length; i++) {
							if (wMessage[i][0].compareTo(messageNum + "") == 0) {
								wM = wMessage[i][1];
								break;
							}
						}
					}
				}else {
					int i1 = Integer.parseInt(tmp.substring(1, tmp.indexOf(",")));
					int i2 = Integer.parseInt(tmp.substring(tmp.indexOf(",") + 2));
					for (int i = 0; i < cMessage.length; i++) {
						if (cMessage[i][0].compareTo(i1 + "") == 0) {
							cM = cMessage[i][1];
							break;
						}
					}
					for (int i = 0; i < wMessage.length; i++) {
						if (wMessage[i][0].compareTo(i2 + "") == 0) {
							wM = wMessage[i][1];
							break;
						}
					}
				}
				
				Move move = new Move(wMove, bMove, cM, wM);
				preMove.setNextMove(move);
				this.parseMove(move, moveList.substring(delimiter + 1), cMessage, wMessage);
			}
		}else {
			String varList = subStr.substring(subStr.indexOf("{"), subStr.lastIndexOf("}") + 1);
			subStr = subStr.substring(0, subStr.indexOf("{")) + ";";
			Move move;
			
			if (subStr.indexOf("*") == -1) {
				if (subStr.indexOf(",") != -1) {
					wMove = subStr.substring(0,	subStr.indexOf(","));
					bMove = subStr.substring(subStr.indexOf(",") + 1, subStr.indexOf(";"));
				}else {
					wMove = subStr.substring(0, subStr.indexOf(";"));
				}
				move = new Move(wMove, bMove, cM, wM);
				preMove.setNextMove(move);
				this.parseMove(move, moveList.substring(delimiter + 1), cMessage, wMessage);
			}else {
				String tmp = subStr.substring(subStr.indexOf("*") + 1, subStr.lastIndexOf("*"));
				subStr = subStr.substring(0, subStr.indexOf("*") + 1);
				if (subStr.indexOf(",") != -1) {
					wMove = subStr.substring(0,	subStr.indexOf(","));
					bMove = subStr.substring(subStr.indexOf(",") + 1, subStr.indexOf("*"));
				}else {
					wMove = subStr.substring(0, subStr.indexOf("*"));
				}
				
				if (tmp.indexOf(",") == -1) {
					int messageNum = Integer.parseInt(tmp.substring(1));
					if (tmp.charAt(0) == 'C') {
						for (int i = 0; i < cMessage.length; i++) {
							if (cMessage[i][0].compareTo(messageNum + "") == 0) {
								cM = cMessage[i][1];
								break;
							}
						}
					}else {
						for (int i = 0; i < wMessage.length; i++) {
							if (wMessage[i][0].compareTo(messageNum + "") == 0) {
								wM = wMessage[i][1];
								break;
							}
						}
					}
				}else {
					int i1 = Integer.parseInt(tmp.substring(1, tmp.indexOf(",")));
					int i2 = Integer.parseInt(tmp.substring(tmp.indexOf(",") + 2));
					for (int i = 0; i < cMessage.length; i++) {
						if (cMessage[i][0].compareTo(i1 + "") == 0) {
							cM = cMessage[i][1];
							break;
						}
					}
					for (int i = 0; i < wMessage.length; i++) {
						if (wMessage[i][0].compareTo(i2 + "") == 0) {
							wM = wMessage[i][1];
							break;
						}
					} 
				}
				
				move = new Move(wMove, bMove, cM, wM);
				preMove.setNextMove(move);
				this.parseMove(move, moveList.substring(delimiter + 1), cMessage, wMessage);
			}
			
			braces = 0;
			StringBuilder str = new StringBuilder();
			
			for (int i = 0; i < varList.length(); i++) {
				if (varList.charAt(i) == '{') {
					if (braces != 0)
						str.append(varList.charAt(i));
					braces++;
				}else if (varList.charAt(i) == '}') {
					braces--;
					if (braces == 0) {
						this.parseMove(preMove, str.toString(), cMessage, wMessage);
						str = new StringBuilder();
					}else
						str.append(varList.charAt(i));
				}else
					str.append(varList.charAt(i));
			}
		}
	}
	
	public Move getMoveList() {
		return this.moveList;
	}
}
