package com.education.officertopline.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ArithDouble {
	
	/** 
     * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入。 
     */  
  
    // 默认除法运算精度  
    private static final int DEF_DIV_SCALE = 10;  
  
    // 这个类不能实例化  
    private ArithDouble() {  
    }  
  
    /** 
     * 提供精确的加法运算。 
     *  
     * @param v1 
     *            被加数 
     * @param v2 
     *            加数 
     * @return 两个参数的和 
     */  
  
    public static double add(double v1, double v2) {  
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();  
    }  
  
    /** 
     * 提供精确的减法运算。 
     *  
     * @param v1 
     *            被减数 
     * @param v2 
     *            减数 
     * @return 两个参数的差 
     */  
  
    public static double sub(double v1, double v2) {  
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();  
    }  
    
    /** 
     * 提供精确的减法运算。 
     *  
     * @param v1 
     *            被减数 
     * @param v2 
     *            减数 
     * @return 两个参数的差 
     */  
  
    public static float sub(float v1, float v2) {  
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.subtract(b2).floatValue();  
    } 
    
    public static float sub(float v1, float v2, int scale) {  
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.subtract(b2).setScale(scale, RoundingMode.HALF_UP).floatValue();
    } 
  
    /** 
     * 提供精确的乘法运算。 
     *  
     * @param v1 
     *            被乘数 
     * @param v2 
     *            乘数 
     * @return 两个参数的积 
     */  
  
    public static double mul(double v1, double v2) {  
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();  
    }  
  
    /** 
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。 
     *  
     * @param v1 
     *            被除数 
     * @param v2 
     *            除数 
     * @return 两个参数的商 
     */  
  
    public static double div(double v1, double v2) {  
        return div(v1, v2, DEF_DIV_SCALE);  
    }  
  
    /** 
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。 
     *  
     * @param v1 
     *            被除数 
     * @param v2 
     *            除数 
     * @param scale 
     *            表示表示需要精确到小数点以后几位。 
     * @return 两个参数的商 
     */  
  
    public static double div(double v1, double v2, int scale) {  
        if (scale < 0) {  
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");  
        }  
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }  
  
    /** 
     * 提供精确的小数位四舍五入处理。 
     *  
     * @param v 
     *            需要四舍五入的数字 
     * @param scale 
     *            小数点后保留几位 
     * @return 四舍五入后的结果 
     */  
  
    public static double round(double v, int scale) {  
        if (scale < 0) {  
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");  
        }  
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }  
    
    /**
     * String 强制转换成double
     */
    
	public static double parseDouble(String str){
    	double ret = 0;
    	if(str == null || "".equals(str)){
    		return ret;
    	}
    	try{
    		ret = Double.parseDouble(str);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return ret;
    }
	
	/**
     * String 强制转换成int
     */
    
	public static int parseInt(String str){
    	int ret = 0;
    	if(str == null || "".equals(str)){
    		return ret;
    	}
    	try{
    		ret = Integer.parseInt(str);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return ret;
    }
	
	/**
     * String 强制转换成long
     */
    
	public static long parseLong(String str){
    	long ret = 0;
    	if(str == null || "".equals(str)){
    		return ret;
    	}
    	try{
    		ret = Long.parseLong(str);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return ret;
    }
	/**
     * String 强制转换成float
     */
    
	public static float parseFloat(String str){
    	float ret = 0;
    	if(str == null|| "".equals(str)){
    		return ret;
    	}
    	try{
    		ret = Float.parseFloat(str);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return ret;
    }
	
	public static double parseDoubleByType(String money, String type){
		
		double ret = 0;
		if(type == null || money == null){
			return parseDouble(money);
		}
		BigDecimal bigDecimal;
		try{
			bigDecimal = new BigDecimal(money);
		}catch(Exception e){
			return ret;
		}
		if(type.equals("0")){
			ret = parseDouble(money);
			//ret = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}else if(type.equals("1")){
			ret = bigDecimal.setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
		}else if(type.equals("2")){
			ret = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		}else if(type.equals("3")){
			ret = bigDecimal.setScale(0, BigDecimal.ROUND_DOWN).doubleValue();
		}else if(type.equals("4")){
			ret = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return ret;
	}
}