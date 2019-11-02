package cn.chennan.miaosha.exception;

import cn.chennan.miaosha.result.CodeMsg;
import cn.chennan.miaosha.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * @author ChenNan
 * @date 2019/10/24
 **/
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value=Exception.class)
    public Result<String> exceptionHandler(Exception e){
        System.out.println("被抓了");
        e.printStackTrace();
        if(e instanceof GlobalException){
            GlobalException ex = (GlobalException)e;
            return Result.error(ex.getCodeMsg());
        }else if(e instanceof BindException){
            BindException ex = (BindException)e;
            List<ObjectError> allErrors = ex.getAllErrors();
            //假定就去第一个
            ObjectError error = allErrors.get(0);
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(error.getDefaultMessage()));
        }else{
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
