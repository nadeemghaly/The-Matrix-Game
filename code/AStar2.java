package code;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class AStar2 implements Strategy {
	boolean visualize;
	
	int nodes = 0;

	int maxRow;
	int maxCol;

	int TelephoneRow;
	int TelephoneCol;

	Node BestSolution;
	Node rootNode;

	ArrayList<String> Portals1;
	ArrayList<String> Portals2;

	ArrayList<State>[][] states;
	ArrayList<State> PreviousFlies;

	PriorityQueue<Node> pq;

	@SuppressWarnings("unchecked")
	public AStar2(String grid,boolean visualize) {
		String[] data = grid.split(";", 0);

		String[] borders = data[0].split(",", 0);
		maxRow = Integer.parseInt(borders[0]) - 1; // max row
		maxCol = Integer.parseInt(borders[1]) - 1; // max col

		states = new ArrayList[maxRow+1][maxCol+1];

		for (int i = 0; i < maxRow + 1; i++)
			for (int j = 0; j < maxCol + 1; j++)
				states[i][j] = new ArrayList<State>();
		

		int NeoCap = Integer.parseInt(data[1]); // neo Capacity

		String[] loc = data[2].split(",", 0);
		int row = Integer.parseInt(loc[0]); // neo row
		int col = Integer.parseInt(loc[1]); // neo col

		String[] TelLoc = data[3].split(",", 0);
		TelephoneRow = Integer.parseInt(TelLoc[0]); // tel row
		TelephoneCol = Integer.parseInt(TelLoc[1]); // tel col

		String[] AgentsLocString = data[4].split(",", 0);
		ArrayList<String> AgentsLoc = new ArrayList<String>(); // ["(1,2)","(4,5)"] hayb2a bel shakl da
		for (int i = 0; i < AgentsLocString.length; i = i + 2) {
			AgentsLoc.add("(" + AgentsLocString[i] + "," + AgentsLocString[i + 1] + ")");
		}

		String[] PillsLocString = data[5].split(",", 0);
		ArrayList<String> PillsLoc = new ArrayList<String>(); // ["(1,2)","(4,5)"] hayb2a bel shakl da
		for (int i = 0; i < PillsLocString.length; i = i + 2) {
			PillsLoc.add("(" + PillsLocString[i] + "," + PillsLocString[i + 1] + ")");
		}

		String[] PortalString = data[6].split(",", 0);
		Portals1 = new ArrayList<String>();
		Portals2 = new ArrayList<String>();
		for (int i = 0; i < PortalString.length; i = i + 4) {
			Portals1.add("(" + PortalString[i] + "," + PortalString[i + 1] + ")");
			Portals2.add("(" + PortalString[i + 2] + "," + PortalString[i + 3] + ")");
		}

		String[] HostagesString = data[7].split(",", 0);
		ArrayList<String> hostageLocs = new ArrayList<String>(); // ["(1,2)","(4,5)"] hayb2a bel shakl da
		ArrayList<Integer> hostageDamages = new ArrayList<Integer>();
		for (int i = 0; i < HostagesString.length; i = i + 3) {
			hostageLocs.add("(" + HostagesString[i] + "," + HostagesString[i + 1] + ")");
			hostageDamages.add(Integer.parseInt(HostagesString[i + 2]));
		}

		PreviousFlies = new ArrayList<State>();
		pq = new PriorityQueue<Node>(Comparator.comparing(Node::getCostPlusHeuristic));

		Node n = new Node(null,row, col, 0, NeoCap, new ArrayList<String>(), new ArrayList<Integer>(),
				new ArrayList<String>(), AgentsLoc, PillsLoc, hostageLocs, hostageDamages, 0,0, "", 0, 0);
		rootNode = n;
		
		this.visualize=visualize;
		
		if(visualize)
			Visual(n.state, 0);
		pq.add(n);
		
	}

	public void Visual(State s, int no) {

		String[][] game = new String[maxRow + 1][maxCol + 1];

		for (int i = 0; i < game.length; i++) {
			for (int j = 0; j < game[i].length; j++) {

				game[i][j] = ".";

				if (i == TelephoneRow && j == TelephoneCol) {
					game[i][j] = "T";
				}
				String a = "(" + i + "," + j + ")";

				if (s.agentLocs.contains(a)) {
					game[i][j] = "A";
				}

				String p = "(" + i + "," + j + ")";

				if (s.pillLocs.contains(p)) {
					game[i][j] = "P";
				}

				String h = "(" + i + "," + j + ")";

				if (s.hostageLocs.contains(h)) {
					int index = s.hostageLocs.indexOf(h);
					game[i][j] = "H" + "(" + s.hostageDamages.get(index) + ")";
				}

				String f = "(" + i + "," + j + ")";

				if (Portals1.contains(f)) {
					if ( Portals1.indexOf(f) % 2==0)
					{
						game[i][j] = "F" + Portals1.indexOf(f);
					}
					else
					{
						game[i][j] = "F" + (Portals1.indexOf(f)-1);
					}
					
				}
				
				String z = "(" + i + "," + j + ")";

				if (s.deadHostages.contains(z)) {
					game[i][j] = "Mutant";
				}
				if (i == s.row && j == s.col) {
					if (game[i][j].equals("."))
						game[i][j] = "N" + "(" + s.Damage + ")";
					else
						game[i][j] = "N" + "(" + s.Damage + ")" + " " + game[i][j];
				}

			}
		}

		String ss = "|";
		for (int i = 0; i < game.length; i++) {
			for (int j = 0; j < game[i].length; j++) {
				if (game[i][j].length() >= 10)
					ss = ss + "\t" + "\t" + game[i][j] + "\t" + "|";
				else
					ss = ss + "\t" + "\t" + game[i][j] + "\t" + "\t" + "|";
			}
			if (i < maxRow) {
				ss += "\n" + "|";
			}

		}

		System.out.println();
		System.out.println(no + " Step");
		System.out.println();
		System.out.println(ss);
	}

	public String solve() {
		int indexOfPortal;
		String newLoc;
		String[] newLocArr;
		Node n;
		State s;
		Node nTemp;
		boolean found;
		ArrayList<State> SimilarStates;
		while (!pq.isEmpty()) {
			n = pq.remove();
			s=n.state;
			
			nodes++;
			found = false;
			if (s.done() && !s.Dead() && BestSolution == null && s.row==TelephoneRow && s.col == TelephoneCol) {
				 
				BestSolution = n;
				break;
			}

			else if ((BestSolution == null || s.cost<=BestSolution.state.cost) && !s.Dead()) {
				SimilarStates = states[s.row][s.col];
				for (int i = 0; i < SimilarStates.size() && !found && SimilarStates.size()!=1; i++) {
					if (IsEqual(SimilarStates.get(i), s)) {
						found = true;
					}
				}

				if (!found) {
					
					 
					if (s.CanCarry()) {
						nTemp = new Node(s,s.row, s.col, s.Damage, s.capacity, s.carriedHostages,
								s.carriedHostagesDamage, s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs,
								s.hostageDamages, 0,s.cost + 1, s.plan + "carry,", s.deaths, s.kills);
						nTemp.state.Carry(); /////// carry walla apply time el awal
						nTemp.state.ApplyTime();
						nTemp.state.AdjustHeuristic2(TelephoneRow,TelephoneCol);
						pq.add(nTemp);
					}

					if (CanDrop(s)) {
						nTemp = new Node(s,s.row, s.col, s.Damage, s.capacity, s.carriedHostages,
								s.carriedHostagesDamage, s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs,
								s.hostageDamages, 0,s.cost + 1, s.plan + "drop,", s.deaths, s.kills);

						nTemp.state.Drop();
						nTemp.state.ApplyTime();
						nTemp.state.AdjustHeuristic2(TelephoneRow,TelephoneCol);
						pq.add(nTemp);
						 
					}

					if (s.IsTherePill()) {
						nTemp = new Node(s,s.row, s.col, s.Damage, s.capacity, s.carriedHostages,
								s.carriedHostagesDamage, s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs,
								s.hostageDamages, 0,s.cost + 1, s.plan + "takePill,", s.deaths, s.kills);
						nTemp.state.TakePill();
						nTemp.state.AdjustHeuristic2(TelephoneRow,TelephoneCol);
						pq.add(nTemp);
						 
					}

					if (s.CanKill() && s.LegalMoveKill()) {
						nTemp = new Node(s,s.row, s.col, s.Damage, s.capacity, s.carriedHostages,
								s.carriedHostagesDamage, s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs,
								s.hostageDamages, 0,s.cost + 1, s.plan, s.deaths, s.kills);
						
						nTemp.state.Kill();
						nTemp.state.ApplyTime();
						nTemp.state.AdjustHeuristic2(TelephoneRow,TelephoneCol);
						pq.add(nTemp);
						
					}

					if (IsTherePortal(s)  && !IsRedundantFly(s)) {
						indexOfPortal = Portals1.indexOf("(" + s.row + "," + s.col + ")");
						newLoc = Portals2.get(indexOfPortal);
						newLoc = newLoc.substring(1, newLoc.length() - 1); // removing brackets
						newLocArr = newLoc.split(",", 0);

						nTemp = new Node(s,Integer.parseInt(newLocArr[0]), Integer.parseInt(newLocArr[1]), s.Damage,
								s.capacity, s.carriedHostages, s.carriedHostagesDamage, s.deadHostages, s.agentLocs,
								s.pillLocs, s.hostageLocs, s.hostageDamages, 0,s.cost + 1, s.plan + "fly,", s.deaths,
								s.kills);
						nTemp.state.ApplyTime();
						nTemp.state.AdjustHeuristic2(TelephoneRow,TelephoneCol);
						pq.add(nTemp);
						 

						PreviousFlies.add(s);
					}

					if (s.row > 0 && !s.IsThereAgentAbove()&&  s.LegalMoveUp()) {
						nTemp = new Node(s,s.row - 1, s.col, s.Damage, s.capacity, s.carriedHostages,
								s.carriedHostagesDamage, s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs,
								s.hostageDamages, 0,s.cost + 1, s.plan + "up,", s.deaths, s.kills);
						nTemp.state.ApplyTime();
						nTemp.state.AdjustHeuristic2(TelephoneRow,TelephoneCol);
						pq.add(nTemp);
						 
					}

					if (s.row < maxRow && !s.IsThereAgentUnder() && s.LegalMoveDown()) {
						nTemp = new Node(s,s.row + 1, s.col, s.Damage, s.capacity, s.carriedHostages,
								s.carriedHostagesDamage, s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs,
								s.hostageDamages, 0,s.cost + 1, s.plan + "down,", s.deaths, s.kills);
						nTemp.state.ApplyTime();
						nTemp.state.AdjustHeuristic2(TelephoneRow,TelephoneCol);
						pq.add(nTemp);

					}

					if (s.col > 0 && !s.IsThereAgentLeft() && s.LegalMoveLeft()) {
						nTemp = new Node(s,s.row, s.col - 1, s.Damage, s.capacity, s.carriedHostages,
								s.carriedHostagesDamage, s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs,
								s.hostageDamages, 0,s.cost + 1, s.plan + "left,", s.deaths, s.kills);
						nTemp.state.ApplyTime();
						nTemp.state.AdjustHeuristic2(TelephoneRow,TelephoneCol);
						pq.add(nTemp);
					}

					if (s.col < maxCol && !s.IsThereAgentRight() && s.LegalMoveRight()) {
						nTemp = new Node(s,s.row, s.col + 1, s.Damage, s.capacity, s.carriedHostages,
								s.carriedHostagesDamage, s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs,
								s.hostageDamages, 0,s.cost + 1, s.plan + "right,", s.deaths, s.kills);
						nTemp.state.ApplyTime();
						nTemp.state.AdjustHeuristic2(TelephoneRow,TelephoneCol);
						pq.add(nTemp);


					}
					states[s.row][s.col].add(s);
				}
			}
		}
		if (BestSolution == null) 
			return "No Solution";

		String returnPlan = BestSolution.state.plan.replaceAll("killMutated", "kill");
		
		if(visualize) {
		s = rootNode.state;

		String[] steps = returnPlan.split(",", 0);

		for (int i = 0; i < steps.length; i++) {
			if (steps[i].contains("right")) {
				nTemp = new Node(s,s.row, s.col + 1, s.Damage, s.capacity, s.carriedHostages, s.carriedHostagesDamage,
						s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs, s.hostageDamages, 0,s.cost + 1,
						s.plan + "right,", s.deaths, s.kills);
				nTemp.state.ApplyTime();
			} else if (steps[i].contains("left")) {
				nTemp = new Node(s,s.row, s.col - 1, s.Damage, s.capacity, s.carriedHostages, s.carriedHostagesDamage,
						s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs, s.hostageDamages, 0,s.cost + 1,
						s.plan + "left,", s.deaths, s.kills);
				nTemp.state.ApplyTime();
			} else if (steps[i].contains("up")) {
				nTemp = new Node(s,s.row - 1, s.col, s.Damage, s.capacity, s.carriedHostages, s.carriedHostagesDamage,
						s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs, s.hostageDamages, 0,s.cost + 1,
						s.plan + "up,", s.deaths, s.kills);
				nTemp.state.ApplyTime();
			} else if (steps[i].contains("down")) {
				nTemp = new Node(s,s.row + 1, s.col, s.Damage, s.capacity, s.carriedHostages, s.carriedHostagesDamage,
						s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs, s.hostageDamages, 0,s.cost + 1,
						s.plan + "down,", s.deaths, s.kills);
				nTemp.state.ApplyTime();
			} else if (steps[i].contains("carry")) {
				nTemp = new Node(s,s.row, s.col, s.Damage, s.capacity, s.carriedHostages, s.carriedHostagesDamage,
						s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs, s.hostageDamages, 0,s.cost + 1,
						s.plan + "carry,", s.deaths, s.kills);
				nTemp.state.Carry();
				nTemp.state.ApplyTime();
			} else if (steps[i].contains("fly")) {
				nTemp = new Node(s,s.row, s.col + 1, s.Damage, s.capacity, s.carriedHostages, s.carriedHostagesDamage,
						s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs, s.hostageDamages, 0,s.cost + 1,
						s.plan + "right,", s.deaths, s.kills);
				indexOfPortal = Portals1.indexOf("(" + s.row + "," + s.col + ")");
				newLoc = Portals2.get(indexOfPortal);
				newLoc = newLoc.substring(1, newLoc.length() - 1); // removing brackets
				newLocArr = newLoc.split(",", 0);

				nTemp = new Node(s,Integer.parseInt(newLocArr[0]), Integer.parseInt(newLocArr[1]), s.Damage, s.capacity,
						s.carriedHostages, s.carriedHostagesDamage, s.deadHostages, s.agentLocs, s.pillLocs,
						s.hostageLocs, s.hostageDamages, 0,s.cost + 1, s.plan + "fly,", s.deaths, s.kills);
				nTemp.state.ApplyTime();
			} else if (steps[i].contains("drop")) {
				nTemp = new Node(s,s.row, s.col, s.Damage, s.capacity, s.carriedHostages, s.carriedHostagesDamage,
						s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs, s.hostageDamages, 0,s.cost + 1,
						s.plan + "drop,", s.deaths, s.kills);
				nTemp.state.ApplyTime();
				nTemp.state.Drop();
			} else if (steps[i].contains("takePill")) {
				nTemp = new Node(s,s.row, s.col, s.Damage, s.capacity, s.carriedHostages, s.carriedHostagesDamage,
						s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs, s.hostageDamages, 0,s.cost + 10,
						s.plan + "takePill,", s.deaths, s.kills);
				nTemp.state.TakePill();
			} else { // kill
				nTemp = new Node(s,s.row, s.col, s.Damage, s.capacity, s.carriedHostages, s.carriedHostagesDamage,
						s.deadHostages, s.agentLocs, s.pillLocs, s.hostageLocs, s.hostageDamages, 0,s.cost + 100, s.plan,
						s.deaths, s.kills);
				nTemp.state.ApplyTime();
				nTemp.state.Kill();
			}
			Visual(nTemp.state, i + 1);

			s = nTemp.state;
		}
		}
		String outtt = returnPlan.substring(0, returnPlan.length() - 1); // removing last comma
		outtt += ";";
		outtt += BestSolution.state.deaths;
		outtt += ";";
		outtt += BestSolution.state.kills;
		outtt += ";";
		outtt += nodes;

		return outtt;
	}

	public boolean CanDrop(State s) {
		if (s.row == TelephoneRow && s.col == TelephoneCol && s.carriedHostages.size() != 0)
			return true;
		return false;
	}

	public boolean IsRedundantFly(State s) {
		for (int i = 0; i < PreviousFlies.size(); i++) {
			if (IsEqual(s, PreviousFlies.get(i)))
				return true;
		}
		return false;

	}

	public Boolean IsEqual(State s, State s2) {
		if (s.row == s2.row && s.col == s2.col && s.Damage == s2.Damage && s.carriedHostages.equals(s2.carriedHostages) && s.deadHostages.equals(s2.deadHostages)
				&& s.agentLocs.equals(s2.agentLocs) && s.pillLocs.equals(s2.pillLocs)
				&& s.hostageLocs.equals(s2.hostageLocs) )
			return true;
		return false;
	}

	public boolean IsTherePortal(State s) {
		if (Portals1.contains("(" + s.row + "," + s.col + ")"))
			return true;
		return false;
	}

	public boolean IsRelevantMove(State s, String move) { // || path[i].contains("killMutated")
		String[] path = s.plan.split(",", 0);

		if (move == "right") {
			for (int i = path.length - 1; i >= 0; i--) {
				if (path[i].contains("left"))
					return false;
				if (path[i].contains("carry") || path[i].contains("drop") || path[i].contains("fly")|| path[i].contains("killMutated")
						|| path[i].contains("takePill")|| path[i].contains("killMutated"))
					return true;
			}
		}

		if (move == "left") {
			for (int i = path.length - 1; i >= 0; i--) {
				if (path[i].contains("right"))
					return false;
				if (path[i].contains("carry") || path[i].contains("drop") || path[i].contains("fly")|| path[i].contains("killMutated")
						|| path[i].contains("takePill")|| path[i].contains("killMutated"))
					return true;
			}
		}

		if (move == "up") {
			for (int i = path.length - 1; i >= 0; i--) {
				if (path[i].contains("down"))
					return false;
				if (path[i].contains("carry") || path[i].contains("drop") || path[i].contains("fly")|| path[i].contains("killMutated")
						|| path[i].contains("takePill")|| path[i].contains("killMutated"))
					return true;
			}
		}

		if (move == "down") {
			for (int i = path.length - 1; i >= 0; i--) {
				if (path[i].contains("up"))
					return false;
				if (path[i].contains("carry") || path[i].contains("drop") || path[i].contains("fly")|| path[i].contains("killMutated")
						|| path[i].contains("takePill")|| path[i].contains("killMutated"))
					return true;
			}
		}

		if (move == "fly" && path[path.length - 1].contains("fly")) {
			return false;
		}
		return true;
	}

}
