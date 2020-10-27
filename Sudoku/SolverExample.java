import java.util.*;
public class SolverExample {
	public static void main(String[] args) {
		SudokuSolver sudo_solve = new SudokuSolver();
		Scanner in = new Scanner(System.in);
		int n = 9;
		char[][] board = new char[n][n];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				board[i][j] = in.next().charAt(0);
			}
		}

		boolean solved = sudo_solve.solve(board);
		if(solved) {
			for(int i = 0; i < n; i++) {
				for(int j = 0; j < n; j++) {
					System.out.print(board[i][j] + " ");
				}

				System.out.println();
			}
		}

		in.close();
	}
}