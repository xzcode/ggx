package com.ggx.spring.common.mongo.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.ggx.core.common.future.GGXDefaultFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.spring.common.base.page.PageQuery;
import com.ggx.spring.common.base.service.BaseService;
import com.ggx.spring.common.base.util.pager.Pager;
import com.ggx.spring.common.mongo.service.model.PageQueryOperaction;
import com.mongodb.client.result.DeleteResult;

import reactor.core.publisher.Mono;

public abstract class AbstractBaseService implements BaseService {

	@Autowired
	protected MongoTemplate mongoTemplate;

	@Autowired
	protected ReactiveMongoTemplate reactiveMongoTemplate;

	public <T> GGXFuture<T> subscribeForFuture(Mono<T> mono) {
		GGXDefaultFuture<T> future = new GGXDefaultFuture<>();
		mono.switchIfEmpty(Mono.create(o -> {
			future.setSuccess(true);
			future.setDone(true);
		})).subscribe(s -> {
			future.setSuccess(true);
			future.setData(s);
			future.setDone(true);
		}, err -> {
			future.setSuccess(false);
			future.setDone(true);
			future.setCause(err);
		});
		return future;
	}
	
	public <Q extends PageQuery, T> Pager<T> page(Q pageQuery, Class<T> clazzT,  PageQueryOperaction<Q,T> pageOper) {

		Query listQuery = new Query();
		Query countQuery = new Query();

		Criteria criteria = new Criteria();

		List<Criteria> criterias = new ArrayList<>();
		
		pageOper.operate(pageQuery, clazzT, listQuery, countQuery, criterias);

		listQuery.limit(pageQuery.getPageSize());
		listQuery.skip((pageQuery.getPageNo() - 1) * pageQuery.getPageSize());
		listQuery.allowSecondaryReads();
		if (criterias.size() > 0) {
			listQuery.addCriteria(criteria);
			countQuery.addCriteria(criteria);
			criteria.andOperator(criterias.toArray(new Criteria[criterias.size()]));
		}

		List<T> list = this.mongoTemplate.find(listQuery, clazzT);
		long total = this.mongoTemplate.count(countQuery, clazzT);

		Pager<T> pager = new Pager<>();
		pager.setTotal((int) total);
		pager.setItems(list);

		return pager;
	}

	@Override
	public void save(Object obj) {
		this.mongoTemplate.save(obj);
	}

	@Override
	public <T> T getByUid(String uid, Class<T> clazz) {
		return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(new ObjectId(uid))), clazz);
	}

	@Override
	public <T> T getByUid(ObjectId uid, Class<T> clazz) {
		return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(uid)), clazz);
	}

	@Override
	public <T> List<T> getByStringUidList(List<String> uidList, Class<T> clazz) {
		if (uidList == null || uidList.size() == 0) {
			return null;
		}
		List<ObjectId> list = new ArrayList<>();
		for (String uidStr : uidList) {
			list.add(new ObjectId(uidStr));
		}
		List<T> returnList = mongoTemplate.find(Query.query(Criteria.where("_id").in(list)), clazz);
		return returnList;
	}
	
	@Override
	public <T> List<T> getByUidList(List<ObjectId> uidList, Class<T> clazz) {
		return mongoTemplate.find(Query.query(Criteria.where("_id").in(uidList)), clazz);
	}

	@Override
	public <T> long deleteByStringUid(String uid, Class<T> clazz) {
		DeleteResult deleteResult = mongoTemplate.remove(Query.query(Criteria.where("_id").is(new ObjectId(uid))),
				clazz);
		return deleteResult.getDeletedCount();
	}
	
	@Override
	public <T> long deleteByStringUidList(List<String> uidList, Class<T> clazz) {
		if (uidList == null || uidList.size() == 0) {
			return 0;
		}
		List<ObjectId> list = new ArrayList<>();
		for (String uidStr : uidList) {
			list.add(new ObjectId(uidStr));
		}
		DeleteResult deleteResult = mongoTemplate.remove(Query.query(Criteria.where("_id").in(list)), clazz);
		return deleteResult.getDeletedCount();
	}

	@Override
	public <T> long deleteByUidList(List<ObjectId> uidList, Class<T> clazz) {
		DeleteResult deleteResult = mongoTemplate.remove(Query.query(Criteria.where("_id").in(uidList)), clazz);
		return deleteResult.getDeletedCount();
	}

}
