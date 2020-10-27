public class MorrisTraversal {
	public TreeNode insert(TreeNode root, int val) {
		if(root == null)
			return new TreeNode(val);

		if(val < root.val) {
			root.left = insert(root.left, val);
		}

		else
			root.right = insert(root.right, val);
		return root;
	}

	public void inorder(TreeNode root) {
		TreeNode cur = root;
		TreeNode pre;
		while(cur != null) {
			//No left subtree, safely traverse right subtree
			if(cur.left == null) {
				System.out.println(cur.val);
				cur = cur.right;
			}

			//Has a left subtree, need to find predecesor of current node
			else {
				pre = cur.left;
				while(pre.right != null && pre.right != cur)
					pre = pre.right;
				//If the threading exists, we already traverse the left subtree
				if(pre.right != null) {
					pre.right = null;
					System.out.println(cur.val);
					cur = cur.right;
				}

				else {
					pre.right = cur;
					cur = cur.left;
				}
			}
		}
	}

	public static void main(String[] args) {
		MorrisTraversal m = new MorrisTraversal();
		//To make the inorder look pretty, we construct a binary search tree
		TreeNode root = null;
		root = m.insert(root, 4);
		root = m.insert(root, 2);
		root = m.insert(root, 6);
		root = m.insert(root, 1);
		root = m.insert(root, 3);
		root = m.insert(root, 5);
		root = m.insert(root, 7);

		m.inorder(root);
	}
}

class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;
	TreeNode() {}
	TreeNode(int val) {
		this.val = val;
	}
}