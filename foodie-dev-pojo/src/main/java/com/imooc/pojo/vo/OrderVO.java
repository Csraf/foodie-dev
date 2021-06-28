package com.imooc.pojo.vo;

/**
 * 用户下单后 VO
 */
public class OrderVO {
    private String orderId; // 订单号
    private MerchantOrdersVO merchantOrdersVO; // 订单对应商家

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
