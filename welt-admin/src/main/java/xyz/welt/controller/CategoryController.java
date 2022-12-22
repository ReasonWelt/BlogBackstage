package xyz.welt.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.entity.Category;
import xyz.welt.bean.vo.ExcelCategoryVo;
import xyz.welt.enums.AppHttpCodeEnum;
import xyz.welt.service.CategoryService;
import xyz.welt.utils.BeanCopyUtils;
import xyz.welt.utils.WebUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/listAllCategory")
    public ResponseResult<Category> listAllCategory(){
        return ResponseResult.okResult(categoryService.list());
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @GetMapping("/list")
    public ResponseResult getCategoryPageList(Integer pageNum,Integer pageSize,String name,String status){
        return categoryService.getCategoryPageList(pageNum,pageSize,name,status);
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category){
        categoryService.save(category);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult<Category> getCategoryById(@PathVariable("id") Long id){
        return ResponseResult.okResult(categoryService.getById(id));
    }

    @PutMapping
    public ResponseResult editCategory(@RequestBody Category category){
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delCategory(@PathVariable("id") List<Long> id){
        categoryService.removeByIds(id);
        return ResponseResult.okResult();
    }
}
