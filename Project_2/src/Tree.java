import java.io.PrintStream;

/**
 * 
 * @author Thomas Ortiz
 * @author Michael Mackrell
 * @author Jacob Stone
 * @author Curtis Moore
 */
public class Tree {

	private Node overallRoot; // null for an empty tree

	/**
	 * Base constructor for Tree.
	 */
	public Tree() {
		overallRoot = null;
	}

	/**
	 * Inserts a call with a given priority
	 */
	public void insert(Ticket ticketToAdd) {
		// Any attempt to insert a ticket with the same priority as one already
		// in the queue should generate a warning.
		// Add based off of a ticket's priority
		overallRoot = add(overallRoot, ticketToAdd);
	}

	/**
	 * add method that puts a node in the tree
	 * @param root the root of the node we are adding
	 * @param ticketToAdd the ticket object added to the list
	 * @return the new root of the added node
	 */
	public Node add(Node root, Ticket ticketToAdd) {
		if (root == null) {
			root = new Node(ticketToAdd);
		} else if (ticketToAdd.getPriority() < root.data.getPriority()) {
			root.left = add(root.left, ticketToAdd);
		} else {
			root.right = add(root.right, ticketToAdd);
		}
		(root.descendants)++;
		return root;
	}


	/**
	 * method to remove a node given a priority p
	 * @param p the priority of the node we are removing
	 */
	public void remove(int p) {
		int temp = overallRoot.data.getPriority();
		overallRoot = remove(overallRoot, p);
		if (overallRoot != null) {
			if (overallRoot.data.getPriority() != temp);
				(overallRoot.descendants)++; //overallRoot.data.changeDescendants(1);
		}
	}
	
	/**
	 * other remove method
	 * @param root the root of the tree
	 * @param p the priority of the node to remove
	 * @return root the root of the tree
	 */
	private Node remove (Node root, int p) {
		if (root == null) {
			//empty tree or value not found, so there is nothing to do.
		} else if (root.data.getPriority() < p) {
			//value could be in the right subtree, lets go in.
			root.right = remove(root.right, p);
		} else if (root.data.getPriority() > p) {
			// value could be in the left subtree, lets go in.
			root.left = remove(root.left, p);
		} else {
			// root.data.getPriority() == p, this is the node to remove.
			if (root.left == null && root.right == null) {
				//Case 1: leaf, replace with null
				root = null;
				return root;
			} else if (root.right == null ) {
				//Case 2: left child only; replace with a left child
				root = root.left;
				return root;
			} else if (root.left == null) {
				//Case 3: right child only; replace with right child
				root = root.right;
				return root;
			} else {
				//Case 4: both children, replace with next in-order from right subtree
				Node followingNode = removeNextInOrder(root.right);
				set(root, followingNode);
				root.right = remove(root.right, followingNode.data.getPriority());
				return root;
			}
		}
		
		if (root != null) {
			(root.descendants)--; //data.changeDescendants(-1);
		}
		return root;
	}
	
	/**
	 * method to copy data from one node to another
	 * @param dest the node that data is copied to
	 * @param source the node that data is copied from
	 */
	private void set(Node dest, Node source) {
		dest.data.setPriority(source.data.getPriority());
		dest.data.setId(source.data.getId());
	}
	
	/**
	 * removes the next node in the tree order
	 * @param root the root of the tree
	 * @return the root of the tree
	 */
	private Node removeNextInOrder(Node root) { // L H R
		if (root.left != null)
			return removeNextInOrder(root.left);
		else
			return root;
	}

	/**
	 * Identify and remove highest priority call.
	 */
	public Ticket getHighest() {
		return getHighest(overallRoot);
	}
	
	/**
	 * returns the highest priority node in the tree
	 * @param root the root of the tree
	 * @return the data of the highest priority node
	 */
	private Ticket getHighest(Node root) {
		if (root.right != null)
			return getHighest(root.right);
		
		return root.data;
	}

	/**
	 * Query about position in the queue (using help ticket id)
	 * 
	 * @param priority
	 * @return
	 * */
	public int query( int p ) {
		return  query(overallRoot, p);
	}
	
	/**
	 * method to find the priority of a given node
	 * @param root the root of the tree
	 * @param p the priority of the node 
	 * @return the key of the node we find
	 */
	private int query( Node root, int p ) {
		if (root == null)
			return 0;
		if ( root.data.getPriority() == p)
			return 1 + getNodeDescendants(root.right);
		if ( root.data.getPriority() > p)
		    return query(root.left, p) + 1 + getNodeDescendants(root.right);
		else // ( root.data.getPriority() < p)
		    return query(root.right, p);
	}
	
	/**
	 * method to get the descendants of a node
	 * @param root the root of the tree
	 * @return the descendants or 0 if there are none
	 */
	private int getNodeDescendants(Node root) {
		if (root != null)
			return root.descendants;
		return 0;
	}
	
	/**
	 * Checks if the tree contains a node.
	 * @param p the priority of the node to find
	 * @return true if tree does contain, false if not.
	 */
	public boolean contains( int p) {
		return recContains(overallRoot, p);
	}
	
	/**
	 * Recursivly finds the requested node in the tree
	 * @param current node used for going through the tree
	 * @param p the priority of the node we are looking for
	 * @return true or false depending on if the node is found
	 */
	private boolean recContains(Node current, int p) {
		if(current.data.getPriority() == p) {
			return true;
		}
		if(current.data.getPriority() > p && current.left == null) {
			return false;
		}
		if(current.data.getPriority() < p && current.right == null) {
			return false;
		}
		
		if(current.data.getPriority() > p && current.left != null) {
			return recContains(current.left, p);
		}
		if(current.data.getPriority() < p && current.right != null) {
			return recContains(current.right, p);
		}	
		return false; //you dun goofed
		
	}
	
	/**
	 * Returns whether or not a tree is empty
	 * @return true or false depending on the condition of the tree
	 */
	boolean isEmpty(){
		return (overallRoot == null);
	}

	/**
	 * Returns the current root of the tree.
	 * 
	 * @return root of tree.
	 */
	public Node getRoot() {
		return this.overallRoot;
	}

	/**
	 * Sets method for root field.
	 * 
	 * @param root
	 *            to set this.root to.
	 */
	public void setRoot(Node root) {
		this.overallRoot = root;
	}
	
	/**
	 * prints the tree in order
	 */
	public void printInorder() {
		printInorder(overallRoot);
	}
	/**
	 * prints the tree in order given a specific root
	 * @param root the root of the tree to print
	 */
	public void printInorder(Node root){
		if( root != null){
			printInorder(root.left);
			System.out.print(" " + root.data.getPriority() + "[" + root.descendants + "]");
			printInorder(root.right);
		}
	}

	/**
	 * Node Class for Tree.
	 * 
	 * @author Thomas Ortiz
	 * @author Michael Mackrell
	 * @author Jacob Stone
	 * @author Curtis Moore
	 */
	public class Node {
		public Ticket data; // data stored at this node
		public Node left; // reference to left subtree
		public Node right; // reference to right subtree
		public int descendants; // amount of descendants

		/**
		 * Constructs a leaf node with the given data.
		 * 
		 * @param data ticket
		 */
		public Node(Ticket data) {
			this(data, null, null);
		}

		/**
		 * Constructs a branch node with the given data and links.
		 * 
		 * @param data ticket
		 * @param left node
		 * @param right node
		 */
		public Node(Ticket data, Node left, Node right) {
			this.data = data;
			this.left = left;
			this.right = right;
			this.descendants = 0;
		}
	}
}
