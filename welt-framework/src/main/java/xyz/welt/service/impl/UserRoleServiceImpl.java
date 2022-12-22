package xyz.welt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.welt.bean.entity.UserRole;
import xyz.welt.service.UserRoleService;
import xyz.welt.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author WWJ
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service实现
* @createDate 2022-11-20 23:17:54
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




