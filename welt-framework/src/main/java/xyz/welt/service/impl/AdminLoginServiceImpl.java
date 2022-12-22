package xyz.welt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.entity.LoginUser;
import xyz.welt.bean.entity.User;
import xyz.welt.mapper.UserMapper;
import xyz.welt.service.AdminLoginService;
import xyz.welt.utils.JwtUtil;
import xyz.welt.utils.RedisCache;
import xyz.welt.utils.SecurityUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
* @author WWJ
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2022-11-08 14:42:34
*/
@Service
public class AdminLoginServiceImpl extends ServiceImpl<UserMapper, User>
    implements AdminLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }

        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String id = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(id);
        //把用户信息存入redis
        redisCache.setCacheObject("adminlogin:"+id,loginUser);

        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        //获取当前登录的用户id
        Long userId = SecurityUtils.getUserId();
        //删除redis中对应的值
        redisCache.deleteObject("adminlogin"+userId);
        return ResponseResult.okResult();
    }

}




