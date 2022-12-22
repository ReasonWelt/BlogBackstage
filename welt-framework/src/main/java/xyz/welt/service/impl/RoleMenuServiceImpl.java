package xyz.welt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.welt.bean.entity.RoleMenu;
import xyz.welt.service.RoleMenuService;
import xyz.welt.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author WWJ
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service实现
* @createDate 2022-11-19 16:59:30
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements RoleMenuService{

    @Override
    public void deleteRoleMenuByRoleId(Long id) {
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        remove(queryWrapper);
    }

    @Override
    public void deleteRoleMenuByRoleIds(List<Long> ids) {
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper();
        for (Long id : ids) {
            queryWrapper.eq(RoleMenu::getRoleId,id);
        }
        remove(queryWrapper);
    }
}




