package com.pekka.service;

import com.pekka.common.pojo.PekkaResult;

/**
 * @author 朱树广
 * @date 2019年12月29日
 */
public interface ManagerService {

	PekkaResult getManagerByToken(String token);
}
