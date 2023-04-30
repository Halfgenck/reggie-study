package org.hgc.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hgc.reggie.entity.AddressBook;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
