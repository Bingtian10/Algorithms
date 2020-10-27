import java.util.*;
import java.io.*;
public class Problem2 {
	public static void solve(int testCase, long[][] matrix, int n) {
		long ans = 0;
		for(int i = 0; i < n; i++) {
			int x = i;
			int y = 0;
			long temp = 0;
			while(x < n && y < n) {
				temp += matrix[x][y];
				x++;
				y++;
			}

			if(temp > ans)
				ans = temp;
		}

		for(int i = 0; i < n; i++) {
			int x = 0;
			int y = i;
			long temp = 0;
			while(x < n && y < n) {
				temp += matrix[x][y];
				x++;
				y++;
			}

			if(temp > ans)
				ans = temp;
		}

		System.out.println("Case #" + testCase +": " + ans);
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int t = in.nextInt();
		for(int i = 0; i < t; i++) {
			int n = in.nextInt();
			long[][] matrix = new long[n][n];
			for(int x = 0; x < n; x++) {
				for(int y = 0; y < n; y++) {
					matrix[x][y] = in.nextInt(); 
				}
			}
			solve(i+1, matrix, n);
		}

		in.close();
	}
}