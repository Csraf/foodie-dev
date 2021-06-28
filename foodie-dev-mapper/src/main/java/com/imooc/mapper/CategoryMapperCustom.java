package com.imooc.mapper;

import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom {

    /**
     * 获取二级和三级分类
     * @param rootCatId 一级商品 id
     * @return
     */
    public List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 获取最新六个商品信息
     * @param map 包含一级商品 id
     * @return
     */
    public List<NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);
}