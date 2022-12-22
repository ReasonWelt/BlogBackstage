package xyz.welt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.entity.Article;
import xyz.welt.bean.entity.Category;
import xyz.welt.bean.vo.CategoryVo;
import xyz.welt.bean.vo.PageVo;
import xyz.welt.constants.SystemConstants;
import xyz.welt.service.ArticleService;
import xyz.welt.service.CategoryService;
import xyz.welt.mapper.CategoryMapper;
import org.springframework.stereotype.Service;
import xyz.welt.utils.BeanCopyUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author Welt
* @description 针对表【welt_category(分类表)】的数据库操作Service实现
* @createDate 2022-11-04 09:51:04
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //查询文章表，状态为已发布的
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articles = articleService.list(articleWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryIds = articles.stream().map(Article::getCategoryId).collect(Collectors.toSet());
        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        List<Category> categoriesStream = categories.stream().filter(category -> category.getStatus().equals(SystemConstants.STATUS_NORMAL)).collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoriesStream, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult getCategoryPageList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(StringUtils.hasText(name),Category::getName,name)
                .eq(StringUtils.hasText(status),Category::getStatus,status);
        Page<Category> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        return ResponseResult.okResult(new PageVo(page.getRecords(),page.getTotal()));
    }
}




