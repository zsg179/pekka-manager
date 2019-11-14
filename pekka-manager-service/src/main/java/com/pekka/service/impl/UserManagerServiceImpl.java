package com.pekka.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pekka.common.pojo.EasyUIDataGridResult;
import com.pekka.common.pojo.PekkaResult;
import com.pekka.mapper.TbUserMapper;
import com.pekka.pojo.TbUser;
import com.pekka.pojo.TbUserExample;
import com.pekka.pojo.TbUserExample.Criteria;
import com.pekka.service.UserManagerService;

@Service
public class UserManagerServiceImpl implements UserManagerService {
	@Autowired
	private TbUserMapper userMapper;

	@Override
	public EasyUIDataGridResult getUserList(Integer page, Integer rows) {
		if (page == null)
			page = 1;
		if (rows == null)
			rows = 30;
		PageHelper.startPage(page, rows);
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameLike("%");
		List<TbUser> list = userMapper.selectByExample(example);
		PageInfo<TbUser> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(pageInfo.getList());
		result.setTotal((int) pageInfo.getTotal());
		return result;
	}

	@Override
	public PekkaResult updateUser(TbUser user) {
		try {
			user.setUpdated(new Date());
			userMapper.updateByPrimaryKeySelective(user);
		} catch (RuntimeException e) {
			return PekkaResult.build(500, "更新失败！");
		}
		return PekkaResult.ok();
	}

	@Override
	public PekkaResult deleteUser(Long[] ids) {
		for (Long id : ids) {
			try {
				userMapper.deleteByPrimaryKey(id);
			} catch (RuntimeException e) {
				PekkaResult.build(500, "id为" + id + "的用户删除失败！");
			}
		}
		return PekkaResult.ok();
	}

	@Override
	public List<TbUser> getUserByUserInfo(String userInfo) {
		List<TbUser> list = new ArrayList<>();
		try {
			if (StringUtils.isNumeric(userInfo)) {
				// 全数字，认为是用户id
				TbUser user = userMapper.selectByPrimaryKey(Long.parseLong(userInfo));
				list.add(user);
				return list;
			}
			// 否则认为是用户名
			TbUserExample example = new TbUserExample();
			Criteria criteria = example.createCriteria();
			criteria.andUsernameLike("%" + userInfo + "%");
			list = userMapper.selectByExample(example);
			return list;
		} catch (RuntimeException e) {
			return list;
		}
	}
}
