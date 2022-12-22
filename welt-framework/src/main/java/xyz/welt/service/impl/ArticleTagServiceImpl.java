package xyz.welt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.welt.bean.entity.ArticleTag;
import xyz.welt.service.ArticleTagService;
import xyz.welt.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;

/**
* @author WWJ
* @description 针对表【welt_article_tag(文章标签关联表)】的数据库操作Service实现
* @createDate 2022-11-18 15:30:57
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService{

}




