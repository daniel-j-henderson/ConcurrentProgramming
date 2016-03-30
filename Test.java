
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
	static Board solution = new Board();
	static Board b = new Board();
	static Board[] boards;
	static int N = 9;
	int numth;
	static int numboards;
	static AtomicInteger index = new AtomicInteger(0);
	static boolean puzzleSolved = false;
	private Thread threads[];

	Test(int n){
		numth = n;
		threads = new Thread[numth];
	}
	class myRunnable implements Runnable {
		Board myBoard;
		int id;


		public myRunnable(int id_) {
			this.id = id_;
		}

		public void run() {
			long start = System.nanoTime();
			int myIndex = index.getAndIncrement();
			while (myIndex < numboards && !puzzleSolved) {
				myBoard = new Board(boards[myIndex]);
				System.out.println("Thread "+id+" checking board "+myIndex+" of "+numboards);
				if (force(0,0,myBoard)) {System.out.println("Solved by Thread "+id+" on board "+myIndex+" in "+(System.nanoTime()-start)/10000 + " milliseconds"); puzzleSolved = true;}
				myIndex = index.getAndIncrement();
			}
		}


	}

	public void createThreads() {
		for(int i=0; i<numth; i++) {
			threads[i] = new Thread(new myRunnable(i));
		}
		for(int i=0; i < numth; i++) {
			threads[i].start();
		}
		for (int i=0; i<numth; i++) {
			try {
			threads[i].join();
			}
			catch (InterruptedException exception) {
			}
		}
	
	}

	public static void main(String[] args) {

		solution.init();
		b.init();
		b.printPosVals(0,1);
		
		int[][] puzzle = {{0,2,9,0,0,0,3,1,0},
				  		  {0,8,0,0,0,0,4,0,9},
						  {3,0,6,0,2,0,0,0,0},
						  {0,6,0,0,3,1,7,0,0},
						  {0,0,0,0,5,8,0,0,0},
						  {0,0,0,0,0,0,0,0,0},
						  {0,3,7,0,0,6,0,8,0},
						  {0,0,1,0,0,0,5,0,0},
						  {0,9,0,0,8,0,0,3,7}};
		
		for (int i = 0; i<9; i++){
			for (int j=0; j<9; j++){
				if(puzzle[i][j]!=0) b.setvalue(i, j, puzzle[i][j]);
			}
		}

		/*numboards = 20;
		boards = new Boards[numboards];
		for (int i=0; i<numboards; i++) {
			boards[i] = new Board(b);
		}*/





		//Vector <Board> bs = new Vector <Board>();
		int l = 0;
		int m = 0;
		numboards = 0;
		int numcellsToPush = 3;
		int[] permsToPush = new int[numcellsToPush];
		int[] cellsToPush = new int[numcellsToPush];
		int[][] valsToPush = new int[numcellsToPush][9];

		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				m++;
				if (b.vals[i][j] == 0) {
					for (int k=0; k<9; k++) {
						if (b.posvalues[i][j][k]) {
							//bs.add(new Board(b));
							valsToPush[l][permsToPush[l]] = k+1;
							permsToPush[l]++;
						}
					cellsToPush[l] = m-1;
					}
					l++;
				}
				if(l == numcellsToPush) break;
			}
			if(l == numcellsToPush) break;

		}
		numboards = 1;
		for (int i=0; i<numcellsToPush; i++) numboards *= permsToPush[i];
		boards = new Board[numboards];
		for (int i=0; i<numboards; i++) {boards[i] = new Board(b);}

		//numboards = bs.size();
		//boards = new Board[numboards];
		//bs.toArray(boards);
		l=0;
		int stride = 45;
		for (int i=0; i<numcellsToPush; i++) {
			stride = stride/(permsToPush[i]);
			int which = 0;
			int count = 0;
			for (int j = 0; j<numboards; j++) {
				count++;
				if(count == stride) {count = 0; which++; which = which%permsToPush[i];}
				//which = (which + j/stride) % permsToPush[i];
				boards[j].setvalue(cellsToPush[i]/9, cellsToPush[i]%9, valsToPush[i][which]);
				

			}
		}


		System.out.println("Original Board\n");
		b.print();
		

		/*boolean solved = force(0,0,b);
		if (solved) System.out.println("Solved\n");
		else System.out.println("Not Solved\n");*/
		
		long initTime = System.nanoTime();
		System.out.println("T\n\n");
		Test t = new Test(1);
		t.createThreads();
		if (!solution.isSolved()) System.out.println("Solution is incorrect\n");
		
		
		System.out.println("T1\n\n");
		index.set(0);
		puzzleSolved = false;
		//solution.print();
		Test t1 = new Test(2);
		t1.createThreads();
		
		System.out.println("T2\n\n");
		index.set(0);
		puzzleSolved = false;
		//solution.print();
		Test t2 = new Test(3);
		t2.createThreads();
		
		System.out.println("T3\n\n");
		index.set(0);
		puzzleSolved = false;
		//solution.print();
		Test t3 = new Test(4);
		t3.createThreads();
		
		System.out.println("T4\n\n");
		index.set(0);
		puzzleSolved = false;
		//solution.print();
		Test t4 = new Test(10);
		t4.createThreads();

		System.out.println(" In total, that took " + (System.nanoTime() - initTime)/1000000 + " milliseconds");
		solution.print();
	}

	static boolean force(int row, int col, Board copy) {
		
		if (row == 8 && col == 8 && (copy.vals[row][col] != 0)) {
			solution = copy; 
			return true;
		}

		int newrow = row;
		boolean found;
		if (col == 8) newrow++;

		if (copy.vals[row][col] != 0) found = force(newrow, (col + 1)%9, copy);
		else {
			found = false;
			for (int j = 0; j<9; j++) {
				if (copy.posvalues[row][col][j]) {
					Board newcopy = new Board(copy);
					newcopy.setvalue(row, col, j+1);

					if ((row == 8) && (col == 8)) {
						solution = newcopy; 
						return true;
					}
					found = force(newrow, (col + 1)%9, newcopy);
				}
				if (found) {
					return true;
				}
			}
		}
		return found;
	}


}

class Board {
	int[][] vals = new int[9][9];
	boolean[][][] posvalues = new boolean[9][9][9];

	public Board(){}

	Board(Board a) {
		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				vals[i][j] = a.vals[i][j];
			}
		}
		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				for (int k=0; k<9; k++) {
					posvalues[i][j][k] = a.posvalues[i][j][k];
				}
			}
		}
	}

	void init()	{
		for (int i=0; i<9; i++){
			for (int j=0; j<9; j++) {
				for (int k=0; k<9; k++) {
					posvalues[i][j][k] = true;
				}
			}
		}
	}

	void setvalue(int row, int col, int x) {
		vals[row][col] = x;
		int rowstart = 3 * (row / 3);
		int colstart = 3 * (col / 3);
		for (int i=0; i<9; i++) {
			if (i != x-1) posvalues[row][col][i] = false;
			if (i != col) posvalues[row][i][x-1] = false;
			if (i != row) posvalues[i][col][x-1] = false;
			if (((rowstart + i/3) != row) && ((colstart + i%3) != col)) posvalues[rowstart + i/3][colstart + i%3][x-1] = false;
		}
	}

	boolean isValid(int row, int col, int x) {
		int rowstart = 3 * (row / 3);
		int colstart = 3 * (col / 3);
		for (int i=0; i<9; i++) {
			if (vals[row][i] == x && i != col) return false;
			if (vals[i][col] == x && i != row) return false;
			if (vals[rowstart + i/3][colstart + i%3] == x && (rowstart + i/3) != row && (colstart + i%3) != col) return false;
		}
		return true;
	}
	
	boolean isSolved() {
		boolean solved = true;
		for (int i=0; i<9; i++){
			for (int j=0; j<9; j++) {
				if (!isValid(i,j,vals[i][j])) solved = false;
			}
		}
		return solved;
	}

	void print() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(vals[i][j] + "\t");
			}
			System.out.println("\n");
		}
		System.out.println("\n");
	}

	void printPosVals(int row, int col) {
		System.out.print("posvalues[" + row + "][" + col + "] = ");
		for (int i = 0; i<9; i++) {
			System.out.print(posvalues[row][col][i] + "\t");
		}
		System.out.print("\n");
	}
	
}