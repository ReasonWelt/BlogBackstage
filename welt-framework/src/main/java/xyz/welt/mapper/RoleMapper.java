package xyz.welt.mapper;

import xyz.welt.bean.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author WWJ
* @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
* @createDate 2022-11-16 15:49:45
* @Entity xyz.welt.bean.entity.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);

    List<Long> selectRoleIdByUserId(Long userId);
}




