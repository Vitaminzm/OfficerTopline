package com.education.officertopline.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.education.officertopline.R;
import com.education.officertopline.app.ConstantData;
import com.education.officertopline.app.MyApplication;
import com.education.officertopline.log.LogUtil;
import com.education.officertopline.result.BaseResult;
import com.education.officertopline.utils.SpSaveUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * simple introduction Volley http 请求基类
 * <p>
 * detailed comment
 *
 * @author CWI-APST Email:lei.zhang@symboltech.com 2015年10月22日
 * @see
 * @since 1.0
 */
public class HttpStringClient {
	private static HttpStringClient httpStringRequest;
	private static OkHttpClient httpClient ;
	private static final int TIMEOUT_MS_DEFAULT = 40 *1000;
	private static final int MAX_RETRIES = 0;
	private static final int BACKOFF_MULT = 0;

	private HttpStringClient() {
		initHttpConfig();
	}

    public void initHttpConfig() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder()
				.connectTimeout(TIMEOUT_MS_DEFAULT, TimeUnit.MILLISECONDS)
				.writeTimeout(TIMEOUT_MS_DEFAULT, TimeUnit.MILLISECONDS)
				.readTimeout(TIMEOUT_MS_DEFAULT, TimeUnit.MILLISECONDS);
		httpClient = builder.build();
    }

	public static HttpStringClient getinstance() {
		if (httpStringRequest == null) {
			synchronized (HttpStringClient.class) {
				if (httpStringRequest == null) {
					httpStringRequest = new HttpStringClient();
				}
			}
		}
		return httpStringRequest;
	}
	private static final int CALLBACK_SUCCESSFUL=0x01;
	private static final int CALLBACK_FAILED=0x02;
	private static final int CALLBACK_FINISH=0x03;
	static class UIHandler<T> extends Handler {
		private WeakReference mWeakReference;
		public UIHandler(HttpActionHandle<T> httpactionhandler){
			super(Looper.getMainLooper());
			mWeakReference=new WeakReference(httpactionhandler);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case CALLBACK_SUCCESSFUL: {
					T t = (T) msg.obj;
					HttpActionHandle httpactionhandler = (HttpActionHandle) mWeakReference.get();
					if (httpactionhandler != null) {
						httpactionhandler.handleActionSuccess(null, t);
					}
					break;
				}
				case CALLBACK_FAILED: {
					String e = (String) msg.obj;
					HttpActionHandle httpactionhandler = (HttpActionHandle) mWeakReference.get();
					if (httpactionhandler != null) {
						httpactionhandler.handleActionError(null, e);
					}
					break;
				}
				case CALLBACK_FINISH: {
					HttpActionHandle httpactionhandler = (HttpActionHandle) mWeakReference.get();
					if (httpactionhandler != null) {
						httpactionhandler.handleActionFinish();
					}
					break;
				}
				default:
					super.handleMessage(msg);
					break;
			}
		}
	}

	/**
	 * 获取请求队列
	 *
	 * @return
	 */
	public OkHttpClient getRequestClient() {
		return httpClient;
	}

	public void cancleRequest(String tag){
		if (tag == null){
			return;
		}
		synchronized (httpClient.dispatcher().getClass()) {
			for (Call call : httpClient.dispatcher().queuedCalls()) {
				if (tag.equals(call.request().tag())) call.cancel();
			}
			for (Call call : httpClient.dispatcher().runningCalls()) {
				if (tag.equals(call.request().tag())) call.cancel();
			}
		}
		//httpClient.dispatcher().cancelAll();
	}
	/**
	 *
	 * @param actionname
	 * @param url
	 * @param map
	 * @param clz
	 * @param httpactionhandler
	 * @param <T>
	 */
	public <T> void getForObject(final String actionname, String url, Map<String, String> map, final Class<T> clz,
			final HttpActionHandle<T> httpactionhandler) {
		final Gson gson = getGson();
		httpactionhandler.handleActionStart();

		Map<String, String> param = new HashMap<>();
		if(SpSaveUtils.read(MyApplication.context, ConstantData.LOGIN_TOKEN, "") != "")
			param.put("gladEyeKey", SpSaveUtils.read(MyApplication.context, ConstantData.LOGIN_TOKEN, ""));
		if(map != null){
			param.put("params", GsonUtil.beanToJson(map));
		}
		FormBody.Builder builder = new FormBody.Builder();

		Set<Map.Entry<String, String>> set = param.entrySet();
		for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
			builder.add(entry.getKey(), entry.getValue());
		}
		LogUtil.i("lgs", "map-----" + param.toString());
		FormBody formBody = builder.build();
		Request request = new Request.Builder().tag(actionname)
				.url(url)
				.post(formBody)
				.build();
		requestPost(url, param, request, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				UIHandler handler = new UIHandler(httpactionhandler);
				Message message=Message.obtain();
				e.printStackTrace();
				Boolean ret = false;
				LogUtil.i("lgs", "======================" + e.getCause() + "---" + e.getMessage());
				if(e.getMessage() != null){
					if (e.getMessage().contains("Failed to connect to") || e.getMessage().contains("failed to connect to")){
						ret = true;
						message.what=CALLBACK_FAILED;
						message.obj = "网络连接超时";
						handler.sendMessage(message);
					}else{
						message.what=CALLBACK_FAILED;
						message.obj = e.getMessage();
						handler.sendMessage(message);
					}
				}else{
					httpactionhandler.handleActionError(actionname, "网络连接异常！");
				}
				handler.sendEmptyMessage(CALLBACK_FINISH);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				T result = null;
				UIHandler handler = new UIHandler(httpactionhandler);
				Message message=Message.obtain();
				LogUtil.i("lgs", response.code()+response.message());
				if(response != null && response.code()== 200){
					try {
						String re =  response.body().string();
						LogUtil.i("lgs", "response==========" +re);
						result = gson.fromJson(re, clz);
						if(result != null){
							if(((BaseResult) result).getCode()!= null){
								message.what=CALLBACK_SUCCESSFUL;
								message.obj=result;
								handler.sendMessage(message);
							}
							else{
								message.what=CALLBACK_FAILED;
								message.obj = MyApplication.context.getString(R.string.exception_opt);
								handler.sendMessage(message);
							}
						}else{
							message.what=CALLBACK_FAILED;
							message.obj = MyApplication.context.getString(R.string.exception_opt);
							handler.sendMessage(message);
						}

				} catch (JsonSyntaxException e) {
						message.what=CALLBACK_FAILED;
						message.obj = MyApplication.context.getString(R.string.exception_opt);
						handler.sendMessage(message);
					e.printStackTrace();
				} catch (JsonParseException e) {
						message.what=CALLBACK_FAILED;
						message.obj = MyApplication.context.getString(R.string.exception_opt);
						handler.sendMessage(message);
						e.printStackTrace();
				} catch (IOException e) {
						message.what=CALLBACK_FAILED;
						message.obj = MyApplication.context.getString(R.string.exception_opt);
						handler.sendMessage(message);
						e.printStackTrace();
				}
					handler.sendEmptyMessage(CALLBACK_FINISH);
				}else{
					LogUtil.i("lgs", "response====fffff======");
					message.what=CALLBACK_FAILED;
					message.obj = response.code()+response.message();
					handler.sendMessage(message);
					handler.sendEmptyMessage(CALLBACK_FINISH);
				}

			}
		});
	}

	/**
	 * 设置有的属性不能映射时候不报错
	 *
	 * @return ObjectMapper
	 */
	public Gson getGson() {
		Gson gson = new GsonBuilder().serializeNulls().create();
		return gson;
	}

	/**
	 *
	 * @param url
	 * @param param
	 * @param request
	 * @param call
	 */
	public void requestPost(String url, Map<String, String> param, Request request, Callback call) {
		request("POST", url, param, null, request, call);
	}

	/**
	 *
	 * @param url
	 * @param param
	 * @param request
	 * @param call
	 */
	public void requestGet(String url, final Map<String, String> param, Request request, Callback call) {

		request("GET", url, param, null, request, call);
	}

	/**
	 *
	 * @param method
	 * @param url
	 * @param param
	 * @param headers
	 * @param request
	 * @param callBack
	 */
	private void request(String method, String url, final Map<String, String> param,
			final Map<String, String> headers, Request request, Callback callBack) {
		try {
			if ("GET".equals(method) && param != null) {
				StringBuilder sb = new StringBuilder();
				sb.append("?");
				Set<Map.Entry<String, String>> set = param.entrySet();
				for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
					sb.append(entry.getKey() + "");
					sb.append("=");
					sb.append(URLEncoder.encode(entry.getValue() + "", "UTF-8"));
					sb.append("&");
				}
				if (sb.length() > 0) {
					url += sb.substring(0, sb.length() - 1);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		httpClient.newCall(request).enqueue(callBack);
	}


}
