package datastructures;

import java.util.List;

import main.Helper;

public class Leaf implements Node {
	public final int category;
	public final double probability;

	public Leaf(int category, double probability){
		this.category = category;
		this.probability = probability;
	}

	@Override
	public String toString() {
		return "Leaf: " + category + " " + probability;
	}

	@Override
	public String show(Helper h) {
		return "Leaf: " + h.categoryNames.get(category) + " " + probability;
	}

	@Override
	public void report(String indent, List<String> categoryNames, List<String> attributeNames) {
			System.out.format("%sClass %s, prob=%4.2f\n",
					indent, categoryNames.get(category), probability);
	}
}
