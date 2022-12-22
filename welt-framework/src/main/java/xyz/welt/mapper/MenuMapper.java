package xyz.welt.mapper;

import org.springframework.stereotype.Component;
import xyz.welt.bean.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author WWJ
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
* @createDate 2022-11-16 15:42:28
* @Entity xyz.welt.bean.entity.Menu
*/

public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long UserId);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Long> selectMenuListByRoleId(Long id);
}




