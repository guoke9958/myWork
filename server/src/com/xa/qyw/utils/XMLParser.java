package com.xa.qyw.utils;

import java.io.InputStream;
import java.util.List;

public interface XMLParser {
	<T> List<T> xmlParse(InputStream is);

	// ππ‘Ïxml
	<T> String xmlSerializer(List<T> list);
}
