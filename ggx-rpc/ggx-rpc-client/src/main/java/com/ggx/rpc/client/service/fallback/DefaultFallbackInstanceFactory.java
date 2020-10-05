package com.ggx.rpc.client.service.fallback;

public class DefaultFallbackInstanceFactory implements FallbackInstanceFactory {

	@Override
	public Object instant(Class<?> clazz) {
		try {
			if (clazz == Void.class) {
				return null;
			}
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
