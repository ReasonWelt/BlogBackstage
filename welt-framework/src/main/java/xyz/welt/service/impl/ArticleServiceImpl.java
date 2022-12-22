package xyz.welt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.dto.AddArticleDto;
import xyz.welt.bean.entity.Article;
import xyz.welt.bean.entity.ArticleTag;
import xyz.welt.bean.entity.Category;
import xyz.welt.bean.vo.ArticleDataVo;
import xyz.welt.bean.vo.ArticleListVo;
import xyz.welt.bean.vo.HotArticleVo;
import xyz.welt.bean.vo.PageVo;
import xyz.welt.constants.SystemConstants;
import xyz.welt.service.ArticleService;
import xyz.welt.mapper.ArticleMapper;
import org.springframework.stereotype.Service;
import xyz.welt.service.ArticleTagService;
import xyz.welt.service.CategoryService;
import xyz.welt.utils.BeanCopyUtils;
import xyz.welt.utils.RedisCache;
import xyz.welt.utils.SecurityUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author Welt
* @description 针对表【welt_article(文章表)】的数据库操作Service实现
* @createDate 2022-11-03 14:42:54
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章，封装返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        //按照浏览量进行排序
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL).orderByDesc(Article::getViewCount);

        //最多只能显示十条消息
        Page<Article> page = new Page<>(1,10);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();
        //这里得使用以下bean拷贝
        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(articleVos);
    }


    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0 ,Article::getCategoryId,categoryId);
        // 状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        lambdaQueryWrapper.orderByAsc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);

        List<Article> articles = page.getRecords();
        //查询categoryName
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 根据id查询文章
     * @param id
     * @return
     */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEWCOUNT, id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成vo
        ArticleDataVo articleDataVo = BeanCopyUtils.copyBean(article, ArticleDataVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDataVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null){
            articleDataVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDataVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中的浏览量
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEWCOUNT,id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addArticle(AddArticleDto articleDto) {
        //添加博客
        Article article = BeanCopyUtils.copyBean(articleDto,Article.class);
        save(article);

        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(),tagId))
                .collect(Collectors.toList());
        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);

        //需要将数据存储到redis中，不然会出现前台无法更新bug
        Map<String, Integer> viewCountMap = new HashMap<>();
        viewCountMap.put(article.getId().toString(),0);
        redisCache.setCacheMap(SystemConstants.ARTICLE_VIEWCOUNT,viewCountMap);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, String title, String summary) {
        // 查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //如果有id就要求查询时候要和传入的相同
        //状态是正式发布的
        //对isTop进行降序
        queryWrapper
                .like(StringUtils.hasText(title),Article::getTitle,title)
                .like(StringUtils.hasText(summary),Article::getSummary,summary)
                .orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        //封装查询结果
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticle(Long id) {
        Article article = getById(id);
        System.out.println(article);
        AddArticleDto articleDto = BeanCopyUtils.copyBean(article,AddArticleDto.class);

        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagService.list(queryWrapper);
        List<Long> tags = new ArrayList<>();
        for (ArticleTag articleTag : articleTags) {
            tags.add(articleTag.getTagId());
        }
        //添加 博客和标签的关联
        articleDto.setTags(tags);
        return ResponseResult.okResult(articleDto);
    }

    @Override
    public ResponseResult updateArticle(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        article.setUpdateBy(SecurityUtils.getUserId());
        article.setUpdateTime(new Date());
        updateById(article);
        //删除原有的 标签和博客的关联
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(articleTagLambdaQueryWrapper);
        //添加新的博客和标签的关联信息
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(articleDto.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getSearchBackData(String data) {
        if (!StringUtils.hasText(data)){
            return ResponseResult.okResult();
        }
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(Article::getTitle,data)
                .or()
                .like(Article::getSummary,data)
                .orderByDesc(Article::getIsTop);
        List<Article> articles = list(queryWrapper);
        return ResponseResult.okResult(articles);
    }

}




