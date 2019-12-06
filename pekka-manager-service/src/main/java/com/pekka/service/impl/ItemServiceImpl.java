package com.pekka.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pekka.common.pojo.EasyUIDataGridResult;
import com.pekka.common.pojo.PekkaResult;
import com.pekka.common.util.IDUtils;
import com.pekka.common.util.JsonUtils;
import com.pekka.jedis.JedisClient;
import com.pekka.mapper.TbItemCatMapper;
import com.pekka.mapper.TbItemDescMapper;
import com.pekka.mapper.TbItemMapper;
import com.pekka.pojo.TbItem;
import com.pekka.pojo.TbItemCat;
import com.pekka.pojo.TbItemDesc;
import com.pekka.pojo.TbItemExample;
import com.pekka.pojo.TbItemExample.Criteria;
import com.pekka.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private TbItemDescMapper descMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private JedisClient jedisClient;
	@Resource(name = "itemAddTopic")
	private Destination destination;

	@Value("${SALES_RANKING}")
	private String SALES_RANKING;
	@Value("${YZWJ}")
	private Long YZWJ;
	@Value("${YAOKONG}")
	private Long YAOKONG;
	@Value("${JMPC}")
	private Long JMPC;
	@Value("${DMMX}")
	private Long DMMX;
	@Value("${JSWJ}")
	private Long JSWJ;
	@Value("${MRWJ}")
	private Long MRWJ;
	@Value("${CYDIY}")
	private Long CYDIY;
	@Value("${YQ}")
	private Long YQ;

	@Override
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		if (page == null) {
			page = 1;
		}
		if (rows == null) {
			rows = 1;
		}
		// 设置分页
		PageHelper.startPage(page, rows);
		// 设置查询条件
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		// 1--正常（上架）,2--下架 ,3--删除
		// 查询状态不等于3的商品
		criteria.andStatusNotEqualTo((byte) 3);
		// 执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		// 获取分页信息
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		// 设置返回值
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int) pageInfo.getTotal());
		result.setRows(pageInfo.getList());
		return result;
	}

	@Override
	public PekkaResult addItem(TbItem item, String desc) {
		try {
			// 补全TbItem对象
			// 商品id
			final long itemId = IDUtils.genItemId();
			item.setId(itemId);
			// 商品状态:1-正常，2-下架，3-删除
			item.setStatus((byte) 1);
			// 是否是广告:0不是，1是
			item.setIsAd(0);
			// 创建时间
			item.setCreated(new Date());
			// 更新时间
			item.setUpdated(new Date());
			// 设置所属大类名称
			Long cid = item.getCid();
			String categoryName = "";
			if (cid > YZWJ && cid < YAOKONG)
				categoryName = "益智玩具";
			else if (cid > YAOKONG && cid < JMPC)
				categoryName = "遥控电动";
			else if (cid > JMPC && cid < DMMX)
				categoryName = "积木拼插";
			else if (cid > DMMX && cid < JSWJ)
				categoryName = "动漫模型";
			else if (cid > JSWJ && cid < MRWJ)
				categoryName = "健身玩具";
			else if (cid > MRWJ && cid < CYDIY)
				categoryName = "毛绒玩具";
			else if (cid > CYDIY && cid < YQ)
				categoryName = "创意DIY";
			else if (cid > YQ)
				categoryName = "乐器";
			item.setCategoryName(categoryName);
			// 是否为热卖商品：0非热卖，1热卖
			item.setIsHot(0);
			itemMapper.insert(item);
			// 补全TbitemDesc对象
			TbItemDesc itemDesc = new TbItemDesc();
			itemDesc.setItemId(itemId);
			itemDesc.setItemDesc(desc);
			itemDesc.setCreated(new Date());
			itemDesc.setUpdated(new Date());
			descMapper.insert(itemDesc);
			// 使用消息队列
			jmsTemplate.send(destination, new MessageCreator() {

				@Override
				public Message createMessage(Session session) throws JMSException {
					TextMessage message = session.createTextMessage(itemId + "");
					return message;
				}
			});
			return PekkaResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return PekkaResult.build(400, "新增商品失败！");
		}

	}

	@Override
	public PekkaResult getItemDesc(Long itemId) {
		// 查询含有该商品描述的商品
		TbItemDesc itemDesc = descMapper.selectByPrimaryKey(itemId);
		return PekkaResult.ok(itemDesc);
	}

	@Override
	public PekkaResult updateItem(TbItem item, String desc) {
		try {
			// 更新时间
			item.setUpdated(new Date());
			itemMapper.updateByPrimaryKeySelective(item);
			// 补全TbitemDesc对象
			TbItemDesc itemDesc = new TbItemDesc();
			itemDesc.setItemId(item.getId());
			itemDesc.setItemDesc(desc);
			itemDesc.setUpdated(new Date());
			descMapper.updateByPrimaryKeySelective(itemDesc);
			return PekkaResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return PekkaResult.build(400, "更新商品失败！");
		}
	}

	@Override
	public PekkaResult deleteItem(Long[] ids) {
		try {
			for (Long id : ids) {
				TbItem item = itemMapper.selectByPrimaryKey(id);
				item.setStatus((byte) 3);
				item.setUpdated(new Date());
				// 把商品状态设置为3，即代表删除
				itemMapper.updateByPrimaryKeySelective(item);
			}
			return PekkaResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return PekkaResult.build(400, "删除商品失败！");
		}
	}

	@Override
	public PekkaResult instock(Long[] ids) {
		try {
			for (Long id : ids) {
				TbItem item = itemMapper.selectByPrimaryKey(id);
				// 2--下架
				item.setStatus((byte) 2);
				item.setUpdated(new Date());
				itemMapper.updateByPrimaryKeySelective(item);
			}
			return PekkaResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return PekkaResult.build(400, "下架商品失败！");
		}
	}

	@Override
	public PekkaResult reshelf(Long[] ids) {
		try {
			for (Long id : ids) {
				TbItem item = itemMapper.selectByPrimaryKey(id);
				// 1--上架
				item.setStatus((byte) 1);
				item.setUpdated(new Date());
				itemMapper.updateByPrimaryKeySelective(item);
			}
			return PekkaResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return PekkaResult.build(400, "上架商品失败！");
		}
	}

	@Override
	public TbItem getItemById(Long itemId) {
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		return item;
	}

	@Override
	public TbItemDesc getItemDescById(Long itemId) {
		return descMapper.selectByPrimaryKey(itemId);
	}

	@Override
	public String getItemCategory(Long cid) {
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(cid);
		return itemCat.getName();
	}

	@Override
	public List<TbItem> getSaelsRanking(String key, int start, int end) {
		Set<String> zrevrange = jedisClient.zrevrange(SALES_RANKING + key, start, end);
		List<TbItem> list = new ArrayList<>();
		for (String string : zrevrange) {
			TbItem tbItem = JsonUtils.jsonToPojo(string, TbItem.class);
			tbItem.setImage(tbItem.getImage().split(",")[0]);
			list.add(tbItem);
		}
		return list;
	}

	@Override
	public EasyUIDataGridResult getItemByTitle(String itemTitle) {
		PageHelper.startPage(1, 30);
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andTitleLike("%" + itemTitle + "%");
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		// result.setRows(list);
		// result.setTotal(list.size());
		result.setRows(pageInfo.getList());
		result.setTotal((int) pageInfo.getTotal());
		return result;
	}

}
