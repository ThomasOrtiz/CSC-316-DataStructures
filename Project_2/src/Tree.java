/**
 * Tree represents an unbalanced binary 
 * search tree for help ticket priorities.
 * 
 * @author Thomas Ortiz
 * @author Michael Mackrell
 * @author Jacob Stone
 * @author Curtis Moore
 */
public class Tree extends AbstractTree {
	
	protected Ticket highestTicket;
	
	/**
	 * Base constructor for Tree.
	 */
	public Tree() {
		super();
		this.highestTicket = new Ticket(0, 0);
	}

	/**
	 * Inserts a call with a given priority.
	 * @param ticketToAdd to the tree.
	 */
	public void insert(Ticket ticketToAdd) {
		
		
		
		if(this.overallRoot == null){
			this.overallRoot = new Node(ticketToAdd);
			this.overallRoot.descendants = 1;
			return; 
		}
		overallRoot = add(overallRoot, ticketToAdd);
	}

	/**
	 * Add method that puts a node in the tree
	 * @param root the root of the node we are adding
	 * @param ticketToAdd the ticket object added to the list
	 * @return the new root of the added node
	 * @author Building Java Programs Third Edition - Marty Stepp & Stuart Reges
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
	 * Method to remove a node given a priority p
	 * @param p the priority of the node we are removing
	 * @author Marty Stepp courses.cs.washington.edu
	 * @author Building Java Programs Third Edition - Marty Stepp & Stuart Reges
	 */
	public void remove(int p) {
		int temp = overallRoot.data.getPriority();
		overallRoot = remove(overallRoot, p);
	}
	
	/**
	 * Recursive remove method
	 * @param root the root of the tree
	 * @param p the priority of the node to remove
	 * @return root the root of the tree
	 * @author Marty Stepp courses.cs.washington.edu
	 * @author Building Java Programs Third Edition - Marty Stepp & Stuart Reges
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
		
		if (root != null)
			(root.descendants)--; 
		
		return root;
	}
	
	/**
	 * Method to copy data from one node to another
	 * @param dest the node that data is copied to
	 * @param source the node that data is copied from
	 */
	private void set(Node dest, Node source) {
		dest.data.setPriority(source.data.getPriority());
		dest.data.setId(source.data.getId());
		dest.descendants--;
	}
	
	/**
	 * Removes the next node in the tree order
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
	public void getHighest() {
		overallRoot = getHighest(overallRoot);
	}
	
	/**
	 * Returns the highest priority node in the tree
	 * @param root the root of the tree
	 * @return the data of the highest priority node
	 */
	private Node getHighest(Node root) {
		if (root == null) {
			// do nothing
		} else if (root.right != null) {
			//System.out.println(" right now, root is: " + root.data.getPriority());
			root.right =  getHighest(root.right);
		} else {
		
			highestTicket.setPriority(root.data.getPriority());
			highestTicket.setId(root.data.getId());
			
			//System.out.println(" root was: " + root.data.getPriority());
			root = root.left;
			//System.out.println(" root is now: " + root.data.getPriority());
			return root;
		}
		
		if (root != null)
			(root.descendants)--; 
		
		//System.out.println(" root we are returning is: " + root.data.getPriority());
		return root;
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
	 * Method to find the priority of a given node
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
	 * Method to get the descendants of a node
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
		return false;
		
	}
	
	/**
	 * Returns whether or not a tree is empty
	 * @return true or false depending on the condition of the tree
	 */
	public boolean isEmpty(){
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

	
}
