package org.hgc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.hgc.reggie.common.BaseContext;
import org.hgc.reggie.common.R;
import org.hgc.reggie.entity.AddressBook;
import org.hgc.reggie.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址簿管理
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增
     *
     * @return
     */
    @PostMapping
    @Transactional
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
//        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);

        return addressBookService.saveAddressBook(addressBook);
    }

    /**
     * 设置默认地址
     */
    @PutMapping("default")
    @Transactional
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        return addressBookService.setDefaultAddress(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R<AddressBook> getById(@PathVariable Long id) {

        return addressBookService.getAddressById(id);
    }

    /**
     * 查询默认地址
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault() {

        return addressBookService.getDefaultAddress();
    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        return addressBookService.selectAllAddress(addressBook);
    }

    /**
     * 根据id删除用户地址
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteAddressById(String id) {
        log.info("id:{}", id);
        return addressBookService.deleteAddressByIds(id);
    }

    /**
     * 更新用户地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<String> updateAddress(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        return addressBookService.updateAddress(addressBook);
    }


}
