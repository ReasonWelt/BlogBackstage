package xyz.welt.service;

import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.dto.AddUserDto;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.welt.bean.entity.User;
import xyz.welt.bean.vo.UserStatusVo;

/**
* @author WWJ
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2022-11-09 19:05:31
*/
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult userList(Integer pageNum, Integer pageSize, String userName, String phonenumber);

    ResponseResult addUser(AddUserDto addUserDto);

    void updateUser(AddUserDto addUserDto);

    ResponseResult changeUserStatus(UserStatusVo userStatusVo);
}
