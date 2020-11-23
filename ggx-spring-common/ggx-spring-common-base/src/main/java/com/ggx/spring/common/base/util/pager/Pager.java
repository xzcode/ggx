package com.ggx.spring.common.base.util.pager;

import java.util.List;

import com.ggx.spring.common.base.util.pager.converter.PagerConverter;
import com.ggx.spring.common.base.util.pager.converter.PagerItemConverter;

public class Pager<T> {

	private Integer total = 0;// 总记录数
	private Integer pages = 0;// 总页数
	private Integer pageNo = 1;// 当前页
	private Integer pageSize = 10;// 每页记录数
	private Integer next = 1;// 下一页
	private Integer prev = 1;// 上一页
	private boolean lastPage = true;// 是否最后一页
	private List<T> items;// 当前页数据

	public Pager() {

	}

	public Pager(Integer total, List<T> items) {
		super();
		this.total = total;
		this.items = items;
	}

	public Pager(Integer pageSize, Integer total, List<T> items) {
		this.total = total;
		this.pageSize = pageSize;
		this.items = items;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		if (total != null) {
			this.total = total;
		}
	}

	public Integer getPages() {
		if (this.pages == 0) {
			if (total != 0 && pageSize != 0) {
				this.pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
			} else {
				if (getTotal() == 0) {
					this.pages = 0;
				} else {
					this.pages = 1;
				}
			}
		}
		return this.pages;
	}
	

	/**
	 * 转换为目标分页器
	 *
	 * @param <E>
	 * @param targetItemClazz
	 * @return
	 * @author zai
	 * 2020-11-22 12:10:44
	 */
	public <E> Pager<E> transferPager(Class<E> targetItemClazz) {
		return PagerTransferUtil.transferPager(this, targetItemClazz);
	}
	
	/**
	 * 转换为目标分页器
	 *
	 * @param <E>
	 * @param targetItemClazz
	 * @param itemConverter
	 * @return
	 * @author zai
	 * 2020-11-22 12:10:59
	 */
	public <E> Pager<E> transferPager( Class<E> targetItemClazz, PagerItemConverter<T, E> itemConverter) {
		return PagerTransferUtil.transferPager(this, targetItemClazz, itemConverter);
	}
	
	/**
	 * 转换为目标分页器
	 *
	 * @param <E>
	 * @param conveter
	 * @return
	 * @author zai
	 * 2020-11-22 12:11:03
	 */
	public <E> Pager<E>  transferPager(PagerConverter<T, E> conveter) {
		return PagerTransferUtil.transferPager(this, conveter);
	}

	public void setPages(Integer pages) {
		if (pages != null) {
			this.pages = pages;
		}
	}

	public Integer getPageNo() {
		if (pageNo == 0 && getTotal() != 0) {
			this.pageNo = 1;
		}
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		if (pageNo != null) {
			this.pageNo = pageNo;
		}
	}

	public Integer getNext() {
		if (this.next == 1) {
			if (getPageNo() < getPages()) {
				this.next = pageNo + 1;
			} else {
				this.next = getPages();
			}
		}
		return this.next;
	}

	public void setNext(Integer next) {
		if (next != null) {
			this.next = next;
		}
	}

	public Integer getPrev() {
		if (this.next == 1) {
			if (getPageNo() > 1) {
				this.prev = pageNo - 1;
			} else {
				this.prev = 1;
			}
		}
		return prev;
	}

	public void setPrev(Integer prev) {
		if (prev != null) {
			this.prev = prev;
		}
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize != null) {
			this.pageSize = pageSize;
		}
	}

	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}

	public boolean isLastPage() {
		return getLastPage();
	}

	public boolean getLastPage() {

		if (pages == 0 || pageNo.intValue() >= pages.intValue()) {
			this.lastPage = true;
		} else {
			this.lastPage = false;
		}
		return this.lastPage;

	}
	

}