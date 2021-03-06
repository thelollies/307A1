package main;

import java.util.List;

public class PBM {
	public final boolean positiveExample;
	public final boolean[][] image;
	private int[] featureValues;
	
	public PBM(String comment, int width, int height, String data){
		positiveExample = comment.equals("#Yes");
		boolean[][] temp = new boolean[height][width];

		char[] charArray = data.toCharArray();

		for(int row = 0; row < width; row++)
			for(int col = 0; col < height; col++)
				temp[row][col] = Character.getNumericValue(charArray[(row*width)+col]) == 1;

		this.image = temp;
	}

	public void determineFeatureValues(Feature[] featureList){
		featureValues = new int[featureList.length];
		for(int i = 0; i < featureList.length; i++)
			featureValues[i] = testFeature(featureList[i]);
	}
	
	public int testFeature(Feature f) {

		if(f.dummy) return 1;
		
		int sum=0;
		
		for(int i=0; i < 4; i++)
			if (image[f.row[i]][f.col[i]]==f.sgn[i]) sum++;
		
		return (sum >= 3) ? 1 : 0;
	}
	
	public int[] featureValues(){
		return featureValues;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder((positiveExample == true ? "#Yes" : "#other") + "\n");
		for(int row = 0; row < image.length; row++){
			for(int col = 0; col < image[0].length; col++)
				str.append(image[row][col]);
			
			str.append("\n");
		}
		return str.toString();
	}
	
}
