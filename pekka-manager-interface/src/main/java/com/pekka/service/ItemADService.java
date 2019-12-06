package com.pekka.service;

import com.pekka.common.pojo.EasyUIDataGridResult;
import com.pekka.common.pojo.PekkaResult;

public interface ItemADService {

	EasyUIDataGridResult getItemADList(String cName);

	/**
	 * 新增商品广告
	 * 
	 * @param id
	 *            商品id
	 * @return
	 */
	PekkaResult add(Long id, String cName);

	/**
	 * 删除商品广告
	 * 
	 * @param id
	 *            商品id
	 * @return
	 */
	PekkaResult delete(Long[] ids, String cName);

	EasyUIDataGridResult getItemADHotList();
}
