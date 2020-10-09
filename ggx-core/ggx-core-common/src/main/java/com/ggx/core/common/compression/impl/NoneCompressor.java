package com.ggx.core.common.compression.impl;

import com.ggx.core.common.compression.Compressor;

/**
 * 
 * 无用的压缩器
 * 
 * @author zai
 * 2019-11-10 21:58:29
 */
public class NoneCompressor implements Compressor{
	
	@Override
	public byte[] compress(byte[] data) {
		return data;
	}

	@Override
	public byte[] uncompress(byte[] data) {
		return data;
	}
	

}
