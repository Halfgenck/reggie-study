package org.hgc.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.hgc.reggie.common.R;
import org.hgc.reggie.dto.SetmealDto;
import org.hgc.reggie.service.SetmealDishService;
import org.hgc.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hgc
 * @version 1.0
 * @date 2023/4/29/0029 22:57
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> saveSetmeal(@RequestBody SetmealDto setmealDto) {
        return setmealService.saveSetmeal(setmealDto);
    }

    /**
     * 套餐隔离-分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageBySetmeal(int page, int pageSize, String name) {
        log.info("page = {},pageSize = {},name = {}", page, pageSize, name);
        return setmealService.pageBySetmeal(page, pageSize, name);
    }

    /**
     * 起售套餐、停售套餐
     * 请求 URL: http://localhost:8080/setmeal/status/0?ids=1415580119015145474
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> sellStatus(@PathVariable("status") Integer status, @RequestParam List<Long> ids) {
        return setmealService.sellStatus(status, ids);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     *
     * @@RequestParam: 用于将请求参数区数据映射到功能处理方法的参数上
     */
    @DeleteMapping
    public R<String> deleteSetmeal(@RequestParam List<Long> ids) {
        log.info("ids:{}",ids);
        return setmealService.deleteSetmeal(ids);
    }
}
