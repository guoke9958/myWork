<%
/* *
 *���ܣ��������֧�����˻����ܽӿڽ���ҳ
 *�汾��3.3
 *���ڣ�2012-08-14
 *˵����
 *���´���ֻ��Ϊ�˷����̻����Զ��ṩ���������룬�̻����Ը����Լ���վ����Ҫ�����ռ����ĵ���д,����һ��Ҫʹ�øô��롣
 *�ô������ѧϰ���о�֧�����ӿ�ʹ�ã�ֻ���ṩһ���ο���

 *************************ע��*****************
 *������ڽӿڼ��ɹ������������⣬���԰��������;�������
 *1���̻��������ģ�https://b.alipay.com/support/helperApply.htm?action=consultationApply�����ύ���뼯��Э�������ǻ���רҵ�ļ�������ʦ������ϵ��Э�����
 *2���̻��������ģ�http://help.alipay.com/support/232511-16307/0-16307.htm?sh=Y&info_type=9��
 *3��֧������̳��http://club.alipay.com/read-htm-tid-8681712.html��
 *�������ʹ����չ���������չ���ܲ�������ֵ��
 **********************************************
 */
%>
<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@ page import="com.alipay.config.*"%>
<%@ page import="com.alipay.util.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gbk">
		<title>֧�����������֧�����˻����ܽӿ�</title>
	</head>
	<%
		////////////////////////////////////�������//////////////////////////////////////

		//�������첽֪ͨҳ��·��
		String notify_url = "http://�̻����ص�ַ/batch_trans_notify-JAVA-GBK/notify_url.jsp";
		//��http://��ʽ������·������������?id=123�����Զ������
		//�����˺�
		String email = new String(request.getParameter("WIDemail").getBytes("ISO-8859-1"),"GBK");
		//����
		//�����˻���
		String account_name = new String(request.getParameter("WIDaccount_name").getBytes("ISO-8859-1"),"GBK");
		//�������֧�����˺�����ʵ������˾֧�����˺��ǹ�˾����
		//���������
		String pay_date = new String(request.getParameter("WIDpay_date").getBytes("ISO-8859-1"),"GBK");
		//�����ʽ����[4λ]��[2λ]��[2λ]���磺20100801
		//���κ�
		String batch_no = new String(request.getParameter("WIDbatch_no").getBytes("ISO-8859-1"),"GBK");
		//�����ʽ����������[8λ]+���к�[3��16λ]���磺201008010000001
		//�����ܽ��
		String batch_fee = new String(request.getParameter("WIDbatch_fee").getBytes("ISO-8859-1"),"GBK");
		//���������detail_data��ֵ�����н����ܺ�
		//�������
		String batch_num = new String(request.getParameter("WIDbatch_num").getBytes("ISO-8859-1"),"GBK");
		//���������detail_data��ֵ�У���|���ַ����ֵ�������1�����֧��1000�ʣ�����|���ַ����ֵ�����999����
		//������ϸ����
		String detail_data = new String(request.getParameter("WIDdetail_data").getBytes("ISO-8859-1"),"GBK");
		//�����ʽ����ˮ��1^�տ�ʺ�1^��ʵ����^������1^��ע˵��1|��ˮ��2^�տ�ʺ�2^��ʵ����^������2^��ע˵��2....
		
		
		//////////////////////////////////////////////////////////////////////////////////
		
		//������������������
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "batch_trans_notify");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("email", email);
		sParaTemp.put("account_name", account_name);
		sParaTemp.put("pay_date", pay_date);
		sParaTemp.put("batch_no", batch_no);
		sParaTemp.put("batch_fee", batch_fee);
		sParaTemp.put("batch_num", batch_num);
		sParaTemp.put("detail_data", detail_data);
		
		//��������
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","ȷ��");
		out.println(sHtmlText);
	%>
	<body>
	</body>
</html>