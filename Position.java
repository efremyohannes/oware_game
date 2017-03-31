
public class Position {
	static int killerMove[];
	
	int playerCells[]; // each cell contains a certain number of seeds
	int computerCells[];
	boolean computerPlay; // boolean true if the computer has to play and false otherwise
	int playerSeeds; // seeds taken by the player
	int computerSeeds; // seeds taken by the computer
	int emptyComputerPits;
	int emptyPlayerPits;
	Position() {
		playerCells = new int[6];
		computerCells = new int[6];
		
		playerCells[0] = 4;
		playerCells[1] = 4;
		playerCells[2] = 4;
		playerCells[3] = 4;
		playerCells[4] = 4;
		playerCells[5] = 4;
		computerCells[0] = 4;
		computerCells[1] = 4;
		computerCells[2] = 4;
		computerCells[3] = 4;
		computerCells[4] = 4;
		computerCells[5] = 4;

		emptyComputerPits = 0;
		emptyPlayerPits = 0;

		computerPlay = true;
		computerSeeds = 0;
		playerSeeds = 0;
	}
	
	void setComputerStarts(boolean computerStarts)
	{
		computerPlay = computerStarts;
	}

	Position(Position a) {
		playerCells = a.playerCells.clone();
		computerCells = a.computerCells.clone();
		computerPlay = a.computerPlay;
		computerSeeds = a.computerSeeds;
		playerSeeds = a.playerSeeds;
		emptyComputerPits = a.emptyComputerPits;
		emptyPlayerPits = a.emptyPlayerPits;
	}

	void simulateMove(int pit) {
		int inOtherTerritory = 0;
		int originalPit = pit;
		int tmp;

		if (computerPlay) {
			tmp = computerCells[pit];
			computerCells[pit] = 0;
		} else {
			tmp = playerCells[pit];
			playerCells[pit] = 0;
		}

		if (computerPlay) //computer
		{
			pit--;
			while (tmp > 0) {
				while (pit >= 0 && tmp > 0) {
					if (pit != originalPit) {
						computerCells[pit] += 1;
						tmp--;
					}
					inOtherTerritory = 0;
					pit--;
				}
				pit++;
				while (pit < 6 && tmp > 0) {
					playerCells[pit] += 1;
					pit++;
					tmp--;
					inOtherTerritory = 1;
				}
				pit--;
			}
		} else if (!computerPlay) //player
		{
			pit++;
			while (tmp > 0) {
				while (pit < 6 && tmp > 0) {
					inOtherTerritory = 0;
					if (originalPit != pit) {
						playerCells[pit] += 1;
						tmp--;
					}
					pit++;
				}
				pit--;
				while (pit >= 0 && tmp > 0) {
					inOtherTerritory = 1;
					computerCells[pit] += 1;
					pit--;
					tmp--;
				}
				pit++;
			}
		}
		Position restore = new Position(this);


		/*now to the real board*/
		if (!restore.leadingToStarvation(pit, inOtherTerritory)) {
			if (inOtherTerritory == 1 && (((computerPlay && playerCells[pit] == 2) || (!computerPlay && computerCells[pit] == 2)) || ((computerPlay && playerCells[pit] == 3) || (!computerPlay && computerCells[pit] == 3)))) {
				if (computerPlay) {
					while (pit >= 0 && (playerCells[pit] == 2 || playerCells[pit] == 3)) {
						computerSeeds += playerCells[pit];
						playerCells[pit] = 0;
						pit--;
					}
				} else if (!computerPlay) {
					while (pit < 6 && (computerCells[pit] == 2 || computerCells[pit] == 3)) {
						playerSeeds += computerCells[pit];
						computerCells[pit] = 0;
						pit++;
					}
				}
			}
		}

		/*updating empty cells*/
		emptyPlayerPits = (playerCells[0] == 0 ? 1 : 0) + (playerCells[1] == 0 ? 1 : 0)
				+ (playerCells[2] == 0 ? 1 : 0) + (playerCells[3] == 0 ? 1 : 0)
				+ (playerCells[4] == 0 ? 1 : 0) + (playerCells[5] == 0 ? 1 : 0);

		emptyComputerPits = (computerCells[0] == 0 ? 1 : 0) + (computerCells[1] == 0 ? 1 : 0)
				+ (computerCells[2] == 0 ? 1 : 0) + (computerCells[3] == 0 ? 1 : 0)
				+ (computerCells[4] == 0 ? 1 : 0) + (computerCells[5] == 0 ? 1 : 0);
		/*done updating empty cells*/

		computerPlay = !computerPlay;
	}

	boolean finalPosition() {
		if ((computerSeeds == 24 && playerSeeds == 24)
				|| (computerSeeds > 24)
				|| (playerSeeds > 24)
				|| (!computerPlay && (computerCells[0]
						+ computerCells[1]
								+ computerCells[2]
										+ computerCells[3]
												+ computerCells[4]
														+ computerCells[5] == 0))
														|| (computerPlay && (playerCells[0]
																+ playerCells[1]
																		+ playerCells[2]
																				+ playerCells[3]
																						+ playerCells[4]
																								+ playerCells[5] == 0))) {
			return true;
		}
		return false;
	}

	boolean validMove(int i) {
		if (emptyPlayerPits == 6 || emptyComputerPits == 6) {
			if (computerPlay) {
				if (computerCells[i] > i) {
					return true;
				}
				return false;
			}

			if (computerCells[i] > 5 - i) //if !computerPlay
			{
				return true;
			}
			return false;

		}
		if (computerPlay && computerCells[i] > 0) {
			return true;
		}
		if ((!computerPlay) && playerCells[i] > 0) {
			return true;
		}
		return false;
	}

	boolean leadingToStarvation(int pit, int inOtherTerritory) {
		/*testing if it makes all positions of opponent 0*/

		if (inOtherTerritory == 1 && (((computerPlay && playerCells[pit] == 2) || (!computerPlay && computerCells[pit] == 2)) || ((computerPlay && playerCells[pit] == 3) || (!computerPlay && computerCells[pit] == 3)))) {

			if (computerPlay) {
				while (pit >= 0 && (playerCells[pit] == 2 || playerCells[pit] == 3)) {
					computerSeeds += playerCells[pit];
					playerCells[pit] = 0;
					pit--;
				}
			} else if (!computerPlay) {
				while (pit < 6 && (computerCells[pit] == 2 || computerCells[pit] == 3)) {
					playerSeeds += computerCells[pit];
					computerCells[pit] = 0;
					pit++;
				}
			}
		}

		emptyPlayerPits = (playerCells[0] == 0 ? 1 : 0) + (playerCells[1] == 0 ? 1 : 0)
				+ (playerCells[2] == 0 ? 1 : 0) + (playerCells[3] == 0 ? 1 : 0)
				+ (playerCells[4] == 0 ? 1 : 0) + (playerCells[5] == 0 ? 1 : 0);

		emptyComputerPits = (computerCells[0] == 0 ? 1 : 0) + (computerCells[1] == 0 ? 1 : 0)
				+ (computerCells[2] == 0 ? 1 : 0) + (computerCells[3] == 0 ? 1 : 0)
				+ (computerCells[4] == 0 ? 1 : 0) + (computerCells[5] == 0 ? 1 : 0);
		return (computerPlay && emptyPlayerPits == 6) || (!computerPlay && emptyComputerPits == 6);
	}

	DepthMonitor minimax(int depth, double alpha, double beta, int maxDepth) {

		int i, winEstimate = 999;
		double winCount = 0, validCount = 0;
		DepthMonitor res = null;
		boolean win = false;
		

		if (finalPosition()) {
			if (computerSeeds == 25 && playerSeeds == 25) {
				return new DepthMonitor(depth, 1, alpha, beta, false, 0.5);
			}

			if (computerSeeds > 24) {
				return new DepthMonitor(depth, 48, alpha, beta, true, 1.0);
			}

			if (playerSeeds > 24) {
				return new DepthMonitor(winEstimate, -48, alpha, beta, false, 0.0);
			}

			if (!computerPlay && ((computerCells[0] + computerCells[1] + computerCells[2] + computerCells[3] + computerCells[4] + computerCells[5]) == 0)) {
				return new DepthMonitor(winEstimate, -48, alpha, beta, false, 0.0);
			}

			if (computerPlay && (playerCells[0] + playerCells[1] + playerCells[2] + playerCells[3] + playerCells[4] + playerCells[5] == 0)) {
				return new DepthMonitor(depth, 48, alpha, beta, true, 1.0);
			}

		}

		if (depth == maxDepth) {
			return new DepthMonitor(depth, (double) (computerSeeds - playerSeeds), alpha, beta, false, 0.0);
		}
		int move = killerMove[depth];
		if (computerPlay) {
			for (i = 0; i < 6; i++) {
				Position nextPos = new Position(this);
				if (validMove(move)) {
					validCount++;
					nextPos.simulateMove(move);
					
					res = nextPos.minimax(depth + 1, alpha, beta, maxDepth);
					winEstimate = Math.min(res.closestWinDepth, winEstimate);
					alpha = Math.max(alpha, res.score);
					
					if (beta <= alpha) {
						killerMove[depth] = move;
						break; // beta cutoff
					}
				}
				move = (move + 1)%6;
			}
			
			return new DepthMonitor(winEstimate, alpha, alpha, beta, win, (double)winCount/validCount);
		}

		if (!computerPlay) {
			for (i = 0; i < 6; i++) {
				Position nextPos = new Position(this);
				if (validMove(move)) {
					validCount++;
					nextPos.simulateMove(move);					
					res = nextPos.minimax(depth + 1, alpha, beta, maxDepth);
					
					beta = Math.min(beta, res.score);
					if (beta <= alpha) {
						break;
					}
				} 
				move = (move + 1)%6;
			}
			
			return new DepthMonitor(winEstimate, beta, alpha, beta, win, (double)winCount/validCount);
		}

		return new DepthMonitor();
	}

	int makeMove(int pit) {

		int inOtherTerritory = 0;
		int originalPit = pit;
		int tmp;

		if (computerPlay) {
			tmp = computerCells[pit];
			computerCells[pit] = 0;
		} else {
			tmp = playerCells[pit];
			playerCells[pit] = 0;
		}

		if (computerPlay) //computer
		{
			pit--;
			while (tmp > 0) {
				while (pit >= 0 && tmp > 0) {
					if (pit != originalPit) {
						computerCells[pit] += 1;
						tmp--;
					}
					inOtherTerritory = 0;
					pit--;
				}
				pit++;
				while (pit < 6 && tmp > 0) {
					playerCells[pit] += 1;
					pit++;
					tmp--;
					inOtherTerritory = 1;
				}
				pit--;
			}
		} else if (!computerPlay) //player
		{
			pit++;
			while (tmp > 0) {
				while (pit < 6 && tmp > 0) {
					inOtherTerritory = 0;
					if (originalPit != pit) {
						playerCells[pit] += 1;
						tmp--;
					}
					pit++;
				}
				pit--;
				while (pit >= 0 && tmp > 0) {
					inOtherTerritory = 1;
					computerCells[pit] += 1;
					pit--;
					tmp--;
				}
				pit++;
			}
		}

		Position restore = new Position(this);

		if (!restore.leadingToStarvation(pit, inOtherTerritory)) {

			if (inOtherTerritory == 1 && (((computerPlay && playerCells[pit] == 2) || (!computerPlay && computerCells[pit] == 2)) || ((computerPlay && playerCells[pit] == 3) || (!computerPlay && computerCells[pit] == 3)))) {

				if (computerPlay) {
					while (pit >= 0 && (playerCells[pit] == 2 || playerCells[pit] == 3)) {
						computerSeeds += playerCells[pit];
						playerCells[pit] = 0;
						pit--;
					}
				} else if (!computerPlay) {
					while (pit < 6 && (computerCells[pit] == 2 || computerCells[pit] == 3)) {
						playerSeeds += computerCells[pit];
						computerCells[pit] = 0;
						pit++;
					}
				}
			}

			/*updating empty cells*/
			emptyPlayerPits = (playerCells[0] == 0 ? 1 : 0) + (playerCells[1] == 0 ? 1 : 0)
			+ (playerCells[2] == 0 ? 1 : 0) + (playerCells[3] == 0 ? 1 : 0)
			+ (playerCells[4] == 0 ? 1 : 0) + (playerCells[5] == 0 ? 1 : 0);

			emptyComputerPits = (computerCells[0] == 0 ? 1 : 0) + (computerCells[1] == 0 ? 1 : 0)
					+ (computerCells[2] == 0 ? 1 : 0) + (computerCells[3] == 0 ? 1 : 0)
					+ (computerCells[4] == 0 ? 1 : 0) + (computerCells[5] == 0 ? 1 : 0);
			/*done updating empty cells*/
		}

		if (computerSeeds == 24 && playerSeeds == 24) {
			System.out.printf("The match is a draw!\n");
			return 1;
		}

		if (computerSeeds > 24) {
			System.out.printf("The computer won!!\n");
			return 1;
		}

		if (playerSeeds > 24) {
			System.out.printf("Congratulations! You won!\n");
			return 1;
		}

		if (!computerPlay && (computerCells[0] + computerCells[1] + computerCells[2] + computerCells[3] + computerCells[4] + computerCells[5] == 0)) {
			System.out.printf("Congratulations! You won!\n");
			return 1;
		}

		if (computerPlay && (playerCells[0] + playerCells[1] + playerCells[2] + playerCells[3] + playerCells[4] + playerCells[5] == 0)) {
			System.out.printf("The computer won! :)\n");
			return 1;
		}
		computerPlay = !computerPlay;

		return 0;
	}

	void dispBoard() {
		System.out.print("=================Computer==================\n");
		System.out.print("A\tB\tC\tD\tE\tF\n");
		System.out.print(computerCells[0]+"\t"+computerCells[1]+"\t"+computerCells[2]+"\t"+computerCells[3]+"\t"+computerCells[4]+"\t"+computerCells[5]+"\n");
		System.out.print("-------------------------------------------\n");
		System.out.print(playerCells[0]+"\t"+playerCells[1]+"\t"+playerCells[2]+"\t"+playerCells[3]+"\t"+playerCells[4]+"\t"+playerCells[5]+"\n");
		System.out.print("a\tb\tc\td\te\tf\n");
		System.out.print("====================You====================\n");
		System.out.print("Computer's Score: "+computerSeeds+"\nYour Score: "+playerSeeds+"\n\n");
	}
}
