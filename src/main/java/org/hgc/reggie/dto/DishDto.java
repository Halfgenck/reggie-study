package org.hgc.reggie.dto;

import lombok.Data;
import org.hgc.reggie.entity.Dish;
import org.hgc.reggie.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
