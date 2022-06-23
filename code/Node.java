package code;

import java.util.ArrayList;

public class Node {
State parentState;
State state;

public Node(State s,int row, int col, int Damage, int capacity, ArrayList<String> carriedHostages,
		ArrayList<Integer> carriedHostagesDamage, ArrayList<String> deadHostages, ArrayList<String> agentsLoc,
		ArrayList<String> pillLocs, ArrayList<String> hostageLocs, ArrayList<Integer> hostageDamages, int heuristic,
		int cost, String plan, int deaths, int kills) {
	state = new State(row,col,Damage,capacity,carriedHostages,carriedHostagesDamage,deadHostages,agentsLoc,pillLocs,hostageLocs,hostageDamages,heuristic,cost,plan,deaths,kills);
	parentState=s;
}
public int getCostPlusHeuristic() {
	return state.getCostPlusHeuristic();
}
}
