package org.hgc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.hgc.reggie.common.R;
import org.hgc.reggie.entity.Category;
import org.hgc.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hgc
 * @version 1.0
 * @date 2023/4/28/0028 21:41
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
//        方法1
//        categoryService.save(category);
//        return R.success("新增分类成功");
//        方法2
        return categoryService.saveCategory(category);
    }

    /**
     * 分类管理-分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize ) {
        return categoryService.pageSort(page,pageSize);
    }

    /**
     * 删除分类,根据id删除分类，删除之前做一下判断，是否有关联
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id) {
        return categoryService.deleteSort(id);
    }
    /**
     * 修改分类
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
//        categoryService.updateById(category);
//        return R.success("修改完成");
        return categoryService.updateSort(category);
    }
    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> getCategoryList(Category category) {
        return categoryService.getListInfo(category);
    }



}
