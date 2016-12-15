package com.education.officertopline.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.education.officertopline.db.EducationDBOpenHelper;
import com.education.officertopline.entity.ToplineNewsListInfo;
import com.education.officertopline.log.LogUtil;
import com.education.officertopline.utils.StringUtil;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 订单信息相关操作 simple introduction
 *
 * <p>
 * detailed comment
 *
 * @author CWI-APST Email:lei.zhang@symboltech.com 2015年11月2日
 * @see
 * @since 1.0
 */
public class ChannelNewsListDao {

	private EducationDBOpenHelper.DatabaseHelper helper;

	private Context context;
	private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public ChannelNewsListDao(Context context) {
		EducationDBOpenHelper dbutil = new EducationDBOpenHelper(context);
		helper = dbutil.getDatabaseHelper();
		this.context = context;
	}

	public EducationDBOpenHelper.DatabaseHelper getHelper() {
		return helper;
	}

	/**
	 * 获取缓存的数据
	 * @param channelCode 频道code
	 * @param count  查询条数
	 * @return
	 */
	public List<ToplineNewsListInfo> getOfflineOrderInfo(String channelCode, int count){
		List<ToplineNewsListInfo> result = new ArrayList<ToplineNewsListInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.query("channel_news_list", null, "channelCode = ? ", new String[] { channelCode }, null, null, " id desc limit " + count);
		while(cursor.moveToNext()){
			ToplineNewsListInfo info = new ToplineNewsListInfo();
			info.setId(cursor.getString(cursor.getColumnIndex("id")));
			info.setChannelCode(cursor.getString(cursor.getColumnIndex("channelCode")));
			info.setChannelName(cursor.getString(cursor.getColumnIndex("channelName")));
			info.setNewsType(cursor.getString(cursor.getColumnIndex("newsType")));
			info.setSrc(cursor.getString(cursor.getColumnIndex("src")));
			info.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			info.setPic(cursor.getString(cursor.getColumnIndex("pic")));
			info.setFirstTime(cursor.getString(cursor.getColumnIndex("firstTime")));
			info.setFirstPerson(cursor.getString(cursor.getColumnIndex("firstPerson")));
			info.setKeyword(cursor.getString(cursor.getColumnIndex("keyword")));
			info.setCommentNum(cursor.getString(cursor.getColumnIndex("commentNum")));
			result.add(info);
		}
		cursor.close();
		db.close();
		return result;
	}

	/**
	 * 缓存newslist数据
	 * @param code 频道code
	 * @param listInfos 新闻信息列表
	 * @return
	 */
	public boolean addOrderGoodsInfo(String code, List<ToplineNewsListInfo> listInfos) {
		boolean ret = false;
		if (listInfos != null && listInfos.size() > 0) {
			deleteChannelList(code);
		}else {
			LogUtil.i("lgs", "addOrderGoodsInfo---" + ret);
			return ret;
		}
		long success = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction();
		int count = 0;
		for(ToplineNewsListInfo info: listInfos){
			ContentValues valuesgood = new ContentValues();
			valuesgood.put("id", info.getId());
			valuesgood.put("channelName", info.getChannelName());
			valuesgood.put("channelCode", info.getChannelCode());
			valuesgood.put("newsType", info.getNewsType());
			valuesgood.put("title", info.getTitle());
			valuesgood.put("pic", info.getPic());
			valuesgood.put("src", info.getSrc());
			valuesgood.put("commentNum", info.getCommentNum());
			valuesgood.put("firstTime", info.getFirstTime());
			valuesgood.put("firstPerson", info.getFirstPerson());
			valuesgood.put("keyword", info.getKeyword());
			success = -1;
			success = db.insert("channel_news_list", null, valuesgood);
			if (success != -1) {
				count++;
			}
		}
		if(count != listInfos.size()){
			ret = false;
		}else{
			db.setTransactionSuccessful();
		}
		db.endTransaction();
		db.close();
		LogUtil.i("lgs", "addchannel_news_list---" + ret);
		return ret;
	}

	/**
	 * 删除对应频道的新闻
	 * @param code
	 * @return
	 */
	public boolean deleteChannelList(String code){
		boolean ret = false;
		if (!StringUtil.isEmpty(code)){
			SQLiteDatabase db = helper.getWritableDatabase();
			db.execSQL("pragma foreign_keys=on");
			int success = 0;
			success = db.delete("channel_news_list", "channelCode = ?", new String[]{ code });
			db.close();
			if(success > 0){
				ret = true;
			}
		}
		LogUtil.i("lgs", "deleteChannelList---"+ ret);
		return ret;
	}

}
