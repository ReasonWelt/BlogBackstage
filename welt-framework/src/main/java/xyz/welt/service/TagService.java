package xyz.welt.service;

import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.welt.bean.vo.PageVo;

import java.util.List;

/**
* @author WWJ
* @description 针对表【welt_tag(标签)】的数据库操作Service
* @createDate 2022-11-15 16:20:07
*/
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, String name);

    ResponseResult addTag(Tag tag);

    ResponseResult<Tag> updateTag(Tag tag);
}
