package com.pekka.service;

import java.util.List;

import com.pekka.common.pojo.EasyUIDataGridResult;
import com.pekka.common.pojo.PekkaResult;
import com.pekka.pojo.TbItem;
import com.pekka.pojo.TbItemDesc;

/**
 * 商品服务接口
 * 
 * @author Lenovo-昱树临风
 *
 */
public interface ItemService {
	/**
	 * 查询全部商品
	 * 
	 * @param page
	 *            当前页码
	 * @param rows
	 *            每页显示数量
	 * @return
	 */
	EasyUIDataGridResult getItemList(Integer page, Integer rows);

	/**
	 * 新增商品
	 * 
	 * @param Item
	 *            商品对象(不含描述)
	 * @param itemDesc
	 *            商品描述
	 * @return
	 */
	PekkaResult addItem(TbItem item, String itemDesc);

	/**
	 * 查询商品描述
	 * 
	 * @param itemId
	 *            商品id
	 * @return
	 */
	PekkaResult getItemDesc(Long itemId);

	/**
	 * 查询商品描述
	 * 
	 * @param itemId
	 *            商品id
	 * @return
	 */
	TbItemDesc getItemDescById(Long itemId);

	/**
	 * 更新商品
	 * 
	 * @param Item
	 *            商品对象(不含描述)
	 * @param itemDesc
	 *            商品描述
	 * @return
	 */
	PekkaResult updateItem(TbItem item, String itemDesc);

	/**
	 * 删除商品
	 * 
	 * @param ids
	 *            所有待删除商品的id
	 * @return
	 */
	PekkaResult deleteItem(Long[] ids);

	/**
	 * 下架商品
	 * 
	 * @param ids
	 *            所有待下架的商品id
	 * @return
	 */
	PekkaResult instock(Long[] ids);

	/**
	 * 上架商品
	 * 
	 * @param ids
	 *            所有待上架的商品id
	 * @return
	 */
	PekkaResult reshelf(Long[] ids);

	/**
	 * 通过id获取商品
	 * 
	 * @param itemId
	 * @return
	 */
	TbItem getItemById(Long itemId);

	/**
	 * 获取商品分类名称
	 * 
	 * @param cid
	 * @return
	 */
	String getItemCategory(Long cid);

	/**
	 * 获取商品销量排行榜
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	List<TbItem> getSaelsRanking(String key, int start, int end);

	EasyUIDataGridResult getItemByTitle(String itemTitle);

}
