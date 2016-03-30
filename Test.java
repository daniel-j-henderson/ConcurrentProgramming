import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
	static Board solution = new Board();
	static Board b = new Board();
	static Board[] boards;
	static int N = 9;
	static int numth = 4;
	static int numboards;
	static AtomicInteger index = new AtomicInteger(0);
	static boolean puzzleSolved = false;
	private Thread threads[] = new Thread[numth];


	class myRunnable implements Runnable {
		Board myBoard;
		int id;


		public myRunnable(int id_) {
			this.id = id_;
		}

		public void run() {
			int myIndex = index.getAndIncrement();
			while (myIndex < numboards && !puzzleSolved) {
				myBoard = new Board(boards[myIndex]);
				System.out.println("Thread "+id+" checking board "+myIndex+" of "+numboards);
				if (force(0,0,myBoard)) {System.out.println("Solved by Thread "+id); puzzleSolved = true;}
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





		Vector <Board> bs = new Vector <Board>();
		int l = 0;
		int m = 0;
		numboards = 0;
		int numcellsToPush = 6;
		int[] permsToPush = new int[numcellsToPush];
		int[] cellsToPush = new int[numcellsToPush];
		int[][] valsToPush = new int[numcellsToPush][9];

		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				m++;
				if (b.vals[i][j] == 0) {
					for (int k=0; k<9; k++) {
						if (b.posvalues[i][j][k]) {
							bs.add(new Board(b));
							valsToPush[l][permsToPush[l]] = k+1;
							permsToPush[l]++;
						}
					cellsToPush[l] = m;
					}
					l++;
				}
				if(l == numcellsToPush) break;
			}
			if(l == numcellsToPush) break;

		}

		numboards = bs.size();
		boards = new Board[numboards];
		bs.toArray(boards);
		l=0;
		int stride = 1;
		int p=0;

		for (int i=0; i<numcellsToPush; i++) {
			stride = numboards/(stride*permsToPush[i]);
			int which = 0;
			for (int j = 0; j<numboards; j++) {
				which = (which + j/stride) % permsToPush[i];
				boards[j].setvalue(cellsToPush[i]/9, cellsToPush[i]%9, valsToPush[i][which]);

			}
		}


		/*for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				if(b.vals[i][j] != 0) {
					stride = numboards / (stride * permsToPush[p]);
					for (int k=0; k<numboards; k++) {
						for (int q=0; q<permsToPush[p]) 

					}
				}
			}
		}

*/




/*
		for (int k=0; k<81; k++) {
			for (int i=0; i<9; i++) {
				if (b.posvalues[k/9][k%9][i] && b.vals[k/9][k%9] == 0) {
					bs.add(new Board(b));
				}
			}
			if (b.vals[k/9][k%9] == 0) l++;
			if(l >=6) break;
		}
		boards = new Board[bs.size()];
		bs.toArray(boards);
		numboards = l;

		l = 0;
		for (int k=0; k<81; k++) {
			for (int i=0; i<9; i++) {
				if (b.posvalues[k/9][k%9][i] && b.vals[k/9][k%9] == 0) {
					boards[j].setvalue(k/9,k%9,i+1);
					j++;
				}
			}
			if (b.vals[k/9][k%9] == 0) l++;
			if(l >=6) break;
		}*/

		/*for (int i=0; i<9; i++) {
			if (b.posvalues[0][0][i]) {
				boards[j].setvalue(0,0,i+1);
				j++;
			}
		}*/
		//numboards = j;
		System.out.println("boards has size "+bs.size());




		System.out.println("Original Board\n");
		b.print();

		/*boolean solved = force(0,0,b);
		if (solved) System.out.println("Solved\n");
		else System.out.println("Not Solved\n");*/

		Test t = new Test();
		t.createThreads();

		index.set(0);
		puzzleSolved = false;

		Test t1 = new Test();
		t1.createThreads();


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
			if (vals[row][i] == x) return false;
			if (vals[i][col] == x) return false;
			if (vals[rowstart + i/3][colstart + i%3] == x) return false;
		}
		return true;
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