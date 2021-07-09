package com.imooc.pojo.vo;

import com.imooc.pojo.bo.ShopcartBO;

import java.util.List;

/**
 * 用户下单后 VO
 */
public class OrderVO {
    private String orderId; // 订单号
    private MerchantOrdersVO merchantOrdersVO; // 订单对应商家
    private List<ShopcartBO> toBeRemovedShopcatdList;

    public List<ShopcartBO> getToBeRemovedShopcatdList() {
        return toBeRemovedShopcatdList;
    }

    public void setToBeRemovedShopcatdList(List<ShopcartBO> toBeRemovedShopcatdList) {
        this.toBeRemovedShopcatdList = toBeRemovedShopcatdList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantOrdersVO getMerchantOrdersVO() {
        return merchantOrdersVO;
    }

    public void setMerchantOrdersVO(MerchantOrdersVO merchantOrdersVO) {
        this.merchantOrdersVO = merchantOrdersVO;
    }
}
