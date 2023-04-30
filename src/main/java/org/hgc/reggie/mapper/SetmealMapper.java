package org.hgc.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hgc.reggie.entity.Dish;

/**
 * @author hgc
 * @version 1.0
 * @date 2023/4/28/0028 22:01
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
