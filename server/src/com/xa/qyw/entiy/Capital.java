package com.xa.qyw.entiy;

import java.util.List;

public class Capital {

	private UserCapital userCapital;
	private List<CapitalHistory> listHistory;
	
	public Capital() {
		super();
	}

	public UserCapital getUserCapital() {
		return userCapital;
	}

	public void setUserCapital(UserCapital userCapital) {
		this.userCapital = userCapital;
	}

	public List<CapitalHistory> getListHistory() {
		return listHistory;
	}

	public void setListHistory(List<CapitalHistory> listHistory) {
		this.listHistory = listHistory;
	}
	
	
}
