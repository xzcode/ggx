package com.ggx.rpc.common.model;

public interface TestInter {
	
	void save(TestA<?> a);
	
	TestB getTestB();
	
	String ok(int a, long b , String c);

}
