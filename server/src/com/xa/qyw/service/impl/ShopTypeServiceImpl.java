package com.xa.qyw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.TypeDao;
import com.xa.qyw.entiy.ShopType;
import com.xa.qyw.service.ShopTypeService;

@Service("shopTypeService")
public class ShopTypeServiceImpl implements ShopTypeService{


	@Resource(name = "typeDao")
	private TypeDao typeDao;
	
	@Override
	public List<ShopType> getAllType() {
		return typeDao.getAllType();
	}

}
