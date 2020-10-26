import java.util.*;
public class Autocomplete {
	public ArrayList<String> suggestSuffix(Trie trie, String prefix) {
		Node root = trie.root;
		ArrayList<String> res = new ArrayList<String>();
		StringBuilder sb = new StringBuilder(prefix);
		Node prefixRoot = this.findNode(root, prefix);

		//If Trie does not have prefix, there's no autocomplete suggestions.
		if(prefixRoot == null)
			return res;
		this.suggestHelper(prefixRoot, res, sb);
		return res;
	}

	/**
	* Find the node at which the prefix resides in the Trie.
	* Return null if the Trie does not contains it.
	*/
	private Node findNode(Node root, String prefix) {
		Node cur = root;
		for(int i = 0; i < prefix.length(); i++) {
			char c = prefix.charAt(i);
			if(cur.alphabet[c - 'a'] == null)
				return null;
			cur = cur.alphabet[c - 'a'];
		}

		if(cur.isEnd)
			return cur;
		else
			return null;
	}

	/**
	* Dfs/backtrack to traverse the subtree of the prefix word, add all
	* word below the subtree as possible word completetion.
	*/
	private void suggestHelper(Node root, ArrayList<String> res, StringBuilder sb) {
		if(root.isEnd)
			res.add(sb.toString());

		for(int i = 0; i < root.alphabet.length; i++) {
			if(root.alphabet[i] != null) {
				int c = i + 'a';
				sb.append((char)c);
				suggestHelper(root.alphabet[i], res, sb);
				sb.deleteCharAt(sb.length()-1);
			}
		}
	}
}

class Trie {
	protected Node root;
	public Trie() {
		root = new Node();
	}

	/* 
	* This method implements insertion into a Trie. We first try to traverse the Trie
	* using the string to be inserted, if we encounters null pointer while traversing, 
	* we need to insert suffix at the current word. If we can traverse the Trie fully,
	* it means the Trie already contains the word as a prefix, we just need to mark
	* it as valid word.
	*/ 
	public void insert(String word) {
		Node cur = root;
		int len = word.length();
		for(int i = 0; i < len; i++) {
			char c = word.charAt(i);
			if(cur.alphabet[c - 'a'] != null) {
				cur = cur.alphabet[c - 'a'];
			}

			else {
				cur.alphabet[c - 'a'] = new Node();
				cur = cur.alphabet[c - 'a'];
			}
		}

		cur.isEnd = true;
	}

	/**
	* Traverse the Trie using word to be search. If encounters null pointer,
	* the word does not exist. When we finish traversing the tree, we need
	* to make sure the word is an word in the Trie instead of a prefix.
	*/
	public boolean search(String word) {
		Node cur = root;
		int len = word.length();
		for(int i = 0; i < len; i++) {
			char c = word.charAt(i);
			if(cur.alphabet[c - 'a'] == null)
				return false;
			else
				cur = cur.alphabet[c - 'a'];
		}

		if(cur.isEnd)
			cur.freq++;

		return cur.isEnd;
	}
}

class Node {
	Node[] alphabet;
	boolean isEnd;
	int freq;
	public Node() {
		alphabet = new Node[26];
		isEnd = false;
		freq = 0;
	}
}