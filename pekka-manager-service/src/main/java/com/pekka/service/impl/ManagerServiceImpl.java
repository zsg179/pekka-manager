package com.pekka.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pekka.common.pojo.PekkaResult;
import com.pekka.common.util.JsonUtils;
import com.pekka.jedis.JedisClient;
import com.pekka.pojo.TbManager;
import com.pekka.service.ManagerService;

/**
 * @author 朱树广
 * @date 2019年12月29日
 */
@Service
public class ManagerServiceImpl implements ManagerService {
	@Autowired
	private JedisClient jedisClient;
	@Value("${MANAGER_SESSION}")
	private String MANAGER_SESSION;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Override
	public PekkaResult getManagerByToken(String token) {
		String json = jedisClient.get(MANAGER_SESSION + ":" + token);
		if (StringUtils.isBlank(json)) {
			return PekkaResult.build(400, "用户登陆已过期");
		}
		// 重置过期时间
		jedisClient.expire(MANAGER_SESSION + ":" + token, SESSION_EXPIRE);
		TbManager manager = JsonUtils.jsonToPojo(json, TbManager.class);
		return PekkaResult.ok(manager);
	}

}
