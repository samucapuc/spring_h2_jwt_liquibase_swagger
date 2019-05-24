package com.bancointer.samuel.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;

public class Utils {

	public static String removeSpecialCaracters(String s) {
		return s.replaceAll("[^a-zA-Z0-9\\s]", "");
	}
	
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(removeSpecialCaracters(s), "UTF-8");
		}catch (UnsupportedEncodingException e) {
			return StringUtils.EMPTY;
		}
	}	
}
