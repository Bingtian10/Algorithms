public class SudokuSolver {
	//The solution board is return in place.
	public boolean solve(char[][] board) {
		if(board == null || board.length == 0 || board[0].length == 0)
			return false;
		return backtrack(board);
	}

	/**
	Check if placing a number 'c' at board[i][j] would causing violation on the 
	current board state. The board is given partially filled with '.' denoted
	empty cell.
	*/
	private boolean isValid(char[][] board, int row, int col, char c) {
		for(int i = 0; i < 9; i++) {
			//If this column already has number c, bad board state.
			if(board[i][col] == c)
				return false;

			if(board[row][i] == c) 
				return false;

			//checking the 3x3 sub-board (i,j) is located at
			int y = 3 * (row/3) + i/3;
			int x = 3 * (col/3) + i%3;
			if(board[y][x] == c) 
				return false;
		}

		//No violation till the end, valid board state.
		return true;
	}

	private boolean backtrack(char[][] board) {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(board[i][j] == '.') {
					//Trying out all number 1-9 in the current cell.
					for(char c = '1'; c <= '9'; c++) {
						//Check if inserting c at current board it (i,j) is valid
						if(isValid(board, i, j, c)) {
							board[i][j] = c;

							//Recursively solve the rest of the board
							if(backtrack(board)) {
								return true;
							}

							//Placing number c is not solvable, backtrack to other solutions
							//Or backtrack even further to previous recursive calls.
							else {
								board[i][j] = '.';
							}
						}
					}

					//Exhaust all 1-9 and has no solvable board, backtrack to previous recursive call.
					return false;
				}
			}
		}

		//This board is solved.
		return true;
	}
}