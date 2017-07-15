/**
 * 
 */
package com.yingt.service.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * @author duminghui
 * 
 */
public class StringUtils
{

	/**
	 * is null or its length is 0 or it is made by space
	 *
	 * <pre>
	 * isBlank(null) = true;
	 * isBlank(&quot;&quot;) = true;
	 * isBlank(&quot;  &quot;) = true;
	 * isBlank(&quot;a&quot;) = false;
	 * isBlank(&quot;a &quot;) = false;
	 * isBlank(&quot; a&quot;) = false;
	 * isBlank(&quot;a b&quot;) = false;
	 * </pre>
	 *
	 * @param str
	 * @return if string is null or its size is 0 or it is made by space, return true, else return false.
	 */
	public static boolean isBlank(String str) {
		return (str == null || str.trim().length() == 0);
	}

	/**
	 * is null or its length is 0
	 *
	 * <pre>
	 * isEmpty(null) = true;
	 * isEmpty(&quot;&quot;) = true;
	 * isEmpty(&quot;  &quot;) = false;
	 * </pre>
	 *
	 * @param str
	 * @return if string is null or its size is 0, return true, else return false.
	 */
	public static boolean isEmpty(CharSequence str) {
		return (str == null || str.length() == 0);
	}

	/**
	 * get length of CharSequence
	 *
	 * <pre>
	 * length(null) = 0;
	 * length(\"\") = 0;
	 * length(\"abc\") = 3;
	 * </pre>
	 *
	 * @param str
	 * @return if str is null or empty, return 0, else return {@link CharSequence#length()}.
	 */
	public static int length(CharSequence str) {
		return str == null ? 0 : str.length();
	}

	/**
	 * null Object to empty string
	 *
	 * <pre>
	 * nullStrToEmpty(null) = &quot;&quot;;
	 * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
	 * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static String nullStrToEmpty(Object str) {
		return (str == null ? "" : (str instanceof String ? (String)str : str.toString()));
	}

	/**
	 * capitalize first letter
	 *
	 * <pre>
	 * capitalizeFirstLetter(null)     =   null;
	 * capitalizeFirstLetter("")       =   "";
	 * capitalizeFirstLetter("2ab")    =   "2ab"
	 * capitalizeFirstLetter("a")      =   "A"
	 * capitalizeFirstLetter("ab")     =   "Ab"
	 * capitalizeFirstLetter("Abc")    =   "Abc"
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static String capitalizeFirstLetter(String str) {
		if (isEmpty(str)) {
			return str;
		}

		char c = str.charAt(0);
		return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
				.append(Character.toUpperCase(c)).append(str.substring(1)).toString();
	}

	/**
	 * encoded in utf-8
	 *
	 * <pre>
	 * utf8Encode(null)        =   null
	 * utf8Encode("")          =   "";
	 * utf8Encode("aa")        =   "aa";
	 * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
	 * </pre>
	 *
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException if an error occurs
	 */
	public static String utf8Encode(String str) {
		if (!isEmpty(str) && str.getBytes().length != str.length()) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
			}
		}
		return str;
	}

	/**
	 * encoded in utf-8, if exception, return defultReturn
	 *
	 * @param str
	 * @param defultReturn
	 * @return
	 */
	public static String utf8Encode(String str, String defultReturn) {
		if (!isEmpty(str) && str.getBytes().length != str.length()) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return defultReturn;
			}
		}
		return str;
	}

	/**
	 * get innerHtml from href
	 *
	 * <pre>
	 * getHrefInnerHtml(null)                                  = ""
	 * getHrefInnerHtml("")                                    = ""
	 * getHrefInnerHtml("mp3")                                 = "mp3";
	 * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
	 * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
	 * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
	 * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
	 * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
	 * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
	 * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
	 * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
	 * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
	 * </pre>
	 *
	 * @param href
	 * @return <ul>
	 *         <li>if href is null, return ""</li>
	 *         <li>if not match regx, return source</li>
	 *         <li>return the last string that match regx</li>
	 *         </ul>
	 */
	public static String getHrefInnerHtml(String href) {
		if (isEmpty(href)) {
			return "";
		}

		String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
		Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
		Matcher hrefMatcher = hrefPattern.matcher(href);
		if (hrefMatcher.matches()) {
			return hrefMatcher.group(1);
		}
		return href;
	}

	/**
	 * process special char in html
	 *
	 * <pre>
	 * htmlEscapeCharsToString(null) = null;
	 * htmlEscapeCharsToString("") = "";
	 * htmlEscapeCharsToString("mp3") = "mp3";
	 * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
	 * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
	 * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
	 * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
	 * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
	 * </pre>
	 *
	 * @param source
	 * @return
	 */
	public static String htmlEscapeCharsToString(String source) {
		return StringUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
				.replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
	}

	/**
	 * transform half width char to full width char
	 *
	 * <pre>
	 * fullWidthToHalfWidth(null) = null;
	 * fullWidthToHalfWidth("") = "";
	 * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
	 * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
	 * </pre>
	 *
	 * @param s
	 * @return
	 */
	public static String fullWidthToHalfWidth(String s) {
		if (isEmpty(s)) {
			return s;
		}

		char[] source = s.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (source[i] == 12288) {
				source[i] = ' ';
				// } else if (source[i] == 12290) {
				// source[i] = '.';
			} else if (source[i] >= 65281 && source[i] <= 65374) {
				source[i] = (char)(source[i] - 65248);
			} else {
				source[i] = source[i];
			}
		}
		return new String(source);
	}

	/**
	 * transform full width char to half width char
	 *
	 * <pre>
	 * halfWidthToFullWidth(null) = null;
	 * halfWidthToFullWidth("") = "";
	 * halfWidthToFullWidth(" ") = new String(new char[] {12288});
	 * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
	 * </pre>
	 *
	 * @param s
	 * @return
	 */
	public static String halfWidthToFullWidth(String s) {
		if (isEmpty(s)) {
			return s;
		}

		char[] source = s.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (source[i] == ' ') {
				source[i] = (char)12288;
				// } else if (source[i] == '.') {
				// source[i] = (char)12290;
			} else if (source[i] >= 33 && source[i] <= 126) {
				source[i] = (char)(source[i] + 65248);
			} else {
				source[i] = source[i];
			}
		}
		return new String(source);
	}

	/**
	 * <p>
	 * Checks if a String is empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.isEmpty(null)      = true
	 * StringUtils.isEmpty("")        = true
	 * StringUtils.isEmpty(" ")       = false
	 * StringUtils.isEmpty("bob")     = false
	 * StringUtils.isEmpty("  bob  ") = false
	 * </pre>
	 * 
	 * <p>
	 * NOTE: This method changed in Lang version 2.0. It no longer trims the
	 * String. That functionality is available in isBlank().
	 * </p>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is empty or null
	 */
	public static boolean isEmpty(String str)
	{
		return str == null || str.length() == 0;
	}

	// Equals
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Compares two Strings, returning <code>true</code> if they are equal.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered to be equal. The comparison is case sensitive.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.equals(null, null)   = true
	 * StringUtils.equals(null, "abc")  = false
	 * StringUtils.equals("abc", null)  = false
	 * StringUtils.equals("abc", "abc") = true
	 * StringUtils.equals("abc", "ABC") = false
	 * </pre>
	 * 
	 * @see String#equals(Object)
	 * @param str1
	 *            the first String, may be null
	 * @param str2
	 *            the second String, may be null
	 * @return <code>true</code> if the Strings are equal, case sensitive, or
	 *         both <code>null</code>
	 */
	public static boolean equals(String str1, String str2)
	{
		return str1 == null ? str2 == null : str1.equals(str2);
	}
  
  /**
	 * <p>
	 * Convert a <code>String</code> to an <code>int</code>, returning
	 * <code>zero</code> if the conversion fails.
	 * </p>
	 * 
	 * @param str
	 *            the string to convert
	 * @return the int represented by the string, or <code>zero</code> if
	 *         conversion fails
	 */
	public static int stringToInt(String str) {
		return stringToInt(str, 0);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to an <code>int</code>, returning a default
	 * value if the conversion fails.
	 * </p>
	 * 
	 * @param str
	 *            the string to convert
	 * @param defaultValue
	 *            the default value
	 * @return the int represented by the string, or the default if conversion
	 *         fails
	 */
	public static int stringToInt(String str, int defaultValue) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}
	public static double stringToDouble(String str) {
		return stringToDouble(str, 0);
	}
	public static double stringToDouble(String str, int defaultValue) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}
	
	public static float stringToFloat(String str) {
		return stringToFloat(str, 0);
	}
	public static float stringToFloat(String str, int defaultValue) {
		try {
			return Float.parseFloat(str);
		} catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}
	
	/**
	 * 十六进制字符串转整形，用于处理颜色十六进制字符串
	 * @param oxStr
	 * @return
	 */
	public static int oxStringToInt(String oxStr){
        String nOxStr = oxStr;
        if(oxStr.contains("0x"))
            nOxStr = oxStr.replaceAll("0x", "");
        else if(oxStr.contains("0X"))
            nOxStr = oxStr.replaceAll("0X", "");
        else if(oxStr.contains("#"))
            nOxStr = oxStr.replaceAll("#", "");
        
       long longInt =  Long.valueOf(nOxStr, 16);
      
       return (int)longInt;
    }
	/**
	 * 将浮点数格式化成百分比
	 * 
	 * @param dValue
	 * @return
	 */
	public static String formatPrecent(double dValue)
	{
		java.text.NumberFormat percentFormat = java.text.NumberFormat
		        .getPercentInstance();
		percentFormat.setMaximumFractionDigits(2); // 最大小数位数
		percentFormat.setMinimumFractionDigits(2); // 最小小数位数
		// percentFormat.setMaximumIntegerDigits(2);// 最大整数位数
		// percentFormat.setMinimumIntegerDigits(2);// 最小整数位数
		return percentFormat.format(dValue);// 自动转换成百分比显示..
	}

	/**
	 * 提供精确的减法运算。
	 * @return
	 */
	public static String ssubString(String v1, String v2)
	{
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2).toString();
	}

	/**
	 * 提供精确的加法运算。
	 * @return
	 */
	public static String saddString(String v1, String v2)
	{
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).toString();
	}

	/**
	 * 提供精确的加法运算。
	 * @return
	 */
	public static double saddDouble(String v1)
	{// , String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		// BigDecimal b2 = new BigDecimal(v2);
		// return b1.add(b2).doubleValue();
		return b1.doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(String v1, String v2)
	{
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2).doubleValue();
	}
	
	/**
	 * 提供（相对）精确的乘法法运算。
	 * 
	 * @param v1
	 * 
	 * @param v2
	 * 
	 * @return 两个参数的积
	 */
	public static double mul(String v1, String v2)
	{
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).doubleValue();
	}
	/**
	 * 提供（相对）精确的乘法法运算。
	 * 
	 * @param v1
	 * 
	 * @param v2
	 * 
	 * @return 两个参数的积
	 */
	public static String multoString(String v1, String v2)
	{
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).toString();
	}
	/**
	 * 去掉后面的0
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		  if (str.indexOf(".") != -1 && str.charAt(str.length() - 1) == '0') {
			  return trim(str.substring(0, str.length() - 1));
		  } else {
			  return str.charAt(str.length() - 1) == '.' ? str.substring(0, str.length() - 1) : str;
		  }
	}
	
	
	public static int compare(String v1, String v2)
	{
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.compareTo(b2);
	}
	/**
	 * 换算单位
	 * @param v1
	 * @return
	 */
	public static String Monad(String v1)
	{
		int length = 0;
		String str = "0.00";
		String v2 = v1.substring(0, v1.indexOf("."));
		length = v2.length();
		
		if (length > 8) {
			str = new DecimalFormat("##0.00").format(StringUtils.stringToFloat(v2)/100000000) + "亿";
		} else if (length > 5) {
			str = new DecimalFormat("##0.00").format(StringUtils.stringToFloat(v2)/10000) + "万";
		}
		return str;
	}

	/**
	 * 数组是否相等
	 * 
	 * @param s1
	 * @param s2
	 * @return true相等 false不相等
	 */
	public static boolean isArrayEqual(String[] s1, String[] s2)
	{
		if (s1 == null && s2 == null)
			return true;
		else if ((s1 == null && s2 != null) || (s1 != null && s2 == null))
			return false;

		if (s1.length != s2.length)
			return false;
		else
			for (int i = 0; i < s1.length; i++)
			{
				if (!s1[i].equals(s2[i]))
					return false;
			}
		return true;
	}

	/**
	 * 校验Tag Alias 只能是数字,英文字母和中文
	 * @param s
	 * @return
	 */
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }
    
    /**
	 * 去除空格、回车、换行
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		  Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		  Matcher m = p.matcher(str);
		  String after = m.replaceAll("");
		  return after;
	}

	/**
	 * 获取String数据, 如果为null或空返回"";
	 * @param str
	 * @return
	 */
	public static String optString(String str) {
		if (TextUtils.isEmpty(str)){
			return "";
		}
		return str;
	}

	/**
	 * 将中文空格替换为英文空格:
	 * @param str
	 * @return
	 */
	public static String replaceCNBlankToEN(String str) {
		Pattern p = Pattern.compile("  ");
		Matcher m = p.matcher(str);
		String after = m.replaceAll(" ");
		return after;
	}

	/**
	 * 将字符文本中指定文本全部替换为目标内容
	 * @param source		操作数据源
	 * @param replaced		被替换文本
	 * @param target		替换内容
	 * @return
	 */
	public static String replaceString(String source, String replaced, String target) {
		Pattern p = Pattern.compile(replaced);
		Matcher m = p.matcher(source);
		String after = m.replaceAll(target);
		return after;
	}

}
