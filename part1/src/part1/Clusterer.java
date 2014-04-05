package part1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Clusterer {

	private List<Instance> instances;
	private List<Instance> cluster1, cluster2, cluster3;
	private double[] ranges;

	public Clusterer(Path trainingSet) throws IOException{

		instances = new ArrayList<Instance>();

		List<String> instanceLines = Files.readAllLines(trainingSet, StandardCharsets.UTF_8);

		// Determine the number of attributes per instance
		int numAttrs = instanceLines.get(0).split("\\s+").length - 1; //TODO catch null pointer if empty

		// Create arrays for calculating attribute ranges
		double[] min = new double[numAttrs];
		double[] max = new double[numAttrs];

		// Initialise the min & max arrays
		for(int i = 0; i > numAttrs; i++){
			min[i] = Double.POSITIVE_INFINITY;
			max[i] = Double.NEGATIVE_INFINITY;
		}

		this.instances = new ArrayList<Instance>();

		// Extract the instances
		for(String line : instanceLines){
			if(line.matches("^\\s*$")) continue;
			Instance inst = new Instance(line);
			this.instances.add(inst);

			// Update the min and max for each attribute
			double[] attrs = inst.attributes;
			for(int i = 0; i < numAttrs; i++){
				min[i] = attrs[i] < min[i] ? attrs[i] : min[i];
				max[i] = attrs[i] > max[i] ? attrs[i] : max[i];
			}
		}

		// Calculate the attribute ranges from the min's and max's
		ranges = new double[numAttrs];
		for(int i = 0; i < numAttrs; i++) ranges[i] = max[i] - min[i];

		// Initialise clusters
		cluster1 = new ArrayList<Instance>();
		cluster2 = new ArrayList<Instance>();
		cluster3 = new ArrayList<Instance>();

		// Select initial means (0th index of each cluster is the current mean)
		Random random = new Random();
		cluster1.add(instances.remove(random.nextInt(instances.size())));
		cluster2.add(instances.remove(random.nextInt(instances.size())));
		cluster3.add(instances.remove(random.nextInt(instances.size())));

		Instance mean1, mean2, mean3;
		
		int i = 0;
		do{
			mean1 = cluster1.get(0);
			mean2 = cluster2.get(0);
			mean3 = cluster3.get(0);

			cluster();
			cluster1 = chooseNewMean(cluster1);
			cluster2 = chooseNewMean(cluster2);
			cluster3 = chooseNewMean(cluster3);
			i++;
		}while((!mean1.equals(cluster1.get(0)) &&
				!mean2.equals(cluster2.get(0)) &&
				!mean3.equals(cluster3.get(0))) ||
				i > 1000);
		
		cluster();
		System.out.println("Cluster1:");
		for(Instance inst : cluster1)
			System.out.println("\t" + inst);
		
		System.out.println("Cluster2:");
		for(Instance inst : cluster2)
			System.out.println("\t" + inst);
		
		System.out.println("Cluster3:");
		for(Instance inst : cluster3)
			System.out.println("\t" + inst);
		
	}

	/**
	 * Classifies instances in the instances list into their respective clusters.
	 * The clustering is chosen by Euclidean distance to the 0th element of each
	 * cluster list.
	 */
	private void cluster(){
		for(Instance inst : instances){
			// Determine distances to each mean
			double dist1 = inst.distanceTo(cluster1.get(0), ranges);
			double dist2 = inst.distanceTo(cluster2.get(0), ranges);
			double dist3 = inst.distanceTo(cluster3.get(0), ranges);

			// Place in closes cluster
			if(dist1 < dist2 && dist1 < dist3){ // Closest to cluster 1
				cluster1.add(inst);
			}
			else if(dist2 < dist1 && dist2 < dist3){ // Closest to cluster 2
				cluster2.add(inst);
			}
			else{ // Closest to cluster 3
				cluster3.add(inst);
			}
		}

		instances = new ArrayList<Instance>();
	}

	/**
	 * Select the new means for each cluster (centroid). (Will remove every instance from each 
	 * cluster and add the mean instance for each as their only element)
	 */
	private List<Instance> chooseNewMean(List<Instance> cluster){
		double[] centroid = centroid(cluster);
		double distToCentroid = Double.MAX_VALUE;
		Instance centroidInst = cluster.get(0);

		for(Instance inst : cluster){
			if(inst.distanceTo(centroid, ranges) < distToCentroid){
				distToCentroid = inst.distanceTo(centroid, ranges);
				centroidInst = inst;
			}
		}

		// Put all the instances apart from the centroid back into the instances list
		cluster.remove(centroidInst);
		instances.addAll(cluster);
		cluster = new ArrayList<Instance>();
		cluster.add(centroidInst);
		return cluster;
	}

	private double[] centroid(List<Instance> cluster){
		double[] sum = new double[cluster.get(0).attributes.length];
		for(Instance inst : cluster)
			for(int i = 0; i < inst.attributes.length; i++){
				sum[i] += inst.attributes[i];
			}

		double[] result = new double[sum.length];
		for(int i = 0; i < sum.length; i++)
			result[i] = sum[i] / cluster.size();

		return result;
	}
}
