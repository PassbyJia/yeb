package com.rainfir.server.exception;

import com.rainfir.server.pojo.RespBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常
 *
 */
@RestControllerAdvice//该注解表示这是一个控制器增强类，当控制器发生异常且符合类中定义的拦截异常类，将会被拦截
public class GlobalException {

    @ExceptionHandler(SQLException.class)//定义拦截的异常类
    public RespBean mySQLException(SQLException e){
        if (e instanceof SQLIntegrityConstraintViolationException){
            return RespBean.error("该数据有关联数据，操作失败！");
        }
        return RespBean.error("数据库异常，操作失败！");
    }
}
