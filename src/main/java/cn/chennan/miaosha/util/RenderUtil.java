package cn.chennan.miaosha.util;

import cn.chennan.miaosha.result.CodeMsg;
import cn.chennan.miaosha.result.Result;
import com.alibaba.fastjson.JSON;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ChenNan
 * @date 2019-11-04 下午2:37
 **/
public class RenderUtil {
    public static void render(HttpServletResponse response, CodeMsg cm) throws Exception{
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
