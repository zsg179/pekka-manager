package com.pekka.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pekka.common.pojo.EasyUITreeNote;
import com.pekka.mapper.TbItemCatMapper;
import com.pekka.pojo.TbItemCat;
import com.pekka.pojo.TbItemCatExample;
import com.pekka.pojo.TbItemCatExample.Criteria;
import com.pekka.service.ItemCatService;

/**
 * 商品类目服务实现
 * 
 * @author Lenovo-昱树临风
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public List<EasyUITreeNote> getItemCatList(Long parentId) {
		// 设置查询条件
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 执行查询
		List<TbItemCat> itemCats = itemCatMapper.selectByExample(example);
		List<EasyUITreeNote> list = new ArrayList<>();
		for (TbItemCat tbItemCat : itemCats) {
			// 封装每一个子节点
			EasyUITreeNote easyUITreeNote = new EasyUITreeNote();
			easyUITreeNote.setId(tbItemCat.getId());
			easyUITreeNote.setText(tbItemCat.getName());
			// 如果子节点还是父节点，则处于关闭状态；子节点不是父节点，则处于打开状态
			easyUITreeNote.setState(tbItemCat.getIsParent() ? "closed" : "open");
			list.add(easyUITreeNote);
		}
		return list;
	}

}
