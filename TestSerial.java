import java.util.concurrent.atomic.AtomicInteger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Math;

public class TestSerial {
	static Board solution;
	static Board myBoard;
	static int N = 9;
	static int rN = 3;

	static boolean force(int row, int col, Board copy) {
		
		if (row == (N-1) && col == (N-1) && (copy.vals[row][col] != 0)) {
			solution = copy; 
			return true;
		}

		int newrow = row;
		boolean found;
		if (col == (N-1)) newrow++;

		if (copy.vals[row][col] != 0) found = force(newrow, (col + 1)%N, copy);
		else {
			found = false;
			for (int j = 0; j<N; j++) {
				if (copy.posvalues[row][col][j]) {
					Board newcopy = new Board(copy);
					newcopy.setvalue(row, col, j+1);

					if ((row == (N-1)) && (col == (N-1))) {
						solution = newcopy; 
						return true;
					}
					found = force(newrow, (col + 1)%N, newcopy);
				}
				if (found) {
					return true;
				}
			}
		}
		return found;
	}

	public static void main(String[] args) throws IOException {

		FileReader file;
		try {
			file = new FileReader(args[0]);
		}
		catch (FileNotFoundException ex) {
			System.out.println("File not found");
			return;
		}

		BufferedReader br = new BufferedReader(file);
	    String line = null;
	    int [][] puzzle = new int[N][N];
	    int ll=0, mm=0;
	    while ((line = br.readLine()) != null) {
	      String[] values = line.split(",");
	      if (ll == 0 && values.length != 9) {
	      	N = values.length;
	      	rN = (int) Math.sqrt(N);
	      	puzzle = new int[N][N];
	      }
	      mm=0;
	      for (String str : values) {
	      	puzzle[ll][mm] = Integer.parseInt(str);
	      	mm++;
	      }
	      ll++;
	    }
	    br.close();

		boolean USE_HUMANISTIC = true; //Flag to use humanistic methods or not
		
		
		myBoard = new Board(puzzle);
		solution = new Board(puzzle);

		System.out.println("Original Board\n");
		myBoard.print();

		double start = System.nanoTime();
		double htime = start;
		boolean puzzleSolved = false;
		boolean solvedit = false;
		if (USE_HUMANISTIC) {
			boolean changedElim = true;
			boolean changedRanger = true;
			boolean changedTwins = true;
			boolean changedTriples = true;
			while (changedElim || changedRanger || changedTwins || changedTriples) {

				changedElim = false;
				changedRanger = false;
				changedTwins = false;
				changedTriples = false;
				
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++) {
						if (myBoard.elimination(i, j)) changedElim = true;
					}
				}
				if (changedElim){
					continue;
				}
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++) {
						if (myBoard.loneRanger(i, j)) {
							changedRanger = true;
							//System.out.println("Lone Ranger Capped a Bitch");
							break;
						}
						// System.out.println(i+"\t"+j);
					}
					if (changedRanger == true)
						break;
				}
				if (changedRanger == true)
					continue;

				if (myBoard.isSolved()) break;
				
				//System.out.println("Entering Twins");
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++) {
						if (myBoard.findTwins(i, j)) {
							changedTwins = true;// found twins b4 6,4 on first time
							//System.out.println("Found some fraternal twins");
							break;
						}
						// System.out.println(i+"\t"+j);
					}
					if (changedTwins == true)
						break;
				}
				if(changedTwins==true) continue;
				if (myBoard.isSolved()) break;
				//System.out.println("Entering Triplets");
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++) {
						if (myBoard.findTriples(i, j)) {
							changedTriples = true;// found twins b4 6,4 on first time
							//System.out.println("Found the three stooges");
							break;
						}
					}
					if (changedTriples == true)
						break;
				}

			}

			solvedit = myBoard.isSolved();
			if (solvedit) {puzzleSolved = true; solution = myBoard;}
			htime = htime + (System.nanoTime() - htime);
		}

			
		if(!solvedit) solvedit = force(0,0,myBoard);
		htime = (htime - start) / 1000000.0;
		double totaltime = (System.nanoTime()-start)/1000000.0;
		double ftime = totaltime - htime;

		if (solvedit) {
			puzzleSolved = true;
			if (USE_HUMANISTIC) {
				System.out.println("Solved in "+ totaltime + " milliseconds \n\tHumn. Alg. Time: " + htime + " Brute Force Time: " + ftime); 
			}
			else {
				System.out.println("Solved in "+(System.nanoTime()-start)/1000000.0 + " milliseconds"); 
			}
		}

		System.out.println("Solution: ");
		solution.print();

	}
}








