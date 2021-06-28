package com.imooc.service;

import com.imooc.pojo.Stu;

public interface StuService {
    Stu getStuInfo(int id);

    int saveStu();

    int updateStu(int id);

    int deleteStu(int id);
}
