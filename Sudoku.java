package Sudoku;

public class Sudoku {
	static int[][]board;
	
	 Sudoku(){
		 board= new int [9][9];
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				board[i][j]=0;
			}
		}
	}
	 
	void setvalue(int row, int column,int i){
		board[row-1][column-1]=i;
	 }
	void Print(){
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				System.out.print(board[i][j]+ "\t");
			}
			System.out.println("\n");
		}
	}
	 
	 public static void main(String[] args){
		 	Sudoku a=new Sudoku();

			a.setvalue(1,2,2);
			a.setvalue(1,3,9);
			a.setvalue(1,7,3);
			a.setvalue(1,8,1);
			
			a.setvalue(2,2,8);
			a.setvalue(2,7,4);
			a.setvalue(2,9,9);
			
			
			a.setvalue(3,1,3);
			a.setvalue(3,3,6);
			a.setvalue(3,5,2);
			
			a.setvalue(4,2,6);
			a.setvalue(4,5,3);
			a.setvalue(4,6,1);
			a.setvalue(4,7,7);
			
			a.setvalue(5,5,5);
			a.setvalue(5,6,8);
			
			//row 6
			
			a.setvalue(7,2,3);
			a.setvalue(7,3,7);
			a.setvalue(7,6,6);
			a.setvalue(7,8,8);
			
			a.setvalue(8,3,1);
			a.setvalue(8,7,5);
			
			a.setvalue(9,2,9);
			a.setvalue(9,5,8);
			a.setvalue(9,8,3);
			a.setvalue(9,9,7);
			
			a.Print();
	}
}
