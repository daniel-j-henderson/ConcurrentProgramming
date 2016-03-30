// humanistic approach

public class sudoku {



	boolean force(int row, int col, int[][] copy) {



		if (board[row][col] != 0) return true;
		boolean found = false;
		int nextrow = row;
		if(col == 8) newrow++;
		int i=0;
		while (copy[row][col] <= 9) {
			if (posvalues[row][col][i]) {
				if (isValid(i, row, col, copy)){
					copy[row][col] = i+1; 
					found = force(newrow, (col + 1)%9, copy);
				}
			}
		}
		if (row*col == N*N && found) board = copy;
		return found;
	}




	

	boolean isValid(int x, int row, int col, int[][] puzzle) {
		for (c = 1-9)
		for (int i=0; i<9; i++) {
			if(puzzle[row][i] == x) return false;
		}
	}

	void init() {
		for (int i=0; i<N; i++) {
			for (int j=0; j<N; j++) {
				if (myPuzzle.cell[i][j].value != 0) myPuzzle.cell[i][j].setValue(myPuzzle.cell[i][j].value); //sets possible bits for all relevant cells to zero
			}
		}
	}

	void eliminate(int row, int col) {
		int count = 0;
		int index = -1;
		for (i=0; i<N; i++) {
			if (myPuzzle.cell[row][col].possible[i] != 0) {
				count++;
				index = i;
			}
		}
		if (count == 1) myPuzzle.cell[i][j].setValue(i+1);
	} 

	void loneRanger(int row, int col) {
		boolean foundR, foundC, foundS;
		for (int x = 1; x<10; x++){
			foundR = false;
			if(posvalues[row][col][x-1]) {
				for (int i=0; i<N; i++) {
					if (posvalues[row][i][x-1] && i != col) found = true;
					if (posvalues[i][col][x-1] && i != row) found = true;
				}
				for (int i=0; i<sqrt(N); i++) {
					for (int j=0; j<sqrt(N); j++) {
						if (posvalues[row/3+i][col/3+j][x-1] && ((row/3+i) != row && (col/3+j) != col)) found = true;
					}
				}
				if(!found) {
				setvalue(row,col,x); 
				return true;
				}
			}
		}
	}

	

}