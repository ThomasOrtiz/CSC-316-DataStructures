public class Graph {
	
	/**
	 * list of vertices in this graph
	 */
	private GenericList<Vertex> vertices;
	
	/**
	 * The popular calculation for this graph
	 */
	private String popular;
	
	/**
	 * The notConnected calculation for this graph
	 */
	private int notConnected;

	/**
	 * Constructs the Graph.
	 */
	public Graph() {
		vertices = new GenericList<Vertex>();
		popular = null;
		notConnected = -1;
	}

	/**
	 * Determines if this graph is empty
	 * 
	 * @return true if the graph is empty.
	 */
	public boolean isEmpty() {
		return vertices.isEmpty();
	}

	/**
	 * Returns true if the graph has this node. 
	 * Runs in O(n) time.
	 * 
	 * @param node
	 *            to search for
	 * @return true if node is in graph.
	 */
	public boolean VertexExists(Vertex vtr) {
		for (int i = 0; i < vertices.size(); i++) {
			if (vertices.get(i).equals(vtr)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the node with the given ID.
	 *
	 * @return node with given ID or null if absent.
	 */
	public Vertex getVertex(String nodeID) {
		for (int i = 0; i < vertices.size(); i++) {
			if (vertices.get(i).getid().equals(nodeID) ) {
				return vertices.get(i);
			}
		}
		return null;
	}

	/**
	 * returns the array of nodes. Used in printing.
	 * 
	 * @return arrayList of nodes.
	 */
	public GenericList<Vertex> getVertexArray() {
		return vertices;
	}
	
	/**
	 * Determines if person1 is a friend of person2
	 * @param person1
	 * @param person2
	 * @return
	 */
	public boolean isFriend( Vertex person1, Vertex person2 ) {
		return person1.isAttached(person2);
	}
	
	public String relation (Vertex start, Vertex goal) {
		if(start.isAttached(goal)){
			return "" + start.toString() + "\n" + goal.toString() + "\n";		
		}
		
		Queue<Vertex> q = new Queue<Vertex>();
		q.add(start);
		start.setMarked(true);
		boolean pathFound = false;
		
		while ( !(q.isEmpty()) ) {
			Vertex current = q.remove();
			
			if ( current.getid().equals(goal.getid()) ) {
				//path is found
				pathFound = true;
				break;
			}
			
			GenericIterator<Vertex> e = current.getAdjVertices().iterator();
			
			while (e.hasNext()) {
				Vertex adj = e.next();
				
				if( !(adj.isMarked()) ){
					q.add(adj);
					adj.setMarked(true);
					adj.setBackPointer(current);
				}
				
			}
		}
		
		
		if ( pathFound ) {
			String shortestPath = "";
			boolean foundStart = false;
			Vertex current = goal;
			Queue<Vertex> answer = new Queue<Vertex>();
			while ( !foundStart ) {
				
				if ( current.getid().equals(start.getid()) ) {
					foundStart = true;
					answer.add(current);
				} else {
					answer.add(current);
					current = current.getBackPointer();
				}
				
			}
			
			while ( !(answer.isEmpty()) ) {
				shortestPath = answer.peek().getid() + "\n" + shortestPath;
				answer.remove();
			}
			
			return shortestPath;
		}
		
		return "";
	}
	
	
	public String mutual (Vertex person1, Vertex person2 ) {
		String s = "";
		GenericIterator<Vertex> e = person1.getAdjVertices().iterator();
		while( e.hasNext() ){
			Vertex next = e.next();
			if( person2.getAdjVertices().contains( next )) {
				s += next + "\n";
			}
		}
		
		return s;
	}
	
	public String getPopular(){
		if(this.popular == null){
			return popular();
		} else {
			return this.popular;
		}
	}
	

	public int getNotConnected(){
		if(this.notConnected == -1){ // if we haven't done not connected yet.
			return notConnected();
		} else {
			return this.notConnected;
		}
	}
	
	
	public int notConnected() {
		// The sum we will return
		int overallSum = 0;
		// A list of integers, each entry will be the number of vertices
		//in each connected component
		GenericList< Integer > cc = new GenericList< Integer >();
		// An iterator over the entire list of vertices in the graph
		GenericIterator<Vertex> vrtList = vertices.iterator();
		// For each vertex in the graph, find it's connected component and
		// calculate how many vertices are in that particular connected component
		while( vrtList.hasNext() ) {
			//the current vertex we are looking at
			Vertex current = vrtList.next();
			// if we have not seen this vertex before, it means that this is a
			// new connected component we have not seen yet.
			if ( !current.isMarked() ) {
				//add this connected component to our list of integers. That is, add it as
				// the number of vertices in that connect component
				cc.add( findComponentNodes(current) );
			}
		}
		
		// now sum up all the node that are not connected to each other
		// note, this may could be done in just one for loop, but even so
		// it is not directly affected by input unless no vertices are connected
		// in the graph at all
		for (int i = 0; i < cc.size(); i++ ) {
			int currentSum = 0;
			for (int j = i + 1; j < cc.size(); j++ ) {
				currentSum += cc.get(i) * cc.get(j);
			}
			overallSum += currentSum;
		}
		this.notConnected = overallSum;
		return overallSum;
	}
	
	private int findComponentNodes( Vertex person ) {
		int count = 0;
		Queue<Vertex> q = new Queue<Vertex>();
		q.add(person);
		person.setMarked(true);
		count++;
		
		while (!q.isEmpty()) {
			Vertex current = q.remove();
			GenericIterator<Vertex> e = current.getAdjVertices().iterator();
			
			while ( e.hasNext() ) {
				Vertex adj = e.next();
				if (!adj.isMarked()) {
					adj.setMarked(true);
					q.add(adj);
					count++;
				}
			}
		}
		
		return count;
	}
	
	public void unmark(){
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setMarked(false) ;
		}
	}

	public String printGraph(){
		return this.vertices.printGenericList();
	}
	
	
	public String popular() {
		double max = 0;
		GenericIterator<Vertex> e = vertices.iterator();
		
		while ( e.hasNext() ) {
			Vertex current = e.next();
			current.setPopularity( popularBFS( current ) );
			unmark();
			if ( current.getPopularity() > max) {
				max = current.getPopularity();
			}
		}
		
		//build the popular String to use in the program
		String popularString = "";
		e = vertices.iterator();
		while ( e.hasNext() ) {
			Vertex current = e.next();
			// If the current person's popularity is one of the 
			// most popular people, add them to the string
			if ( current.getPopularity() == max )
				popularString += current.getid() + "\n";
			}
			this.popular = popularString;
			return popularString;
	}
	
	
	public double popularBFS( Vertex start ) {
		int lengthSum = 0;
		int vertCounter = 0;
		
		Queue<Vertex> q = new Queue<Vertex>();
		q.add(start);
		start.setMarked(true);
		start.setLevel(0);
		
		while ( !(q.isEmpty()) ) {
			Vertex current = q.remove();
			
			GenericIterator<Vertex> e = current.getAdjVertices().iterator();
			
			while (e.hasNext()) {
				Vertex adj = e.next();
				if( !(adj.isMarked()) ){
					q.add(adj);
					adj.setMarked(true);
					adj.setLevel( current.getLevel() + 1 );
					vertCounter++;
					lengthSum += adj.getLevel();
				}
			}
		}
		
		if ( vertCounter == 0 ) {
			return 0.0;
		} else {
			return (double) vertCounter / lengthSum;
		}
		
	}
	
	
}