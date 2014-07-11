package parser;

public class Tester {
	public static void main(String[] args) {
		Parser p = new Parser("BB5,NC6;PA1,QC2*C3*{PA2,QC2*C3,W1*;BH3,PC4*C1,W1*;}{PA1,QC3;};BH3,PC4*C1,W1*;", new String[][] {{"1", "C1"}, {"3", "C3"}}, new String[][] {{"3", "W3"}});
		Move m = p.getMoveList();
		int a = '8';
		System.out.println(a);
		print(m);
	}
	
	static void print(Move m) {
		System.out.println(m.toString());
		if (m.getNextMove().size() != 0) {
			for (int i = 0; i < m.getNextMove().size(); i++) {
				print(m.getNextMove().get(i));
			}
		}
	}
}
