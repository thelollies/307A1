package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Helper.Instance;
import datastructures.InnerNode;
import datastructures.Leaf;
import datastructures.Node;

/**
 * Classifier that constructs a decision tree based on a set of training data
 * and then provides methods to evaluate unseen instances based on the
 * constructed decision tree.
 * @author stepherory
 *
 */
public class DecisionTreeClassifier {

	private final Helper helperTraining;
	private final Helper helperTesting;
	private final Node tree;

	public DecisionTreeClassifier(String trainingFile, String testingFile){
		// Instantiate helpers
		helperTraining = new Helper();
		helperTraining.readDataFile(trainingFile);
		helperTesting = new Helper();
		helperTesting.readDataFile(testingFile);

		tree = constructTree(helperTraining.allInstances, helperTraining.attNames);
	}

	// TODO fix this to print the name of the node then true or false
	private void printTree(Node tree, int indent) {
		for(int i = 0; i < indent; i++) System.out.printf(" ");
		System.out.println(tree.show(helperTraining));
		if(tree instanceof InnerNode){
			InnerNode inner = (InnerNode) tree;
			printTree(inner.left, ++indent);
			printTree(inner.right, ++indent);
		}
	}

	public void printTree(){
		printTree(tree, 0);
	}

	private Node constructTree(List<Instance> instances, List<String> attributes){
		if(instances.isEmpty()) // return leaf with name of most probable class and it's prob (baseline predictor)
			return new Leaf(helperTraining.mostCommonCategory,
					helperTraining.mostCommonCatCount / helperTraining.allInstances.size());
		if(homogeneous(instances))
			return new Leaf(instances.get(0).getCategory(), 1); // leaf with category of all instances and prob 1
		if(attributes.size() == 0){
			// Create map of category number -> category count
			Map<Integer, Integer> modeMap = new HashMap<Integer, Integer>();
			for(Instance inst : instances){
				if(modeMap.get(inst.getCategory()) != null)
					modeMap.put(inst.getCategory(), modeMap.get(inst.getCategory()) + 1);
				else
					modeMap.put(inst.getCategory(), 1);
			}

			// Extract mode instance
			int mode = 0;
			int modeCount = 0;
			for(Map.Entry<Integer, Integer> entry : modeMap.entrySet()){
				if(entry.getValue() > modeCount){
					modeCount = entry.getValue();
					mode = entry.getKey();
				}
			}

			// leaf containing name and prob of majority class (random choice if equal)
			return new Leaf(mode, (double)modeCount / instances.size());
		}

		double bestPurity = Double.POSITIVE_INFINITY;
		List<Instance> bestTrueSet = new ArrayList<Instance>();
		List<Instance> bestFalseSet = new ArrayList<Instance>();
		int bestAttrIndex = 0;
		for(int i = 0; i < attributes.size(); i++){
			// seperate into two sets: ones where attr is false and ones where attr is true
			List<Instance> trueSet = new ArrayList<Instance>();
			List<Instance> falseSet = new ArrayList<Instance>();
			for(Instance inst : instances){
				if(inst.getAtt(i)) trueSet.add(inst);
				else falseSet.add(inst);
			}
			// compute purity of each set
			double truePurity = purity(trueSet);
			double falsePurity = purity(falseSet);

			// calculate weighted average purity of the set
			double weightedPurity = (truePurity * (trueSet.size() / instances.size())) +
					(falsePurity * (falseSet.size() / instances.size()));

			// if weighted average purity of sets is best so far record it
			if(weightedPurity < bestPurity){
				bestAttrIndex = i;
				bestTrueSet = trueSet;
				bestFalseSet = falseSet;
			}
		}
		// Remove the selected attribute from the attribute set
		attributes = new ArrayList<String>(attributes);
		attributes.remove(bestAttrIndex);

		// build subtree using remaining attributes
		Node left = constructTree(bestTrueSet, attributes);
		Node right = constructTree(bestFalseSet, attributes);

		// return a new node (best attr, left, right)
		return new InnerNode(bestAttrIndex, left, right);
	}

	/**
	 * Uses the P(A)P(B) purity measure to calculate the weighted average purity
	 * of the set of provided instances
	 * @param instances
	 * @return
	 */
	private double purity(List<Instance> instances){
		double aliveCount = 0;
		double deadCount = 0;
		for(int i = 0; i < instances.size(); i++){
			if(instances.get(i).getCategory() == 0) aliveCount++;
			else deadCount++;
		}
		return (aliveCount / instances.size()) * (deadCount / instances.size());
	}

	/**
	 * Small helper class to determine if all the instances are of the same category
	 * @param instances
	 * @return
	 */
	private boolean homogeneous(List<Instance> instances){
		int firstCategory = instances.get(0).getCategory();
		for(int i = 1; i < instances.size(); i++)
			if(instances.get(i).getCategory() != firstCategory)
				return false;

		return true;
	}

	/**
	 * Runs each testing instance through the constructed decision tree and
	 * print the results.
	 */
	public void test(){
		int count = 0;
		int correct = 0;
		int countLive = 0;
		int countDead = 0;
		int correctLive = 0;
		int correctDead = 0;
		for(Instance testInst : helperTesting.allInstances){
			count++;
			countLive += testInst.getCategory() == 0 ? 1 : 0;
			countDead += testInst.getCategory() == 1 ? 1 : 0;

			if(evaluateInstance(testInst, tree) == testInst.getCategory()){
				correctLive += testInst.getCategory() == 0 ? 1 : 0;
				correctDead += testInst.getCategory() == 1 ? 1 : 0;
				correct++;
			}
		}
		System.out.printf("%d/%d = %.2f%%\n", correct, count, 100*(double)correct/count);
		System.out.printf("Live: %d/%d = %.2f%%\n", correctLive, countLive, (double)correctLive / countLive);
		System.out.printf("Die: %d/%d = %.2f%%\n", correctDead, countDead, (double)correctDead / countDead);
	}

	private int evaluateInstance(Instance instance, Node tree){
		if(tree instanceof Leaf)
			return ((Leaf)tree).category;

		InnerNode inner = (InnerNode)tree;
		if(instance.getAtt(inner.attribute))
			return evaluateInstance(instance, inner.left);

		return evaluateInstance(instance, inner.right);
	}

	public static void main(String[] args){
		// Ensure we have the right arguments
		if(args.length < 2 || args[0] == null || args[1] == null){
			System.err.println("Insufficient arguments, should have a training set name and a test set name.");
			return;
		}

		new DecisionTreeClassifier(args[0], args[1]).test();
	}

}
