package com.pekka.service;

import java.util.List;

import com.pekka.common.pojo.EasyUITreeNote;

/**
 * 商品类目服务接口
 * 
 * @author Lenovo-昱树临风
 *
 */
public interface ItemCatService {
	/**
	 * 获取商品类目列表
	 * 
	 * @param parentId
	 *            父节点id
	 * @return
	 */
	List<EasyUITreeNote> getItemCatList(Long parentId);
}
