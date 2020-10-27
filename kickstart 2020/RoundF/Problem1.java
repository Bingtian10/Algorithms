import java.util.*;
import java.io.*;
public class Problem1 {
	static class customComparator implements Comparator<int[]> {
		public int compare(int[] a, int[] b) {
			if(a[0] < b[0])
				return -1;
			else if(a[0] > b[0])
				return 1;
			else {
				if(a[1] < b[1])
					return -1;
				else
					return 1;
			}
		}
	}

	public static void solve(int n, int caseNum, int[] wants) {
		Map<Integer, List<Integer>> map = new HashMap<>();

		StringBuilder sb = new StringBuilder("Case #"+caseNum+":");
		int maxkey = -1;
		for(int i = 0; i < n; i++) {
			maxkey = Math.max(maxkey, wants[i]);
			if(!map.containsKey(wants[i])) {
				List<Integer> list = new ArrayList<>();
				list.add(i+1);
				map.put(wants[i], list);
			}

			else {
				map.get(wants[i]).add(i+1);
			}
		}

		for(int i = 0; i <= maxkey; i++) {
			if(map.containsKey(i)) {
				for(int num : map.get(i))
					sb.append(" " + num);
			}
		}
		System.out.println(sb.toString());
	}
	public static void main(String[] args) {
		Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
		int t = in.nextInt();
		for(int i = 0; i < t; i++) {
			int n = in.nextInt();
			int limit = in.nextInt();
			int[] wants = new int[n];
			for(int j = 0; j < n; j++) {
				wants[j] = in.nextInt();
				if(wants[j] % limit == 0)
					wants[j]= wants[j]/limit - 1;
				else
					wants[j] = wants[j]/limit;
			}

			solve(n, i+1, wants);
		}

		in.close();
	}
}