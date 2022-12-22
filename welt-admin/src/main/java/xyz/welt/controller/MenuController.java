package xyz.welt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.entity.Menu;
import xyz.welt.bean.vo.MenuTreeVo;
import xyz.welt.bean.vo.RoleMenuTreeSelectVo;
import xyz.welt.service.MenuService;
import xyz.welt.utils.SystemConverter;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult<Menu> menuList(Menu menu){
        return ResponseResult.okResult(menuService.menuList(menu));
    }

    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    @GetMapping("/{id}")
    public ResponseResult<Menu> getInfo(@PathVariable("id") Long id){
        return ResponseResult.okResult(menuService.getById(id));
    }

    /**
     * 修改菜单
     */
    @PutMapping
    public ResponseResult edit(@RequestBody Menu menu) {
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(500,"修改菜单'" + menu.getMenuName() + "'上级菜单不能选择自己");
        }
        menuService.updateById(menu);
        return ResponseResult.okResult();
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    public ResponseResult remove(@PathVariable("id") Long id) {
        if (menuService.hasChild(id)) {
            return ResponseResult.errorResult(500,"存在子菜单，删除失败");
        }
        menuService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public ResponseResult treeselect() {
        //复用之前的selectMenuList方法。方法需要参数，参数可以用来进行条件查询，而这个方法不需要条件，所以直接new Menu()传入
        List<Menu> menus = menuService.menuList(new Menu());
        List<MenuTreeVo> options =  SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(options);
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeSelect(@PathVariable("id") Long id){
        List<Menu> menus = menuService.menuList(new Menu());
        List<Long> checkedKeys = menuService.selectMenuListByRoleId(id);
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(checkedKeys,menuTreeVos);
        return ResponseResult.okResult(vo);
    }

}
