package com.xa.qyw.utils;

import java.io.InputStream;
import java.util.List;

public interface XMLParser {
	<T> List<T> xmlParse(InputStream is);

	// ����xml
	<T> String xmlSerializer(List<T> list);
}
