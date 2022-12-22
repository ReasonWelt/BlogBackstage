package xyz.welt.service;

import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author WWJ
* @description 针对表【welt_comment(评论表)】的数据库操作Service
* @createDate 2022-11-09 16:05:02
*/
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
