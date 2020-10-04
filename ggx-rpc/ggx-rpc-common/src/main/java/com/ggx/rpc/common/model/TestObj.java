package com.ggx.rpc.common.model;

public class TestObj implements TestInter {

	@Override
	public void save(TestA<?> a) {
		System.out.println("saved testA!");
	}

	@Override
	public TestB getTestB() {
		return new TestB("xxxB", 132);
	}

	@Override
	public String ok(int a, long b, String c) {
		return "" + a + b + c;
	}

}
