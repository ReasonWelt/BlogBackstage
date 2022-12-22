package xyz.welt.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.welt.bean.entity.Role;
import xyz.welt.bean.entity.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetRoleAndRolesVo {
    private List<Long> roleIds;

    private List<Role> roles;

    private User user;
}
