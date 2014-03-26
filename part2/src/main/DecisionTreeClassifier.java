package main;

import java.util.List;

import main.Helper.Instance;
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

	private Node constructTree(List<Instance> instances, List<String> attributes){
		if(instances.isEmpty())
			return null; // return leaf with name of most probably class and it's prob (baseline predictor
		if(pure(instances))
			return null; // leaf with name of all instances and prob 1
		if(attributes.size() == 0) // TODO makes sense??
			return null; // leaf containing name and prob of majority class (random choice if equal)

		for(String attribute : attributes){
			// seperate into two sets: ones where attr is false and ones where attr is true
			// compute purity of each set
			// if weighted average purity of sets is best so far:
				// bestAtt = current attr
				// bestInstTrue = set of true instances
				// bestInstFalse = set of false instances
		}
		// build subtree using remaining attributes
		// left = buildnode(bestInstTrue, attributes - best attr)
		// right = buildNode(bestInstFalse, attributes - bestAttr)

		// return a new node (best attr, left, right)
		return null;
	}

	private boolean pure(List<Instance> instances){
		return false;
	}

	public static void main(String[] args){
		// Ensure we have the right arguments
		if(args.length < 2 || args[0] == null || args[1] == null){
			System.err.println("Insufficient arguments, should have a training set name and a test set name.");
			return;
		}

		new DecisionTreeClassifier(args[0], args[1]);
	}

}
