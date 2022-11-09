package com.amnesie.reggie.service.impl;

import com.amnesie.reggie.entity.AddressBook;
import com.amnesie.reggie.mapper.AddressBookMapper;
import com.amnesie.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-08
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
