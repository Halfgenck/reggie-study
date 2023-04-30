package com.fafa.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fafa.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
