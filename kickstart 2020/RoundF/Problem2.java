import java.util.*;
import java.io.*;
public class Problem2 {
	static class customComparator implements Comparator<int[]> {
		public int compare(int[] a, int[] b) {
			if(a[1] < b[1])
				return -1;
			else
				return 1;
		}
	}

	public static void solve(int n, int k, int caseNum, int[][] intervals) {
		Arrays.sort(intervals, new customComparator());
		int ans = 0;
		int end = 0;
		for(int i = 0; i < n; i++) {
			//New interval
			if(end < intervals[i][0]) {
				if(k >= intervals[i][1] - intervals[i][0]) {
					end = intervals[i][0] + k - 1;
					ans++;
				}

				else {
					int diff = intervals[i][1] - intervals[i][0];
					int temp = diff / k;
					if(diff % k != 0)
						temp++;
					end = intervals[i][0] + temp*k - 1;
					ans += temp;
				}
			}

			else if(end >= intervals[i][0] && end < intervals[i][1] - 1) {
				int diff = intervals[i][1] - end - 1;
				int temp = diff / k;
				if(diff % k != 0)
					temp++;
				end = end + temp * k;
				ans += temp;
			}

			//end covers the current interval, do nothing
		}
		StringBuilder sb = new StringBuilder("Case #"+caseNum+": " + ans);
		System.out.println(sb.toString());
	}
	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
		int t = in.nextInt();
		for(int i = 0; i < t; i++) {
			int n = in.nextInt();
			int k = in.nextInt();
			int[][] intervals = new int[n][2];
			for(int j = 0; j < n; j++) {
				intervals[j][0] = in.nextInt();
				intervals[j][1] = in.nextInt();
			}

			solve(n, k, i+1, intervals);
		}

		in.close();
	}
}