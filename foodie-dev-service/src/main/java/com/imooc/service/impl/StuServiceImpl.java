package com.imooc.service.impl;

import com.imooc.mapper.StuMapper;
import com.imooc.pojo.Stu;
import com.imooc.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StuServiceImpl implements StuService {

    public StuMapper stuMapper;

    @Autowired
    public void setStuMapper(StuMapper stuMapper) {
        this.stuMapper = stuMapper;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Stu getStuInfo(int id) {
        return stuMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int saveStu() {
        Stu stu = new Stu();
        stu.setName("lucy");
        stu.setAge("23");
        return stuMapper.insert(stu);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int updateStu(int id) {
        Stu stu = new Stu();
        stu.setName("jerry");
        stu.setAge("24");
        stu.setId(id);
        return stuMapper.updateByPrimaryKey(stu);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int deleteStu(int id) {
        return stuMapper.deleteByPrimaryKey(id);
    }
}
