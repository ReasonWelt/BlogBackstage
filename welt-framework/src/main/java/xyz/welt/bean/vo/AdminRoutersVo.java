package xyz.welt.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.welt.bean.entity.Menu;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoutersVo {
    private List<Menu> menus;
}
