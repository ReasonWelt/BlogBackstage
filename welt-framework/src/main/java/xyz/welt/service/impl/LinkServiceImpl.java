package xyz.welt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.StringUtils;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.entity.Link;
import xyz.welt.bean.vo.LinkVo;
import xyz.welt.bean.vo.PageVo;
import xyz.welt.constants.SystemConstants;
import xyz.welt.service.LinkService;
import xyz.welt.mapper.LinkMapper;
import org.springframework.stereotype.Service;
import xyz.welt.utils.BeanCopyUtils;

import java.util.List;

/**
* @author WWJ
* @description 针对表【welt_link(友链)】的数据库操作Service实现
* @createDate 2022-11-07 16:11:59
*/
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link>
    implements LinkService{

    @Override
    public ResponseResult  getAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult<Link> linkPageList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(StringUtils.hasText(name),Link::getName,name)
                .eq(StringUtils.hasText(status),Link::getStatus,status);
        Page<Link> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        return ResponseResult.okResult(new PageVo(page.getRecords(),page.getTotal()));
    }
}




