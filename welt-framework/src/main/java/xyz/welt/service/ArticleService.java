package xyz.welt.service;

import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.dto.AddArticleDto;
import xyz.welt.bean.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author WWJ
* @description 针对表【welt_article(文章表)】的数据库操作Service
* @createDate 2022-11-03 14:42:54
*/
public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(AddArticleDto article);

    ResponseResult articleList(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult getArticle(Long id);

    ResponseResult updateArticle(AddArticleDto articleDto);

    ResponseResult getSearchBackData(String data);
}
