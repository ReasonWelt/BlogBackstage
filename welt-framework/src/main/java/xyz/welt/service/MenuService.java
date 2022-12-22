package xyz.welt.service;

import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author WWJ
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2022-11-16 15:42:28
*/
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Menu> menuList(Menu menu);

    ResponseResult addMenu(Menu menu);

    boolean hasChild(Long menuId);

    List<Long> selectMenuListByRoleId(Long id);
}
