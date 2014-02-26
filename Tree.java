

public class Tree {
    
    public static class Node {
        private int data;
        private Node parent;
        private List<Node> children;

        public Node(int data, Node parent) {
            this.parent = parent
            this.data = data 
        }


        public boolean equals(Node obj) {
            if (obj == null)
                return false;
            if obj.data.equals(this.data)
                return true
        }
    }


    private Node root;

    public Tree(int rootData) {
        root = new Node();
        root.data = rootData;
        root.children = new ArrayList<Node>();
    }

    public addLocation(int location) {
        Node a = Node(location, root)
        root.children.add(Node(location))
    }

    public addServer(int location, int server) {
        rootLocation = root.children.get(indexOf(location))
        rootLocation.add(Node<Integer(server, rootLocation))
    }


    public addProcess(int location, int server, int process) {
        rootServer = root.children.get(indexOf(location)).get(indexOf(server))
        rootLocation.add(Node<Integer(server, rootLocation))
        root.children.get(indexOf(location)).add(Node<Integer(process))

    }

    public calculateUtility() {


    }



}