package com.xa.qyw.service;

import java.util.List;

import com.xa.qyw.entiy.SimpUser;
import com.xa.qyw.entiy.SimpleUser;
import com.xa.qyw.entiy.User;

public interface UserService {
	
	public int getMaxId();

	public User getUserById(User user);
	
	public List<SimpUser> getAllUser();
	
	public void updatePasswrod(User user);
	
	public void addUser(User user);
	
	public void updateUserName(SimpUser user);
	
	public void updateType(User user);
	
	public SimpleUser getDoctorName(int id);
	
	public SimpleUser getUserName(String id);
	
	public SimpUser getUserByUserName(String userName);
	
	public void addUserInfo(User user);
	
	public void updateUserInfo(User user);

	public void updateUserPhoto(User user);
	
	public User queryUser(String userId);
	
	public void deleteUser(String userId);
	
	public void updateInvitePhone(User user);
	
	public void updateUserLoginCount(User user);
}
