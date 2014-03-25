package part1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Classifier {

	private List<Instance> trainingSet;
	private List<Instance> testSet;
	private final int k; // Number of neighbors to assess in nearest-neighbors method
	private final double[] ranges; // The ranges of the training set attributes
	private final int numAttrs; // The number of attributes per instance

	public Classifier(Path trainingSet, Path testSet, int k) throws IOException{

		// Set the number of neighbors
		this.k = k;
		
		// Read the trainingSet file into memory
		List<String> trainingLines = Files.readAllLines(trainingSet, StandardCharsets.UTF_8);
		
		// Determine the number of attributes per instance
		numAttrs = trainingLines.get(0).split("\\s+").length - 1; //TODO catch null pointer if empty
		
		// Create arrays for calculating attribute ranges
		double[] min = new double[numAttrs];
		double[] max = new double[numAttrs];
		
		// Initialise the min & max arrays
		for(int i = 0; i > numAttrs; i++){
			min[i] = Double.POSITIVE_INFINITY;
			max[i] = Double.NEGATIVE_INFINITY;
		}
		
		this.trainingSet = new ArrayList<Instance>();

		// Extract the instances
		for(String line : trainingLines){
			if(line.matches("^\\s*$")) continue;
			Instance inst = new Instance(line);
			this.trainingSet.add(inst);

			
			// Update the min and max for each attribute
			double[] attrs = inst.attributes;
			for(int i = 0; i < numAttrs; i++){
				min[i] = attrs[i] < min[i] ? attrs[i] : min[i];
				max[i] = attrs[i] > max[i] ? attrs[i] : max[i];
			}
		}
		
		// Calculate the attribute ranges from the min's and max's
		double[] tempRanges = new double[numAttrs];
		for(int i = 0; i < numAttrs; i++) tempRanges[i] = max[i] - min[i];
		ranges = tempRanges;
		
		// Read the testSet file into memory
		List<String> testLines = Files.readAllLines(testSet, StandardCharsets.UTF_8);
		this.testSet = new ArrayList<Instance>();
		for(String line : testLines){ 
			if(line.matches("^\\s*$")) continue;
			this.testSet.add(new Instance(line));
		}
		
	}
	
	public String classify(){
		
		// String for delivering results
		StringBuilder results = new StringBuilder("Results:\n");
		double correct = 0;
		double total = 0;

		// Evaluate classifier performance against each instance in test set
		for(Instance testInst : testSet){
			// Order a copy of trainingSet by distance to test instance
			Instance[] trainingArray = trainingSet.toArray(new Instance[trainingSet.size()]);
			Arrays.sort(trainingArray, new InstanceComparator(testInst, ranges));
			
			// Create a map of Classification -> count of first k neighbours
			Map<String, Integer> classMap = new HashMap<String, Integer>();
			for(int i = 0; i < k; i++){
				String classification = trainingArray[i].classification;
				
				// Add the class to the map if it isn't in there, otherwise increment
				// its count
				if(classMap.get(classification) == null)
					classMap.put(classification, 1);
				else
					classMap.put(classification, classMap.get(classification) + 1);
				
			}
			
			// Determine the most-chosen 'nearest neighbor'
			String chosenClass = "";
			int classCount = 0;
			for(Map.Entry<String, Integer> entry : classMap.entrySet()){
				if(entry.getValue() > classCount){
					classCount = entry.getValue();
					chosenClass = entry.getKey();
				}
			}
			
			// Increment counters
			total++;
			if(testInst.classification.equals(chosenClass)) correct++;
			
			// Record result
			results.append(String.format("Classified %s as %s \n",testInst.classification, chosenClass));
		}
		
		results.append(String.format("%.0f/%.0f = %.2f%% correct\n", correct, total, (100*correct)/total));
		return results.toString();
	}
	
	/**
	 * A comparator for Instances which orders instances
	 * based on their distance to the instance provided in the constructor.
	 * @author Rory Stephenson 300160212
	 *
	 */
	private class InstanceComparator implements Comparator<Instance>{

		private final Instance compInstance;
		private final double[] range;
		
		public InstanceComparator(Instance compInstance, double[] range){
			this.compInstance = compInstance;
			this.range = range;
		}
		
		@Override
		public int compare(Instance arg0, Instance arg1) {
			return compInstance.distanceTo(arg0, range).compareTo(compInstance.distanceTo(arg1, range));
		}
		
	}

	public static void main(String[] args){

		// Ensure we have the right arguments
		if(args.length < 2 || args[0] == null || args[1] == null){
			System.err.println("Insufficient arguments, should have a training set name and a test set name.");
			return;
		}

		// File variables
		Path trainingSet = Paths.get(args[0]);
		Path testSet = Paths.get(args[1]);

		try{
			// Determine k, the number of neighbours to evaluate
			int k = args.length > 2 ? Integer.parseInt(args[2]) : 1;
			System.out.println(new Classifier(trainingSet, testSet, k).classify());
		}catch(IOException e){
			System.err.println("One of the specified files does not exist or is in an improper"
					+ "format which led to the following error:");
			e.printStackTrace();
		}
		
	}

}
