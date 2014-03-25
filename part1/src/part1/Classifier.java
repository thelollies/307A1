package part1;

import java.io.File;
public class Classifier {

	public Classifier(File trainingSet, File testSet){

		// TODO does it have to be a general read (i.e. figure out the number of variants and go from there?")
		// TODO read the training data into an appropriate data structure
	}

	public static void main(String[] args){

		// Ensure we have the right arguments
		if(args.length < 2 || args[0] == null || args[1] == null){
			System.err.println("Insufficient arguments, should have a training set name and a test set name.");
			return;
		}

		// File variables
		File trainingSet = new File(args[0]);
		File testSet = new File(args[1]);

		// Make sure training set exists
		if(!trainingSet.exists()){
			System.err.printf("Training set file %s does not exist.", trainingSet);
			return;
		}

		// Make sure test set exists
		if(!testSet.exists()){
			System.err.printf("Test set file %s does not exist.", testSet);
			return;
		}

		new Classifier(trainingSet, testSet);

	}

}
