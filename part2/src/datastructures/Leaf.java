package datastructures;

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
}
