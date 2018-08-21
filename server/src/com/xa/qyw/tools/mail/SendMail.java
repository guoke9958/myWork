package com.xa.qyw.tools.mail;

public class SendMail {
	
	public static void main(String[] args) {  
//        send_163("你好的");  
    }  
      
    // 163邮箱  
    public static void send_163(String title,String content) {  
        // 这个类主要是设置邮件  
        MailSenderInfo mailInfo = new MailSenderInfo();  
        mailInfo.setMailServerHost("smtp.163.com");  
        mailInfo.setMailServerPort("25");  
        mailInfo.setValidate(true);  
        mailInfo.setUserName("ftyiliao@163.com"); // 实际发送者  
        mailInfo.setPassword("yanglelele");// 您的邮箱密码  
        mailInfo.setFromAddress("ftyiliao@163.com"); // 设置发送人邮箱地址  
        mailInfo.setToAddress("148169117@qq.com"); // 设置接受者邮箱地址  
        mailInfo.setSubject(title);  
        mailInfo.setContent(content);  
        // 这个类主要来发送邮件  
        SimpleMailSender sms = new SimpleMailSender();  
        sms.sendTextMail(mailInfo); // 发送文体格式  
    }  
    
    public static void sendAddDoctor(final String content){
    	
    	new Thread(){
    		public void run() {
    			try {
    				send_163("平台有新的医生加入，需要客服人员登录后台对新的医生进行审核",content);
				} catch (Exception e) {
					e.printStackTrace();
				}
    		};
    	}.start();
    	
    }
    
    public static void sendAddUser(final String content){
    	
    	new Thread(){
    		public void run() {
    			try {
    				send_163("平台有新的用户加入核",content);
				} catch (Exception e) {
					e.printStackTrace();
				}
    		};
    	}.start();
    }
    
    public static void addHospital(final String content){
    	
    	
    	new Thread(){
    		public void run() {
    			try {
    				send_163("平台有人添加了新的医院加入", content);
				} catch (Exception e) {
					e.printStackTrace();
				}
    		};
    	}.start();
    }
    
    public static void updateHospitalGrade(final String content){
    	
    	
    	new Thread(){
    		public void run() {
    			try {
    				send_163("有医生对医院的等级进行了编辑，请后台审核", content);
				} catch (Exception e) {
					e.printStackTrace();
				}
    		};
    	}.start();
    }
    
}
