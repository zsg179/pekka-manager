package com.pekka.service;

import java.util.List;

import com.pekka.common.pojo.EasyUIDataGridResult;
import com.pekka.common.pojo.PekkaResult;
import com.pekka.pojo.TbUser;

/**
 * 用户管理接口
 * 
 * @author Lenovo-昱树临风
 *
 */
public interface UserManagerService {
	EasyUIDataGridResult getUserList(Integer page, Integer rows);

	PekkaResult updateUser(TbUser user);

	PekkaResult deleteUser(Long[] ids);

	List<TbUser> getUserByUserInfo(String userInfo);

}
