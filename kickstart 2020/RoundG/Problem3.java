import java.util.*;
import java.io.*;

/**
* Brute force, only pass the first test set.
*/
public class Problem3 {
	public static void solve(int testCase, int w, int range, int[] wheels) {
		int ans = Integer.MAX_VALUE;
		for(int i = 1; i <= range; i++) {
			int target = i;
			int temp = 0;
			for(int j = 0 ; j < w; j++) {
				if(wheels[j] < target) {
					int x = wheels[j] + range;
					temp += Math.min(target-wheels[j], x - target);
				}

				else if(wheels[j] > target) {
					int x = target + range;
					temp += Math.min(wheels[j] - target, x - wheels[j]);
				}
			}

			ans = Math.min(ans, temp);
		}
		System.out.println("Case #" + testCase +": " + ans);
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int t = in.nextInt();
		for(int i = 0; i < t; i++) {
			int w = in.nextInt();
			int n = in.nextInt();
			int[] wheels = new int[w];
			for(int j = 0; j < w; j++)
				wheels[j] = in.nextInt();
			solve(i+1, w, n, wheels);
		}

		in.close();
	}
}