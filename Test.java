
import java.util.concurrent.atomic.AtomicInteger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Math;

public class Test {
	static Board solution;
	static Board b;
	static Board[] boards;
	static int N = 9;
	static int rN = 3;
	int numth;
	boolean USE_HUMANISTIC;
	static int numboards;
	static AtomicInteger index = new AtomicInteger(0);
	static boolean puzzleSolved = false;
	private Thread threads[];

	Test(int n, boolean h){
		numth = n;
		USE_HUMANISTIC = h;
		threads = new Thread[numth];
	}
	class myRunnable implements Runnable {
		Board myBoard;
		int id;


		public myRunnable(int id_) {
			this.id = id_;
		}

		public void run() {
			double start = System.nanoTime();
			double htime = start;
			int myIndex = index.getAndIncrement();
			while (myIndex < numboards && !puzzleSolved) {
				myBoard = new Board(boards[myIndex]);
				//System.out.println("Thread "+id+" checking board "+myIndex+" of "+numboards);

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
						System.out.println("Solved by Thread "+id+" on board "+myIndex+" in "+ totaltime + " milliseconds \n\tHumn. Alg. Time: " + htime + " Brute Force Time: " + ftime); 
					}
					else {
						System.out.println("Solved by Thread "+id+" on board "+myIndex+" in "+(System.nanoTime()-start)/1000000.0 + " milliseconds"); 
					}
				}
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

		boolean hum = true; //Flag to use humanistic methods or not
		
		
		b = new Board(puzzle);
		solution = new Board(puzzle);


		System.out.println("Original Board\n");
		b.print();

		int l = 0;
		int m = 0;
		numboards = 0;
		int numcellsToPush = 3;
		int[] permsToPush = new int[numcellsToPush];
		int[] cellsToPush = new int[numcellsToPush];
		int[][] valsToPush = new int[numcellsToPush][N];

		for (int i=0; i<N; i++) {
			for (int j=0; j<N; j++) {
				m++;
				if (b.vals[i][j] == 0) {
					for (int k=0; k<N; k++) {
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

		l=0;
		int stride = numboards;
		for (int i=0; i<numcellsToPush; i++) {
			stride = stride/(permsToPush[i]);
			int which = 0;
			int count = 0;
			for (int j = 0; j<numboards; j++) {
				count++;
				if(count == stride) {count = 0; which++; which = which%permsToPush[i];}
				//which = (which + j/stride) % permsToPush[i];
				boards[j].setvalue(cellsToPush[i]/N, cellsToPush[i]%N, valsToPush[i][which]);
				

			}
		}

		
		long initTime = System.nanoTime();
		System.out.println("\n\nWith 1 Thread:\n");
		Test t = new Test(1, hum);
		t.createThreads();
		if (!solution.isSolved()) System.out.println("Solution is incorrect\n");
		
		
		System.out.println("\n\nWith 2 Threads:\n");
		index.set(0);
		puzzleSolved = false;
		//solution.print();
		Test t1 = new Test(2, hum);
		t1.createThreads();
		
		System.out.println("\n\nWith 3 Threads:\n");
		index.set(0);
		puzzleSolved = false;
		//solution.print();
		Test t2 = new Test(3, hum);
		t2.createThreads();
		
		System.out.println("\n\nWith 4 Threads:\n");
		index.set(0);
		puzzleSolved = false;
		//solution.print();
		Test t3 = new Test(4, hum);
		t3.createThreads();
		
		System.out.println("\n\nWith 10 Threads:\n");
		index.set(0);
		puzzleSolved = false;
		//solution.print();
		Test t4 = new Test(10, hum);
		t4.createThreads();

		System.out.println(" In total, that took " + (System.nanoTime() - initTime)/1000000 + " milliseconds");
		solution.print();
	}

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


}

class Board {
	static int N = 9;
	static int rN = 3;

	int[][] vals;
	boolean[][][] posvalues;

	public Board(){
		vals = new int[N][N];
		posvalues = new boolean[N][N][N];
		for (int i=0; i<N; i++){
			for (int j=0; j<N; j++) {
				for (int k=0; k<N; k++) {
					posvalues[i][j][k] = true;
				}
			}
		}
	}

	public Board(int[][] puzzle){
		
		vals = new int[N][N];
		posvalues = new boolean[N][N][N];

		for (int i=0; i<N; i++){
			for (int j=0; j<N; j++) {
				for (int k=0; k<N; k++) {
					posvalues[i][j][k] = true;
				}
			}
		}
		for (int i = 0; i<N; i++){
			for (int j=0; j<N; j++){
				if(puzzle[i][j]!=0) setvalue(i, j, puzzle[i][j]);
			}
		}
	}

	public Board(Board a) {
		
		vals = new int[N][N];
		posvalues = new boolean[N][N][N];
		for (int i=0; i<N; i++) {
			for (int j=0; j<N; j++) {
				vals[i][j] = a.vals[i][j];
			}
		}
		for (int i=0; i<N; i++) {
			for (int j=0; j<N; j++) {
				for (int k=0; k<N; k++) {
					posvalues[i][j][k] = a.posvalues[i][j][k];
				}
			}
		}
	}

	




	int[] getRow(int row) { // returns the row array
		int[] rows = new int[N];

		for (int i = 0; i < N; i++) {
			rows[i] = vals[row][i];
		}
		return rows;
	}

	int[] getCol(int col) { // returns the column array
		int[] cols = new int[N];

		for (int i = 0; i < N; i++) {
			cols[i] = vals[i - 1][col];
		}
		return cols;
	}

	int[] getSquare(int squareval) { // returns the square array squares are
										// defined from 0-8 going from top left
										// to top right then down a square like
										// a book
		int firstcol = (squareval * rN) % N;
		int firstrow = (squareval / rN) * rN;
		int[] square = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		for (int j = 0; j < rN; j++) {
			for (int i = 0; i < rN; i++) {
				square[rN * j + i] = vals[firstrow + j][firstcol + i];
			}
		}
		return square;
	}

	boolean[][] getSquarePosValues(int squareval) {// returns the possible
													// values for the cells in a
													// square
		int firstcol = (squareval * rN) % N;
		int firstrow = (squareval / rN) * rN;
		boolean[][] square = new boolean[N][N];

		for (int ii = 0; ii < N; ii++) {
			for (int j = 0; j < rN; j++) {
				for (int i = 0; i < rN; i++) {
					square[rN * j + i][ii] = posvalues[firstrow + j][firstcol + i][ii];
				}
			}
		}
		return square;
	}


/****************************************************************
*
* Elimination: This humanistic method looks for only one possible 
* option in the cell and, if found, writes to the board and  
* updates possible values of all other pertinent cells.
*
*****************************************************************/


	public boolean elimination(int row, int col) { 
		int j = 0;
		for (int i = 0; i < N; i++) { // checking how many values are possible for one cell
			if (posvalues[row][col][i])
				j++;
		}
		if (j == 1) { 
			int ii = 0;
			while (!posvalues[row][col][ii]) {
				ii++;
			}
			setvalue(row, col, ii + 1);
			return true;
		}
		return false;
	}

/****************************************************************
*
* Lone Ranger: This humanistic method looks for a number that only
* appears once in a possible values array for all cells in a given 
* row, column, or square. If so then it sets that value. 
*
*****************************************************************/

	boolean loneRanger(int row, int col) {
		boolean foundInSquare;
		boolean foundInRow;
		boolean foundInCol;
		for (int x = 0; x < N; x++) {
			foundInSquare = false;
			foundInCol = false;
			foundInRow = false;
			if (posvalues[row][col][x]) {
				int rowstart = (row / rN) * rN;
				int colstart = (col / rN) * rN;
				for (int i = 0; i < N; i++) {
					if (posvalues[row][i][x] && (i != col)){
						foundInCol = true;
						break;
					}
				}
				
				for (int i = 0; i < N; i++) {
					if (posvalues[i][col][x] && (i != row))
						foundInRow = true;
						
				}
				
				for (int i = 0; i < N; i++) {
					if (posvalues[rowstart + i / rN][colstart + i % rN][x] && (((rowstart + i / rN) != row) || ((colstart + i % rN) != col)))
						foundInSquare = true;
				}
				
				if (!foundInSquare || !foundInCol || !foundInRow) {
					setvalue(row, col, x + 1);
					return true;
				}
			}
		}
		return false;
	}




/****************************************************************
*
* Twins: This humanistic method takes a given sector of the board, 
* either a row, column or square, and looks for a pair of possible 
* values that only occur in a pair of cells and eliminates all but
* the twins as possible values for those cells.
*
*****************************************************************/

	boolean findTwins(int index, int type) { 
		int counttwin1 = 0;
		boolean[][] item = new boolean[N][N];
		if (type == 0) { // type 0=row type 1=column type2=square
			item = posvalues[index];
		}
		if (type == 1) {
			for (int j = 0; j < N; j++) {
				item[j] = posvalues[j][index];
			}
		}
		if (type == 2) {
			item = getSquarePosValues(index);
		}

		int[] indextwin1 = new int[2];
		for (int twin1 = 0; twin1 < N; twin1++) {
			for (int j = 0; j < N; j++) {
				if (item[j][twin1] == true) {
					counttwin1++;
					if (counttwin1 <= 2) {
						indextwin1[counttwin1 - 1] = j;
					} else {
						counttwin1 = 0;
						break;
					}
				}
			}
			if (counttwin1 == 2) {
				boolean istwin = false;
				for (int twin2 = 0; twin2 < N; twin2++) {
					if (twin2 != twin1) {
						if (item[indextwin1[0]][twin2] == true) {
							if (item[indextwin1[1]][twin2] == true) {
								istwin = true;
								for (int j = 0; j < N; j++) {
									if (j != indextwin1[0] && j != indextwin1[1] && item[j][twin2]) {
										istwin = false;
										break;
									}
								}
							}
						}
					}
					int numchanges = 0;
					if (istwin) { // update the possible values cell based on
									// which type it is
						if (type == 0) {
							for (int k = 0; k < N; k++) {
								if (k != twin1 && k != twin2) {
									if(posvalues[index][indextwin1[0]][k] ==true){
										posvalues[index][indextwin1[0]][k] = false;
										numchanges++;
									}
									if(posvalues[index][indextwin1[1]][k] ==true){
										numchanges++;
										posvalues[index][indextwin1[1]][k] = false;
									}
								}
							}
						}
						if (type == 1) {
							for (int k = 0; k < N; k++) {
								if (k != twin1 && k != twin2) {
									if(posvalues[indextwin1[0]][index][k] ==true){
										posvalues[indextwin1[0]][index][k] = false;
										numchanges++;
									}
									if(posvalues[indextwin1[1]][index][k] ==true){
										posvalues[indextwin1[1]][index][k] = false;
										numchanges++;
									}
								}
							}
						}
						if (type == 2) {
							for (int k = 0; k < N; k++) {
								if (k != twin1 && k != twin2) {
									if (posvalues[(index / rN) * rN + indextwin1[0] / rN][(index * rN) % N + (indextwin1[0] % rN)][k] == true) {
										posvalues[(index / rN) * rN + indextwin1[0] / rN][(index * rN) % N + (indextwin1[0] % rN)][k] = false;
										numchanges++;
									}
									
									if (posvalues[(index / rN) * rN + indextwin1[1] / rN][(index * rN) % N + (indextwin1[1] % rN)][k] == true) {
										posvalues[(index / rN) * rN + indextwin1[1] / rN][(index * rN) % N + (indextwin1[1] % rN)][k] = false;
										numchanges++;
									}
								}
							}
						}
						if (numchanges == 0) return false;
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}




/****************************************************************
*
* Triplets: This humanistic method takes a given sector of the board, 
* either a row, column or square, and looks for a trio of possible 
* values that only occur in a trio of cells and eliminates all but
* the trio as possible values for those cells.
*
*****************************************************************/

	boolean findTriples(int index, int type) { // item can be row column or
												// square array (defined by
												// index and type) from the
												// possible values variable. If
												// finds twin numbers that only
												// appear in two indexes and
												// they are the same two,
												// eliminates all other options
												// in those cells
		int counttriple1 = 0;
		int triple1 = 0;
		int triple2 = 0;
		int triple3 = 0;
		boolean[][] item = new boolean[N][N];
		if (type == 0) { // type 0=row type 1=column type2=square
			item = posvalues[index];
		}
		if (type == 1) {
			for (int j = 0; j < N; j++) {
				item[j] = posvalues[j][index];
			}
		}
		if (type == 2) {
			item = getSquarePosValues(index);
		}
		int[] indextriple1 = new int[3];
		for (triple1 = 0; triple1 < N; triple1++) {
			for (int j = 0; j < N; j++) {
				if (item[j][triple1] == true) {
					counttriple1++;
					if (counttriple1 <= 3) {
						indextriple1[counttriple1 - 1] = j;
					} else {
						counttriple1 = 0;
						break;
					}
				}
			}
			if (counttriple1 == 3) {
				boolean istriple = false;
				for (triple2 = 0; triple2 < N; triple2++) {
					if (triple2 != triple1) {
						if (item[indextriple1[0]][triple2] == true) {
							if (item[indextriple1[1]][triple2] == true) {
								if (item[indextriple1[2]][triple2] == true) {
									istriple = true;
									for (int j = 0; j < N; j++) {
										if (j != indextriple1[0] && j != indextriple1[1] && j != indextriple1[2]
												&& item[j][triple2]) {
											istriple = false;
											break;
										}
										// check for triple3
										else {
											for (triple3 = 0; triple3 < N; triple3++) {
												if (triple3 != triple1 && triple3 != triple2) {
													if (item[indextriple1[0]][triple3] == true) {
														if (item[indextriple1[1]][triple3] == true) {
															if (item[indextriple1[2]][triple3] == true) {
																istriple = true;
																for (int k = 0; k < N; k++) {
																	if (k != indextriple1[0] && k != indextriple1[1]
																			&& k != indextriple1[2]
																			&& item[k][triple3]) {
																		istriple = false;
																		break;
																	}
																}
																if (istriple) { 
																	int numchanges = 0;
																	if (type == 0) {
																		
																		for (int k = 0; k < N; k++) {
																			if (k != triple1 && k != triple2
																					&& k != triple3) {
																				if (posvalues[index][indextriple1[0]][k] == true) {posvalues[index][indextriple1[0]][k] = false; numchanges++;}
																				if (posvalues[index][indextriple1[1]][k] == true) {posvalues[index][indextriple1[1]][k] = false; numchanges++;}
																				if (posvalues[index][indextriple1[2]][k] == true) {posvalues[index][indextriple1[2]][k] = false; numchanges++;}
																			}
																		}
																	}
																	if (type == 1) {
																		for (int k = 0; k < N; k++) {
																			if (k != triple1 && k != triple2
																					&& k != triple3) {
																				if (posvalues[indextriple1[0]][index][k] == true) {posvalues[indextriple1[0]][index][k] = false; numchanges++;}
																				if (posvalues[indextriple1[1]][index][k] == true) {posvalues[indextriple1[1]][index][k] = false; numchanges++;}
																				if (posvalues[indextriple1[2]][index][k] == true) {posvalues[indextriple1[2]][index][k] = false; numchanges++;}
																			}
																		}
																	}
																	if (type == 2) {
																		for (int k = 0; k < N; k++) {
																			if (k != triple1 && k != triple2
																					&& k != triple3) {
																				if (posvalues[(index / rN) * rN + indextriple1[0] / rN][(index * rN) % N + (indextriple1[0] % rN)][k] == true) {
																					posvalues[(index / rN) * rN + indextriple1[0] / rN][(index * rN) % N + (indextriple1[0] % rN)][k] = false;
																					numchanges++;
																				}
																				
																				if (posvalues[(index / rN) * rN + indextriple1[1] / rN][(index * rN) % N + (indextriple1[1] % rN)][k] == true) {
																					posvalues[(index / rN) * rN + indextriple1[1] / rN][(index * rN) % N + (indextriple1[1] % rN)][k] = false;
																					numchanges++;
																				}
																				
																				if (posvalues[(index / rN) * rN + indextriple1[2] / rN][(index * rN) % N + (indextriple1[2] % rN)][k] == true) {
																					posvalues[(index / rN) * rN + indextriple1[2] / rN][(index * rN) % N + (indextriple1[2] % rN)][k] = false;
																					numchanges++;
																				}
																			}
																		}
																	}
																	if (numchanges == 0) return false;
																	return true;
																}
															}
														}
													}
												}
											}
										}
										if (istriple == false)
											break;
									}
								}
							}
						}
					}
				}
				return false;
			}
		}

		return false;
	}





	void setvalue(int row, int col, int x) {
		vals[row][col] = x;
		int rowstart = rN * (row / rN);
		int colstart = rN * (col / rN);
		for (int i=0; i<N; i++) {
			posvalues[row][col][i] = false;
			posvalues[row][i][x-1] = false;
			posvalues[i][col][x-1] = false;
			posvalues[rowstart + i/rN][colstart + i%rN][x-1] = false;
		}
	}

	boolean isValid(int row, int col, int x) {
		int rowstart = rN * (row / rN);
		int colstart = rN * (col / rN);
		for (int i=0; i<N; i++) {
			if (vals[row][i] == x && i != col) return false;
			if (vals[i][col] == x && i != row) return false;
			if (vals[rowstart + i/rN][colstart + i%rN] == x && (rowstart + i/rN) != row && (colstart + i%rN) != col) return false;
		}
		return true;
	}
	
	boolean isSolved() {
		boolean solved = true;
		for (int i=0; i<N; i++){
			for (int j=0; j<N; j++) {
				if (vals[i][j] == 0 || !isValid(i,j,vals[i][j])) solved = false;
			}
		}
		return solved;
	}

	void print() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.print(vals[i][j] + "\t");
			}
			System.out.println("\n");
		}
		System.out.println("\n");
	}

	void printValue(int i, int j) { // Displays value in cell
		System.out.print(vals[i][j]);
		System.out.println("\n");
	}

	void printPosVals(int row, int col) {
		System.out.print("posvalues[" + row + "][" + col + "] = ");
		for (int i = 0; i<N; i++) {
			System.out.print(posvalues[row][col][i] + "\t");
		}
		System.out.print("\n");
	}
	
}