package xyz.welt.controller;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.entity.Link;
import xyz.welt.service.LinkService;

import java.util.List;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult<Link> linkPageList(Integer pageNum, Integer pageSize,String name ,String status){
        return linkService.linkPageList(pageNum,pageSize,name,status);
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody Link link){
        linkService.save(link);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult<Link> getLinkById(@PathVariable("id") Long id){
        return ResponseResult.okResult(linkService.getById(id));
    }

    @PutMapping
    public ResponseResult editLink(@RequestBody Link link){
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delLinkByIds(@PathVariable("id") List<Long> ids){
        linkService.removeByIds(ids);
        return ResponseResult.okResult();
    }
}
