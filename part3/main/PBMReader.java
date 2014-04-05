package main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Should read in the PBM's
 * @author Rory Stephenson stepherory 300160212
 *
 */

public class PBMReader {

	public static List<PBM> readImages(Path file) throws IOException{
		List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
		List<PBM> pbmList = new ArrayList<PBM>(100);
		for(int i = 0; i < lines.size(); i+=5){
			String[] dimension = lines.get(i+2).split(" ");
			pbmList.add(new PBM(lines.get(i + 1),
					Integer.parseInt(dimension[0]),
					Integer.parseInt(dimension[1]),
					lines.get(i + 3) + lines.get(i + 4)));
		}

		return pbmList;
	}



	public static void main(String[] args){
		List<PBM> pbmList = null;
		try {
			pbmList = readImages(Paths.get("image.data"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int numFeatures = 50;
		
		Perceptron perceptron = new Perceptron(numFeatures, 0);

		perceptron.preprocess(pbmList);
		
		boolean success = false;
		double accuracy = 0;
		int i;
		for(i = 100; i < 1; i++){
			if((accuracy = perceptron.learnPBMs(pbmList)) == 1 && 
					(accuracy = perceptron.evaluatePBMs(pbmList)) == 1)
			{
				success = true;
				break;
			}
		}
		
		System.out.println("Features selected:");
		for(Feature f : perceptron.getFeatures())
			System.out.println(f);
		
		System.out.printf("Final weights used:\n[");
		for(int j = 0; j < perceptron.getWeights().length; j++){
			System.out.printf("%d",perceptron.getWeights()[j]);
			if(j < perceptron.getWeights().length - 1)
				System.out.printf(", ");
		}
		System.out.println("]");
		
		
		if(success)
			System.out.printf("Achieved 100%% correctness after %d iterations\n", i);
		else
			System.out.printf("%.2f%% accuracy, stopped after %d iterations\n",
					100*accuracy, i);
	}

}
