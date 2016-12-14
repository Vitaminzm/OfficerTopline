package com.education.officertopline.app;

/**
 * 常量池 用于保存常量 simple introduction
 * 
 * @author CWI-APST Email:lei.zhang@symboltech.com 2015年10月26日
 * @see
 * @since 1.0
 */
public class ConstantData {

	//服务器地址
	public static  String host_config = "123.56.221.24:8060";  //41

	/** 基数,用于配置常量参数 */
	public static final int BASE_CODE = 0x200;


	/** http 请求成功 */
	public static final String HTTP_RESPONSE_OK = "00";

	/** save token */
	public static final String LOGIN_TOKEN = "token";

	/** 更新时间 */
	public static final String POS_UPDATE_TIME = "pos_update_time";

	/** 网络请求Tag */
	public static final String NET_TAG = "net_tag";

	/** IP_HOST_CONFIG 配置host前缀*/
	public static final String IP_HOST_CONFIG_PREFIX = "http://";

	//新闻的类型 无图 单图 多图
	public static final String NEWS_TYPE_NO_PIC = "1";
	public static final String NEWS_TYPE_SINGLE_PIC = "2";
	public static final String NEWS_TYPE_MORE_PIC = "3";

	//默认每次请求的条数
	public static final int PAGESIZE = 20;

	//频道列表
	public static final String TOPLINE_CHANNEL_LIST= "topline_channel_list";
}
