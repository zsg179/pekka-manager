package com.pekka.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.pekka.common.pojo.PekkaResult;
import com.pekka.common.util.JsonUtils;
import com.pekka.jedis.JedisClient;
import com.pekka.mapper.TbManagerMapper;
import com.pekka.pojo.TbManager;
import com.pekka.pojo.TbManagerExample;
import com.pekka.pojo.TbManagerExample.Criteria;
import com.pekka.service.LoginManagerService;

/**
 * @author 朱树广
 * @date 2020年1月13日
 */
@Service
public class LoginManagerServiceImpl implements LoginManagerService {

	@Autowired
	private TbManagerMapper managerMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${MANAGER_SESSION}")
	private String MANAGER_SESSION;
	@Value("${SESSION_EXPIRE}")
	private int SESSION_EXPIRE;

	@Override
	public PekkaResult login(String managerName, String password) {
		// 判断用户名和密码是否正确
		TbManagerExample example = new TbManagerExample();
		Criteria criteria = example.createCriteria();
		criteria.andManagerNameEqualTo(managerName);
		List<TbManager> list = managerMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return PekkaResult.build(400, "用户名或密码不正确");
		}
		// 密码进行md5加密后再校验
		TbManager manager = list.get(0);
		if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(manager.getPassword())) {
			// 返回登陆失败
			return PekkaResult.build(400, "用户名或密码不正确");
		}
		// 生成token,使用uuid
		String token = UUID.randomUUID().toString();
		// 清空密码
		manager.setPassword(null);
		// 把管理员信息保存到redis,key是token,value是管理员信息
		jedisClient.set(MANAGER_SESSION + ":" + token, JsonUtils.objectToJson(manager));
		// 设置key的过期时间
		jedisClient.expire(MANAGER_SESSION + ":" + token, SESSION_EXPIRE);
		// 返回登陆成功,把token返回
		return PekkaResult.ok(token);
	}

	@Override
	public PekkaResult checkData(String data, int type) {
		TbManagerExample example = new TbManagerExample();
		Criteria criteria = example.createCriteria();
		// 设置查询条件
		// 1.判断用户名是否可用
		if (type == 1) {
			criteria.andManagerNameEqualTo(data);
			// 2.判断手机号是否可用
		} else if (type == 2) {
			criteria.andPhoneEqualTo(data);
			// 3.判断邮箱是否可用
		} else if (type == 3) {
			criteria.andEmailEqualTo(data);
		} else {
			return PekkaResult.build(400, "非法数据");
		}
		List<TbManager> list = managerMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			// 查询到数据，返回false
			return PekkaResult.ok(false);
		}
		// 数据可以使用
		return PekkaResult.ok(true);
	}

	@Override
	public PekkaResult register(TbManager manager) {
		// 检查数据的有效性
		// 判断用户名是否为空
		if (StringUtils.isBlank(manager.getManagerName())) {
			return PekkaResult.build(400, "用户名不能为空！");
		}
		// 判断用户名是否重复
		PekkaResult pekkaResult = checkData(manager.getManagerName(), 1);
		if (!(boolean) pekkaResult.getData()) {
			return PekkaResult.build(400, "用户名重复");
		}
		// 判断密码是否为空
		if (StringUtils.isBlank(manager.getPassword())) {
			return PekkaResult.build(400, "密码不能为空！");
		}
		// 判断手机号是否重复
		pekkaResult = checkData(manager.getPhone(), 2);
		if (!(boolean) pekkaResult.getData()) {
			return PekkaResult.build(400, "手机号重复");
		}
		// 判断邮箱是否重复
		pekkaResult = checkData(manager.getEmail(), 3);
		if (!(boolean) pekkaResult.getData()) {
			return PekkaResult.build(400, "邮箱重复");
		}
		// 补全pojo
		manager.setCreatetime(new Date());
		manager.setUpdatetime((new Date()));
		// 密码进行md5加密
		String md5pass = DigestUtils.md5DigestAsHex(manager.getPassword().getBytes());
		manager.setPassword(md5pass);
		// 插入数据
		managerMapper.insert(manager);
		// 返回注册成功
		return PekkaResult.ok();
	}

	@Override
	public PekkaResult getManagerInfoByManagerName(String managerName) {
		TbManagerExample example = new TbManagerExample();
		Criteria criteria = example.createCriteria();
		criteria.andManagerNameEqualTo(managerName);
		List<TbManager> list = managerMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return PekkaResult.build(400, "用户名不存在！");
		}
		TbManager manager = list.get(0);
		return PekkaResult.ok(manager);
	}

	@Override
	public PekkaResult logout(String token) {
		jedisClient.del(MANAGER_SESSION + ":" + token);
		return PekkaResult.ok();
	}

	@Override
	public PekkaResult updatePwd(String managerName, String oldPwd, String newPwd) {
		TbManagerExample example = new TbManagerExample();
		Criteria criteria = example.createCriteria();
		criteria.andManagerNameEqualTo(managerName);
		List<TbManager> list = managerMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return PekkaResult.build(400, "账号不存在！");
		}
		TbManager manager = list.get(0);
		// 判断原密码是否正确
		if (!(DigestUtils.md5DigestAsHex(oldPwd.getBytes()).equals(manager.getPassword()))) {
			return PekkaResult.build(400, "原密码输入不正确！");
		}
		// 修改密码
		manager.setPassword(DigestUtils.md5DigestAsHex(newPwd.getBytes()));
		manager.setUpdatetime(new Date());
		managerMapper.updateByPrimaryKeySelective(manager);
		return PekkaResult.ok();
	}

}
