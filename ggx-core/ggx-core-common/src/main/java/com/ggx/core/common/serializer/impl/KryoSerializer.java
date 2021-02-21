package com.ggx.core.common.serializer.impl;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.esotericsoftware.kryo.kryo5.util.Pool;
import com.ggx.core.common.serializer.Serializer;

/**
 * json序列化与反序列化工具
 * 
 * 
 * @author zai 2017-07-28
 */
public class KryoSerializer implements Serializer {

	private int poolSize = Runtime.getRuntime().availableProcessors() * 2;

	private Pool<Kryo> kryoPool;

	private Pool<Output> outputPool;

	private Pool<Input> inputPool;

	public KryoSerializer() {
		init();
	}

	public KryoSerializer(int poolSize) {
		this.poolSize = poolSize;
		init();
	}

	private void init() {
		kryoPool = new Pool<Kryo>(true, false, poolSize) {
			protected Kryo create() {
				Kryo kryo = new Kryo();
				kryo.setRegistrationRequired(false);
				return kryo;
			}
		};

		outputPool = new Pool<Output>(true, false, poolSize) {
			protected Output create() {
				return new Output(1024, -1);
			}
		};

		inputPool = new Pool<Input>(true, false, poolSize) {
			protected Input create() {
				return new Input(1024);
			}
		};
	}

	@Override
	public byte[] serialize(Object message) throws Exception {
		Kryo kryo = kryoPool.obtain();
		Output output = outputPool.obtain();
		kryo.writeObject(output, message);
		byte[] bytes = output.toBytes();
		kryoPool.free(kryo);
		outputPool.free(output);
		return bytes;
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> t) throws Exception {
		Kryo kryo = kryoPool.obtain();
		Input input = inputPool.obtain();
		input.setBuffer(bytes);
		T readObject = kryo.readObject(input, t);
		inputPool.free(input);
		kryoPool.free(kryo);
		return readObject;
	}
}
