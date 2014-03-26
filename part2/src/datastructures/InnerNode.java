package datastructures;

public class InnerNode implements Node{
	public final String attribute;
	public final Node left, right;

	public InnerNode(String attribute, Node left, Node right){
		this.attribute = attribute;
		this.left = left;
		this.right = right;
	}
}
