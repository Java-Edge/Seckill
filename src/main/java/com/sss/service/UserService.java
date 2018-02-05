package com.sss.service;

import com.alibaba.druid.util.StringUtils;
import com.sss.dao.UserDao;
import com.sss.domain.User;
import com.sss.exception.GlobalException;
import com.sss.redis.UserKey;
import com.sss.redis.RedisService;
import com.sss.result.CodeMsg;
import com.sss.util.MD5Util;
import com.sss.util.UUIDUtil;
import com.sss.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author v_shishusheng
 */
@Service
public class UserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    UserDao userDao;

    @Autowired
    RedisService redisService;

    public User getById(long id) {
        return userDao.getById(id);
    }


    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        //读缓存
        User user = redisService.get(UserKey.token, token, User.class);
        //延长有效期,生命周期应为从最后一次获取token开始计算,即直接更新设置cookie
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }


    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        //第一层MD5后的密码
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        User user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        //二次MD5所需盐
        String dbSalt = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, dbSalt);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //只在第一次登录时生成token,之后只需要更新即可
        String token = UUIDUtil.uuid();
        //将token写到cookie中传给客户端,同时又需要标识token对应哪个用户,所以将用户信息写入到三方缓存redis中
        addCookie(response, token, user);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, User user) {
        //于是后续,只需要拿到token,就可知对应用户
        redisService.set(UserKey.token, token, user);
        //生成Cookie
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        //与token同生命周期
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
