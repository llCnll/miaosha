package cn.chennan.miaosha.controller;

import cn.chennan.miaosha.domain.MiaoshaUser;
import cn.chennan.miaosha.redis.GoodsKey;
import cn.chennan.miaosha.redis.RedisService;
import cn.chennan.miaosha.result.Result;
import cn.chennan.miaosha.service.GoodsService;
import cn.chennan.miaosha.service.MiaoshaUserService;
import cn.chennan.miaosha.vo.GoodsDetailVo;
import cn.chennan.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;
    /**
     * 使用了cn.chennan.miaosha.config.UserArgumentResolver 自动注入MiaoshaUser
     * QPS:1758
     * 1000*20
     *
     * 加入缓存
     * QPS:3000
     *
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(value = "/to_list"/*, produces = "text/html"*/)
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user){
       //log.info(user.getId()+"--->cn.chennan.miaosha.controller.GoodsController.toList");

        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        //使用dcl检测
        synchronized (this) {
            html = redisService.get(GoodsKey.getGoodsList, "", String.class);
            if(!StringUtils.isEmpty(html)){
                return html;
            }

            List<GoodsVo> goodsList = goodsService.listGoodsVo();
            model.addAttribute("goodsList", goodsList);
            SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);

            html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);

            if (!StringUtils.isEmpty(html)) {
                redisService.set(GoodsKey.getGoodsList, "", html);
                System.out.println("执行了...");
            }

            return html;
        }
    }
    /*@RequestMapping("/to_list")
    public String toList(Model model, MiaoshaUser user*//*HttpServletResponse response,
                          @CookieValue(value= MiaoshaUserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                          @RequestParam(value= MiaoshaUserService.COOKIE_NAME_TOKEN, required = false) String paramToken*//*){

        *//*if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return "login";
        }

        String token = StringUtils.isEmpty(cookieToken) ? paramToken : cookieToken;

        MiaoshaUser user = miaoshaUserService.getByToken(response, token);*//*
        log.info(user.getId()+"--->cn.chennan.miaosha.controller.GoodsController.toList");

        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }*/
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail2(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user, @PathVariable("goodsId")Long goodsId){
        //雪花算法 snowflake

        GoodsDetailVo goodsDeatilVo = new GoodsDetailVo();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        long endTime = goods.getEndDate().getTime();
        long startTime = goods.getStartDate().getTime();
        long nowTime = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;

        if(nowTime < startTime){
            miaoshaStatus = 0;
            remainSeconds = (int)(startTime-nowTime)/1000;
        }else if(nowTime > endTime){
            miaoshaStatus = 2;
        }else{
            miaoshaStatus = 1;
        }

        goodsDeatilVo.setGoods(goods);
        goodsDeatilVo.setUser(user);
        goodsDeatilVo.setMiaoshaStatus(miaoshaStatus);
        goodsDeatilVo.setRemainSeconds(remainSeconds);

        return Result.success(goodsDeatilVo);
    }
    /*
    * QPS 3300
    * */
    @RequestMapping(value = "/to_detail/{goodsId}", produces = "text/html")
    @ResponseBody
    public String toDetail(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user, @PathVariable("goodsId")Long goodsId){
        //雪花算法 snowflake
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        synchronized (this){
            html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
            if(!StringUtils.isEmpty(html)){
                return html;
            }
            GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

            model.addAttribute("goods", goods);
            long endTime = goods.getEndDate().getTime();
            long startTime = goods.getStartDate().getTime();
            long nowTime = System.currentTimeMillis();

            int miaoshaStatus = 0;
            int remainSeconds = 0;

            if(nowTime < startTime){
                miaoshaStatus = 0;
                remainSeconds = (int)(startTime-nowTime)/1000;
            }else if(nowTime > endTime){
                miaoshaStatus = 2;
            }else{
                miaoshaStatus = 1;
            }

            model.addAttribute("miaoshaStatus", miaoshaStatus);
            model.addAttribute("remainSeconds", remainSeconds);

            SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);

            html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);

            if (!StringUtils.isEmpty(html)) {
                redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
                System.out.println("执行了...");
            }
            return html;
        }
    }
    /*
    * QPS:1400
    * */
    /*@RequestMapping("/to_detail/{goodsId}")
    public String doLogin(Model model, MiaoshaUser user, @PathVariable("goodsId")Long goodsId){
        //雪花算法 snowflake

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        model.addAttribute("goods", goods);

        long endTime = goods.getEndDate().getTime();
        long startTime = goods.getStartDate().getTime();
        long nowTime = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;

        if(nowTime < startTime){
            miaoshaStatus = 0;
            remainSeconds = (int)(startTime-nowTime)/1000;
        }else if(nowTime > endTime){
            miaoshaStatus = 2;
        }else{
            miaoshaStatus = 1;
        }

        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        return "goods_detail";
    }*/
}
