package com.ggx.core.common.handler.serializer.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.esotericsoftware.kryo.kryo5.util.Pool;
import com.ggx.core.common.handler.serializer.Serializer;

/**
 * json序列化与反序列化工具
 * 
 * 
 * @author zai 2017-07-28
 */
public class KryoSerializer implements Serializer {

	private static final Pool<Kryo> KRYO_POOL = new Pool<Kryo>(true, false, 1000) {
		protected Kryo create() {
			Kryo kryo = new Kryo();
			kryo.setRegistrationRequired(false);
			return kryo;
		}
	};

	private static final Pool<Output> OUTPUT_POOL = new Pool<Output>(true, false, 1000) {
		protected Output create() {
			return new Output(1024, -1);
		}
	};

	private static final Pool<Input> INPUT_POOL = new Pool<Input>(true, false, 1000) {
		protected Input create() {
			return new Input(1024);
		}
	};

	@Override
	public byte[] serialize(Object message) throws Exception {
		Kryo kryo = KRYO_POOL.obtain();
		Output output = OUTPUT_POOL.obtain();
		kryo.writeObject(output, message);
		byte[] bytes = output.toBytes();
		KRYO_POOL.free(kryo);
		OUTPUT_POOL.free(output);
		return bytes;
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> t) throws Exception {
		Kryo kryo = KRYO_POOL.obtain();
		Input input = INPUT_POOL.obtain();
		input.setBuffer(bytes);
		T readObject = kryo.readObject(input, t);
		INPUT_POOL.free(input);
		KRYO_POOL.free(kryo);
		return readObject;
	}
	/*
	 * public static void main(String[] args) throws Exception { KryoSerializer
	 * serializer = new KryoSerializer(); byte[] bytes1 = serializer.serialize(123);
	 * System.out.println(Arrays.toString(bytes1)); byte[] bytes2 =
	 * serializer.serialize(new int[] { 3, 2, 1 });
	 * System.out.println(Arrays.toString(bytes2)); int[] dbytes2 =
	 * serializer.deserialize(bytes2, int[].class);
	 * System.out.println(Arrays.toString(dbytes2)); byte[] bytes3 =
	 * serializer.serialize(Arrays.asList("1", "2", "3"));
	 * System.out.println(Arrays.toString(bytes3)); List<String> dbytes3 =
	 * serializer.deserialize(bytes3, ArrayList.class); System.out.println(dbytes3);
	 * 
	 * }
	 */
}
