package com.imooc.service;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;

public interface UserService {

    /**
     * 查询用户名是否存在
     * @param username: 用户名
     * @return: 存在 / 不存在
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * 创建用户
     * @param userBO
     * @return
     */
    public Users createUser(UserBO userBO);

    /**
     * 查询用户名和密码是否存在
     * @param username: 用户名
     * @param password: 密码
     * @return
     */
    public Users queryUserForLogin(String username, String password);

}
