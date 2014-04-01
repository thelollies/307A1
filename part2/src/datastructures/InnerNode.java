package datastructures;

import java.util.List;

import main.Helper;

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

	@Override
	public String show(Helper h) {
		return h.attNames.get(attribute);
	}

	@Override
	public void report(String indent, List<String> categoryNames,
			List<String> attributeNames) {
		System.out.format("%s%s = True:\n",
				indent, attributeNames.get(attribute));
				left.report(indent+" ", categoryNames, attributeNames);
				System.out.format("%s%s = False:\n",
				indent, attributeNames.get(attribute));
				right.report(indent+" ", categoryNames, attributeNames);
	}

}
