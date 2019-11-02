package com.pekka.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pekka.common.pojo.EasyUIDataGridResult;
import com.pekka.common.pojo.PekkaResult;
import com.pekka.mapper.TbItemMapper;
import com.pekka.pojo.TbItem;
import com.pekka.pojo.TbItemExample;
import com.pekka.pojo.TbItemExample.Criteria;
import com.pekka.service.ItemADService;

@Service
public class ItemADServiceImpl implements ItemADService {

	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public EasyUIDataGridResult getItemADList(Long adId) {
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andAdIdEqualTo(adId);
		List<TbItem> list = itemMapper.selectByExample(example);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(list.size());
		result.setRows(list);
		return result;
	}

	@Override
	public PekkaResult add(Long id, Long adId) {
		TbItem item = itemMapper.selectByPrimaryKey(id);
		// 修改广告分类值 -1：非广告，0：当季热卖;1:益智玩具。。。
		item.setAdId(adId);
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);
		return PekkaResult.ok();
	}

	@Override
	public PekkaResult delete(Long[] ids) {
		for (Long id : ids) {
			TbItem item = itemMapper.selectByPrimaryKey(id);
			// 将商品广告分类改成-1：非广告
			item.setAdId((long) -1);
			item.setUpdated(new Date());
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return PekkaResult.ok();
	}

}
