package xyz.welt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.dto.AddUserDto;
import xyz.welt.bean.entity.User;
import xyz.welt.bean.entity.UserRole;
import xyz.welt.bean.vo.PageVo;
import xyz.welt.bean.vo.UserInfoVo;
import xyz.welt.bean.vo.UserStatusVo;
import xyz.welt.constants.SystemConstants;
import xyz.welt.enums.AppHttpCodeEnum;
import xyz.welt.exception.SystemException;
import xyz.welt.mapper.UserMapper;
import xyz.welt.service.UserRoleService;
import xyz.welt.service.UserService;
import org.springframework.stereotype.Service;
import xyz.welt.utils.BeanCopyUtils;
import xyz.welt.utils.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author WWJ
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2022-11-09 19:05:31
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    //这里之后可以来写一点判断和维护
    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if (Exist(user.getUserName(),1)){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (Exist(user.getNickName(),2)){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (Exist(user.getEmail(),3)){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult userList(Integer pageNum, Integer pageSize, String userName, String phonenumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(StringUtils.hasText(userName),User::getUserName,userName)
                .like(StringUtils.hasText(userName),User::getUserName,userName);
        Page<User> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        return ResponseResult.okResult(new PageVo(page.getRecords(),page.getTotal()));
    }

    @Override
    @Transactional
    public ResponseResult addUser(AddUserDto addUserDto) {
        //对数据进行非空判断
        if (!StringUtils.hasText(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if (Exist(addUserDto.getUserName(),1)){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (Exist(addUserDto.getNickName(),2)){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (Exist(addUserDto.getEmail(),3)){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        //对密码进行加密
        addUserDto.setPassword(passwordEncoder.encode(addUserDto.getPassword()));
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        //存入数据库
        save(user);

        //将用户角色关系存入数据库
        List<UserRole> sysUserRoles = addUserDto.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(sysUserRoles);
        return ResponseResult.okResult();
    }

    @Override
    public void updateUser(AddUserDto addUserDto) {
        // 删除用户与角色关联
        LambdaQueryWrapper<UserRole> userRoleUpdateWrapper = new LambdaQueryWrapper<>();
        userRoleUpdateWrapper.eq(UserRole::getUserId,addUserDto.getId());
        userRoleService.remove(userRoleUpdateWrapper);

        // 新增用户与角色管理
        List<UserRole> sysUserRoles = addUserDto.getRoleIds().stream()
                .map(roleId -> new UserRole(addUserDto.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(sysUserRoles);

        // 更新用户信息
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        updateById(user);
    }

    @Override
    public ResponseResult changeUserStatus(UserStatusVo user) {

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(User::getId,user.getUserId())
                .set(User::getStatus,user.getStatus());
        userMapper.update(null,updateWrapper);
        return ResponseResult.okResult();
    }


    private boolean Exist(String aux, int flag) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        switch (flag){
            case 1:
                queryWrapper.eq(User::getUserName,aux);
                return count(queryWrapper)>0;
            case 2:
                queryWrapper.eq(User::getNickName,aux);
                return count(queryWrapper)>0;
            case 3:
                queryWrapper.eq(User::getEmail,aux);
                return count(queryWrapper)>0;
        }

        return true;
    }
}




