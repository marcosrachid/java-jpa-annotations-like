package com.ef.util;

/**
 * Utils to handle any String transformation and formatters
 * 
 * @author marcosrachid
 *
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
	
	/**
	 * 
	 * @param word
	 * @return
	 */
	public static String removeQuotes(String word) {
		return word.replaceAll("^\"|\"$", "");
	}

}
