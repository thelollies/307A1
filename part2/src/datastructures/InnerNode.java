package datastructures;

public class InnerNode implements Node{
	public final int attribute;
	public final Node left, right;

	public InnerNode(int attribute, Node left, Node right){
		this.attribute = attribute;
		this.left = left;
		this.right = right;
	}
	
	@Override
	public String toString() {
		return "Node: " + attribute;
	}
}