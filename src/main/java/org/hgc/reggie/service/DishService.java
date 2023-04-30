package org.hgc.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hgc.reggie.common.R;
import org.hgc.reggie.dto.DishDto;
import org.hgc.reggie.entity.Dish;

import java.util.List;

/**
 * @author hgc
 * @version 1.0
 * @date 2023/4/28/0028 21:38
 */
public interface DishService extends IService<Dish> {
    // 新增菜品，同时插入对应的口味数据，操作两张表
    R<String> saveDishWithFlavor(DishDto dishDto);

    R<Page> pageByDish(int page, int pageSize, String name);

    R<String> updateDishWithFlavor(DishDto dishDto);

    R<DishDto> getDishInfoById(Long id);

    R<String> deleteDish(List<Long> ids);

    R<List<Dish>> getDishInfoByCondition(Dish dish);

    R<String> sellStatus(Integer status, List<Long> ids);
}
