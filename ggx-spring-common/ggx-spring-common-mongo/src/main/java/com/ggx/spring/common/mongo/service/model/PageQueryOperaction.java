package com.ggx.spring.common.mongo.service.model;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.ggx.spring.common.base.page.PageQuery;

public interface PageQueryOperaction<Q extends PageQuery, T> {

	void operate(Q pageQuery, Class<T> clazzT, Query listQuery, Query countQuery, List<Criteria> criterias);
}
