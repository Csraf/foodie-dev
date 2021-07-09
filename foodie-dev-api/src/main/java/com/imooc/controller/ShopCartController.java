package com.imooc.controller;

import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "购物车接口controller", tags = "购物车操作相关接口api")
@RestController
@RequestMapping("shopcart")
public class ShopCartController extends BaseController{

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(
            @RequestParam String userId,
            @RequestBody ShopcartBO shopcartBO,
            HttpServletRequest request,
            HttpServletResponse response){

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        System.out.println(shopcartBO);

        // redis 中 存储 key = "shopcart:{userId}" value = {List<ShopCart>}
        // redis 中 该用户没有购物车对象, 新建购物车,
        // redis 中 存在购物车, 但是没有指定商品规格, 创建新的商品规格.
        // redis 中 有购物车, 有商品规格, 更新该商品的数量 {buyCounts}.

        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        List<ShopcartBO> shopcartList = null;
        if (StringUtils.isNotBlank(shopcartJson)) {
            // redis中已经有购物车了
            shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            // 判断购物车中是否存在已有商品，如果有的话counts累加
            boolean isHaving = false;
            for (ShopcartBO sc: shopcartList) {
                String tmpSpecId = sc.getSpecId();
                if (tmpSpecId.equals(shopcartBO.getSpecId())) {
                    sc.setBuyCounts(sc.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if (!isHaving) {
                shopcartList.add(shopcartBO);
            }
        } else {
            // redis中没有购物车
            shopcartList = new ArrayList<>();
            // 直接添加到购物车中
            shopcartList.add(shopcartBO);
        }
        // 覆盖现有redis中的购物车
        redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartList));
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult del(
            @RequestParam String userId,
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }

        // 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除redis购物车中的商品
        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        if (StringUtils.isNotBlank(shopcartJson)) {
            // redis中已经有购物车了
            List<ShopcartBO> shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            // 判断购物车中是否存在已有商品，如果有的话则删除
            for (ShopcartBO sc: shopcartList) {
                String tmpSpecId = sc.getSpecId();
                if (tmpSpecId.equals(itemSpecId)) {
                    shopcartList.remove(sc);
                    break;
                }
            }
            // 覆盖现有redis中的购物车
            redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartList));
        }

        return IMOOCJSONResult.ok();
    }

}
