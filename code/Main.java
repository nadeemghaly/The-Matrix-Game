package code;

import java.util.Random;

public class Main {
	@SuppressWarnings("unused")
	public static void main(String []args) {
		
		String grid0 = "5,5;2;3,4;1,2;0,3,1,4;2,3;4,4,0,2,0,2,4,4;2,2,91,2,4,62";
		String grid1 = "5,5;1;1,4;1,0;0,4;0,0,2,2;3,4,4,2,4,2,3,4;0,2,32,0,1,38";
		String grid2 = "5,5;2;3,2;0,1;4,1;0,3;1,2,4,2,4,2,1,2,0,4,3,0,3,0,0,4;1,1,77,3,4,34";
		String grid3 = "5,5;1;0,4;4,4;0,3,1,4,2,1,3,0,4,1;4,0;2,4,3,4,3,4,2,4;0,2,98,1,2,98,2,2,98,3,2,98,4,2,98,2,0,1";
		String grid4 = "5,5;1;0,4;4,4;0,3,1,4,2,1,3,0,4,1;4,0;2,4,3,4,3,4,2,4;0,2,98,1,2,98,2,2,98,3,2,98,4,2,98,2,0,98,1,0,98";
		String grid5 = "5,5;2;0,4;3,4;3,1,1,1;2,3;3,0,0,1,0,1,3,0;4,2,54,4,0,85,1,0,43";
		String grid6 = "5,5;2;3,0;4,3;2,1,2,2,3,1,0,0,1,1,4,2,3,3,1,3,0,1;2,4,3,2,3,4,0,4;4,4,4,0,4,0,4,4;1,4,57,2,0,46";
		String grid7 = "5,5;3;1,3;4,0;0,1,3,2,4,3,2,4,0,4;3,4,3,0,4,2;1,4,1,2,1,2,1,4,0,3,1,0,1,0,0,3;4,4,45,3,3,12,0,2,88";
		String grid8 = "5,5;2;4,3;2,1;2,0,0,4,0,3,0,1;3,1,3,2;4,4,3,3,3,3,4,4;4,0,17,1,2,54,0,0,46,4,1,22";
		String grid9 = "5,5;2;0,4;1,4;0,1,1,1,2,1,3,1,3,3,3,4;1,0,2,4;0,3,4,3,4,3,0,3;0,0,30,3,0,80,4,4,80";
		String grid10 = "5,5;4;1,1;4,1;2,4,0,4,3,2,3,0,4,2,0,1,1,3,2,1;4,0,4,4,1,0;2,0,0,2,0,2,2,0;0,0,62,4,3,45,3,3,39,2,3,40";

		
		String gridTest= genGrid();
		
		System.out.println(solve(grid0,"UC",true));  
		
	}
	
	public static String genGrid() {
		String grid="";
		Random random = new Random();
		
		
		//GRID SIZE
		int m= 6;
		int n= 6;
		
		
		//NUMBER OF HOSTAGES NEO CAN CARRY
		
		int c= random.nextInt(4) + 1;
		
		
		//LOCATION OF NEO
		int NeoRow = random.nextInt(m) ;
		int NeoCol = random.nextInt(n) ;
		
		
		//LOCATION OF TELEPHONE
		int TelephoneRow;
		int TelephoneCol;
		do{    
			TelephoneRow = random.nextInt(m) ;
			TelephoneCol = random.nextInt(n) ;
			}while (TelephoneRow==NeoRow && TelephoneCol==NeoCol); 
		
		
		//SPAWN AGENTS ON THE GRID
		int numberOfAgents = random.nextInt((m*n)/3) +1;
		int AgentLocation[] = new int[numberOfAgents*2];   // [Agent1 x, Agent1 Y, Agent2 x, Agent2 Y, ....]
		int AgentRow;
		int AgentCol;
		for(int i =0;i<numberOfAgents;i++) {
			do{    
				AgentRow = random.nextInt(m) ;
				AgentCol = random.nextInt(n) ;
				}while (	(AgentRow==NeoRow && AgentCol==NeoCol) || 
							(AgentRow==TelephoneRow && AgentCol==TelephoneCol) || 
							checkLocation(AgentRow,AgentCol,AgentLocation)	);    
			
			AgentLocation[2*i]=AgentRow;
			AgentLocation[2*i+1]=AgentCol;
		}
		
		
		//SPAWN HOSTAGES ON THE GRID
		int numberOfHostages = random.nextInt(8) +3;
		int HostageLocation[] = new int[numberOfHostages*2];   // [Hostage1 x, Hostage1 Y, Hostage2 x, Hostage2 Y ....]
		int HostageDamage[] = new int[numberOfHostages]; // [Hostage1 DMG, Hostage2 DMG, Hostage3 DMG, ....]
		int HostageRow;
		int HostageCol;
		int HostageDMG;
		for(int i =0;i<numberOfHostages;i++) {
			do{    
				HostageRow = random.nextInt(m);
				HostageCol = random.nextInt(n);
				HostageDMG = random.nextInt(99) + 1;
				}while (	(HostageRow==NeoRow && HostageCol==NeoCol) || 
							(HostageRow==TelephoneRow && HostageCol==TelephoneCol) || 
							checkLocation(HostageRow,HostageCol,AgentLocation) ||
							checkLocation(HostageRow,HostageCol,HostageLocation));    
			
			HostageLocation[2*i]=HostageRow;
			HostageLocation[2*i+1]=HostageCol;
			HostageDamage[i]=HostageDMG;
		}
		
		
		//SPAWN PILLS
		int numberOfPills = random.nextInt(numberOfHostages) +1;
		int PillLocation[] = new int[numberOfPills*2];   // [Pill1 x, Pill1 Y, Pill2 x, Pill2 Y ....]
		int PillRow;
		int PillCol;
		for(int i =0;i<numberOfPills;i++) {
			do{    
				PillRow = random.nextInt(m) ;
				PillCol = random.nextInt(n) ;
				}while (	(PillRow==NeoRow && PillCol==NeoCol) || 
							(PillRow==TelephoneRow && PillCol==TelephoneCol) || 
							checkLocation(PillRow,PillCol,AgentLocation) ||
							checkLocation(PillRow,PillCol,HostageLocation)||
							checkLocation(PillRow,PillCol,PillLocation));
			
			PillLocation[2*i]=PillRow;
			PillLocation[2*i+1]=PillCol;
		}
		
		
		//SPAWN PADS
		int totalCells = m*n;
		int freeCells = totalCells-numberOfAgents-numberOfHostages-numberOfPills-2;   //2 is for Neo and telephone
		int numberOfPads = random.nextInt(freeCells/4)+1;
		int StartPad[] = new int[numberOfPads*2]; // [StartPad1 x, StartPad1 Y, StartPad2 x, StartPad2 Y ....]
		int FinishPad[] = new int[numberOfPads*2]; // [FinishPad1 x, FinishPad1 Y, FinishPad2 x, FinishPad2 Y ....]
		int StartPadRow;
		int StartPadCol;
		int FinishPadRow;
		int FinishPadCol;
		for(int i =0;i<numberOfPads;i++) {
			do{    
				StartPadRow = random.nextInt(m);
				StartPadCol = random.nextInt(n);
				}while (	(StartPadRow==NeoRow && StartPadCol==NeoCol) || 
							(StartPadRow==TelephoneRow && StartPadCol==TelephoneCol) || 
							checkLocation(StartPadRow,StartPadCol,AgentLocation) ||
							checkLocation(StartPadRow,StartPadCol,HostageLocation)||
							checkLocation(StartPadRow,StartPadCol,PillLocation) ||
							checkLocation(StartPadRow,StartPadCol,StartPad) ||
							checkLocation(StartPadRow,StartPadCol,FinishPad) );
			
			StartPad[2*i]=StartPadRow;
			StartPad[2*i+1]=StartPadCol;
			
			do{    
				FinishPadRow = random.nextInt(m);
				FinishPadCol = random.nextInt(n);
				}while (	(FinishPadRow==NeoRow && FinishPadCol==NeoCol) || 
							(FinishPadRow==TelephoneRow && FinishPadCol==TelephoneCol) || 
							checkLocation(FinishPadRow,FinishPadCol,AgentLocation) ||
							checkLocation(FinishPadRow,FinishPadCol,HostageLocation)||
							checkLocation(FinishPadRow,FinishPadCol,PillLocation)||
							checkLocation(FinishPadRow,FinishPadCol,StartPad)||
							checkLocation(FinishPadRow,FinishPadCol,FinishPad));
			
			FinishPad[2*i]=FinishPadRow;
			FinishPad[2*i+1]=FinishPadCol;
		}
		
		
		//STRING output
		grid+=m+","+n+";";

		grid+=c+";";
		
		grid+=NeoRow+","+NeoCol+";";
		
		grid+=TelephoneRow+","+TelephoneCol+";";
		
		for(int i=0;i<AgentLocation.length;i++) {
			grid+=AgentLocation[i];
			if(i+1<AgentLocation.length)
				grid+=",";
		}
		grid+=";";
		
		for(int i=0;i<PillLocation.length;i++) {
			grid+=PillLocation[i];
			if(i+1<PillLocation.length)
				grid+=",";
		}
		grid+=";";
		
		for(int i=0;i<StartPad.length;i=i+2) {
			//normal
			grid+=StartPad[i]+",";
			grid+=StartPad[i+1]+",";
			grid+=FinishPad[i]+",";
			grid+=FinishPad[i+1]+",";
			//inverted
			grid+=FinishPad[i]+",";
			grid+=FinishPad[i+1]+",";
			grid+=StartPad[i]+",";
			grid+=StartPad[i+1];
			if(i+2<StartPad.length)
				grid+=",";
			
		}
		grid+=";";
		
		for(int i =0;i<numberOfHostages;i++) {
			grid+=HostageLocation[2*i]+",";
			grid+=HostageLocation[2*i+1]+",";
			grid+=HostageDamage[i];
			if(i+1<numberOfHostages)
				grid+=",";
		}
		grid+=";";
		
		return grid;
	}
	
	public static boolean checkLocation(int row, int column, int[] Locations) { //true means overlapping items
		for(int i=0;i<Locations.length;i=i+2) {
			if(row==Locations[i] && column==Locations[i+1])
				return true;
		}
		return false;
	}

	public static String solve(String grid, String strategy, boolean visualize) {
		Strategy strat = null;
		
		if(strategy=="BF")
			strat=new BreadthFirst(grid,visualize);
		
		else if(strategy=="DF")
			strat=new DepthFirst(grid,visualize);
		
		else if(strategy=="ID")
			strat=new IterativeDeepening(grid,visualize);
		
		else if(strategy=="UC")
			strat=new UniformCost(grid,visualize);
		
		else if(strategy=="GR1")
			strat=new Greedy1(grid,visualize);
		
		else if(strategy=="GR2")
			strat=new Greedy2(grid,visualize);
		
		else if(strategy=="AS1")
			strat=new AStar1(grid,visualize);
		
		else if(strategy=="AS2")
			strat=new AStar2(grid,visualize);
		
		else return "ERROR, invalid strategy";
		
		return strat.solve();
	}
}
