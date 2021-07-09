package com.imooc.controller;

import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.List;

@Api(value = "首页", tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {
    @Autowired
    private CarouselService carouselService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public IMOOCJSONResult carousel(){
        List<Carousel> list = new ArrayList<>();
        String carouselStr = redisOperator.get("carousel");
        if(StringUtils.isBlank(carouselStr)){
            list = carouselService.queryAll(YesOrNo.YES.type);
            redisOperator.set("carousel", JsonUtils.objectToJson(list));
        }
        else{
            list = JsonUtils.jsonToList(carouselStr, Carousel.class);
        }
        return IMOOCJSONResult.ok(list);
    }

    /**
     * 首页分类展示需求：<p>
     * 1. 第一次刷新主页查询大分类，渲染展示到首页<p>
     * 2. 如果鼠标上移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载（懒加载）
     */
    @ApiOperation(value = "获取商品分类(一级分类)", notes = "获取商品分类(一级分类)", httpMethod = "GET")
    @GetMapping("/cats")
    public IMOOCJSONResult cats(){
        List<Category> list = new ArrayList<>();
        String catsStr = redisOperator.get("cats");
        if(StringUtils.isBlank(catsStr)){
            list = categoryService.queryAllRootLevelCat();
            redisOperator.set("cats", JsonUtils.objectToJson(list));
        }
        else{
            list = JsonUtils.jsonToList(catsStr, Category.class);
        }
        return IMOOCJSONResult.ok(list);
    }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品二级分类，三级分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult subCat(
            @ApiParam(name = "商品一级分类 id", required = true)
            @PathVariable Integer rootCatId){
        if(rootCatId == null){
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<CategoryVO> list = new ArrayList<>();
        String subCatStr = redisOperator.get("subCat:" + rootCatId);
        if(StringUtils.isBlank(subCatStr)){
            list = categoryService.getSubCatList(rootCatId);
            redisOperator.set("subCat:"+rootCatId, JsonUtils.objectToJson(list));
        }
        else{
            list = JsonUtils.jsonToList(subCatStr, CategoryVO.class);
        }
        return IMOOCJSONResult.ok(list);
    }

    @ApiOperation(value = "查询每个一级分类下的最新6条商品数据", notes = "查询每个一级分类下的最新6条商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public IMOOCJSONResult sixNewItems(
            @ApiParam(name = "商品一级分类 id", required = true)
            @PathVariable Integer rootCatId){
        if(rootCatId == null){
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<NewItemsVO> list = categoryService.getSixNewItemsLazy(rootCatId);
        return IMOOCJSONResult.ok(list);
    }

}
