package main;

import java.util.List;
import java.util.Random;

public class Perceptron {

	private int[] weights;
	private final int threshold;
	private final Feature[] features;

	public Perceptron(int numFeatures, int threshold){
		// Generate weights
		weights = new int[numFeatures];
		Random rand = new Random();
		for(int i = 0; i < numFeatures; i++)
			weights[i] = rand.nextInt(3) - 1;

		// Set threshold
		this.threshold = threshold;

		// Generate the feature list (including dummy feature)
		features = new Feature[numFeatures];
		features[0] = Feature.dummy(); // Dummy feature always evaluates to true on comparison
		for(int i = 1; i < numFeatures; i++)
			features[i] = new Feature(10,10);
	}

	public double learnPBMs(List<PBM> pbmList){
		// Pass each pbm through the perceptron and adjust weights
		double correct = 0;
		for(PBM pbm : pbmList){
			boolean right = testInstance(pbm);
			correct += right ? 1 : 0;

			if(right) continue; // Do nothing if instance was correctly evaluated

			// Learn from wrongly classified instance
			if(!pbm.positiveExample){// Negative Example
				int[] features = pbm.featureValues();
				for(int i = 0; i < weights.length; i++)
					weights[i] = weights[i] - features[i];	
			}
			else{ // Positive Example
				int[] features = pbm.featureValues();
				for(int i = 0; i < weights.length; i++)
					weights[i] = weights[i] + features[i];
			}
		}

		return correct / pbmList.size();
	}

	public double evaluatePBMs(List<PBM> pbmList){
		// Pass each pbm through the perceptron
		double correct = 0;
		for(PBM pbm : pbmList)
			correct += testInstance(pbm) ? 1 : 0;

		return correct / pbmList.size();
	}

	private boolean testInstance(PBM pbm){
		int sum = 0;
		for(int i = 0; i < pbm.featureValues().length; i++)
			sum += pbm.featureValues()[i] * weights[i];

		return (sum > threshold) == pbm.positiveExample;
	}

	public int[] getWeights(){
		return weights;
	}

	/** Stores a list of feature evaluation values for each pbm
	 *  provided so that they do not need to be calculated again.
	 * @param pbmList
	 */
	public void preprocess(List<PBM> pbmList) {
		for(PBM pbm : pbmList)
			pbm.determineFeatureValues(features);
	}
	
	public Feature[] getFeatures(){
		return features;
	}

}
