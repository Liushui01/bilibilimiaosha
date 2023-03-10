package com.example.javamiaosha.controller;

import com.example.generator.pojo.User;
import com.example.javamiaosha.dto.DetailHtmlDto;
import com.example.javamiaosha.dto.GoodsDto;
import com.example.javamiaosha.dto.RespBean;
import com.example.javamiaosha.service.IGoodsService;
import com.example.javamiaosha.service.IUserService;
import com.mysql.cj.PreparedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IGoodsService iGoodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    /**
     * 跳转到商品页面
     */
    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String tolist(Model model, User user, HttpServletRequest request, HttpServletResponse response){
        //从redis中获取页面，如果不为空，直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String goodsList = (String) valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(goodsList)){
            return goodsList;
        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",iGoodsService.findGoodsDto());
        //如果为空，手动渲染，存入redis
        WebContext webContext=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        goodsList = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
        if(!StringUtils.isEmpty(goodsList)){
            valueOperations.set("goodsList",goodsList,60, TimeUnit.SECONDS);
        }
        return goodsList;
    }

    /**
     * 跳转商品详情页
     */
    @RequestMapping(value = "/toDetail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(Model model, User user, @PathVariable Long goodsId){
        GoodsDto goodsDto = iGoodsService.findGoodsDtoByGoodsId(goodsId);
        LocalDateTime startDate = goodsDto.getStartDate();
        LocalDateTime endDate = goodsDto.getEndDate();
        LocalDateTime nowDate=LocalDateTime.now();
        int secKillStatus=0;
        int remainSeconds=0;
        if(nowDate.isBefore(startDate)){
            remainSeconds= (int) nowDate.until(startDate,ChronoUnit.SECONDS);
        }else if(nowDate.isAfter(endDate)){
            secKillStatus=2;
            remainSeconds=-1;

        }else {
            secKillStatus=1;
        }
        DetailHtmlDto detailHtmlDto = new DetailHtmlDto(user, goodsDto, secKillStatus, remainSeconds);
        return RespBean.success(detailHtmlDto);
    }

//    /**
//     * 跳转商品详情页
//     */
//    @RequestMapping(value = "/toDetail/{goodsId}",produces = "text/html;charset=utf-8")
//    @ResponseBody
//    public String toDetail(Model model, User user, @PathVariable Long goodsId,
//                           HttpServletRequest request,
//                           HttpServletResponse response){
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        String goodsDetail = (String) valueOperations.get("goodsDetail:"+goodsId);
//        if(!StringUtils.isEmpty(goodsDetail)){
//            return goodsDetail;
//        }
//
//        GoodsDto goodsDto = iGoodsService.findGoodsDtoByGoodsId(goodsId);
//        LocalDateTime startDate = goodsDto.getStartDate();
//        LocalDateTime endDate = goodsDto.getEndDate();
//        LocalDateTime nowDate=LocalDateTime.now();
//        int secKillStatus=0;
//        int remainSeconds=0;
//        if(nowDate.isBefore(startDate)){
//            remainSeconds= (int) nowDate.until(startDate,ChronoUnit.SECONDS);
//        }else if(nowDate.isAfter(endDate)){
//            secKillStatus=2;
//            remainSeconds=-1;
//
//        }else {
//            secKillStatus=1;
//        }
//        model.addAttribute("user",user);
//        model.addAttribute("goods",goodsDto);
//        model.addAttribute("secKillStatus",secKillStatus);
//        model.addAttribute("remainSeconds",remainSeconds);
//        WebContext webContext=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
//        goodsDetail = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
//        if(!StringUtils.isEmpty(goodsDetail)){
//            valueOperations.set("goodsDetail:"+goodsId,goodsDetail,60,TimeUnit.SECONDS);
//        }
//        return goodsDetail;
//    }
}
