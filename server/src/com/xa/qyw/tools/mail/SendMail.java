package com.xa.qyw.tools.mail;

public class SendMail {
	
	public static void main(String[] args) {  
//        send_163("��õ�");  
    }  
      
    // 163����  
    public static void send_163(String title,String content) {  
        // �������Ҫ�������ʼ�  
        MailSenderInfo mailInfo = new MailSenderInfo();  
        mailInfo.setMailServerHost("smtp.163.com");  
        mailInfo.setMailServerPort("25");  
        mailInfo.setValidate(true);  
        mailInfo.setUserName("ftyiliao@163.com"); // ʵ�ʷ�����  
        mailInfo.setPassword("yanglelele");// ������������  
        mailInfo.setFromAddress("ftyiliao@163.com"); // ���÷����������ַ  
        mailInfo.setToAddress("148169117@qq.com"); // ���ý����������ַ  
        mailInfo.setSubject(title);  
        mailInfo.setContent(content);  
        // �������Ҫ�������ʼ�  
        SimpleMailSender sms = new SimpleMailSender();  
        sms.sendTextMail(mailInfo); // ���������ʽ  
    }  
    
    public static void sendAddDoctor(final String content){
    	
    	new Thread(){
    		public void run() {
    			try {
    				send_163("ƽ̨���µ�ҽ�����룬��Ҫ�ͷ���Ա��¼��̨���µ�ҽ���������",content);
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
    				send_163("ƽ̨���µ��û������",content);
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
    				send_163("ƽ̨����������µ�ҽԺ����", content);
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
    				send_163("��ҽ����ҽԺ�ĵȼ������˱༭�����̨���", content);
				} catch (Exception e) {
					e.printStackTrace();
				}
    		};
    	}.start();
    }
    
}
