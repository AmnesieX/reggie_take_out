package com.amnesie.reggie.common;

/**
 * @Description: 自定义业务异常
 * @author: Amnesie
 * @Date: 2022-10-04
 */
public class CustomException extends RuntimeException {
    public CustomException(String msg){
        super(msg);
    }
}
