package xyz.welt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.entity.User;

/**
* @author WWJ
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2022-11-08 14:42:34
*/
public interface AdminLoginService extends IService<User> {

    ResponseResult login(User user);

    ResponseResult logout();

}
