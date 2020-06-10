package com.ggx.core.common.encryption.random;

public class CustomRandom {
	
	private long seed;
	
	private static final int NUM_A = 9301;
	private static final int NUM_B = 49297;
	private static final int NUM_C = 233280;
	
	
	
	public CustomRandom() {
		this(System.nanoTime());
	}

	public CustomRandom(long seed) {
		this.seed = seed;
	}

	public int nextInt(int max) {
		return this.nextInt(0, max);
	}
	
	public int nextInt(int min, int max) {
		seed= (seed * NUM_A + NUM_B) % NUM_C;
		double rnd = (seed / (1.0 * NUM_C));
		return (int)(min + rnd * (max - min)) ; 
	}
	

}
