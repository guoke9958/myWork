package com.xa.qyw.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
   
/** 
 * Ãô¸Ð×Ö´Ê´¦ÀíÀà 
 * @author Wiker 
 * @date 2010-1-11 ÏÂÎç10:51:30 
 */ 
public class BadWord {  
   
    private final static File wordfilter = new File("D:/wordfilter.txt");  
   
    private static long lastModified = 0L;  
    private static List<String> words = new ArrayList<String>();  
    
    
    static {
    	words = new ArrayList<String>();  
    	checkReload();
    }
    
       
    private static void checkReload(){  
        if(wordfilter.lastModified() > lastModified){  
            synchronized(BadWord.class){  
                try{  
                    lastModified = wordfilter.lastModified();  
                    LineIterator lines = FileUtils.lineIterator(wordfilter);  
                    while(lines.hasNext()){  
                        String line = lines.nextLine();  
                        if(!StringUtils.isEmpty(line))  
                            words.add(line.trim().toLowerCase());  
                    }  
                }catch(IOException e){  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
       
    /** 
     * ¼ì²éÃô¸Ð×ÖÄÚÈÝ 
     * @param contents 
     */ 
    public static String check(String ...contents) {  
        if(!wordfilter.exists())  
            return null;  
        for(String word : words){  
            for(String content : contents)  
                if(content!=null && content.indexOf(word) >= 0)  
                    return word;  
        }  
        return null;  
    }  
       
    /** 
     * ¼ì²é×Ö·û´®ÊÇ·ñ°üº¬Ãô¸Ð´Ê 
     * 
     * @param content 
     * @return 
     */ 
    public static boolean isContain(String content) {  
        if(!wordfilter.exists())  
            return false;  
        for(String word : words){  
            if(content!=null && content.indexOf(word) >= 0)  
                return true;  
        }  
        return false;  
    }  
       
    /** 
     * Ìæ»»µô×Ö·û´®ÖÐµÄÃô¸Ð´Ê 
     * 
     * @param str µÈ´ýÌæ»»µÄ×Ö·û´® 
     * @param replaceChar Ìæ»»×Ö·û 
     * @return 
     */ 
    public static String replace(String str,String replaceChar){  
    	
    	if(words.size()==0){
    		checkReload();
    	}
    	
        for(String word : words){  
            if(str.indexOf(word)>=0){  
                String reChar = "";  
                for(int i=0;i<word.length();i++){  
                    reChar += replaceChar;  
                }  
                str = str.replaceAll(word, reChar);  
            }  
        }  
        return str;  
    }  
       
    public static List<String> lists() {  
        return words;  
    }  
       
    /** 
     * Ìí¼ÓÃô¸Ð´Ê 
     * 
     * @param word 
     * @throws IOException 
     */ 
    public static void add(String word) throws IOException {  
        word = word.toLowerCase();  
        if(!words.contains(word)){  
            words.add(word);  
            FileUtils.writeLines(wordfilter,words);  
            lastModified = wordfilter.lastModified();  
        }  
    }  
   
    /** 
     * É¾³ýÃô¸Ð´Ê 
     * 
     * @param word 
     * @throws IOException 
     */ 
    public static void delete(String word) throws IOException {  
        word = word.toLowerCase();  
        words.remove(word);  
        FileUtils.writeLines(wordfilter, words);  
        lastModified = wordfilter.lastModified();  
    }  
       
    public static void main(String[] args) throws Exception{  
        System.out.println(BadWord.replace("ÄãÂèµÄ","*"));  
        System.out.println(BadWord.isContain("µº"));  
    }  
       
}  