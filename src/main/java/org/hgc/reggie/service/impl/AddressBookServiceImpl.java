package org.hgc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hgc.reggie.common.BaseContext;
import org.hgc.reggie.common.R;
import org.hgc.reggie.entity.AddressBook;
import org.hgc.reggie.mapper.AddressBookMapper;
import org.hgc.reggie.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
        implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public R<AddressBook> saveAddressBook(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.insert(addressBook);
        return R.success(addressBook);
    }

    @Override
    @Transactional
    public R<AddressBook> setDefaultAddress(AddressBook addressBook) {
        // 根据id查询设置的地址,设置位默认标识
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.eq(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookMapper.update(null, wrapper);

        // 然后在更新传来的新默认地址
        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBookMapper.updateById(addressBook);
        return R.success(addressBook);
    }

    @Override
    public R<AddressBook> getAddressById(Long id) {
        AddressBook addressBook = addressBookMapper.selectById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    @Override
    public R<AddressBook> getDefaultAddress() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookMapper.selectOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }

    @Override
    public R<List<AddressBook>> selectAllAddress(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        List<AddressBook> addressBooks = addressBookMapper.selectList(queryWrapper);
        return R.success(addressBooks);
    }

    @Override
    public R<String> deleteAddressByIds(String id) {
        // 根据id删除指定地址
        addressBookMapper.deleteById(id);
        return R.success("删除成功");
    }

    @Override
    public R<String> updateAddress(AddressBook addressBook) {
        addressBookMapper.updateById(addressBook);
        return R.success("更新成功");
    }
}
