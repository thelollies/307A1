package part1;

import java.security.InvalidParameterException;

public class Instance {

	public final double[] attributes;  
	public final String classification;

	public Instance(String rawLine){
		// Separate the line by whitespace
		String[] splitLine = rawLine.split("\\s+");

		// Extract the instance's classification name
		classification = splitLine[splitLine.length - 1];

		// Convert the String attributes to doubles
		double[] attrs = new double[splitLine.length - 1];
		for(int i = 0; i < splitLine.length - 1; i++)
			attrs[i] = Double.parseDouble(splitLine[i]);

		// Store the attributes
		attributes = attrs;
	}

	public Double distanceTo(Instance other, double[] range){

		// Ensure the instances have the same number of attributes
		if(other.attributes.length != attributes.length) 
			throw new InvalidParameterException("Instance attribute counts do not match.");

		// Ensure the number of ranges matches the number of attributes
		if(range.length != attributes.length)
			throw new InvalidParameterException("Number of ranges does not match number of attributes.");

		// Determines the distance between the instances using weighted Euclidean distance
		double distance = 0.0;
		for(int i = 0; i < this.attributes.length; i++)
			distance += Math.pow((attributes[i] - other.attributes[i]), 2) / Math.pow(range[i], 2);

		return distance;
	}
	
}
