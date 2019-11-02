package com.pekka.service;

import com.pekka.common.pojo.EasyUIDataGridResult;
import com.pekka.common.pojo.PekkaResult;

public interface ItemADService {

	EasyUIDataGridResult getItemADList(Long adId);

	/**
	 * 新增商品广告
	 * 
	 * @param id
	 *            商品id
	 * @return
	 */
	PekkaResult add(Long id, Long adId);

	/**
	 * 删除商品广告
	 * 
	 * @param id
	 *            商品id
	 * @return
	 */
	PekkaResult delete(Long[] ids);
}
