package com.ggx.spring.common.base.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.ggx.core.common.future.GGXFuture;

import reactor.core.publisher.Mono;

/**
 * 基础service接口
 *
 * @author zai
 * 2020-11-12 14:28:20
 */
public interface BaseService {
	
	/**
	 * 订阅并返回GGXFuture
	 *
	 * @param <T>
	 * @param mono
	 * @return
	 * @author zai
	 * 2020-11-12 14:27:57
	 */
	<T> GGXFuture<T> subscribeForFuture(Mono<T> mono);

	/**
	 * 保存或更新记录
	 *
	 * @param obj
	 * @author zai 2020-11-10 17:05:08
	 */
	void save(Object obj);
	
	/**
	 * 保存所有记录
	 *
	 * @param objs
	 * 2020-11-30 11:55:17
	 */
	<T> void saveAll(List<? extends T> objs);

	/**
	 * 根据uid获取
	 *
	 * @param <T>
	 * @param uid
	 * @param clazz
	 * @return
	 * @author zai 2020-11-10 17:05:25
	 */
	<T> T getByUid(String uid, Class<T> clazz);
	
	/**
	 * 根据uid获取
	 *
	 * @param <T>
	 * @param uid
	 * @param clazz
	 * @return
	 * @author zai 2020-11-10 17:05:25
	 */
	<T> T getByUid(ObjectId uid, Class<T> clazz);
	
	/**
	 * 根据uid删除
	 *
	 * @param uid
	 * @param clazz
	 * 2020-11-25 17:53:47
	 */
	void deleteByUid(ObjectId uid, Class<?> clazz);

	/**
	 * 根据字符串uidlist批量获取
	 *
	 * @param <T>
	 * @param uidList
	 * @param clazz
	 * @return
	 * @author zai 2020-11-10 21:59:00
	 */
	<T> List<T> getByStringUidList(List<String> uidList, Class<T> clazz);

	/**
	 * 根据uidlist批量获取
	 *
	 * @param <T>
	 * @param uidList
	 * @param clazz
	 * @return
	 * @author zai 2020-11-10 21:59:00
	 */
	<T> List<T> getByUidList(List<ObjectId> uidList, Class<T> clazz);
	
	/**
	 * 根据uid删除
	 *
	 * @param <T>
	 * @param uid
	 * @param clazz
	 * @return
	 * @author zai 2020-11-10 17:05:33
	 */
	<T> long deleteByStringUid(String uid, Class<T> clazz);

	/**
	 * 批量根据uid删除
	 *
	 * @param <T>
	 * @param uidList
	 * @param clazz
	 * @return
	 * @author zai 2020-11-10 17:09:40
	 */
	<T> long deleteByStringUidList(List<String> uidList, Class<T> clazz);
	
	/**
	 * 批量根据uid删除
	 *
	 * @param <T>
	 * @param uidList
	 * @param clazz
	 * @return
	 * @author zai 2020-11-22 09:36:36
	 */
	<T> long deleteByUidList(List<ObjectId> uidList, Class<T> clazz);

	

}