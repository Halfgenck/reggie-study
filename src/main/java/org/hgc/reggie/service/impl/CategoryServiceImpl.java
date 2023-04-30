package org.hgc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hgc.reggie.common.CustomException;
import org.hgc.reggie.common.R;
import org.hgc.reggie.entity.Category;
import org.hgc.reggie.entity.Dish;
import org.hgc.reggie.entity.Setmeal;
import org.hgc.reggie.mapper.CategoryMapper;
import org.hgc.reggie.service.CategoryService;
import org.hgc.reggie.service.DishService;
import org.hgc.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author hgc
 * @version 1.0
 * @date 2023/4/28/0028 21:39
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;


    @Override
    public R<String> saveCategory(Category category) {
        categoryMapper.insert(category);
        return R.success("添加分类成功");
    }

    @Override
    public R<Page> pageSort(int page, int pageSize) {
        Page<Category> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);
        categoryMapper.selectPage(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @Override
    public R<String> deleteSort(Long id) {
        // 按分类id查询
        // 查询当前菜品是否关联了其他菜品，已关联则抛出异常
        // 查询当前菜品是否关联了套餐，如果已经关联，抛出业务异常
        // 正常删除

        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId,id);

        int countDish = dishService.count(dishQueryWrapper);

        if (countDish > 0 ) {
            throw new CustomException("当前分类下关联了菜品，不能删除！");
        }
        LambdaQueryWrapper<Setmeal> setMealQueryWrapper = new LambdaQueryWrapper<>();
        setMealQueryWrapper.eq(Setmeal::getCategoryId,id);
        int countMeal = setmealService.count(setMealQueryWrapper);
        if (countMeal > 0 ) {
            throw new CustomException("当前分类下套餐了菜品，不能删除！");
        }

        super.removeById(id);

        return R.success("分类信息删除成功");
    }

    @Override
    public R<String> updateSort(Category category) {
        categoryMapper.updateById(category);
        return R.success("修改完成");
    }

    @Override
    public R<List<Category>> getListInfo(Category category) {
        // 条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        // 排序
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        // 执行查询
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        return R.success(categories);
    }
}
