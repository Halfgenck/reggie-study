package org.hgc.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hgc.reggie.common.R;
import org.hgc.reggie.dto.SetmealDto;
import org.hgc.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author hgc
 * @version 1.0
 * @date 2023/4/28/0028 21:38
 */
public interface SetmealService extends IService<Setmeal> {
    R<String> saveSetmeal(SetmealDto setmealDto);

    R<Page> pageBySetmeal(int page, int pageSize, String name);

    R<String> sellStatus(Integer status, List<Long> ids);

    R<String> deleteSetmeal(List<Long> ids);
}
