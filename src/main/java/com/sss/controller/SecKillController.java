package com.sss.controller;

import com.sss.domain.SecKillOrder;
import com.sss.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sss.domain.User;
import com.sss.domain.OrderInfo;
import com.sss.redis.RedisService;
import com.sss.result.CodeMsg;
import com.sss.service.GoodsService;
import com.sss.service.UserService;
import com.sss.service.OrderService;
import com.sss.vo.GoodsVo;

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

	@RequestMapping("/do_seckill")
	public String list(Model model, User user, @RequestParam("goodsId") long goodsId) {
		model.addAttribute("user", user);
		if (user == null) {
			return "login";
		}
		//判断库存
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		int stock = goods.getStockCount();
		if (stock <= 0) {
			model.addAttribute("errMsg", CodeMsg.SECKILL_OVER.getMsg());
			return "seckill_fail";
		}
		//判断是否已经秒杀到了,防止重复下单秒杀
		SecKillOrder order = orderService.getSecKillOrderByUserIdGoodsId(user.getId(), goodsId);
		if (order != null) {
			model.addAttribute("errMsg", CodeMsg.REPEATE_SECKILL.getMsg());
			return "seckill_fail";
		}
		//减库存->下订单->写入秒杀订单 事务性!!!
        OrderInfo orderInfo = secKillService.secKill(user, goods);
		model.addAttribute("orderInfo", orderInfo);
		model.addAttribute("goods", goods);
		return "order_detail";
	}
}
