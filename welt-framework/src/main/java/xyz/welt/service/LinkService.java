package xyz.welt.service;

import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.entity.Link;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author WWJ
* @description 针对表【welt_link(友链)】的数据库操作Service
* @createDate 2022-11-07 16:11:59
*/
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult<Link> linkPageList(Integer pageNum, Integer pageSize, String name, String status);

}
