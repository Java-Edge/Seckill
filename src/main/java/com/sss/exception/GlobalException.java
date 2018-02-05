package com.sss.exception;

import com.sss.result.CodeMsg;
import lombok.Getter;

/**
 * 当出现错误时,应先抛确切表达方法含义的全局异常,而不是CodeMsg
 */
@Getter
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }
}
