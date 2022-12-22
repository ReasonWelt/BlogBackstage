package xyz.welt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.entity.Menu;
import xyz.welt.constants.SystemConstants;
import xyz.welt.service.MenuService;
import xyz.welt.mapper.MenuMapper;
import org.springframework.stereotype.Service;
import xyz.welt.utils.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author WWJ
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
* @createDate 2022-11-16 15:42:28
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if (SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper
                    .in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON)
                    .eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());

            return perms;
        }
        //否则返回所具有的权限

        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        List<Menu> menus = null;
        //判断是否是管理员
        if (SecurityUtils.isAdmin()){
            //如果是，返回所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else {
            //否则获取当前用户所具有的menu
            menus =  menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        //构建tree
        //先找出第一层的菜单，然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);

        return menuTree;

    }

    @Override
    public List<Menu> menuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName())
                .eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,menu.getStatus())
                .orderByAsc(Menu::getParentId)
                .orderByAsc(Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);
        return menus;
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }




    private List<Menu> builderMenuTree(List<Menu> menus, long parentId) {
        List<Menu> MenuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return MenuTree;
    }

    /**
     * 获取传入参数的子菜单
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }

    @Override
    public boolean hasChild(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        return count(queryWrapper) != 0;
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long id) {
        return menuMapper.selectMenuListByRoleId(id);
    }
}




