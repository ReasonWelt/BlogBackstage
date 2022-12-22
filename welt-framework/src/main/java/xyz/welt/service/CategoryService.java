package xyz.welt.service;

import xyz.welt.bean.ResponseResult;
import xyz.welt.bean.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author WWJ
* @description 针对表【welt_category(分类表)】的数据库操作Service
* @createDate 2022-11-04 09:51:04
*/
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult getCategoryPageList(Integer pageNum, Integer pageSize, String name, String status);
}
