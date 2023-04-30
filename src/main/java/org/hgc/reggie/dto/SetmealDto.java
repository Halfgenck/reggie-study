package org.hgc.reggie.dto;

import lombok.Data;
import org.hgc.reggie.entity.Setmeal;
import org.hgc.reggie.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
