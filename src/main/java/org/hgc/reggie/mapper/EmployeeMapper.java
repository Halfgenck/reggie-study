package org.hgc.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hgc.reggie.entity.Employee;

/**
 * @author hgc
 * @version 1.0
 * @date 2023/4/28/0028 0:10
 */

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
