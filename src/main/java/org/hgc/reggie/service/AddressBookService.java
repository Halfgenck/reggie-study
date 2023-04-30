package org.hgc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hgc.reggie.common.R;
import org.hgc.reggie.entity.AddressBook;

import java.util.List;

public interface AddressBookService extends IService<AddressBook> {
    R<AddressBook> saveAddressBook(AddressBook addressBook);

    R<AddressBook> setDefaultAddress(AddressBook addressBook);


    R<AddressBook> getAddressById(Long id);

    R<AddressBook> getDefaultAddress();

    R<List<AddressBook>> selectAllAddress(AddressBook addressBook);

    R<String> deleteAddressByIds(String ids);

    R<String> updateAddress(AddressBook addressBook);
}
