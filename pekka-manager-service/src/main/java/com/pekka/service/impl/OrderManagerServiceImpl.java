package com.pekka.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pekka.common.pojo.EasyUIDataGridResult;
import com.pekka.common.pojo.PekkaResult;
import com.pekka.jedis.JedisClient;
import com.pekka.mapper.TbOrderItemMapper;
import com.pekka.mapper.TbOrderMapper;
import com.pekka.pojo.TbOrder;
import com.pekka.pojo.TbOrderExample;
import com.pekka.pojo.TbOrderItem;
import com.pekka.pojo.TbOrderItemExample;
import com.pekka.pojo.TbOrderItemExample.Criteria;
import com.pekka.service.OrderManagerService;

@Service
public class OrderManagerServiceImpl implements OrderManagerService {

	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Value("${SHIPPING_CODE_KEY}")
	private String SHIPPING_CODE_KEY;
	@Value("${SHIPPING_CODE_BEGIN_BALUE}")
	private String SHIPPING_CODE_BEGIN_BALUE;

	@Override
	public EasyUIDataGridResult getOrderList(Integer page, Integer rows) {
		if (page == 0)
			page = 1;
		if (rows == 0)
			rows = 30;
		PageHelper.startPage(page, rows);
		TbOrderExample example = new TbOrderExample();
		example.setOrderByClause("create_time DESC");
		List<TbOrder> list = orderMapper.selectByExample(example);
		PageInfo<TbOrder> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(pageInfo.getList());
		result.setTotal((int) pageInfo.getTotal());
		return result;
	}

	@Override
	public PekkaResult deliverGoods(String orderId) {
		TbOrder order = orderMapper.selectByPrimaryKey(orderId);
		if (!jedisClient.exists(SHIPPING_CODE_KEY)) {
			// 不存在物流单号
			// 设置初始值
			jedisClient.set(SHIPPING_CODE_KEY, SHIPPING_CODE_BEGIN_BALUE);
		}
		// '状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭'
		// 状态3和状态2重叠，已弃用状态3
		if (order.getStatus() == 1) {
			return PekkaResult.build(500, "人家还没付钱呢，别发货！");
		} else if (order.getStatus() == 4) {
			return PekkaResult.build(500, "之前发过货了哦！");
		} else if (order.getStatus() == 5) {
			return PekkaResult.build(500, "老大！交易都已经结束了！");
		} else if (order.getStatus() == 6) {
			return PekkaResult.build(500, "老大！交易都已经关闭了！");
		}
		order.setStatus(4);
		// 发货时间
		order.setConsignTime(new Date());
		// 设置物流单号
		order.setShippingCode(jedisClient.incr(SHIPPING_CODE_KEY).toString());
		order.setUpdateTime(new Date());
		orderMapper.updateByPrimaryKeySelective(order);
		return PekkaResult.build(200, "发货成功！");
	}

	@Override
	public EasyUIDataGridResult getOrderByOrderId(String orderId) {
		TbOrder order = orderMapper.selectByPrimaryKey(orderId);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(1);
		result.setRows(Arrays.asList(order));
		return result;
	}

	@Override
	public PekkaResult deleteOrderByOrderId(String[] orderIds) {
		for (String orderId : orderIds) {
			try {
				orderMapper.deleteByPrimaryKey(orderId);
			} catch (Exception e) {
				return PekkaResult.build(500, "删除" + orderId + "号订单失败");
			}
		}
		return PekkaResult.ok();
	}

	@Override
	public PekkaResult getOrderItemList(String orderId) {
		TbOrderItemExample example = new TbOrderItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andOrderIdEqualTo(orderId);
		List<TbOrderItem> list = orderItemMapper.selectByExample(example);
		return PekkaResult.ok(list);
	}

}
