package com.example.javamiaosha.controller;

import com.example.generator.pojo.User;
import com.example.javamiaosha.dto.GoodsDto;
import com.example.javamiaosha.service.IGoodsService;
import com.example.javamiaosha.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IGoodsService iGoodsService;
    /**
     * 跳转到商品页面
     */
    @RequestMapping("/toList")
    public String tolist(Model model, User user){
//        if(StringUtils.isEmpty(ticket)){
//            log.error("{}","出错了000000000000");
//            return "login";
//        }
//        User user = iUserService.getUserByCookie(ticket,request,response);
//        if(null==user){
//            log.error("{}","出错了11111111111111");
//            System.out.println(ticket);
//            return "login";
//        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",iGoodsService.findGoodsDto());
        return "goodsList";
    }

    /**
     * 跳转商品详情页
     */
    @RequestMapping("/toDetail/{goodsId}")
    public String toDetail(Model model, User user, @PathVariable Long goodsId){
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
        model.addAttribute("user",user);
        model.addAttribute("goods",goodsDto);
        model.addAttribute("secKillStatus",secKillStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        return "goodsDetail";

    }
}
