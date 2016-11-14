package com.education.officertopline.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class CodeBitmap {

	public static Bitmap createQrBitmap(Context context,String content,int width,int height)
	{
		 DisplayMetrics dm = new DisplayMetrics();
	      dm = context.getResources().getDisplayMetrics();
	        float density = dm.density;
	        System.err.println("createQrBitmap"+width+"-"+height+"-"+density);
	        if (width<=0) {
				width=300;
			}
	        if (height<=0) {
	        	height=300;
			}
	        return create(content, (int)(width*density), (int)(height*density),BarcodeFormat.QR_CODE);
//		return create(content, (int)(320), (int)(320),BarcodeFormat.QR_CODE);
	}
	
	public static Bitmap create128Bitmap(String content,int width,int height)
	{
		return create(content, width, height,BarcodeFormat.CODE_128);
	}
	
	public static Bitmap create39Bitmap(String content,int width,int height)
	{
		return create(content, width, height,BarcodeFormat.CODE_39);
	}
	
	public static Bitmap createEAN13Bitmap(String content,int width,int height)
	{
		return create(content, width, height,BarcodeFormat.EAN_13);
	}
	/**
	 * 创建一维二维码图片
	 */
	public static Bitmap create(String content,int width,int height,BarcodeFormat marcodeFormat)
	{
			// 用于设置QR二维码参数
			Hashtable<EncodeHintType, Object> qrParam = new Hashtable<EncodeHintType, Object>();
			// 设置QR二维码的纠错级别——这里选择最高H级别
			qrParam.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H );
			// 设置编码方式
			qrParam.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			// 生成QR二维码数据——这里只是得到一个由true和false组成的数组
			// 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
			try {
				BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
						marcodeFormat, width, height, qrParam);
				System.err.println("width"+width);
				// 开始利用二维码数据创建Bitmap图片，分别设为黑白两色
				int w = bitMatrix.getWidth();
				System.err.println("width getWidth"+w);
				int h = bitMatrix.getHeight();
				int[] data = new int[w * h];

				for (int y = 0; y < h; y++) {
					for (int x = 0; x < w; x++) {
						if (bitMatrix.get(x, y))
							data[y * w + x] = 0xff000000;// 黑色
						else
							data[y * w + x] = 0x00000000;// -1 相当于0xffffffff 白色
					}
				}

				// 创建一张bitmap图片，采用最高的图片效果ARGB_8888
				Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
				// 将上面的二维码颜色数组传入，生成图片颜色
				bitmap.setPixels(data, 0, w, 0, 0, w, h);
				return bitmap;
			} catch (WriterException e) {
				e.printStackTrace();
			}
			return null;
	}

	/**
	 * 创建二维码
	 *
	 * @param content   content
	 * @param widthPix  widthPix
	 * @param heightPix heightPix
	 * @param logoBm    logoBm
	 * @return 二维码
	 */
	public static Bitmap createQRCode(String content, int widthPix, int heightPix, Bitmap logoBm) {
		try {
			if (content == null || "".equals(content)) {
				return null;
			}
			// 配置参数
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 容错级别
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix,
					heightPix, hints);
			int[] pixels = new int[widthPix * heightPix];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < heightPix; y++) {
				for (int x = 0; x < widthPix; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * widthPix + x] = 0xff000000;
					} else {
						pixels[y * widthPix + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
			if (logoBm != null) {
				bitmap = addLogo(bitmap, logoBm);
			}
			//必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 在二维码中间添加Logo图案
	 */
	private static Bitmap addLogo(Bitmap src, Bitmap logo) {
		if (src == null) {
			return null;
		}
		if (logo == null) {
			return src;
		}
		//获取图片的宽高
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		int logoWidth = logo.getWidth();
		int logoHeight = logo.getHeight();
		if (srcWidth == 0 || srcHeight == 0) {
			return null;
		}
		if (logoWidth == 0 || logoHeight == 0) {
			return src;
		}
		//logo大小为二维码整体大小的1/5
		float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
		Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
		try {
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(src, 0, 0, null);
			canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
			canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
		} catch (Exception e) {
			bitmap = null;
			e.getStackTrace();
		}
		return bitmap;
	}
}
