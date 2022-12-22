package xyz.welt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.dto.AddCommentDto;
import xyz.welt.bean.entity.Comment;
import xyz.welt.constants.SystemConstants;
import xyz.welt.service.CommentService;
import xyz.welt.utils.BeanCopyUtils;

@RestController
@RequestMapping("/comment")
// @Api(tags="评论",description = "评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto comment){
        Comment bean = BeanCopyUtils.copyBean(comment, Comment.class);
        return commentService.addComment(bean);
    }

    @GetMapping("/linkCommentList")
    // @ApiOperation(value = "友链评论列表",notes = "获取一页友链评论")
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
