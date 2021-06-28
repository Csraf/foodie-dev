package com.imooc.controller;

import com.imooc.enums.PayMethod;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.OrderService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("orders")
@Api(value = "订单相关操作", tags = {"订单相关的api接口"})
public class OrdersController {
    final static Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrderService orderService;

//    @Autowired
//    private RestTemplate restTemplate;

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response
            ){
        Integer payMethod = submitOrderBO.getPayMethod();
        if(payMethod != PayMethod.WEIXIN.type || payMethod != PayMethod.ALIPAY.type){
            IMOOCJSONResult.errorMsg("支付方式不正确");
        }

        logger.error("orderBO :{}", submitOrderBO.toString());
        // 1. 创建订单
        OrderVO orderVO = orderService.createOrder(submitOrderBO);
        String orderId = orderVO.getOrderId();

        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
        /**
         * 1001
         * 2002 -> 用户购买
         * 3003 -> 用户购买
         * 4004
         */
        // TODO 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        return IMOOCJSONResult.ok(orderId);
    }

}
