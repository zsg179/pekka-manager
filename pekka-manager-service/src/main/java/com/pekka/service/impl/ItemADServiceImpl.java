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
	public EasyUIDataGridResult getItemADList(String cName) {
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryNameEqualTo(cName);
		criteria.andIsAdEqualTo(1);
		List<TbItem> list = itemMapper.selectByExample(example);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(list.size());
		result.setRows(list);
		return result;
	}

	@Override
	public PekkaResult add(Long id, String cName) {
		TbItem tbItem = itemMapper.selectByPrimaryKey(id);
		if (cName.equals("当季热卖")) {
			// 直接修改为热门
			tbItem.setIsHot(1);
			tbItem.setUpdated(new Date());
			itemMapper.updateByPrimaryKeySelective(tbItem);
			return PekkaResult.ok();
		} else {
			if (!(tbItem.getCategoryName().equals(cName))) {
				// 分类冲突
				return PekkaResult.build(500, "分类冲突！");
			} else {
				if (tbItem.getIsAd() == 1) {
					return PekkaResult.build(500, "广告重复！");
				}
				tbItem.setIsAd(1);
				tbItem.setUpdated(new Date());
				itemMapper.updateByPrimaryKeySelective(tbItem);
				return PekkaResult.ok();
			}
		}
	}

	@Override
	public PekkaResult delete(Long[] ids, String cName) {
		if (cName.equals("当季热卖")) {
			for (Long id : ids) {
				TbItem tbItem = itemMapper.selectByPrimaryKey(id);
				tbItem.setIsHot(0);
				tbItem.setUpdated(new Date());
				itemMapper.updateByPrimaryKeySelective(tbItem);
			}
			return PekkaResult.ok();
		} else {
			for (Long id : ids) {
				TbItem tbItem = itemMapper.selectByPrimaryKey(id);
				tbItem.setIsAd(0);
				tbItem.setUpdated(new Date());
				itemMapper.updateByPrimaryKeySelective(tbItem);
			}
			return PekkaResult.ok();
		}
	}

	@Override
	public EasyUIDataGridResult getItemADHotList() {
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsHotEqualTo(1);
		List<TbItem> list = itemMapper.selectByExample(example);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(list.size());
		return result;
	}

}
