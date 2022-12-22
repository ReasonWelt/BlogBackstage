package xyz.welt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.entity.Tag;
import xyz.welt.bean.vo.PageVo;
import xyz.welt.service.TagService;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, String name){
        return tagService.pageTagList(pageNum,pageSize,name);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody Tag tag){
        return tagService.addTag(tag);
    }

    @DeleteMapping("/{id}")
    public ResponseResult delTag(@PathVariable("id") List<Long> ids){
        tagService.removeByIds(ids);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult<Tag> getTag(@PathVariable("id") Long id){
        return ResponseResult.okResult(tagService.getById(id));
    }

    @PutMapping
    public ResponseResult<Tag> updateTag(@RequestBody Tag tag){
        return tagService.updateTag(tag);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return ResponseResult.okResult(tagService.list());
    }

}
