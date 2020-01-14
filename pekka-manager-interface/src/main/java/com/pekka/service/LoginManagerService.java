package com.pekka.service;

import com.pekka.common.pojo.PekkaResult;
import com.pekka.pojo.TbManager;

/**
 * @author 朱树广
 * @date 2020年1月13日
 */
public interface LoginManagerService {
	PekkaResult login(String managerName, String password);

	PekkaResult checkData(String data, int type);

	PekkaResult register(TbManager manager);

	PekkaResult getManagerInfoByManagerName(String managerName);

	PekkaResult logout(String token);

	PekkaResult updatePwd(String managerName, String oldPwd, String newPwd);
}
