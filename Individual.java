

import java.util.Vector;
import java.util.List;
import java.util.Enumeration;

public class Individual {

	public static class Node {
        public int id;
        public Node parent;
        public List<Node> children;

        public Node(int id) {
            this.id = id;
            this.children = new Vector<Node>();
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
            newNode.parent = this;
        }

        public Node addChildren(int newChildId) {
        	Node newChild = new Node(newChildId, this);
        	this.children.add(newChild);
            newChildId.parent = this;
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
	public Node root;
    public Vector<Node> quickMachineAccess;
    public Vector<Node> quickProcessAccess;
    public int changeCost;
    public Random rand; 
    public boolean isViable;

    /*"inverted" tree, with all the process*/
    public Individual () {
        isViable = false;
    }

    public Individual(Vector< Integer > machineLocations , Vector< Integer > processServices , 
        int numLocations, int numServices, int numMachines, int numProcesses) {

        this.quickMachineAccess = new Vector<Node>(numMachines);
        this.quickProcessAccess = new Vector<Node>(numProcesses);
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


    public void initializer(Vector< Integer > initialAssignment, int numProcesses) {
        this.isViable = true;
        int i = 0;
        while (i < numProcesses) {
            Node newNode = Node(i);
            quickProcessAccess.add(i, newNode);
            quickMachineAccess.get(initialAssignment.get(i)).addChildren(newNode);
        }
    }

    public void dump() {
        root.dump(0);
    }

    public boolean isViable() {
        return isViable;
    }

    public int calculateFit( Vector< Vector<Integer> > machineCapacities, 
                Vector< Vector<Integer> > softMachineCapacities,
                Vector< Vector<Integer> > processRequirements) {

        int totalCost = this.changeCost;
        return 0;
    }

    public void mutate(Vector<Integer> processMovingCosts, int mutationProb, 
                Vector<Integer> initialAssignment, int numProcesses) {

        // Mutation is a random change form one machine to anoter.
        boolean val;
        Enumeration<Node> e= quickProcessAccess.elements();

        while (e.hasMoreElements()) {
            val = rand.nextInt(1/mutationProb)==0;
            if (val == True) {
                toMachine = rand(numProcesses);
                this.changeGene(e.id, toMachine);
                if (toMachine != initialAssignment.get(e.id))
                    this.changeCost = processMovingCosts.get(e.id);
                else
                    this.changeCost = 0;
            }
            else 
                continue;
        }
    }

    public void changeGene(int process, int machine) {
        Node processNode = quickProcessAccess.get(process);
        Node destinationMachineNode = quickMachineAccess.get(machine);

        processNode.parent.removeChild(processNode);
        destinationMachineNode.destinationMachineNode.addChildren(processNode);
    }

    public void uniformlyCrossOver() {

    }

    public Individual clone(int numServices, int numMachines) {
        Node newRoot = new Node(0);
        Vector<Node>
        qM = new Vector<Node>(numMachines);
        qP = new Vector<Node>(numProcesses);

        for(Node location : root.children) {
            Node locationCopy = new Node(location.id);
            root.addChildren(locationCopy);

                for(Node server : location.children) {
                    Node machineCopy = new Node(server.id);
                    locationCopy.addChildren(machineCopy);
                    qM.add(machineCopy.id, machineCopy);

                    for(Node proc : server.children) {
                        Node processCopy = new Node(proc.id);
                        machineCopy.addChildren(processCopy);
                        qP.add(processCopy.id, machineCopy);
                    }
                }
            }
    }
}