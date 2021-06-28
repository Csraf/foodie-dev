package com.imooc.enums;

public enum YesOrNo {

    NO(0, "不展示"),
    YES(1, "展示");

    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value){
        this.type = type;
        this.value = value;
    }

}
