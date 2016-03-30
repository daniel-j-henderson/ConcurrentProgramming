package Sudoku;


public class Sudoku {
	public static int[][] board;
	static boolean[][][] posvalues;

	Sudoku() {
		board = new int[9][9];
		posvalues = new boolean[9][9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				//board[i][j] = 0;
				for (int ii = 0; ii < 9; ii++) {
					posvalues[i][j][ii] = true;
				}
			}
		}
	}

	public int[] Getrow(int row) {
		int[] rows = new int[9];

		for (int i = 0; i < 9; i++) {
			rows[i] = Sudoku.board[row][i];
		}
		return rows;
	}

	public int[] Getcol(int col) {
		int[] cols = new int[9];

		for (int i = 1; i < 10; i++) {
			cols[i - 1] = Sudoku.board[i - 1][col];
		}
		return cols;
	}

	
	
	
	
	 public boolean Elimination(int row,int col) {
			
			 int j=0;
			 for(int i=0;i<9;i++){
				 if(Sudoku.posvalues[row][col][i])
					 j++;
			 }
			 if(j==1){
				 int ii=0;
				 while(!Sudoku.posvalues[row][col][ii]){
					 ii++;
				 }
				 setvalue(row,col,ii+1);
				 return true;
			 }
			 return false;
		 }
	
	
	
     boolean loneRanger(int row, int col) {
         boolean found;
         for (int x = 1; x<10; x++){
                 found = false;
                 if(posvalues[row][col][x-1]) {
                         for (int i=0; i<9; i++) {
                                 if (posvalues[row][i][x-1] && i != col) found = true;
                                 if (posvalues[i][col][x-1] && i != row) found = true;
                         }
                         for (int i=0; i<3; i++) {
                                 for (int j=0; j<3; j++) {
                                         if (posvalues[row/3+i][col/3+j][x-1] && ((row/3+i) != row && (col/3+j) != col)) found = true;
                                 }
                         }
                         if(!found) {
                         setvalue(row,col,x);
                         return true;
                         }
                 }
         }
         return false;
 }

	
	
	
	
	public void SetPosValues(int rowindex, int colindex) {
		// need to copy in values from square thread, row thread and col thread
		// run from row 1 column 1 to row 1 column 9 all the way until row 9
		// column 9
		// if value in square go to next value
		// if value in same row/column go to next value
		// if none of above store value in an array to be returned
		// if return 1 value then place it in cell
		// if returns more than 1 value keep 0 in cell.
		int[] rowchecker = new int[9];
		rowchecker = Getrow(rowindex);
		int[] colchecker = new int[9];
		colchecker = Getcol(colindex);
		int[] retArray = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		if (rowindex < 4) {
			if (colindex < 4) {
				int[] squarechecker = { Sudoku.board[1][1], Sudoku.board[1][2], Sudoku.board[1][3], Sudoku.board[2][1],
						Sudoku.board[2][2], Sudoku.board[2][3], Sudoku.board[3][1], Sudoku.board[3][2],
						Sudoku.board[3][3] };
				for (int j = 1; j < 10; j++) {
					for (int i = 1; i < 10; i++) {// checking value 1-9
						if (Sudoku.board[rowindex][i] == j || Sudoku.board[i][colindex] == j || squarechecker[i] == j) {
							Sudoku.posvalues[rowindex][colindex][j] = false;
						}
					}
				}
			} else if (colindex < 7) {
				// second square

				int[] squarechecker = { Sudoku.board[1][4], Sudoku.board[1][5], Sudoku.board[1][6], Sudoku.board[2][4],
						Sudoku.board[2][5], Sudoku.board[2][6], Sudoku.board[3][4], Sudoku.board[3][5],
						Sudoku.board[3][6] };
				for (int j = 1; j < 10; j++) {
					for (int i = 1; i < 10; i++) {// checking value 1-9
						if (Sudoku.board[rowindex][i] == j || Sudoku.board[i][colindex] == j || squarechecker[i] == j) {
							Sudoku.posvalues[rowindex][colindex][j] = false;
						}
					}
				}
			}

			else if (colindex < 10) {
				// third square

				int[] squarechecker = { Sudoku.board[1][7], Sudoku.board[1][8], Sudoku.board[1][9], Sudoku.board[2][7],
						Sudoku.board[2][8], Sudoku.board[2][9], Sudoku.board[3][7], Sudoku.board[3][8],
						Sudoku.board[3][9] };
				for (int j = 1; j < 10; j++) {
					for (int i = 1; i < 10; i++) {// checking value 1-9
						if (Sudoku.board[rowindex][i] == j || Sudoku.board[i][colindex] == j || squarechecker[i] == j) {
							Sudoku.posvalues[rowindex][colindex][j] = false;
						}
					}
				}
			}
		} else if (rowindex < 7) {
			if (colindex < 4) {
				// fourth square

				int[] squarechecker = { Sudoku.board[4][1], Sudoku.board[4][2], Sudoku.board[4][3], Sudoku.board[5][1],
						Sudoku.board[5][2], Sudoku.board[5][3], Sudoku.board[6][1], Sudoku.board[6][2],
						Sudoku.board[6][3] };
				for (int j = 1; j < 10; j++) {
					for (int i = 1; i < 10; i++) {// checking value 1-9
						if (Sudoku.board[rowindex][i] == j || Sudoku.board[i][colindex] == j || squarechecker[i] == j) {
							Sudoku.posvalues[rowindex][colindex][j] = false;
						}
					}
				}
			}

			else if (colindex < 7) {
				// fifth square

				int[] squarechecker = { Sudoku.board[4][4], Sudoku.board[4][5], Sudoku.board[4][6], Sudoku.board[5][4],
						Sudoku.board[5][5], Sudoku.board[5][6], Sudoku.board[6][4], Sudoku.board[6][5],
						Sudoku.board[6][6] };
				for (int j = 1; j < 10; j++) {
					for (int i = 1; i < 10; i++) {// checking value 1-9
						if (Sudoku.board[rowindex][i] == j || Sudoku.board[i][colindex] == j || squarechecker[i] == j) {
							Sudoku.posvalues[rowindex][colindex][j] = false;
						}
					}
				}
			}

			else if (colindex < 10) {
				// sixth square

				int[] squarechecker = { Sudoku.board[4][7], Sudoku.board[4][8], Sudoku.board[4][9], Sudoku.board[5][7],
						Sudoku.board[5][8], Sudoku.board[5][9], Sudoku.board[6][7], Sudoku.board[6][8],
						Sudoku.board[6][9] };
				for (int j = 1; j < 10; j++) {
					for (int i = 1; i < 10; i++) {// checking value 1-9
						if (Sudoku.board[rowindex][i] == j || Sudoku.board[i][colindex] == j || squarechecker[i] == j) {
							Sudoku.posvalues[rowindex][colindex][j] = false;
						}
					}
				}
			}
		} else if (rowindex < 10) {
			if (colindex < 4) {
				// seventh square

				int[] squarechecker = { Sudoku.board[7][1], Sudoku.board[7][2], Sudoku.board[7][3], Sudoku.board[8][1],
						Sudoku.board[8][2], Sudoku.board[8][3], Sudoku.board[9][1], Sudoku.board[9][2],
						Sudoku.board[9][3] };
				for (int j = 1; j < 10; j++) {
					for (int i = 1; i < 10; i++) {// checking value 1-9
						if (Sudoku.board[rowindex][i] == j || Sudoku.board[i][colindex] == j || squarechecker[i] == j) {
							Sudoku.posvalues[rowindex][colindex][j] = false;
						}
					}
				}
			}

			else if (colindex < 7) {
				// eighth square

				int[] squarechecker = { Sudoku.board[7][4], Sudoku.board[7][5], Sudoku.board[7][6], Sudoku.board[8][4],
						Sudoku.board[8][5], Sudoku.board[8][6], Sudoku.board[9][4], Sudoku.board[9][5],
						Sudoku.board[9][6] };
				for (int j = 1; j < 10; j++) {
					for (int i = 1; i < 10; i++) {// checking value 1-9
						if (Sudoku.board[rowindex][i] == j || Sudoku.board[i][colindex] == j || squarechecker[i] == j) {
							Sudoku.posvalues[rowindex][colindex][j] = false;
						}
					}
				}
			}

			else if (colindex < 10) {
				// ninth square

				int[] squarechecker = { Sudoku.board[7][7], Sudoku.board[7][8], Sudoku.board[7][9], Sudoku.board[8][7],
						Sudoku.board[8][8], Sudoku.board[8][9], Sudoku.board[9][7], Sudoku.board[9][8],
						Sudoku.board[9][9] };
				for (int j = 1; j < 10; j++) {
					for (int i = 1; i < 10; i++) {// checking value 1-9
						if (Sudoku.board[rowindex][i] == j || Sudoku.board[i][colindex] == j || squarechecker[i] == j) {
							Sudoku.posvalues[rowindex][colindex][j] = false;
						}
					}
				}
			}
		}
	}

	public void setvalue(int row, int column, int value) {
		if(value!=0){
			board[row][column] = value;
			for (int j = 0; j < 9; j++) {
				posvalues[row][column][j] = false;
				posvalues[j][column][value-1]=false;
				posvalues[row][j][value-1]=false;
				int r = (row / 3) * 3;
				int c = (column / 3) * 3;
				for (int i = 0; i < 9; i++){
					posvalues[(r + (i % 3))] [c + (i / 3)][value-1]=false; 
				}
			
			}
		}
	}

	int getvalue(int row, int column) {
		return board[row][column];
	}

	void Printboard() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(board[i][j] + "\t");
			}
			System.out.println("\n");
		}
		System.out.println("\n");
	}

	void Printvalue(int i, int j) {
		System.out.print(board[i][j]);
		System.out.println("\n");
	}

	void Printposvalues(int row, int col) {
		for (int k = 0; k < 9; k++) {

			System.out.print(posvalues[row][col][k] + "\t");
		}
		System.out.println("\n");
	}

	public static void main(String[] args) {
		Sudoku a = new Sudoku();
		int[][] puzzle1 = {{0,2,9,0,0,0,3,1,0},{0,8,0,0,0,0,4,0,9},{3,0,6,0,2,0,0,0,0},{0,6,0,0,3,1,7,0,0},{0,0,0,0,5,8,0,0,0},{0,0,0,0,0,0,0,0,0},{0,3,7,0,0,6,0,8,0},{0,0,1,0,0,0,5,0,0},{0,9,0,0,8,0,0,3,7}};
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				a.setvalue(i, j, puzzle1[i][j]);
			}
		}
		/*int[][] puzzle2 = {{0,2,0,0,9,0,5,0,7},
						   {8,0,9,0,0,0,0,0,0},
						   {0,7,0,4,0,0,0,2,9},
						   {0,0,4,0,0,0,0,0,6},
						   {3,0,0,0,0,4,0,0,5},
						   {0,0,0,0,5,9,4,1,0},
						   {2,0,0,0,0,7,0,0,0},
						   {0,0,7,0,0,1,0,3,2},
						   {5,0,1,3,2,0,0,7,0}};
	
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				a.setvalue(i, j, puzzle2[i][j]);
			}
		}
	*/	
		a.Printboard();
		/*boolean changedElim = true;
		while(changedElim) {
			changedElim=false;
			for(int i=0;i<9;i++){
				for(int j=0;j<9;j++){
					if(a.Elimination(i, j)) changedElim = true;
				}
			}
		}
*/		boolean changedRanger=true;
		while(changedRanger){
			changedRanger=false;
			for(int i=0;i<9;i++){
				for(int j=0;j<9;j++){
					if(a.loneRanger(i, j)) changedRanger=true;
				}
			}
		}
		
		a.Printboard();
	}
}
