package com.education.officertopline.http;


import com.education.officertopline.app.AppManager;
import com.education.officertopline.app.ConstantData;

import java.util.Map;


/**
 * 
 * simple introduction http 请求封装
 * <p>
 * detailed comment
 * 
 * @see
 * @since 1.0
 */
public class HttpRequestUtil {
	
	/**
	 * 获取实际接口地址
	 * 
	 * @param url
	 * @return
	 */
	private static String getUrl(String url) {
		return ConstantData.IP_HOST_CONFIG_PREFIX + ConstantData.host_config +"/"+ url;
	}

	private HttpRequestUtil() {
	}

	public static HttpRequestUtil getinstance() {
		return new HttpRequestUtil();
	}

	/**
	 * 
	 * @author CWI-APST emial:26873204@qq.com
	 * @param <T>
	 * @Description: 根据频道id获取list信息
	 * @param param
	 * @param clz
	 * @param httpactionhandler
	 */
	public <T> void getToplineNewsList(String tag, Map<String, String> param, final Class<T> clz, final HttpActionHandle<T> httpactionhandler) {
		HttpStringClient.getinstance().getForObject(tag, getUrl("officer_topline_core/toplineNewsController/getToplineNewsList"), param, clz,
				httpactionhandler);
	}

	/**
	 *
	 * @author CWI-APST emial:26873204@qq.com
	 * @param <T>
	 * @Description: 获取频道list
	 * @param param
	 * @param clz
	 * @param httpactionhandler
	 */
	public <T> void getToplineChannel(String tag, Map<String, String> param, final Class<T> clz, final HttpActionHandle<T> httpactionhandler) {
		HttpStringClient.getinstance().getForObject(tag, getUrl("officer_topline_core/toplineChannelController/getToplineChannel"), param, clz,
				httpactionhandler);
	}
}
