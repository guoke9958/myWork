package com.xa.qyw.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
public class DuXMLDoc {
    public List xmlElements(String xmlDoc) {
    	
    	try {
    		 //����һ���µ��ַ���
        	FileInputStream is = new FileInputStream("D:/�½��ı��ĵ�.txt");
            //�����µ�����ԴSAX ��������ʹ�� InputSource ������ȷ����ζ�ȡ XML ����
            InputSource source = new InputSource(is);
            //����һ���µ�SAXBuilder
            SAXBuilder sb = new SAXBuilder();
            try {
                //ͨ������Դ����һ��Document
                Document doc = sb.build(source);
                //ȡ�ĸ�Ԫ��
                Element root = doc.getRootElement();
                System.out.println(root.getName());//�����Ԫ�ص����ƣ����ԣ�
                //�õ���Ԫ��������Ԫ�صļ���
                List jiedian = root.getChildren();
                //���XML�е������ռ䣨XML��δ����ɲ�д��
                Namespace ns = root.getNamespace();
                Element et = null;
                for(int i=0;i<jiedian.size();i++){
                    et = (Element) jiedian.get(i);//ѭ�����εõ���Ԫ��
                   
                    System.out.println(et.getChild("users_id",ns).getText());
                    System.out.println(et.getChild("users_address",ns).getText());
                }
               
                et = (Element) jiedian.get(0);
                List zjiedian = et.getChildren();
                for(int j=0;j<zjiedian.size();j++){
                    Element xet = (Element) zjiedian.get(j);
                    System.out.println(xet.getName());
                }
            } catch (JDOMException e) {
                // TODO �Զ����� catch ��
                e.printStackTrace();
            } catch (IOException e) {
                // TODO �Զ����� catch ��
                e.printStackTrace();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
       
        return null;
    }
    public static void main(String[] args){
        DuXMLDoc doc = new DuXMLDoc();
        String xml = "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+
        "<Result xmlns=\"http://www.fiorano.com/fesb/activity/DBQueryOnInput2/Out\">"+
           "<row resultcount=\"1\">"+
              "<users_id>1001     </users_id>"+
              "<users_name>wangwei   </users_name>"+
              "<users_group>80        </users_group>"+
              "<users_address>1001��   </users_address>"+
           "</row>"+
           "<row resultcount=\"1\">"+
              "<users_id>1002     </users_id>"+
              "<users_name>wangwei   </users_name>"+
              "<users_group>80        </users_group>"+
              "<users_address>1002��   </users_address>"+
           "</row>"+
        "</Result>";
        doc.xmlElements(xml);
    }
}
