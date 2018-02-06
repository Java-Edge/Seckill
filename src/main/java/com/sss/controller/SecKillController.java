package com.sss.controller;

import com.sss.domain.SecKillOrder;
import com.sss.result.Result;
import com.sss.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sss.domain.User;
import com.sss.domain.OrderInfo;
import com.sss.redis.RedisService;
import com.sss.result.CodeMsg;
import com.sss.service.GoodsService;
import com.sss.service.UserService;
import com.sss.service.OrderService;
import com.sss.vo.GoodsVo;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author v_shishusheng
 */
@Controller
@RequestMapping("/")
public class SecKillController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SecKillService secKillService;

    @RequestMapping(value = "/do_secKill", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> list(Model model, User user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            model.addAttribute("errMsg", CodeMsg.SECKILL_OVER.getMsg());
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //判断是否已经秒杀到了,防止重复下单秒杀
        SecKillOrder order = orderService.getSecKillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }
        //减库存->下订单->写入秒杀订单 事务性!!!
        OrderInfo orderInfo = secKillService.secKill(user, goods);
        return Result.success(orderInfo);

    }
}
