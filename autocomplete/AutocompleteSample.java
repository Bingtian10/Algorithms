import java.util.*;
public class AutocompleteSample {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Trie trie = new Trie();
		while(in.hasNext()) {
			String word = in.next();
			trie.insert(word);
		}

		Autocomplete ac = new Autocomplete();
		ArrayList<String> res = ac.suggestSuffix(trie, "hel");
		for(String s : res)
			System.out.println(s);
		in.close();
	}
}