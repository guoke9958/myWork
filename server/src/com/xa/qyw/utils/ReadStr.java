package com.xa.qyw.utils;

import java.io.BufferedReader;
import java.io.FileReader;

public class ReadStr {

	public static void main(String[] args) {
		try {
			String filePath = "D:/×¨¼ÒºÅÂë.txt";

			FileReader fr = new FileReader(filePath);
			BufferedReader bufferedreader = new BufferedReader(fr);
			String instring;
			while ((instring = bufferedreader.readLine()) != null) {
				if (0 != instring.length()) {
					System.out.println(instring);
				}
			}
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
