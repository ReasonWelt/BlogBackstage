package xyz.welt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.dto.RoleDto;
import xyz.welt.bean.entity.ArticleTag;
import xyz.welt.bean.entity.Role;
import xyz.welt.bean.entity.RoleMenu;
import xyz.welt.bean.entity.User;
import xyz.welt.bean.vo.PageVo;
import xyz.welt.bean.vo.RoleStatusVo;
import xyz.welt.service.RoleMenuService;
import xyz.welt.service.RoleService;
import xyz.welt.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import xyz.welt.utils.BeanCopyUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author WWJ
* @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
* @createDate 2022-11-16 15:49:45
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是就返回集合中只需要有admin
        if (id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return roleMapper.selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult roleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Role> roles = queryWrapper
                .like(StringUtils.hasText(roleName), Role::getRoleName, roleName)
                .eq(StringUtils.hasText(status), Role::getStatus, status);

        Page<Role> page = new Page<>(pageNum,pageSize);
        page(page, roles);
        List<Role> roleList = page.getRecords();
        PageVo pageVo = new PageVo(roleList,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addRole(RoleDto roleDto) {
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        save(role);

        List<RoleMenu> roleMenuList = Arrays.stream(roleDto.getMenuIds())
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult editRole(RoleDto roleDto) {
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        updateById(role);
        roleMenuService.deleteRoleMenuByRoleId(role.getId());

        List<RoleMenu> roleMenuList = Arrays.stream(roleDto.getMenuIds())
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
        return ResponseResult.okResult();
    }

    @Override
    public List<Long> selectRoleIdByUserId(Long userId) {
        return roleMapper.selectRoleIdByUserId(userId);
    }

    @Override
    public ResponseResult changeRoleStatus(RoleStatusVo roleStatusVo) {
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(Role::getId,roleStatusVo.getRoleId())
                .set(Role::getStatus,roleStatusVo.getStatus());
        roleMapper.update(null,updateWrapper);
        return ResponseResult.okResult();
    }
}




