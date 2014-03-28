package main;
import java.util.*;
import java.io.*;

public class Helper{
	// some bits of java code that you may use if you wish.
	// assumes that the enclosing class has fields:
	int numCategories;
	int numAtts;
	List<String> categoryNames;
	List<String> attNames;
	List<Instance> allInstances;
	int mostCommonCategory = 0;
	double mostCommonCatCount = 0;


	public void readDataFile(String fname){
		/* format of names file:
		 * names of categories, separated by spaces
		 * names of attributes
		 * category followed by true's and false's for each instance
		 */
		System.out.println("Reading data from file "+fname);
		try {
			Scanner din = new Scanner(new File(fname));

			categoryNames = new ArrayList<String>();
			for (Scanner s = new Scanner(din.nextLine()); s.hasNext();) categoryNames.add(s.next());
			numCategories=categoryNames.size();
			System.out.println(numCategories +" categories");

			attNames = new ArrayList<String>();
			for (Scanner s = new Scanner(din.nextLine()); s.hasNext();) attNames.add(s.next());
			numAtts = attNames.size();
			System.out.println(numAtts +" attributes");

			allInstances = readInstances(din);
			din.close();
		}
		catch (IOException e) {
			throw new RuntimeException("Data File caused IO exception");
		}
	}


	private List<Instance> readInstances(Scanner din){
		/* instance = classname and space separated attribute values */
		List<Instance> instances = new ArrayList<Instance>();
		Map<Integer, Integer> modeMap = new HashMap<Integer, Integer>();
		while (din.hasNext()){
			Scanner line = new Scanner(din.nextLine());
			int cat = categoryNames.indexOf(line.next());
			if(modeMap.get(cat) == null)
				modeMap.put(cat, 1);
			else
				modeMap.put(cat, modeMap.get(cat) + 1);
			instances.add(new Instance(cat,line));
		}

		for(Map.Entry<Integer, Integer> entry : modeMap.entrySet()){
			if(entry.getValue() > mostCommonCatCount){
				mostCommonCatCount = entry.getValue();
				mostCommonCategory = entry.getKey();
			}
		}

		System.out.println("Read " + instances.size()+" instances, most common category: "
				+ categoryNames.get(mostCommonCategory) + "(" + mostCommonCatCount + ")");
		return instances;
	}



	public class Instance {

		private int category;
		private List<Boolean> vals;

		public Instance(int cat, Scanner s){
			category = cat;
			vals = new ArrayList<Boolean>();
			while (s.hasNextBoolean()) vals.add(s.nextBoolean());
		}

		public boolean getAtt(int index){
			return vals.get(index);
		}

		public int getCategory(){
			return category;
		}

		public String toString(){
			StringBuilder ans = new StringBuilder(categoryNames.get(category));
			ans.append(" ");
			for (Boolean val : vals)
				ans.append(val?"true  ":"false ");
			return ans.toString();
		}

	}
}
