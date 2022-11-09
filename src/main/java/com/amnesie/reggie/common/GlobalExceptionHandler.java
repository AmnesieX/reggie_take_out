package com.amnesie.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;


/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-03
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * SQL重复添加异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e){
        String emsg = e.getMessage();
        log.error(emsg);
        //java.sql.SQLIntegrityConstraintViolationException: Duplicate entry 'testUser' for key 'employee.idx_username'
        if (emsg.contains("Duplicate entry")){
            if (emsg.contains("username")){
                return R.error("该账号已被注册!");
            }
            else{
                //将异常信息按照单引号分隔开
                String[] split = e.getMessage().split("\'");
                String msg = split[1] + "已存在";
                return R.error(msg);
            }
        }
        return R.error("未知错误");
    }
    /**
     * 处理文件未找到情况
     * @param e
     * @return
     */
    @ExceptionHandler(FileNotFoundException.class)
    public R<String> exceptionHandler(FileNotFoundException e){
        log.error(e.getMessage());
        return R.error("文件丢失");
    }
    /**
     * 自定义异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException e){
        log.error(e.getMessage());
        return R.error(e.getMessage());
    }
}
