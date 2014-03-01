
import java.util.*;

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
                if (n.id == a)
                    return n;
            }
            return null;    
        }

        public void addChildren(Node newNode) {
        	children.add(newNode); 
            newNode.parent = this;
        }

        public Node addChildren(int newChildId) {
        	Node newChild = new Node(newChildId);
        	this.children.add(newChild);
            newChild.parent = this;
            return newChild;
        }

        public Node getChild(int childId) {
        	return children.get(children.indexOf(childId));
        }

        public void removeChild (Node child) {
        	children.remove(child);
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
    public Vector<Node> quickServiceAccess;
    public int changeCost;
    public Random rand; 
    public boolean isViable;
    public Vector<Integer> processMovingCosts; 
    public Vector<Integer> initialAssignment;
    public Vector< Integer > serviceMinSpreads;
    public Vector< Vector<Integer> > machineCapacities;
    public Vector< Vector<Integer> > processRequirements;
    public Vector< Vector<Integer> > softMachineCapacities;
    public Vector< Integer > processServices ;
    public int numProcesses;    
    public int numResources;
    public int numServices;
    public int numMachines;
    public float currentFit;
    public int rank;
    /*"inverted" tree, with all the process*/

    public Individual(Vector< Integer > machineLocations , Vector< Integer > processServices , 
        int numLocations, int numServices, int numMachines, int numProcesses, 
        Vector<Integer> processMovingCosts, Vector<Integer> initialAssignment,
        Vector< Integer > serviceMinSpreads, int numResources, Vector< Vector<Integer> > machineCapacities, 
                 Vector< Vector<Integer> > processRequirements,  Vector< Vector<Integer> > softMachineCapacities) {

        this.isViable = false;
        this.numProcesses = numProcesses;
        this.initialAssignment = initialAssignment;
        this.processMovingCosts = processMovingCosts;
        this.serviceMinSpreads = serviceMinSpreads;
        this.numResources = numResources;
        this.numServices = numServices;
        this.processRequirements = processRequirements;
        this.machineCapacities = machineCapacities;
        this.softMachineCapacities = softMachineCapacities;
        this.numMachines = numMachines;
        this.processServices = processServices;
        this.quickMachineAccess = new Vector<Node>(numMachines);
        this.quickProcessAccess = new Vector<Node>(numProcesses);
        this.quickServiceAccess = new Vector<Node>(numServices);
        this.root = new Node(0);
        rand = new Random();

        int i = 0;
        while (i<= numLocations) {
            Node child = new Node(i);
            root.addChildren(child);
            i++;    
        }

        Enumeration<Integer> e= machineLocations.elements();
        int machineId = 0;
        while (e.hasMoreElements()) {
            int locationId = (int) e.nextElement();
            Node location = root.searchNode(locationId);
            Node machine = new Node(machineId);
            location.addChildren(machine);
            quickMachineAccess.add(machineId, machine);
            machineId++;    
        }

        i = 0;
        while (i < this.numProcesses) {
            Node newNode = new Node(i);
            quickProcessAccess.add(i, newNode);
            i++;
        }

        i = 0;
        while (i < this.numServices) {
            Node newNode = new Node(i);
            quickServiceAccess.add(i, newNode);
            i++;
        }

        Enumeration<Integer> t= processServices.elements();
        int processId = 0;
        while (t.hasMoreElements()) {
            int serviceId = (int) t.nextElement();
            quickServiceAccess.get(serviceId).addChildren(quickProcessAccess.get(processId));
            processId++;    
        }
    }

    public void reset() {
        for (Node n: quickProcessAccess) {
            n.parent.removeChild(n);
        }
    }

    public void initializer(Vector< Integer > assignment) {
        
        Enumeration<Integer> e= assignment.elements();
        int i = 0;
        this.changeCost = 0;
        while (e.hasMoreElements()) {
            Node process = quickProcessAccess.get(i);
            Node machine = quickMachineAccess.get(e.nextElement());
            machine.addChildren(process);

            if (machine.id != this.initialAssignment.get(process.id))
                this.changeCost += this.processMovingCosts.get(i);
            i++;    
        }
    }

    public void firstInit() {
        this.initializer(initialAssignment);
    }

    public Node getGene(int i) {
        return quickProcessAccess.get(i);
    }

    public boolean checkSpreadAndColision(Node s) {

        HashSet<Integer> noDupSet = new HashSet<Integer>();
        HashSet<Integer> serviceConflict = new HashSet<Integer>();
        int numberOfMachines = 0;

        for (Node p: s.children) {
            serviceConflict.add(p.parent.id);
            noDupSet.add(p.parent.parent.id);
            numberOfMachines ++;
        }

        int serviceSpread = noDupSet.size();
        int differnteMachines = serviceConflict.size();
        if (serviceSpread <  this.serviceMinSpreads.get(s.id) || differnteMachines < numberOfMachines)
            return false;
        else
            return true;
    }

    public boolean checkMachineLoad(Node m) {
         Vector<Integer> total = new Vector<Integer>();
            for (int t = 0; t!= numResources; t++)
                total.add(0);

            for(Node p : m.children) {
                 for(int z = 0; z!= numResources; z++) {
                    total.set(z, (total.get(z) + this.processRequirements.get(p.id).get(z)));
                    }
                }

            for (int t = 0; t!= numResources; t++) {
                 if (total.get(t) > machineCapacities.get(m.id).get(t))
                    return false;
            }
            return true;
    }

    public boolean isViable() {
        for(Node s : this.quickServiceAccess) {
            if (!checkSpreadAndColision(s)) {
                return false;
            }
        }

        for(Node s : this.quickMachineAccess) { 
            if (!checkMachineLoad(s)) {
                return false;
            }
        }
        return true;    
    }

    public double fitness () {
        //CHECKS IF IS VIABLE AND RETURNS FITNESS:
        //Biggest penalty
        double result = Double.POSITIVE_INFINITY;
        double penalties = 0;
        
        // Check hard Machine Requirments and calculates penalties
        for(Node s : this.quickMachineAccess) {

            Vector<Integer> total = new Vector<Integer>();
            for (int t = 0; t!= numResources; t++)
                total.add(0);

            for(Node p : s.children) {
                 for(int z = 0; z!= numResources; z++) {
                    total.set(z, (total.get(z) + this.processRequirements.get(p.id).get(z)));
                }
            }

            for (int t = 0; t!= numResources; t++) {
                if(total.get(t) > softMachineCapacities.get(s.id).get(t))
                    penalties += total.get(t) - softMachineCapacities.get(s.id).get(t);
                }
        
        }
        currentFit = (float) penalties + this.changeCost;
        return this.currentFit;
    }


    public void mutate(int mutationProb) {

        // Mutation is a random change form one machine to anoter.
        boolean val;
        Enumeration<Node> e= quickProcessAccess.elements();

        while (e.hasMoreElements()) {
            val = this.rand.nextInt(mutationProb)==0;
            Node a = e.nextElement();
            int previousMachine = a.parent.id;
            if (val) {
                int toMachine = this.rand.nextInt(this.numMachines);
                this.changeGene(a.id, toMachine);
                if (!checkMachineLoad(quickMachineAccess.get(toMachine)) || !checkSpreadAndColision(quickServiceAccess.get(processServices.get(a.id)))) {
                    this.changeGene(a.id, previousMachine);
                    // if infeasible, rollback
                }
            }
            else 
                continue;
        }
    }

    public void changeGene(int process, int machine) {
        Node processNode = quickProcessAccess.get(process);
        Node destinationMachineNode = quickMachineAccess.get(machine);

        if (processNode.parent.id == this.initialAssignment.get(process) && 
                machine != this.initialAssignment.get(process)) {
                // IF CURRENT INITIAL ASSIGNMENT
                this.changeCost += this.processMovingCosts.get(process); 
            }
        else  {
            if (processNode.parent.id != this.initialAssignment.get(process) && 
                machine == this.initialAssignment.get(process)) {
                this.changeCost -= this.processMovingCosts.get(process);
            } 
        }
            
        processNode.parent.removeChild(processNode);
        destinationMachineNode.addChildren(processNode);
    }


    public void uniformlyCrossOver(Individual mate, int probability) {
        Enumeration<Node> e= quickProcessAccess.elements();
        boolean val ;
        while (e.hasMoreElements()) {
            Node a = e.nextElement();

            val = this.rand.nextInt(probability)==0;
            if (val) {
                Node aGene = this.getGene(a.id);
                Node bGene = mate.getGene(a.id);
                
                int aGeneMachine = aGene.parent.id;
                int bGeneMachine = bGene.parent.id;

                this.changeGene(a.id , bGeneMachine);
                mate.changeGene(a.id , aGeneMachine);

                if (!this.checkMachineLoad(this.quickMachineAccess.get(bGeneMachine)) || 
                    !mate.checkMachineLoad(mate.quickMachineAccess.get(aGeneMachine)) ||
                    !this.checkSpreadAndColision(this.quickServiceAccess.get(this.processServices.get(a.id))) ||
                    !mate.checkSpreadAndColision(mate.quickServiceAccess.get(this.processServices.get(a.id))) ) {
                    this.changeGene(a.id , aGeneMachine);
                    mate.changeGene(a.id , bGeneMachine);
                    // if infeasible, rollback
                }
            }
            else    
                continue;
        }
    }

    public float getCurrentFitness() {
        return currentFit;
    }

    public Vector<Integer> toConfiguration() {
        Vector<Integer> result = new Vector<Integer>(numProcesses);

        for(Node n : this.quickProcessAccess) {
                result.add(n.id, new Integer(n.parent.id));
            }
        return result;
    }
}