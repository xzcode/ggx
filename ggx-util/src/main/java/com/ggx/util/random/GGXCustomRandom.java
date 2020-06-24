package com.ggx.util.random;

/**
 * 自定义随机数生产类
 *
 * @author zai
 * 2020-06-10 18:39:18
 */
public class GGXCustomRandom {
	
	/**
	 * 随机种子
	 */
	private long seed;
	
	private static final int NUM_A = 9301;
	private static final int NUM_B = 49297;
	private static final int NUM_C = 233280;
	
	
	
	public GGXCustomRandom() {
		this(System.nanoTime());
	}

	public GGXCustomRandom(long seed) {
		this.seed = seed;
	}

	/**
	 * 获取下一个int随机数
	 *
	 * @param max 可以被随机到的最大值，不包含此值
	 * @return
	 * @author zai
	 * 2020-06-10 18:39:43
	 */
	public int nextInt(int max) {
		return this.nextInt(0, max);
	}
	
	/**
	 * 获取下一个int随机数
	 *
	 * @param min 可以被随机到的最小值，包含此值
	 * @param max 可以被随机到的最大值，不包含此值
	 * @return
	 * @author zai
	 * 2020-06-10 18:39:43
	 */
	public int nextInt(int min, int max) {
		seed= (seed * NUM_A + NUM_B) % NUM_C;
		double rnd = (seed / (1.0 * NUM_C));
		return (int)(min + rnd * (max - min)) ; 
	}
	

}
