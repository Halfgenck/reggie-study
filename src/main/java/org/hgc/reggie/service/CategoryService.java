package org.hgc.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hgc.reggie.common.R;
import org.hgc.reggie.entity.Category;

import java.util.List;

/**
 * @author hgc
 * @version 1.0
 * @date 2023/4/28/0028 21:38
 */
public interface CategoryService extends IService<Category> {
    R<String> saveCategory(Category category);

    R<Page> pageSort(int page, int pageSize);

    R<String> deleteSort(Long id);

    R<String> updateSort(Category category);

    R<List<Category>> getListInfo(Category category);
}
