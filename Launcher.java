
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Launcher {
	
	public static void main(String args[]) throws IOException {
		Position.killerMove = new int[25];
		
		Position game = new Position();
		int result = 0, c = 0;
		char x = '.';
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		long time = 0;
		System.out.print("WELCOME to Oware!\n");
		System.out.print("Do you want the Computer to make the first move (y/n)? ");
		if(br.readLine().equalsIgnoreCase("y"))
			game.setComputerStarts(true);
		else
			game.setComputerStarts(false);
		System.out.println();
		
		do {
			game.dispBoard();

			if (game.computerPlay) {
				long t = System.currentTimeMillis();
				result = computerMove(game);
				time = time + (System.currentTimeMillis() - t);
				c++;
			} else {
				System.out.printf("Your Move: ");
				String a = br.readLine();
				x = a.charAt(0);

				switch (x) {
				case 'a':
					result = game.makeMove(0);
					break;

				case 'b':
					result = game.makeMove(1);
					break;

				case 'c':
					result = game.makeMove(2);
					break;

				case 'd':
					result = game.makeMove(3);
					break;

				case 'e':
					result = game.makeMove(4);
					break;

				case 'f':
					result = game.makeMove(5);
					break;

				case 'x':
				case 'X':
					for (int i = 0; i < 6; i++) {
						game.computerSeeds += game.computerCells[i];
						game.playerSeeds += game.playerCells[i];
					}
					if (game.computerSeeds == 24 && game.playerSeeds == 24) {
						System.out.println("It is a draw!");
					}
					if (game.computerSeeds > 24) {
						System.out.println("Computer Won!");
					}
					if (game.playerSeeds > 24) {
						System.out.println("You won!");
					}

					break;

				default:
					System.out.print("Invalid Move\n");
					break;
				}
			}

		} while (x != 'x' && x != 'X' && result != 1);

		System.out.print("Final board scenario:\n");
		game.dispBoard();
		System.out.print("Goodbye! Hope to see you again. \n");
		System.out.printf("Avg time taken per move: %3.2f s",((double)time/(1000*c)));
	}

	static int computerMove(Position currPos) {
		int i;
		
		Position tmp, t1;
		DepthMonitor winningChance[] = new DepthMonitor[6];
		tmp = new Position(currPos); // copy
		double alpha = -1000, beta = 1000;
		int move = Position.killerMove[0], res = move;
		int scoreDiff[] = new int[6];

		for (i = 0; i < 6; i++) 
		{
			if (tmp.validMove(move)) 
			{
				t1 = new Position(tmp);
				tmp.simulateMove(move);
				winningChance[move] = tmp.minimax(1, alpha, beta, 17+(int)(((double)tmp.computerSeeds + (double)tmp.playerSeeds)/8));
				if(winningChance[move].score > alpha)
				{
					res = move;
					scoreDiff[move] = tmp.computerSeeds - tmp.playerSeeds;
					alpha = winningChance[move].score ;
					Position.killerMove[0] = move;
				}
				else if(winningChance[move].score == alpha )
				{
					if (winningChance[res].closestWinDepth > winningChance[move].closestWinDepth)
						res = move;
					else if(winningChance[res].closestWinDepth == winningChance[move].closestWinDepth && scoreDiff[res] < scoreDiff[move])
						res = move;
				}
				tmp = new Position(t1);
			} 
			move = (move + 1)%6;
		}
		System.out.printf("\nComputer's Move: %c\n", 65 + res);
		return currPos.makeMove(res);
	}

}
