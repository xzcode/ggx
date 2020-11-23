package com.ggx.spring.common.base.page.impl;

import com.ggx.spring.common.base.page.PageQuery;

import io.swagger.annotations.ApiModelProperty;

/**
 * 基础分页查询条件模型
 *
 * @author zai 2020-11-05 18:58:48
 */
public abstract class BasePageQuery implements PageQuery {

	@ApiModelProperty(value = "分页大小")
	protected int pageSize = 10;

	@ApiModelProperty(value = "当前页码")
	protected int pageNo = 1;

	@Override
	public int getPageSize() {
		return pageSize;
	}

	@Override
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public int getPageNo() {
		return pageNo;
	}

	@Override
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

}
