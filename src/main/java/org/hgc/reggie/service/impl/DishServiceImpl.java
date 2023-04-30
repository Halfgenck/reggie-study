package org.hgc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.hgc.reggie.common.CustomException;
import org.hgc.reggie.common.R;
import org.hgc.reggie.dto.DishDto;
import org.hgc.reggie.entity.Category;
import org.hgc.reggie.entity.Dish;
import org.hgc.reggie.entity.DishFlavor;
import org.hgc.reggie.mapper.CategoryMapper;
import org.hgc.reggie.mapper.DishFlavorMapper;
import org.hgc.reggie.mapper.DishMapper;
import org.hgc.reggie.service.CategoryService;
import org.hgc.reggie.service.DishFlavorService;
import org.hgc.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hgc
 * @version 1.0
 * @date 2023/4/28/0028 22:07
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private DishService dishService;
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增菜品，同时插入对应的口味数据
     *
     * @param dishDto
     * @return
     */
    @Transactional
    @Override
    public R<String> saveDishWithFlavor(DishDto dishDto) {
        // 保存菜品基本信息到dish
        this.save(dishDto);
        // 保存flavors集合，联系到指定的菜品
        Long dishDtoId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();


        for (DishFlavor flavor : flavors) {
            // 保存到口味表 dish_flavors
            dishFlavorMapper.insert(flavor);
        }

//        flavors = flavors.stream().map((item) -> {
//            item.setDishId(dishId);
//            return item;
//        }).collect(Collectors.toList());
//        dishFlavorService.saveBatch(flavors);
        return R.success("新增菜品成功");
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public R<Page> pageByDish(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        // 获取套餐类型
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(name), Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);


        dishService.page(pageInfo, queryWrapper);
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        // 获取菜品
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtoList = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            // 根据id查询分类对象
            Category category = categoryMapper.selectById(categoryId);
            if (category != null) {
                // 获取对象名臣
                String categoryName = category.getName();
                // 把分类名称保存到dto
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        // 保存到dtopage
        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    @Override
    @Transactional
    public R<String> updateDishWithFlavor(DishDto dishDto) {
        // 更新 dish 表基本信息
        this.updateById(dishDto);
        // 清理当前菜品的 flavor 信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorMapper.delete(queryWrapper);
        // 重新提交 flavor 信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
            dishFlavorMapper.insert(flavor);
        }

//        flavors.stream().map((item) -> {
//            item.setDishId(dishDto.getId());
//            return item;
//
//        }).collect(Collectors.toList());
//        dishFlavorService.saveBatch(flavors);
        return R.success("修改成功");
    }

    @Override
    public R<DishDto> getDishInfoById(Long id) {
        // 查询数据库
        // 根据 id 查询 对应的菜品信息
        // 查询dish dto
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        // 拷贝到dishDto
        BeanUtils.copyProperties(dish, dishDto);
        // 查询口味列表 ， 根据dish id 查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(queryWrapper);
        dishDto.setFlavors(dishFlavors);
        return R.success(dishDto);
    }

    @Override
    public R<String> deleteDish(List<Long> ids) {
        // 查菜品的状态，是否可以删除（）
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.in(Dish::getId, ids);
        dishQueryWrapper.eq(Dish::getStatus, 1);

        int count = this.count(dishQueryWrapper);
        if (count > 0) {
            throw new CustomException("菜品正在售卖中！不能删除！");
        }

        // 删除dish
        dishMapper.deleteBatchIds(ids);
        // 删除flavor
        LambdaQueryWrapper<DishFlavor> dishFlavorQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorMapper.delete(dishFlavorQueryWrapper);
        return R.success("删除菜品成功！");
    }

    @Override
    public R<List<Dish>> getDishInfoByCondition(Dish dish) {
        // Category 分类信息（type为1表示菜品, 2表示套餐）
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        // 展示启用的菜品或者套餐
        queryWrapper.eq(Dish::getStatus,1);
        // 按时间排序
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        // 查询符合条件的dish
        List<Dish> dishList = dishMapper.selectList(queryWrapper);

        return R.success(dishList);
    }

    @Override
    public R<String> sellStatus(Integer status, List<Long> ids) {
        // 根据 id 查询对应的dish
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);
        List<Dish> dishList = dishMapper.selectList(queryWrapper);
        // 将状态设置位0
        for (Dish dish : dishList) {
            if (dish != null) {
                dish.setStatus(status);
                dishMapper.updateById(dish);
            }
        }
        return R.success("售卖状态修改成功");
    }
}
