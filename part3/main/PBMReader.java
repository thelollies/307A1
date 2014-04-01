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

		// Generate the feature list (50 including dummy feature)
		List<Feature> featureList = new ArrayList<Feature>(50);
		featureList.add(Feature.dummy()); // Dummy feature always evaluates to true on comparison
		for(int i = 0; i < 49; i++)
			featureList.add(new Feature(10,10));

		for(PBM pbm : pbmList)
			pbm.determineFeatureValues(featureList);
		
		System.out.println("Done");
	}

}
