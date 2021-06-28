package com.imooc.service.impl;

import com.imooc.enums.Sex;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    private final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Autowired
    private UsersMapper usersMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username", username);
        Users users = usersMapper.selectOneByExample(userExample);
        return users == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        // 设置 userID
        String userId = new Sid().nextShort();
        Users user = new Users();
        user.setId(userId);
        // 设置 username
        user.setUsername(username);
        // 设置 password
        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 设置 birthday
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        // 设置 sex
        user.setSex(Sex.secret.type);
        // 设置 昵称
        user.setNickname(username);
        // 设置 用户头像(URL)
        user.setFace(USER_FACE);
        // 设置 用户创建时间
        user.setCreatedTime(new Date());
        // 设置 用户更新时间
        user.setUpdatedTime(new Date());
        usersMapper.insert(user);
        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username", username);
        userCriteria.andEqualTo("password", password);
        Users user = usersMapper.selectOneByExample(userExample);
        return user;
    }
}
