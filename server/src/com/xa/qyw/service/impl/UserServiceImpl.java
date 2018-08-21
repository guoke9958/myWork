package com.xa.qyw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.UserDao;
import com.xa.qyw.entiy.SimpUser;
import com.xa.qyw.entiy.SimpleUser;
import com.xa.qyw.entiy.User;
import com.xa.qyw.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Resource(name = "userDao")
	private UserDao userDao;
	
	@Override
	public int getMaxId() {
		return userDao.getMaxId();
	}

	@Override
	public User getUserById(User user) {
		return userDao.getUserById(user);
	}

	@Override
	public List<SimpUser> getAllUser() {
		return userDao.getAllUser();
	}

	@Override
	public void updatePasswrod(User user) {
		userDao.updatePasswrod(user);
	}

	@Override
	public void addUser(User user) {
		userDao.addUser(user);
	}

	@Override
	public void updateUserName(SimpUser user) {
		userDao.updateUserName(user);
	}

	@Override
	public void updateType(User user) {
		userDao.updatePasswrod(user);
	}

	@Override
	public SimpleUser getDoctorName(int id) {
		return userDao.getDoctorName(id);
	}

	@Override
	public SimpleUser getUserName(String id) {
		return userDao.getUserName(id);
	}
	
	public SimpUser getUserByUserName(String userName){
		return userDao.getUserByUserName(userName);
	}

	@Override
	public void addUserInfo(User user) {
		userDao.addUserInfo(user);
		
	}

	@Override
	public void updateUserInfo(User user) {
		userDao.updateUserInfo(user);
		
	}

	@Override
	public void updateUserPhoto(User user) {
		userDao.updateUserPhoto(user);
	}

	@Override
	public User queryUser(String userId) {
		return userDao.queryUser(userId);
	}

	@Override
	public void deleteUser(String userId) {
		userDao.deleteUser(userId);
	}

	@Override
	public void updateInvitePhone(User user) {
		userDao.updateInvitePhone(user);
	}

	@Override
	public void updateUserLoginCount(User user) {
		userDao.updateUserLoginCount(user);
	}

}
