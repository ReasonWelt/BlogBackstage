package xyz.welt.service;

import xyz.welt.bean.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author WWJ
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service
* @createDate 2022-11-19 16:59:30
*/
public interface RoleMenuService extends IService<RoleMenu> {

    void deleteRoleMenuByRoleId(Long id);

    void deleteRoleMenuByRoleIds(List<Long> id);
}
