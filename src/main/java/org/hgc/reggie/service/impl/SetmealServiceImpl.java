package org.hgc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hgc.reggie.common.CustomException;
import org.hgc.reggie.common.R;
import org.hgc.reggie.dto.SetmealDto;
import org.hgc.reggie.entity.Category;
import org.hgc.reggie.entity.Setmeal;
import org.hgc.reggie.entity.SetmealDish;
import org.hgc.reggie.mapper.CategoryMapper;
import org.hgc.reggie.mapper.SetmealDishMapper;
import org.hgc.reggie.mapper.SetmealMapper;
import org.hgc.reggie.service.SetmealDishService;
import org.hgc.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hgc
 * @version 1.0
 * @date 2023/4/28/0028 22:09
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    @Transactional
    public R<String> saveSetmeal(SetmealDto setmealDto) {
        // 保存基本套餐信息
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
//        setmealDishes.stream().map((item) -> {
//            item.setSetmealId(setmealDto.getId());
//            return item;
//        }).collect(Collectors.toList());
//        setmealDishService.saveBatch(setmealDishes);
        // 向 setmeal_dish 表插入多条数据
        for (SetmealDish setmealDish: setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
            setmealDishMapper.insert(setmealDish);
        }

        return R.success("条件成功！");
    }

    @Override
    public R<Page> pageBySetmeal(int page, int pageSize, String name) {
        Page<Setmeal> mealPage = new Page<>(page, pageSize);
        Page<SetmealDto> mealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealMapper.selectPage(mealPage, queryWrapper);

        // pageInfo 里面的 records 是没有categoryName的！
        // 所以 records 直接 忽略，要在后面手动赋值
        BeanUtils.copyProperties(mealPage, mealDtoPage, "records");
        List<Setmeal> records = mealPage.getRecords();

        // 处理 records
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            // item是一条records(list)记录，将Setmeal中的字段赋值给 setmealDto
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        mealDtoPage.setRecords(list);

        return R.success(mealDtoPage);
    }

    @Override
    public R<String> sellStatus(Integer status, List<Long> ids) {
        // 根据id查询对应套餐
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        // 查询启用的套餐
        queryWrapper.eq(Setmeal::getStatus,1);
        List<Setmeal> setmealList = setmealMapper.selectList(queryWrapper);
        for (Setmeal setmeal : setmealList) {
            setmeal.setStatus(status);
            setmealMapper.updateById(setmeal);
        }
        return R.success("售卖状态修改成功");

    }

    @Override
    public R<String> deleteSetmeal(List<Long> ids) {
        // 查询套餐信息
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId,ids);
        // 获取启用的未禁用的套餐
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(setmealLambdaQueryWrapper);
        if (count > 0) {
            throw new CustomException("菜品正在售卖中！不能删除！");
        }
        // 执行删除操作 setmeal
        setmealMapper.deleteBatchIds(ids);
        // 删除 setmeal dish
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishMapper.delete(setmealDishLambdaQueryWrapper);

        return R.success("删除菜品成功！");
    }
}
