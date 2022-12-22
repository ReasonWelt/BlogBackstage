package xyz.welt.service;

import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.dto.RoleDto;
import xyz.welt.bean.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.welt.bean.vo.RoleStatusVo;

import java.util.List;

/**
* @author WWJ
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
* @createDate 2022-11-16 15:49:45
*/
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult roleList(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult addRole(RoleDto roleDto);

    ResponseResult editRole(RoleDto roleDto);

    List<Long> selectRoleIdByUserId(Long userId);

    ResponseResult changeRoleStatus(RoleStatusVo roleStatusVo);
}
