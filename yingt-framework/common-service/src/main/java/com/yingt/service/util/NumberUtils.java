/**
 * 
 */
package com.yingt.service.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author duminghui
 * 
 */
public class NumberUtils
{

    /**
     * <p>
     * Convert a <code>String</code> to an <code>int</code>, returning
     * <code>zero</code> if the conversion fails.
     * </p>
     * 
     * <p>
     * If the string is <code>null</code>, <code>zero</code> is returned.
     * </p>
     * 
     * <pre>
     *   NumberUtils.toInt(null) = 0
     *   NumberUtils.toInt("")   = 0
     *   NumberUtils.toInt("1")  = 1
     * </pre>
     * 
     * @param str
     *            the string to convert, may be null
     * @return the int represented by the string, or <code>zero</code> if
     *         conversion fails
     * @since 2.1
     */
    public static int toInt(String str)
    {
        return toInt(str, 0);
    }

    /**
     * <p>
     * Convert a <code>String</code> to an <code>int</code>, returning a default
     * value if the conversion fails.
     * </p>
     * 
     * <p>
     * If the string is <code>null</code>, the default value is returned.
     * </p>
     * 
     * <pre>
     *   NumberUtils.toInt(null, 1) = 1
     *   NumberUtils.toInt("", 1)   = 1
     *   NumberUtils.toInt("1", 0)  = 1
     * </pre>
     * 
     * @param str
     *            the string to convert, may be null
     * @param defaultValue
     *            the default value
     * @return the int represented by the string, or the default if conversion
     *         fails
     * @since 2.1
     */
    public static int toInt(String str, int defaultValue)
    {
        if (str == null)
        {
            return defaultValue;
        }
        try
        {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe)
        {
            return defaultValue;
        }
    }

    /**
     * <p>
     * Convert a <code>String</code> to a <code>float</code>, returning
     * <code>0.0f</code> if the conversion fails.
     * </p>
     * 
     * <p>
     * If the string <code>str</code> is <code>null</code>, <code>0.0f</code> is
     * returned.
     * </p>
     * 
     * <pre>
     *   NumberUtils.toFloat(null)   = 0.0f
     *   NumberUtils.toFloat("")     = 0.0f
     *   NumberUtils.toFloat("1.5")  = 1.5f
     * </pre>
     * 
     * @param str
     *            the string to convert, may be <code>null</code>
     * @return the float represented by the string, or <code>0.0f</code> if
     *         conversion fails
     * @since 2.1
     */
    public static float toFloat(String str)
    {
        return toFloat(str, 0.0f);
    }

    /**
     * <p>
     * Convert a <code>String</code> to a <code>float</code>, returning a
     * default value if the conversion fails.
     * </p>
     * 
     * <p>
     * If the string <code>str</code> is <code>null</code>, the default value is
     * returned.
     * </p>
     * 
     * <pre>
     *   NumberUtils.toFloat(null, 1.1f)   = 1.0f
     *   NumberUtils.toFloat("", 1.1f)     = 1.1f
     *   NumberUtils.toFloat("1.5", 0.0f)  = 1.5f
     * </pre>
     * 
     * @param str
     *            the string to convert, may be <code>null</code>
     * @param defaultValue
     *            the default value
     * @return the float represented by the string, or defaultValue if
     *         conversion fails
     * @since 2.1
     */
    public static float toFloat(String str, float defaultValue)
    {
        if (StringUtils.isEmpty(str))
        {
            return defaultValue;
        }
        try
        {
            return Float.parseFloat(str);
        } catch (NumberFormatException nfe)
        {
            return defaultValue;
        }
    }

    // -----------------------------------------------------------------------
    /**
     * 判断字符串是否是数字0
     * 1.空字符串表示0
     * 2. 如.5表示非0
     * @param value
     * @return
     */
    public static boolean isZero(String value){
    	if (StringUtils.isEmpty(value))
    		return true;
    	
    	try {
	    	int dotIndex = value.indexOf("."); //小数点位数
	    	
	    	if (dotIndex<0){
	    		//不存在小数点
	    		return	(Character.isDigit(value.charAt(0))
			        && Long.parseLong(value) == 0);
	    	}
	    	else if (dotIndex==0){
	    		//以小数点开头，如".5"
	    		if (value.length() == 1){
	    			return true;
	    		}else{
	    			//判断小数部分是否是0
	    			return (Long.parseLong(value.substring(1)) == 0);
	    		}
	    		
	    	}else {
				//存在小数位数
	    		//判断整数部分
	    		if (!isDigits(value.substring(0,dotIndex)) || !isDigits(value.substring(dotIndex+1))){
	    			//整数部分或小数部分非数字
	    			return false;
	    		}else{
	    			//判断整数部分
	    			if (Long.parseLong(value.substring(0,dotIndex)) != 0){
	    				//整数部分大于0
	    				return false;
	    			}else{
	    				//整数部分是0，判断小数部分
	    				if (Long.parseLong(value.substring(dotIndex+1)) != 0){
	    					//小数部分非0
	    					return false;
	    				}else{
	    					return true;
	    				}
	    			}
	    		}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
    
    /**
     * 判断字符串是否是数字0(支持高精度数值, 避免因Number子类转换导致报错)
     * @param value
     * @return
     */
	public static boolean isZeroAsHighPrecise(String value) {
		if (StringUtils.isEmpty(value))
			return true;

		// if(!isNumber(value))return false;
		try {
			return BigDecimal.ZERO.equals(new BigDecimal(NumberFormat.getNumberInstance().parse(value).toString()));
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
    
    /**
     * 判断一个字符串数字是否是负数，备注，假设此字符串确实是数字
     * 判断标准为
     * 1. 第一个字符为"-",则表示负数
     * @param value
     * @return
     */
    public static boolean isNegativeNumber(String value){
    	if (StringUtils.isEmpty(value)){
    		return false;
    	}
    	
    	return (value.charAt(0)=='-');
    }
    
    /**
     * <p>
     * Checks whether the <code>String</code> contains only digit characters.
     * </p>
     * 
     * <p>
     * <code>Null</code> and empty String will return <code>false</code>.
     * </p>
     * 
     * @param str
     *            the <code>String</code> to check
     * @return <code>true</code> if str contains only unicode numeric
     */
    public static boolean isDigits(String str)
    {
        if (StringUtils.isEmpty(str))
        {
            return false;
        }
        for (int i = 0; i < str.length(); i++)
        {
            if (!Character.isDigit(str.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Checks whether the String a valid Java number.
     * </p>
     * 
     * <p>
     * Valid numbers include hexadecimal marked with the <code>0x</code>
     * qualifier, scientific notation and numbers marked with a type qualifier
     * (e.g. 123L).
     * </p>
     * 
     * <p>
     * <code>Null</code> and empty String will return <code>false</code>.
     * </p>
     * 
     * @param str
     *            the <code>String</code> to check
     * @return <code>true</code> if the string is a correctly formatted number
     */
    public static boolean isNumber(String str)
    {
        if (StringUtils.isEmpty(str))
        {
            return false;
        }
        char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        int start = (chars[0] == '-') ? 1 : 0;
        if (sz > start + 1)
        {
            if (chars[start] == '0' && chars[start + 1] == 'x')
            {
                int i = start + 2;
                if (i == sz)
                {
                    return false; // str == "0x"
                }
                // checking hex (it can't be anything else)
                for (; i < chars.length; i++)
                {
                    if ((chars[i] < '0' || chars[i] > '9')
                            && (chars[i] < 'a' || chars[i] > 'f')
                            && (chars[i] < 'A' || chars[i] > 'F'))
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        sz--; // don't want to loop to the last char, check it afterwords
              // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another
        // digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit))
        {
            if (chars[i] >= '0' && chars[i] <= '9')
            {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.')
            {
                if (hasDecPoint || hasExp)
                {
                    // two decimal points or dec in exponent
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E')
            {
                // we've already taken care of hex.
                if (hasExp)
                {
                    // two E's
                    return false;
                }
                if (!foundDigit)
                {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-')
            {
                if (!allowSigns)
                {
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else
            {
                return false;
            }
            i++;
        }
        if (i < chars.length)
        {
            if (chars[i] >= '0' && chars[i] <= '9')
            {
                // no type qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E')
            {
                // can't have an E at the last byte
                return false;
            }
            if (chars[i] == '.')
            {
                if (hasDecPoint || hasExp)
                {
                    // two decimal points or dec in exponent
                    return false;
                }
                // single trailing decimal point after non-exponent is ok
                return foundDigit;
            }
            if (!allowSigns
                    && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F'))
            {
                return foundDigit;
            }
            if (chars[i] == 'l' || chars[i] == 'L')
            {
                // not allowing L with an exponent
                return foundDigit && !hasExp;
            }
            // last character is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't
        // pass
        return !allowSigns && foundDigit;
    }

    /** 格式化数值: 如为正数,则在其前添加"+"符号; */
    public static String formatPositive(String value){
    	if (!StringUtils.isEmpty(value) && !value.contains("-") && !value.contains("+") && !value.equals("0.00")) {
    		value = "+" + value;
		}
    	return value;
    }

    /**
     * 保留2位小数
     * @param formatVal
     * @return
     */
    public static String formatDecimals(double formatVal){
        return new DecimalFormat("##0.00").format(formatVal);   // #.00 表示两位小数 #.0000四位小数 以此类推.
    }

    /**
     * 保留4位小数
     * @param formatVal
     * @return
     */
    public static String formatDecimal4s(double formatVal){
        return new DecimalFormat("##0.0000").format(formatVal);   // #.00 表示两位小数 #.0000四位小数 以此类推.
    }

    public static String formatBigDecimals(String formatVal){
        if (!TextUtils.isEmpty(formatVal)) {
            BigDecimal b = new BigDecimal(formatVal);
            return b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }else
            return "";
    }
}
