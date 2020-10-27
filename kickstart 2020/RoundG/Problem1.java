import java.util.*;
import java.io.*;
public class Problem1 {
	public static void solve(int testCase, String s) {
		int kick = 0;
		int ans = 0;
		for(int i = 0; i < s.length(); i++) {
			if(i+4 <= s.length() && s.substring(i, i+4).equals("KICK"))
				kick++;
			if(i+5 <= s.length() && s.substring(i, i+5).equals("START")) 
				ans += kick;
		}

		System.out.println("Case #" + testCase +": " + ans);
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int t = in.nextInt();
		for(int i = 0; i < t; i++) {
			String s = in.next();
			solve(i+1, s);
		}

		in.close();
	}
}