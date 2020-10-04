package com.ggx.rpc.common.model;

public class TestFallbackObj implements TestInter {

	@Override
	public void save(TestA<?> a) {
		System.out.println("fallback -- save");
	}

	@Override
	public TestB getTestB() {
		System.out.println("fallback -- getTestB");
		return new TestB("fallback", 0);
	}

	@Override
	public String ok(int a, long b, String c) {
		System.out.println("fallback -- ok");
		return "fallback";
	}

}
