package Sudoku;

public class Sudoku {
	public static int[][] board;
	static boolean[][][] posvalues;

	Sudoku() {
		board = new int[9][9]; // initializing board
		posvalues = new boolean[9][9][9]; // initializing options for each cell
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				for (int ii = 0; ii < 9; ii++) {
					posvalues[i][j][ii] = true; // Setting all options to true
				}
			}
		}
	}

	public void setvalue(int row, int column, int value) { 	// Puts a number in the board and sets all possible values to false showing the cell has been written to while also updating the effected row/column/square
		if (value != 0) {
			board[row][column] = value;
			for (int j = 0; j < 9; j++) {
				posvalues[row][column][j] = false;
				posvalues[j][column][value - 1] = false;
				posvalues[row][j][value - 1] = false;
				int r = (row / 3) * 3;
				int c = (column / 3) * 3;
				for (int i = 0; i < 9; i++) {
					posvalues[(r + (i % 3))][c + (i / 3)][value - 1] = false;
				}

			}
		}
	}

	int getvalue(int row, int column) { // Checks the value in a cell
		return board[row][column];
	}
	
	public int[] Getrow(int row) { // returns the row array
		int[] rows = new int[9];

		for (int i = 0; i < 9; i++) {
			rows[i] = Sudoku.board[row][i];
		}
		return rows;
	}

	public int[] Getcol(int col) { // returns the column array
		int[] cols = new int[9];

		for (int i = 1; i < 10; i++) {
			cols[i - 1] = Sudoku.board[i - 1][col];
		}
		return cols;
	}

	int[] getSquare(int squareval) { // returns the square array squares are defined from 0-8 going from top left to top right then down a square like a book
		int firstcol = (squareval * 3) % 9;
		int firstrow = (squareval / 3) * 3;
		int[] square = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 3; i++) {
				square[3 * j + i] = board[firstrow + j][firstcol + i];
			}
		}
		return square;
	}

	boolean[][] getSquareposvalues(int squareval) {//returns the possible values for the cells in a square
		int firstcol = (squareval * 3) % 9;
		int firstrow = (squareval / 3) * 3;
		boolean[][] square = new boolean[9][9];

		for (int ii = 0; ii < 9; ii++) {
			for (int j = 0; j < 3; j++) {
				for (int i = 0; i < 3; i++) {
					square[3 * j + i][ii] = posvalues[firstrow + j][firstcol + i][ii];
				}
			}
		}
		return square;
	}

	void Printboard() { // Displays the board for the user
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(board[i][j] + "\t");
			}
			System.out.println("\n");
		}
		System.out.println("\n");
	}

	void Printvalue(int i, int j) { // Displays value in cell
		System.out.print(board[i][j]);
		System.out.println("\n");
	}

	void Printposvalues(int row, int col) { //Displays the options for the cell
		for (int k = 0; k < 9; k++) {

			System.out.print(posvalues[row][col][k] + "\t");
		}
		System.out.println("\n");
	}
	
	public boolean Elimination(int row, int col) { // Looks for only one possible option in the cell if found writes to the board and updates possible values
		int j = 0;
		for (int i = 0; i < 9; i++) {
			if (Sudoku.posvalues[row][col][i])
				j++;
		}
		if (j == 1) {
			int ii = 0;
			while (!Sudoku.posvalues[row][col][ii]) {
				ii++;
			}
			setvalue(row, col, ii + 1);
			return true;
		}
		return false;
	}

	boolean loneRanger(int row, int col) { // Looks for a number that only appears once in the possible option array. If so then it writes that value and updates possible values
		boolean found;
		for (int x = 0; x < 9; x++) {
			found = false;
			if (posvalues[row][col][x]) {
				int rowstart = row / 3;
				int colstart = col / 3;
				for (int i = 0; i < 9; i++) {
					if (posvalues[row][i][x] && i != col)
						found = true;
					if (posvalues[i][col][x] && i != row)
						found = true;
					if (posvalues[rowstart + i / 3][colstart + i % 3][x] && (rowstart + i / 3) != row
							&& (colstart + i % 3) != col)
						found = true;
				}
				if (!found) {
					setvalue(row, col, x + 1);
					return true;
				}
			}
		}
		return false;
	}

	boolean FindTwins(int index, int type) { // item can be row column or square array (defined by index and type) from the possible values variable. If finds twin numbers that only appear in two indexes and they are the same two, eliminates all other options in those cells
		int counttwin1 = 0;
		boolean[][] item = new boolean[9][9];
		if (type == 0) { // type 0=row type 1=column type2=square
			item = posvalues[index];
		}
		if (type == 1) {
			for (int j = 0; j < 9; j++) {
				item[j] = posvalues[j][index];
			}
		}
		if (type == 2) {
			item = getSquareposvalues(index);
		}
		int[] indextwin1 = new int[2];
		for (int twin1 = 0; twin1 < 9; twin1++) {
			for (int j = 0; j < 9; j++) {
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
				for (int twin2 = 0; twin2 < 9; twin2++) {
					if (twin2 != twin1) {
						if (item[indextwin1[0]][twin2] == true) {
							if (item[indextwin1[1]][twin2] == true) {
								istwin = true;
								for (int j = 0; j < 9; j++) {
									if (j != indextwin1[0] && j != indextwin1[1] && item[j][twin2]) {
										istwin = false;
										break;
									}
								}
							}
						}
					}
					if (istwin) { // update the possible values cell based on which type it is
						if (type == 0) {
							for (int k = 0; k < 9; k++) {
								if (k != twin1 && k != twin2) {
									posvalues[index][indextwin1[0]][k] = false;
									posvalues[index][indextwin1[1]][k] = false;
								}
							}
						}
						if (type == 1) {
							for (int k = 0; k < 9; k++) {
								if (k != twin1 && k != twin2) {
									posvalues[indextwin1[0]][index][k] = false;
									posvalues[indextwin1[1]][index][k] = false;
								}
							}
						}
						if (type == 2) {
							for (int k = 0; k < 9; k++) {
								if (k != twin1 && k != twin2) {
									posvalues[(index / 3) * 3 + indextwin1[0] / 3][(index * 3) % 9
											+ (indextwin1[0] % 3)][k] = false;
									posvalues[(index / 3) * 3 + indextwin1[1] / 3][(index * 3) % 9
											+ (indextwin1[1] % 3)][k] = false;
								}
							}
						}
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		Sudoku a = new Sudoku();
		
		int[][] puzzle1 = { { 0, 2, 9, 0, 0, 0, 3, 1, 0 }, 
							{ 0, 8, 0, 0, 0, 0, 4, 0, 9 }, 
							{ 3, 0, 6, 0, 2, 0, 0, 0, 0 },
							{ 0, 6, 0, 0, 3, 1, 7, 0, 0 }, 
							{ 0, 0, 0, 0, 5, 8, 0, 0, 0 }, 
							{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
							{ 0, 3, 7, 0, 0, 6, 0, 8, 0 }, 
							{ 0, 0, 1, 0, 0, 0, 5, 0, 0 }, 
							{ 0, 9, 0, 0, 8, 0, 0, 3, 7 } };
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				a.setvalue(i, j, puzzle1[i][j]);
			}
		}
		
		a.Printboard();

		/*
		 * boolean changedElim = true; boolean changedRanger=true;
		 * while(changedElim||changedRanger) { changedElim=false;
		 * changedRanger=false; for(int i=0;i<9;i++){ for(int j=0;j<9;j++){
		 * if(a.Elimination(i, j)) changedElim = true; if(a.loneRanger(i, j))
		 * changedRanger=true; } } }
		 */
	}
}
