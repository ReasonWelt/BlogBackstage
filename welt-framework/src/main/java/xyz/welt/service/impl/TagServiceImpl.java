package xyz.welt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.util.StringUtils;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.entity.Tag;
import xyz.welt.bean.vo.PageVo;
import xyz.welt.enums.AppHttpCodeEnum;
import xyz.welt.exception.SystemException;
import xyz.welt.service.TagService;
import xyz.welt.mapper.TagMapper;
import org.springframework.stereotype.Service;
import xyz.welt.utils.BeanCopyUtils;

import java.util.List;

/**
* @author WWJ
* @description 针对表【welt_tag(标签)】的数据库操作Service实现
* @createDate 2022-11-15 16:20:07
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, String name) {
        //分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Tag::getName,name);

        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);

        //封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        if (!StringUtils.hasText(tag.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_NAME_NOT_NULL);
        }
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Tag> updateTag(Tag tag) {
        if (!StringUtils.hasText(tag.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_NAME_NOT_NULL);
        }
        updateById(tag);
        return ResponseResult.okResult();
    }

}




