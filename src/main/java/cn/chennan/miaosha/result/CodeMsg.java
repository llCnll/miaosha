package cn.chennan.miaosha.result;

/**
 * @author ChenNan
 * @date 2019/10/23
 **/
public class CodeMsg {
    private int code;
    private String msg;

    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500100, "参数校验异常:%s");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500104, "请求非法");

    //登陆模块 5002xx
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经消失");
    public static CodeMsg PASSWRD_EMPTY = new CodeMsg(500211, "密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
    public static CodeMsg PASSWRD_ERROR = new CodeMsg(500215, "密码错误");
    public static CodeMsg NO_COOKIE = new CodeMsg(500216, "请重新登录");

    //商品模块 5003xx

    //订单模块5004xx
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500401, "订单不存在");

    //秒杀模块 5005xx
    public static CodeMsg Miao_SHA_OVER = new CodeMsg(500501, "秒杀结束!");
    public static CodeMsg Miao_SHA_REPEAT = new CodeMsg(500502, "重复秒杀!");


    public CodeMsg fillArgs(Object... args){
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
