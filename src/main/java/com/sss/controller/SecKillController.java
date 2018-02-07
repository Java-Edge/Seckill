package com.sss.controller;

import com.sss.domain.SecKillOrder;
import com.sss.rabbitmq.MqSender;
import com.sss.rabbitmq.SecKillMessage;
import com.sss.redis.GoodsKey;
import com.sss.result.Result;
import com.sss.service.SecKillService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sss.domain.User;
import com.sss.domain.OrderInfo;
import com.sss.redis.RedisService;
import com.sss.result.CodeMsg;
import com.sss.service.GoodsService;
import com.sss.service.UserService;
import com.sss.service.OrderService;
import com.sss.vo.GoodsVo;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * @author v_shishusheng
 */
@Controller
@RequestMapping("/")
public class SecKillController implements InitializingBean {

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

    @Autowired
    MqSender sender;

    private HashMap<Long, Boolean> localOverMap =  new HashMap<>();


    /**
     * 系统启动时将商品库存加载到redis中
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList == null) {
            return;
        }

        for(GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getSecKillGoodsStock, ""+goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }

    @RequestMapping(value = "/{path}/do_secKill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list(Model model, User user, @RequestParam("goodsId") long goodsId,@PathVariable("path") String path) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //验证path
        boolean check = secKillService.checkPath(user, goodsId, path);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if(over) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getSecKillGoodsStock, ""+goodsId);
        if(stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //是否已秒到
        SecKillOrder order = orderService.getSecKillOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }
        //没秒到则入队
        SecKillMessage secKillMessage = new SecKillMessage();
        secKillMessage.setUser(user);
        secKillMessage.setGoodsId(goodsId);
        sender.sendSecKillMessage(secKillMessage);
        //排队中
        return Result.success(0);
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> secKillResult(Model model,User user,
                                      @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result  =secKillService.getSecKillResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @RequestMapping(value="/path", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getSecKillPath(User user,
                                         @RequestParam("goodsId")long goodsId) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        String path  =secKillService.createSecKillPath(user, goodsId);
        return Result.success(path);
    }
}
