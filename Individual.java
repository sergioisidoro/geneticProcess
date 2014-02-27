

import java.util.Vector;
import java.util.List;
import java.util.Enumeration;

public class Individual {

	public static class Node {
        public int id;
        private Node parent;
        public List<Node> children;

        public Node(int id, Node parent) {
            this.id = id;
            this.parent = parent;
            this.children = new Vector<Node>();
        }


        public Node deepClone(Node copyParent) {
        	Node newNode = new Node(this.id, copyParent);
        	for(Node n : children) {
                n.deepClone(newNode);
        		newNode.addChildren(n);
        	}
        	return newNode;
        }

        public Node searchNode(int a) {
            for(Node n : this.children) {
                if (n.id == a);
                    return n;
            }
            return null;    
        }


        public void addChildren(Node newNode) {
        	children.add(newNode); 
        }

        public Node addChildren(int newChildId) {
        	Node newChild = new Node(newChildId, this);
        	this.children.add(newChild);
            return newChild;
        }

        public Node getChild(int childId) {
        	return children.get(children.indexOf(childId));
        }

        public void removeChild (int childId) {
        	children.remove(children.indexOf(childId));
        }

        public void dump(int i) {
            System.out.print("level ");
            System.out.print(i);
            System.out.print(" ");
            System.out.println(id);
             for(Node n : children) {
                n.dump(i+1);
            }
        }
    }

    /* Tree strucutre with Root-Location-Machines-Process levels*/
	private Node root;
    private Vector<Node> quickMachineAccess;
    private int changeCost;
    private Random rand; 

    /*"inverted" tree, with all the process*/
    public Individual(Vector< Integer > machineLocations , Vector< Integer > processServices , 
        int numLocations, sint numServices, int numMachines, numProcesses) {

        this.quickMachineAccess = new Vector<Node>(numMachines);
        this.quickProcessAccess = new Vector<Node>(numProcesses)
        this.root = new Node(0, this.root);
        rand = new Random();

        int i = 0;
        while (i<= numLocations) {
            Node child = new Node(i, root);
            root.addChildren(child);
            i++;    
        }

        Enumeration<Integer> e= machineLocations.elements();
        int machineId = 0;
        while (e.hasMoreElements()) {
            int locationId = (int) e.nextElement();
            System.o
            ut.print(locationId);
            Node location = root.searchNode(locationId);
            Node machine = new Node(machineId, location);
            location.addChildren(machine);
            quickMachineAccess.add(machineId, machine);
            machineId++;    
        }
    }


    public void initializer(Vector< Integer > initialAssignment) {
        // TO DO
    }


    public void dump() {
        root.dump(0);
    }

    public int calculateFit( Vector< Vector<Integer> > machineCapacities, Vector< Vector<Integer> > softMachineCapacities,
        Vector< Vector<Integer> > processRequirements) {
        int totalCost = this.changeCost;
        return 0;
    }

    public void mutate(Vector< Integer > processMovingCosts, int mutationProb) {
        
        boolean val = rand.nextInt(1/mutationProb)==0;
        // To do
        // FOR ALL NODES, SEE IF IT MUTATES
            // IF MUTATES, PROCESS TO RAND (numberOfMachines)
                // Update changeCost
    }

    public void changeGene(int process, int machine) {
        //Change process from actual machine to specified machine
        //Update 
    }


    public 

}