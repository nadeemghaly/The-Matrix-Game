package code;

import java.util.ArrayList;

public class State {

	public int row; // neo Location
	public int col; // neo Location
	public int Damage; // neo Damage

	public int capacity;
	public ArrayList<String> carriedHostages;
	public ArrayList<Integer> carriedHostagesDamage;

	public ArrayList<String> deadHostages;

	public ArrayList<String> agentLocs;
	public ArrayList<String> pillLocs;
	public ArrayList<String> hostageLocs;
	public ArrayList<Integer> hostageDamages;

	public int heuristic;
	public int cost; // ashan akhtar el optimum solution
	public String plan;
	public int deaths;
	public int kills;

	public State(int row, int col, int Damage, int capacity, ArrayList<String> carriedHostages,
			ArrayList<Integer> carriedHostagesDamage, ArrayList<String> deadHostages, ArrayList<String> agentsLoc,
			ArrayList<String> pillLocs, ArrayList<String> hostageLocs, ArrayList<Integer> hostageDamages, int heuristic,
			int cost, String plan, int deaths, int kills) {
		this.row = row;
		this.col = col;
		this.Damage = Damage;

		this.capacity = capacity;
		this.carriedHostages = new ArrayList<String>();
		for (int i = 0; i < carriedHostages.size(); i++) {
			this.carriedHostages.add(carriedHostages.get(i));
		}
		this.carriedHostagesDamage = new ArrayList<Integer>();
		for (int i = 0; i < carriedHostagesDamage.size(); i++) {
			this.carriedHostagesDamage.add(carriedHostagesDamage.get(i));
		}

		this.deadHostages = new ArrayList<String>();
		for (int i = 0; i < deadHostages.size(); i++) {
			this.deadHostages.add(deadHostages.get(i));
		}
		this.agentLocs = new ArrayList<String>();
		for (int i = 0; i < agentsLoc.size(); i++) {
			this.agentLocs.add(agentsLoc.get(i));
		}
		this.pillLocs = new ArrayList<String>();
		for (int i = 0; i < pillLocs.size(); i++) {
			this.pillLocs.add(pillLocs.get(i));
		}
		this.hostageLocs = new ArrayList<String>();
		for (int i = 0; i < hostageLocs.size(); i++) {
			this.hostageLocs.add(hostageLocs.get(i));
		}
		this.hostageDamages = new ArrayList<Integer>();
		for (int i = 0; i < hostageDamages.size(); i++) {
			this.hostageDamages.add(hostageDamages.get(i));
		}
		this.heuristic = heuristic;
		this.cost = cost;
		this.plan = plan;
		this.deaths = deaths;
		this.kills = kills;
	}

	public void AdjustHeuristic1() {
		this.heuristic = deadHostages.size() * 100;
	}

	public void AdjustHeuristic2(int telRow, int telCol) {

		int numberOfPills = pillLocs.size();

		int hostageDamage;
		int numberOfStepsToHostage = 0;
		int numberOfStepsToTelBooth = 0;
		String hostageLoc;
		int hostageRow;
		int hostageCol;

		for (int i = 0; i < hostageDamages.size(); i++) {
			hostageLoc = hostageLocs.get(i);
			hostageLoc = hostageLoc.substring(1, hostageLoc.length() - 1); // to remove the brakcets

			hostageRow = Integer.parseInt(hostageLoc.split(",", 0)[0]);
			hostageCol = Integer.parseInt(hostageLoc.split(",", 0)[1]);

			numberOfStepsToHostage = Math.abs(this.row - hostageRow) + Math.abs(this.col - hostageCol); // the number of
																										// steps to walk
																										// directly to
																										// this hostage

			hostageDamage = hostageDamages.get(i);
			hostageDamage -= 20 * numberOfPills; // his health after taking all the pills on the map
			if (hostageDamage < 0)
				hostageDamage = 0;

			hostageDamage += 2 * numberOfStepsToHostage; // his health after neo walks directly to him

			if (hostageDamage >= 100) // there is no way to save this hostage, i have to let him die then kill him
				this.heuristic += 200;
		}
		for (int i = 0; i < carriedHostagesDamage.size(); i++) {
			hostageRow = this.row;
			hostageCol = this.col;

			numberOfStepsToTelBooth = Math.abs(this.row - hostageRow) + Math.abs(this.col - hostageCol); // the number
																											// of steps
																											// to walk
																											// directly
																											// to this
																											// hostage

			hostageDamage = carriedHostagesDamage.get(i);
			hostageDamage -= 20 * numberOfPills; // his health after taking all the pills on the map
			if (hostageDamage < 0)
				hostageDamage = 0;

			hostageDamage += 2 * numberOfStepsToTelBooth; // his health after neo walks directly to the telephone booth
															// to drop him

			if (hostageDamage >= 100) // there is no way to save this hostage, he has to die
				this.heuristic += 100;
		}
	}

	public boolean IsThereAgentAbove() {
		if (agentLocs.contains("(" + (row - 1) + "," + col + ")")
				|| deadHostages.contains("(" + (row - 1) + "," + col + ")"))
			return true;
		return false;
	}

	public boolean LegalMoveUp() {
		if (hostageLocs.contains("(" + (row - 1) + "," + col + ")")) {
			int i = hostageLocs.indexOf("(" + (row - 1) + "," + col + ")");
			if (hostageDamages.get(i) == 98 || hostageDamages.get(i) == 99)
				return false;
		}
		return true;
	}

	public boolean IsThereAgentUnder() {
		if (agentLocs.contains("(" + (row + 1) + "," + col + ")")
				|| deadHostages.contains("(" + (row + 1) + "," + col + ")"))
			return true;
		return false;
	}

	public boolean LegalMoveDown() {
		if (hostageLocs.contains("(" + (row + 1) + "," + col + ")")) {
			int i = hostageLocs.indexOf("(" + (row + 1) + "," + col + ")");
			if (hostageDamages.get(i) == 98 || hostageDamages.get(i) == 99)
				return false;
		}
		return true;
	}

	public boolean IsThereAgentRight() {
		if (agentLocs.contains("(" + row + "," + (col + 1) + ")")
				|| deadHostages.contains("(" + row + "," + (col + 1) + ")"))
			return true;
		return false;
	}

	public boolean LegalMoveRight() {
		if (hostageLocs.contains("(" + row + "," + (col + 1) + ")")) {
			int i = hostageLocs.indexOf("(" + row + "," + (col + 1) + ")");
			if (hostageDamages.get(i) == 98 || hostageDamages.get(i) == 99)
				return false;
		}
		return true;
	}

	public boolean IsThereAgentLeft() {
		if (agentLocs.contains("(" + row + "," + (col - 1) + ")")
				|| deadHostages.contains("(" + row + "," + (col - 1) + ")"))
			return true;
		return false;
	}

	public boolean LegalMoveLeft() {
		if (hostageLocs.contains("(" + row + "," + (col - 1) + ")")) {
			int i = hostageLocs.indexOf("(" + row + "," + (col - 1) + ")");
			if (hostageDamages.get(i) == 98 || hostageDamages.get(i) == 99)
				return false;
		}
		return true;
	}

	public boolean LegalMoveKill() {
		if (hostageLocs.contains("(" + row + "," + col + ")")) {
			int i = hostageLocs.indexOf("(" + row + "," + col + ")");
			if (hostageDamages.get(i) == 98 || hostageDamages.get(i) == 99)
				return false;
		}
		return true;
	}

	public boolean CanKill() {
		if (IsThereAgentAbove() || IsThereAgentUnder() || IsThereAgentLeft() || IsThereAgentRight())
			return true;
		return false;
	}

	public void Kill() {
		Damage += 20;
		Boolean flag = false;

		if (Damage >= 100) {
			Damage = 100; // Neo mat fa da ma3nah en el le3ba bazet
		}
		if (deadHostages.contains("(" + (row - 1) + "," + col + ")")
				|| deadHostages.contains("(" + (row + 1) + "," + col + ")")
				|| deadHostages.contains("(" + row + "," + (col + 1) + ")")
				|| deadHostages.contains("(" + row + "," + (col - 1) + ")"))
			flag = true;

		if (IsThereAgentAbove()) {
			agentLocs.remove("(" + (row - 1) + "," + col + ")");
			deadHostages.remove("(" + (row - 1) + "," + col + ")");
			kills++;
			cost += 1000;
		}
		if (IsThereAgentUnder()) {
			agentLocs.remove("(" + (row + 1) + "," + col + ")");
			deadHostages.remove("(" + (row + 1) + "," + col + ")");
			kills++;
			cost += 1000;
		}
		if (IsThereAgentRight()) {
			agentLocs.remove("(" + row + "," + (col + 1) + ")");
			deadHostages.remove("(" + row + "," + (col + 1) + ")");
			kills++;
			cost += 1000;
		}
		if (IsThereAgentLeft()) {
			agentLocs.remove("(" + row + "," + (col - 1) + ")");
			deadHostages.remove("(" + row + "," + (col - 1) + ")");
			kills++;
			cost += 1000;
		}

		if (flag)
			plan += "killMutated,";
		else
			plan += "kill,";

	}

	////////////////////////////////////////////////////////////////////////

	public boolean IsTherePill() {
		if (pillLocs.contains("(" + row + "," + col + ")"))
			return true;
		return false;
	}

	public void TakePill() {
		Damage -= 20;
		if (Damage < 0)
			Damage = 0;

		int TempHostageDamage;
		for (int i = 0; i < hostageDamages.size(); i++) {
			TempHostageDamage = hostageDamages.get(i);
			TempHostageDamage -= 20;
			if (TempHostageDamage < 0)
				TempHostageDamage = 0;
			hostageDamages.set(i, TempHostageDamage);
		}

		for (int i = 0; i < carriedHostagesDamage.size(); i++) {
			if (carriedHostagesDamage.get(i) != 100) {
				TempHostageDamage = carriedHostagesDamage.get(i);
				TempHostageDamage -= 20;
				if (TempHostageDamage < 0)
					TempHostageDamage = 0;
				carriedHostagesDamage.set(i, TempHostageDamage);
			}
		}

		pillLocs.remove("(" + row + "," + col + ")");
	}

	////////////////////////////////////////////////////////////////////////

	public void ApplyTime() {

		int CurrentHostageDamage;
		for (int i = 0; i < hostageDamages.size(); i = i + 1) {
			CurrentHostageDamage = hostageDamages.get(i);

			if (CurrentHostageDamage >= 98) // da mayet already
			{
				deadHostages.add(hostageLocs.get(i));
				hostageDamages.remove(i);
				hostageLocs.remove(i);
				i--; // ashan shelt element fa lazem arga3 el index
				deaths++;
				cost += 100000;
			} else // da 3ayesh rabena yedelo tolet el 3omr
				hostageDamages.set(i, CurrentHostageDamage + 2);
		}

		for (int i = 0; i < carriedHostagesDamage.size(); i = i + 1) {
			CurrentHostageDamage = carriedHostagesDamage.get(i);

			if (CurrentHostageDamage == 100) {

			} else if (CurrentHostageDamage == 98 || CurrentHostageDamage == 99) // da haymoot now
			{
				deaths++;
				carriedHostagesDamage.set(i, 100);
				cost += 100000;
			} else // da 3ayesh rabena yedelo tolet el 3omr
				carriedHostagesDamage.set(i, CurrentHostageDamage + 2);
		}
	}

	public boolean CanCarry() {
		if (carriedHostages.size() < capacity && hostageLocs.contains("(" + row + "," + col + ")"))
			return true;
		return false;
	}

	public void Carry() {
		int index = hostageLocs.indexOf("(" + row + "," + col + ")");

		carriedHostages.add("(" + row + "," + col + ")");
		carriedHostagesDamage.add(hostageDamages.get(index));

		hostageLocs.remove(index);
		hostageDamages.remove(index);

	}

	////////////////////////////////////////////////////////////////////////

	public Boolean Dead() {
		if (Damage >= 100)
			return true;
		return false;
	}

	public void Drop() {

		carriedHostages.clear();
		carriedHostagesDamage.clear();
	}

	public boolean done() {
		if (hostageLocs.size() == 0 && deadHostages.size() == 0 && carriedHostages.size() == 0)
			return true;
		return false;
	}

	public int getCost() {
		return this.cost;
	}

	public int getHeuristic() {
		return this.heuristic;
	}

	public int getCostPlusHeuristic() {
		return this.cost + this.heuristic;
	}

}
