package xyz.welt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.dto.RoleDto;
import xyz.welt.bean.entity.Role;
import xyz.welt.bean.vo.RoleStatusVo;
import xyz.welt.service.RoleMenuService;
import xyz.welt.service.RoleService;

import javax.management.relation.RoleStatus;
import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMenuService roleMenuService;

    @GetMapping("/list")
    public ResponseResult roleList(Integer pageNum,Integer pageSize,String roleName,String status){
        return roleService.roleList(pageNum,pageSize,roleName,status);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody RoleDto roleDto){
        return roleService.addRole(roleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult<Role> getRoleById(@PathVariable("id") Long id){
        return ResponseResult.okResult(roleService.getById(id));
    }

    @PutMapping
    public ResponseResult editRole(@RequestBody RoleDto roleDto){
        return roleService.editRole(roleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable("id") List<Long> ids){
        roleService.removeByIds(ids);
        roleMenuService.deleteRoleMenuByRoleIds(ids);
        return ResponseResult.okResult();
    }

    @GetMapping("/listAllRole")
    public ResponseResult<Role> listAllRole(){
        List<Role> roles = roleService.list();
        return ResponseResult.okResult(roles);
    }

    @PutMapping("changeStatus")
    public ResponseResult changeRoleStatus(@RequestBody RoleStatusVo roleStatusVo){
        return roleService.changeRoleStatus(roleStatusVo);
    }
}
