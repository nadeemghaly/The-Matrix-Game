
# The Matrix Game

# Introduction:
In this project, we have a simulation for a THE MATRIX game. This game consists of a grid and on that grid we have NEO who is our main character, to win the game Neo has to carry all hostages to the telephone booth which then takes them outside the matrix but in doing so we he can face multiple scenarios like: 

1. Finding agents (which are his enemies)
2. Being late to reach a hostage (when a hostage dies he becomes an empowered agent)
3. Finding a pill (pills decrease damage of Neo and all hostages on the map)
4. Finding a pad (pads are found in pairs, Neo can take the pad to transport to the other pad)

To finish the game, Neo must make sure that all the Mutated Hostages that are now agents are successfuly killed, otherwise the game won't end.

# Grid Specification:
The grid is described in a String form. Each item will be seperated from the next and previous items using the ";" character.

1. Size of the grid (Rows,Columns)
2. Maximum number of hostages Neo can carry
3. Starting Location of Neo (Row,Column)
4. Location of Telephone Booth (Row,Column)
5. Location of all Agents on the map, (Row,Column) for each Agent
6. Location of all Pills on the map, (Row,Column) for each Pill
7. Location of pads, (Row pad1, Column pad1, Row pad2, Column pad2, Row pad2, Column pad2, Row pad1, Column pad1)
8. Hostage Location and Damage (Row, Column, Damage)

NOTE: to create pads (4,4)(0,2) they would be 4,4,0,2,0,2,4,4 in the grid string.

#Neo Actions:
Neo can do all of the following Actions:

1. Move in any direction (can't go outside grid and cant stand on the same cell with an agent)
2. Carry a Hostage (must be in the same cell with this hostage)
3. Take pill (which decreases damage of Neo and all Hostages by 20)
4. Drop Hostages (must be at telephone booth and carrying any hostages)
5. Kill Agents (kills all agents surronding Neo but increasing his damage by 20)
6. Fly (must be standing on a pad and that transports him to its finish pad) 

# Approaches: 

- Breadth First
- Depth First
- Iterative Deepening
- Uniform Cost
- Greedy1
- Greedy2
- Astar1
- Astar2

# Result:
When done, The code should output the set of actions required by Neo in order to finish the game with best effeciency with respect to the chosen approach, Followed by the number of hostages that died during this game simulation, follow by number of agents killed, followed by the number of game instances inspected to find this result.

##EXAMPLE:
up,carry,left,takePill,left,carry,up,drop;0;2;224
Means that none of the hostages died, but Neo had to kill 2 Agents, and we had to check 224 game instances to find this answer.


