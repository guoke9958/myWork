package com.cn.xa.qyw.entiy;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserInfo implements Serializable{
	private int id;   //编号
	private String userId;  // 用户id
	private int type;       //用户会员类型
	private int userSex;    //性别 1 男 2 女
	private String nickName; //昵称
	private String trueName; //真实姓名
	private String photo;  //图像
	private String hospitalName;  //所在医院名称
	private String professionalTitle; //职称
	private String goodAt;  //擅长
	private String phone;  //电话
	private String intro;  //简介
	private String departmentName; //所在科室
	private String duty;   //职务
	private String province; //省
	private String city;   //城市
	private String area;   //区
	private String detailAddress;  //详细地址
	private String alipayAccount;  //支付宝账户
	private String phoneAdvisory; //电话咨询费用
	private String orderAadvisory; // 预约就诊费用
	private String noteOrderAdvisory; // 短信咨询费用
	private String state; //是否在线
	private String token;  //支付票据
	private int hospitalId;  //医院等级id
	private int departmentId; //科室id
	private String normalAdvisory; // 普通咨询费用
	private String bigAdvisory;  //大病会诊费用
	private String familyDoctor; //家庭医生费用
	private String healthManager; //健康管理
	private int isPass;        //审核状态
	private String hospitalGrade; //医院等级名称
	private int hospitalPass;  //医院通过
	private String brithday;   //生日
	private String cardId;   //卡号
	private Integer balance;   //零钱余额
	private int hospitalType; //医院类型id
	private Timestamp createTime;
	
	public UserInfo() {
		super();
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUserSex() {
		return userSex;
	}

	public void setUserSex(int userSex) {
		this.userSex = userSex;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getProfessionalTitle() {
		return professionalTitle;
	}

	public void setProfessionalTitle(String professionalTitle) {
		this.professionalTitle = professionalTitle;
	}

	public String getGoodAt() {
		return goodAt;
	}

	public void setGoodAt(String goodAt) {
		this.goodAt = goodAt;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getPhoneAdvisory() {
		return phoneAdvisory;
	}

	public void setPhoneAdvisory(String phoneAdvisory) {
		this.phoneAdvisory = phoneAdvisory;
	}

	public String getOrderAadvisory() {
		return orderAadvisory;
	}

	public void setOrderAadvisory(String orderAadvisory) {
		this.orderAadvisory = orderAadvisory;
	}

	public String getNoteOrderAdvisory() {
		return noteOrderAdvisory;
	}

	public void setNoteOrderAdvisory(String noteOrderAdvisory) {
		this.noteOrderAdvisory = noteOrderAdvisory;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}


	public String getProvince() {
		return province;
	}


	public void setProvince(String province) {
		this.province = province;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getNormalAdvisory() {
		return normalAdvisory;
	}


	public void setNormalAdvisory(String normalAdvisory) {
		this.normalAdvisory = normalAdvisory;
	}


	public String getBigAdvisory() {
		return bigAdvisory;
	}


	public void setBigAdvisory(String bigAdvisory) {
		this.bigAdvisory = bigAdvisory;
	}


	public String getFamilyDoctor() {
		return familyDoctor;
	}


	public void setFamilyDoctor(String familyDoctor) {
		this.familyDoctor = familyDoctor;
	}


	public String getHealthManager() {
		return healthManager;
	}


	public void setHealthManager(String healthManager) {
		this.healthManager = healthManager;
	}


	public int getIsPass() {
		return isPass;
	}


	public void setIsPass(int isPass) {
		this.isPass = isPass;
	}


	public String getHospitalGrade() {
		return hospitalGrade;
	}


	public void setHospitalGrade(String hospitalGrade) {
		this.hospitalGrade = hospitalGrade;
	}


	public int getHospitalPass() {
		return hospitalPass;
	}


	public void setHospitalPass(int hospitalPass) {
		this.hospitalPass = hospitalPass;
	}


	public Timestamp getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}


	public String getBrithday() {
		return brithday;
	}


	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}


	public int getHospitalType() {
		return hospitalType;
	}


	public void setHospitalType(int hospitalType) {
		this.hospitalType = hospitalType;
	}
	
	
	
	

}
