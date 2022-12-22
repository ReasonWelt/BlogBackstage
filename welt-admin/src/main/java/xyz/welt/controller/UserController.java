package xyz.welt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.dto.AddUserDto;
import xyz.welt.bean.entity.Role;
import xyz.welt.bean.entity.User;
import xyz.welt.bean.vo.RetRoleAndRolesVo;
import xyz.welt.bean.vo.UserStatusVo;
import xyz.welt.service.RoleService;
import xyz.welt.service.UserRoleService;
import xyz.welt.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult userList(Integer pageNum,Integer pageSize,String userName,String phonenumber){
        return userService.userList(pageNum,pageSize,userName,phonenumber);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult delUser(@PathVariable("id") List<Long> ids){
        userService.removeByIds(ids);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getRoleIds(@PathVariable("id") Long id){
        List<Role> roles = roleService.list();
        User user = userService.getById(id);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.selectRoleIdByUserId(id);

        RetRoleAndRolesVo vo = new RetRoleAndRolesVo(roleIds,roles,user);
        return ResponseResult.okResult(vo);
    }

    @PutMapping
    public ResponseResult editUser(@RequestBody AddUserDto addUserDto){
        userService.updateUser(addUserDto);
        return ResponseResult.okResult();
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeUserStatus(@RequestBody UserStatusVo userStatusVo){
        return userService.changeUserStatus(userStatusVo);
    }

}
