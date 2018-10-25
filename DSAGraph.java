import java.util.*;
/**
 *  FILE: DSAGraph.java <br>
 *  PURPOSE: Implementation of a graph <br>
 *  REFERENCE: From DSA prac 6
 *
 *  @author Kei Sum Wang - 19126089
 */
public class DSAGraph
{
    /**
     * Vertex class -private inner class
     */
    private class DSAVertex
    {
        private String state;
        private String division;
        private double lattitude;
        private double longitude;

        private DSALinkedList<DSAEdge> edges;//edge list
        private DSALinkedList<DSAVertex> links;//edge list
        private boolean visited;
        private int visitDuration;

        public DSAVertex(String inState, String inDiv, double inLat, double inLong)
        {
            this.state = inState;
            this.division = inDiv;
            this.lattitude = inLat;
            this.longitude = inLong;
            this.edges = new DSALinkedList<DSAEdge>();
            this.links = new DSALinkedList<DSAVertex>();
            this.visited = false;
            this.visitDuration = 180;//3 hours(180 minutes) to meet and greet
        }

        //maybe remove this
        public void addEdge(DSAEdge edge)
        {
            this.edges.insertLast(edge);
        }

        //remove this maybe
        public void addAdjVertex(DSAVertex v)
        {
            this.links.insertLast(v);
        }

        /**
         * method to get adjacent list from node
         * @return list in string form
         */
        public String adjacentList()
        {
            String str = "";
            DSAVertex adj = null;
            Iterator<DSAVertex> it = this.links.iterator();

            while(it.hasNext())
            {
                adj = it.next();
                str += adj.toString() + " ";
            }

            return str;
        }

        /**
         * method to get edge list from node
         * @return list in string form
         */
        public String edgeListString()
        {
            String str = "";
            DSAEdge edge = null;
            Iterator<DSAEdge> it = this.edges.iterator();

            while(it.hasNext())
            {
                edge = it.next();
                str += edge.toString() + "\n";
            }

            return str;
        }

        public String toString()
        {
            return this.division + "(" + this.state + ")";
        }
    }

    /**
     * Edge class -private inner class
     */
    private class DSAEdge
    {
        private DSAVertex start;//from vertex
        private DSAVertex dest;//to vertex
        private double distance;
        private int minutes;
        private int totalVisitTime;
        private String transport;
        private boolean visited;

        public DSAEdge(DSAVertex inV1, DSAVertex inV2, double dist, int mins, String trans)
        {
            this.start = inV1;
            this.dest = inV2;
            this.distance = dist;
            this.minutes = mins;
            this.totalVisitTime = 0;
            this.transport = trans;
            this.visited = false;
        }

        public int addVisitDuration()
        {
            int visitDur = 0;
            if(!start.visited && !this.dest.division.contains("Airport") && !this.start.division.contains("Airport"))
            {
                visitDur = start.visitDuration + dest.visitDuration;
                this.totalVisitTime += visitDur;
            }
            else if(this.start.division.contains("Airport") && !this.dest.division.contains("Airport"))
            {
                visitDur = dest.visitDuration;
                this.totalVisitTime += visitDur;
            }
            else if(start.visited && !this.dest.division.contains("Airport") && !this.start.division.contains("Airport"))
            {
                visitDur = dest.visitDuration;
                this.totalVisitTime += visitDur;
            }


            return visitDur;
        }

        public String toString()
        {
            return "From: " + start.toString() + ", To: " +
                    dest.toString() + ", Distance: " + distance + "m, " +
                    "Time: " + minutes + "mins, " + "Mode Of Transport: " + transport + ", Time for meet/greet: " + totalVisitTime + "mins";
        }

    }


    //Graph class fields
    private DSALinkedList<DSAVertex> vertices;

    /**
     * DEFAULT Constructor for graph
     */
    public DSAGraph()
    {
        this.vertices = new DSALinkedList<DSAVertex>();
    }

    /**
    //ALTERNATE CTOR for reading file
    public DSAGraph(String fileName)
    {
        DSAGraph graph = null;

        FileIO f = new FileIO(fileName);
        f.buildGraph();
        graph = f.getGraph();

        this.vertices = graph.getVertices();
    }
    **/

    /**
     * method to add vertex(place) to graph
     * @param state abbreviation of state(String)
     * @param div name of division(String)
     * @param latt lattitude(Real)
     * @param longit lattitude(Real)
     */
    public void addVertex(String state, String div, double latt, double longit)
    {
        if(this.getVertex(div) == null)//check if vertex doesn't exist then add it, this is to prevent duplicate nodes
        {
            vertices.insertLast(new DSAVertex(state, div, latt, longit));
        }
    }

    /**
     * method to add edge to graph
     * @param v1 start vertex
     * @param v2 end vertex
     * @param dist distance(Real)
     * @param mins minutes(int)
     * @param trans transport type(String)
     */
    public void addEdge(DSAVertex v1, DSAVertex v2, double dist, int mins, String trans)
    {
        //v1 is start of edge
        //v2 is end of edge
        v1.links.insertLast(v2);//add to adjacency list
        v1.edges.insertLast(new DSAEdge(v1, v2, dist, mins, trans));//create edge, insert
    }

    /**
     * method to get vertex count
     * @return num of vertex
     */
    public int getVertexCount()
    {
        return vertices.getCount();
    }

    /**
     * method to get list of vertices
     * @return list of vertices
     */
    public DSALinkedList<DSAVertex> getVertices()
    {
        return vertices;
    }

    /**
     * method to get edge count
     * @return number of edges
     */
    public int getEdgeCount()
    {
        int numEdge = 0;
        DSAVertex v = null;
        Iterator<DSAVertex> it = vertices.iterator();

        while(it.hasNext())
        {
            v = it.next();
            numEdge += v.links.getCount();
        }

        return numEdge;
    }

    /**
     * method to get vertex by label
     * @param inLabel label of vertex(String)
     * @return vertex
     */
    public DSAVertex getVertex(String inLabel)
    {
        boolean found = false;
        DSAVertex v = null;
        Iterator<DSAVertex> it = vertices.iterator();

        while(it.hasNext() && !found)
        {
            v = it.next();
            if(v.division.equals(inLabel))
            {
                found = true;
            }
            else
            {
                v = null;
            }
        }

        return v;
    }

    /**
     * getter for vertex adjacency list
     * @param vertex
     * @return adjacency of vertex
     */
    public DSALinkedList getAdjacent(DSAVertex vertex)
    {
        return vertex.links;
    }

    /**
     * method to check if two vertices are adjacent
     * @param v1 vertex
     * @param v2 vertex
     * @return boolean telling whether vertices are adjacent
     */
    public boolean isAdjacent(DSAVertex v1, DSAVertex v2)
    {
        boolean adjacent = false;
        DSAVertex checkVertex = null;
        Iterator<DSAVertex> it = v1.links.iterator();

        while(it.hasNext())
        {
            checkVertex = it.next();
            if(checkVertex.equals(v2))
            {
                adjacent = true;
            }
        }

        return adjacent;
    }

    /**
     * Method to Display adjacency list of graph    
     */
    public void displayList()
    {
        DSAVertex v = null, adj = null;
        Iterator<DSAVertex> it; 
        
        it = vertices.iterator();

        while(it.hasNext())
        {
            v = it.next();
            System.out.println(v.toString() + "=> " + v.adjacentList());
        }

    }
    
    /**
     * Method to Display edge list of graph    
     */
    public void displayEdges()
    {
        DSAVertex v = null;
        Iterator<DSAVertex> it; 
        
        it = vertices.iterator();

        while(it.hasNext())
        {
            v = it.next();
            System.out.print(v.edgeListString());
        }

    }

    /**
     * Method to Display adjacency matrix of graph    
     */
    public void displayMatrix()
    {
        int[][] matrix = createMatrix();
        Iterator<DSAVertex> it;

        it = vertices.iterator();
        while(it.hasNext())
        {
            System.out.print("  " + it.next().division);
        }
        System.out.println();

        it = vertices.iterator();
        for(int ii = 0; ii < matrix.length; ii++)
        {
            System.out.print(it.next().division + " ");
            for(int jj = 0; jj < matrix[ii].length; jj++)
            {
                System.out.print(matrix[ii][jj] + "  ");
            }
            System.out.println();
        }
    }

//Search Methods
    /**
     * Depth-first-search
     * @param v vertex
     */
    public void DFS(DSAVertex v)
    {
        boolean found;
        DSAVertex newVertex = null, top, temp;
        DSAStack<DSAVertex> stack;
        Iterator<DSAVertex> vLinks;

        this.clearAllVisits();//mark all vertices as new
        stack = new DSAStack<DSAVertex>();
        v.visited = true;

        stack.push(v);
        while(!stack.isEmpty())
        {
            found = false;
            top = stack.top();
            vLinks = top.links.iterator();
            while(vLinks.hasNext() && !found)//loop until new vertex is found
            {
                newVertex = vLinks.next();
                if(!newVertex.visited)
                {
                    found = true;//this to stop loop once a new vertex is found
                    System.out.println(top.division + "=>" + newVertex.division);
                    newVertex.visited = true;
                    stack.push(newVertex);
                }
            }

            //Directed graph problem.
            //without this, the search would not reach nodes with empty adjlist
            //even though other nodes were connect to this node.
            //These nodes weren't used in the 2nd loop after the first push
            temp = stack.pop();
            stack.push(temp);/*to fix this, temporarily pop what was recently
                               pushed then push it back, then use check below*/

            if(this.allVisited(temp.links) || temp.links.isEmpty())//check if vertex on top stack has all adjacent vertices visited or is empty
            {
                stack.pop();
            }
        }

    }

    /**
     * Breadth-first-search
     * @param v vertex
     */
    public void BFS(DSAVertex v)
    {
        DSAVertex newVertex, front;
        DSAQueue<DSAVertex> queue;
        Iterator<DSAVertex> vLinks;

        this.clearAllVisits();//mark all vertices as new
        queue = new DSAQueue<DSAVertex>();
        v.visited = true;

        queue.enqueue(v);
        while(!queue.isEmpty())
        {
            front = queue.dequeue();//get front of queue
            vLinks = front.links.iterator();
            while(vLinks.hasNext())
            {
                newVertex = vLinks.next();
                if(!newVertex.visited)
                {
                    System.out.println(front.division + "->" + newVertex.division);
                    newVertex.visited = true;
                    queue.enqueue(newVertex);
                }
            }
        }
    }

    /**
     * Shortest path not complete<br>
     * based on breadth first search.
     * @param v vertex
     */
    public void shortPath(DSAQueue<Division> vertexQ)
    {
        boolean found = false;
        int shortTime = Integer.MAX_VALUE;
        int totalTravelTime = 0, meetGreet = 0, curMeet, totalTime;
        DSAVertex newVertex, front, v;
        DSAEdge curEdge;
        DSAQueue<DSAVertex> queue;
        Iterator<DSAEdge> edg;
        Iterator<Division> divQ;

        queue = sortDivQueue(vertexQ);
        this.clearAllVisits();//mark all vertices as new
        v = this.getVertex(vertexQ.dequeue().getDivName());
        v.visited = true;

        meetGreet += v.visitDuration;//initially will be meeting and greeting for 3 hours at start location
        if(vertexQ.isEmpty())
        {
            System.out.println("No travel, only visit at " + v.toString());
        }

        queue.enqueue(v);
        while(!queue.isEmpty())
        {
            curMeet = 0;
            found = false;
            front = queue.dequeue();//get front of queue
            //System.out.println(front.division);
            edg = front.edges.iterator();
            while(edg.hasNext() && !found && !vertexQ.isEmpty())
            {
                divQ = vertexQ.iterator();
                curEdge = edg.next();
                //totalTravelTime =  curEdge.minutes;
                //System.out.println(curEdge.dest.division);
                //if(!vertexQ.isEmpty() && !curEdge.dest.visited && totalTravelTime < shortTime && curEdge.dest.equals(getVertex(vertexQ.peek().getDivName()))) 
                if(!curEdge.dest.visited && curEdge.dest.equals(getVertex(vertexQ.peek().getDivName()))) 
                {
                    curMeet = curEdge.addVisitDuration();
                    System.out.println(curEdge.toString());
                    shortTime = curEdge.minutes;
                    curEdge.dest.visited = true;
                    curEdge.visited = true;
                    found = true;
                    queue.enqueue(curEdge.dest);
                    vertexQ.dequeue().getDivName();
                    meetGreet += curMeet;
                    totalTravelTime += curEdge.minutes;
                }
                else if(!containNeighbour(curEdge.dest, vertexQ.peek())) 
                {
                    //System.out.println(curEdge.dest.division + " " + vertexQ.peek().getState());
                    //System.out.println(curEdge.dest.division + " " + front.state);
                    if(curEdge.dest.division.contains("Airport") && curEdge.dest.state.equals(front.state))
                    {
                        curMeet = curEdge.addVisitDuration();
                        System.out.println(curEdge.toString() + 1.0);
                        curEdge.dest.visited = true;
                        curEdge.visited = true;
                        queue.enqueue(curEdge.dest);
                        meetGreet += curMeet;
                        totalTravelTime += curEdge.minutes;
                    }
                    if(curEdge.dest.division.contains("Airport") && curEdge.dest.state.equals(vertexQ.peek().getState()))
                    {
                        curMeet = curEdge.addVisitDuration();
                        System.out.println(curEdge.toString() + 2.0);
                        curEdge.dest.visited = true;
                        curEdge.visited = true;
                        queue.enqueue(curEdge.dest);
                        meetGreet += curMeet;
                        totalTravelTime += curEdge.minutes;
                    }
                    //System.out.println(!curEdge.dest.visited && curEdge.dest.division.contains("Airport") && curEdge.dest.state.equals(vertexQ.peek().getState()));
                    //System.out.println(vertexQ.peek().getState() + " " + curEdge.dest.division);
                }
                else
                {
                    if(curEdge.dest.division.contains("Airport") && curEdge.dest.state.equals(vertexQ.peek().getState()) && curEdge.transport.equals("plane"))
                    {
                        curMeet = curEdge.addVisitDuration();
                        System.out.println(curEdge.toString() + 3.0);
                        curEdge.dest.visited = true;
                        curEdge.visited = true;
                        queue.enqueue(curEdge.dest);
                        meetGreet += curMeet;
                        totalTravelTime += curEdge.minutes;
                    }
                    /**
                    if(curEdge.dest.state.equals(vertexQ.peek().getState()))
                    {
                        //System.out.println(curEdge.toString() + 3.0);
                        curEdge.dest.visited = true;
                        curEdge.visited = true;
                        queue.enqueue(curEdge.dest);
                    }
                    **/
                }
            //System.out.println("time meet and greets: " + meetGreet + "mins" + front.division);
            }

        }
            System.out.println("===Campaign Summary===");
            System.out.println("Total travel time for journey: " + totalTravelTime + "mins");
            System.out.println("Total time for meet and greets: " + meetGreet + "mins");
            System.out.println("Total time overall for campaign: " + (totalTravelTime+meetGreet) + "mins");
    }
    
    public DSALinkedList<DSAVertex> sortDivQueue(DSAQueue<Division> divQueue)
    {
        DSALinkedList<DSAVertex> sortedQ = new DSALinkedList<DSAVertex>();
        DSALinkedList<DSAVertex> queueHelp = new DSALinkedList<DSAVertex>();
        DSALinkedList<DSAVertex> queue = new DSALinkedList<DSAVertex>();
        Division cur = null;

        Iterator<Division> it = divQueue.iterator();

        sortedQ.insertLast(getVertex(divQueue.dequeue().getDivName()));//store the first location in queue first since it is chosen div to visit 1st

        while(it.hasNext())
        {
            cur = it.next();
            if(cur.getState().equals(sortedQ.peekLast().state))
            {
                sortedQ.insertLast(getVertex(cur.getDivName()));
            }
            else
            {
                queue.insertLast(getVertex(cur.getDivName()));
            }
        }


        while(!queue.isEmpty())
        {
            DSAVertex temp = sortedQ.peekLast();

            if(fastInterstateByPlane(temp.state, divQueue).equals(queue.peek().state)
            {
                sortedQ.insertLast(stack.pop());
            }
            else
            {
                queue.enqueue(queue.dequeue());
            }
       }

        return sortedQ;
    }

    public boolean containNeighbour(DSAVertex division, Division needDiv)
    {
        boolean found = false;
        DSAVertex v = null;
        Iterator<DSAVertex> adjList;
        adjList = division.links.iterator();

        while(adjList.hasNext() && !found)
        {
            v = adjList.next();
            if(v.division.equals(needDiv.getDivName()))
            {
                found = true;
            }
        }

        return found;
    }
    
    public String fastInterstateByPlane(DSAVertex fromDiv, DSAQueue<Division> div)
    {
        int totalTime = 0, temp = 0, quickest = 0;
        DSAEdge curEdg = null;
        DSAVertex airport = null, shortDiv = null;
        DSAStack<Integer> timeStack = new DSAStack<Integer>();
        Iterator<DSAEdge> edgeIt;

        airport = findAirportFromDiv(fromDiv);
        edgeIt = airport.edges.iterator();

        while(edgeIt.hasNext())
        {
            curEdg = edgeIt.next();
            if(curEdg.dest.division.contains("Airport") && !curEdg.dest.state.equals(airport.state) && matchStates(curEdge.dest.state, div))
            {
                if(curEdg.distance <= quickest)
                {
                    quickest = curEdg.distance;
                    shortState = curEdg.dest;
                }
            }
        }
        return shortState.state;
    }

    public boolean matchStates(String state, DSAQueue<Division> div)
    {
        boolean found = false;
        Division cur = null;
        Iterator<Division> it = div.iterator();

        while(it.hasNext() && !found)
        {
            cur = it.next();
            found = (state.equals(cur.getState()));
        }

        return found;
    }

    public DSAVertex findAirportFromDiv(DSAVertex fromDiv)
    {
        boolean found = false;
        int totalTime = 0, temp = 0;
        DSAEdge curEdg = null;
        DSAVertex airport = null;
        Iterator<DSAEdge> edgeIt;

        edgeIt = fromDiv.edges.iterator();

        while(edgeIt.hasNext() && !found)
        {
            curEdg = edgeIt.next();
            if(curEdg.dest.division.contains("Airport") && curEdg.dest.state.equals(fromState.state))
            {
                found = true;
                airport = curEdge.dest;
            }
        }

        return airport;
    }
                
    /**
     * Method to clear visit of vertices
     */
    public void clearAllVisits()
    {
        Iterator<DSAVertex> it;

        it = this.vertices.iterator();

        while(it.hasNext())
        {
            it.next().visited = false;
        }
    }

    /**
     * Method to check if all vertices have been visited
     * @return boolean if all visited or not
     */
    public boolean allVisited(DSALinkedList<DSAVertex> list)
    {
        int visits, total;
        Iterator<DSAVertex> it;

        visits = 0;
        total = list.getCount();
        
        it = list.iterator();

        while(it.hasNext())
        {
            if(it.next().visited == true)
            {
                visits++;
            }
        }

        return (visits == total);
    }
       
//private methods
    private int[][] createMatrix()
    {
        int size, ii, jj;
        int[][] matrix; 
        DSAVertex v1 = null, v2 = null;
        Iterator<DSAVertex> it1, it2;
        
        size = vertices.getCount();
        matrix = new int[size][size]; 
        it1 = vertices.iterator();

        ii = 0;
        while(it1.hasNext())
        {
            jj = 0;
            v1 = it1.next();
            it2 = vertices.iterator();

            while(it2.hasNext())
            {
                v2 = it2.next();
                if(this.isAdjacent(v1, v2))
                {
                    matrix[ii][jj] = 1;
                }
                else
                {
                    matrix[ii][jj] = 0;
                }
                jj++;
            }
            ii++;
        }
        return matrix;
    }
}
