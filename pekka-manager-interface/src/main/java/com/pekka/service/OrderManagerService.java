package com.pekka.service;

import com.pekka.common.pojo.EasyUIDataGridResult;
import com.pekka.common.pojo.PekkaResult;

public interface OrderManagerService {
	EasyUIDataGridResult getOrderList(Integer page, Integer rows);

	PekkaResult deliverGoods(String orderId);

	EasyUIDataGridResult getOrderByOrderId(String orderId);

	PekkaResult deleteOrderByOrderId(String[] orderIds);

	PekkaResult getOrderItemList(String orderId);
}
