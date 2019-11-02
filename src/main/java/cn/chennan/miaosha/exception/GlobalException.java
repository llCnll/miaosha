package cn.chennan.miaosha.exception;

import cn.chennan.miaosha.result.CodeMsg;

/**
 * @author ChenNan
 * @date 2019/10/24
 **/
public class GlobalException extends RuntimeException {

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg cm){
        super(cm.toString());
        this.codeMsg = cm;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
