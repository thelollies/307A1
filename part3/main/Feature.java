package main;

import java.util.Random;

public class Feature {
	public final int[] row;
	public final int[] col;
	public final boolean[] sgn;
	
	public final boolean dummy;
	
	public Feature(int xRange, int yRange){
		row = new int[4];
		col = new int[4];
		sgn = new boolean[4];
		dummy = false;
	
		fillRandomly(row, xRange);
		fillRandomly(col, xRange);
		fillRandomly(sgn);
	}
	
	private Feature(){
		row = new int[4];
		col = new int[4];
		sgn = new boolean[4];
		dummy = true;
	}
	
	private void fillRandomly(int[] array, int range){
		Random rand = new Random();
		for(int i = 0; i < array.length; i++)
			array[i] = rand.nextInt(range);
	}
	
	private void fillRandomly(boolean[] array){
		Random rand = new Random();
		for(int i = 0; i < array.length; i++)
			array[i] = rand.nextBoolean();
	}
	
	public static Feature dummy(){
		return new Feature();
	}
	
}
