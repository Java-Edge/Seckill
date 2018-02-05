package com.sss.exception;

import com.sss.result.CodeMsg;
import com.sss.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author v_shishusheng
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value=Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception exception){
        exception.printStackTrace();

        if(exception instanceof GlobalException) {
            GlobalException ex = (GlobalException)exception;
            return Result.error(ex.getCodeMsg());
        }else if(exception instanceof BindException) {
            BindException bindException = (BindException)exception;
            List<ObjectError> errors = bindException.getAllErrors();
            //简单起见,只取第一个错误
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
