package xyz.welt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.dto.AddArticleDto;
import xyz.welt.service.ArticleService;

import java.util.List;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult addArticle(@RequestBody AddArticleDto article){
        return articleService.addArticle(article);
    }

    @GetMapping("/list")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,String title,String summary){
        return articleService.articleList(pageNum,pageSize,title,summary);
    }

    @GetMapping("{id}")
    public ResponseResult getArticle(@PathVariable("id")Long id){
        return articleService.getArticle(id);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody AddArticleDto articleDto){
        return articleService.updateArticle(articleDto);
    }

    @DeleteMapping("{id}")
    public ResponseResult deleteArticle(@PathVariable("id") List<Long> ids){
        articleService.removeByIds(ids);
        return ResponseResult.okResult();
    }
}
