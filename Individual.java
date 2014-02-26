
import Tree

class Individual {

	public static class Node {
        private int id;
        private Node parent;
        public List<Node> children;

        public Node(int id, Node parent) {
            this.id = id;
            this.parent = parent; 
        }

        public Node deepClone(Node copyParent) {
        	newNode = Node(this.id, copyParent);
        	for(Node n : children) {
        		newNode.addChildren(n.deepClone(newNode));
        	}
        	return newNode;
        }

        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if obj.id.equals(this.id)
                return true;
            else
            	return false;
        }

        public void addChildren(Node newNode) {
        	children.add(newNode);
        }

        public void addChildren(int newChildId) {
        	newChild = new Child(newChildId, this.root);
        	children.add(newChild);
        }

        public Node getChild(int childId) {
        	return children.get(children.indexOf(childId));
        }

        public void removeChild (int childId) {
        	children.remove(children.indexOf(childId));
        }

    }

	private Node root;


}