package com.android.phone.assistant.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.youmi.android.AdManager;
import net.youmi.android.dev.AppUpdateInfo;
import net.youmi.android.dev.CheckAppUpdateCallBack;
import net.youmi.android.dev.OnlineConfigCallBack;
import net.youmi.android.diy.AdObject;
import net.youmi.android.diy.DiyManager;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsChangeNotify;
import net.youmi.android.offers.PointsManager;

import com.android.phone.assistant.R;
import com.android.phone.assistant.widget.AppInfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class YouMi implements PointsChangeNotify{

	/**
	 * 当前金币
	 */
	public static int currentPoints = 0;

	static List<AdObject> adList = null;
	public static List<AppInfo> appInfoList = new ArrayList<AppInfo>();

	Context mContext;
	Handler mHandler;
	private static YouMi mYouMi = null;

	public static void creatYoumiInstance(Context context){
		mYouMi = new YouMi(context);
	}
	public static YouMi getInstance(){

		return mYouMi;
	}
	public YouMi(Context context){
		mContext = context;

	}

	public void setHandler(Handler handler){
		mHandler = handler;
	}

	/**
	 * 初始化应用的发布ID和密钥，以及设置测试模式
	 * @param context
	 */
	public void initYoumi(){
		//youmiAppId  产品ID
		//youmiAppMiyao 产品秘钥

		if(AppManger.youmiID != null && AppManger.youmiMiYao != null){
			AdManager.getInstance(mContext).init(AppManger.youmiID, AppManger.youmiMiYao, false);
			Log.i("YouMi", "initYoumi [packageName:"+ AppManger.appPackageName+"][appNname:"+AppManger.projectName+"]注册 OK!");
			Log.i("YouMi", "initYoumi AppManger.youmiID = "+AppManger.youmiID+", AppManger.youmiMiYao = "+AppManger.youmiMiYao);
		}

		/*if(packageName.equals("com.youmi.assistivetouch")){   //EasyTouch(高流畅版)
			Log.i("YouMi", "initYoumi packageName = com.youmi.assistivetouch EasyTouch(高流畅版) is OK!");
			AdManager.getInstance(mContext).init("f8a23c604905cf1a", "f8232b9df99d9f92", false);

		}else if(packageName.equals("com.younet.hometouch")){ //HomeTouch
			Log.i("YouMi", "initYoumi packageName = com.younet.hometouch HomeTouch is OK!");
			AdManager.getInstance(mContext).init("3eeb7102b0373308", "1390cb429fc402e7", false);

		}else if(packageName.equals("com.easyunet.easytouch.tools")){ //小白点工具箱 2
			Log.i("YouMi", "initYoumi packageName = com.easyunet.easytouch.tools 小白点工具箱2 is OK!");

			AdManager.getInstance(mContext).init("baa5b0582cc494be", "71ef28ddde2d06c1", false);

		}else if(packageName.equals("com.easyunet.iphone.xiaobaidian")){ //安卓小白点
			Log.i("YouMi", "initYoumi packageName = com.easyunet.iphone.xiaobaidian 安卓小白点 is OK!");

			AdManager.getInstance(mContext).init("ed59d7ac84e221a8", "e835a3be0e40c985", false);
		}else{
			Toast.makeText(mContext, "没有使用正确的初始化有米密钥！！", Toast.LENGTH_LONG).show();
		}*/

		// 开启用户数据统计服务,默认不开启，传入 false 值也不开启，只有传入 true 才会调用
		//AdManager.getInstance(mContext).setUserDataCollect(true);

		// 如果使用积分广告，请务必调用积分广告的初始化接口:
		OffersManager.getInstance(mContext).onAppLaunch();
		PointsManager.getInstance(mContext).registerNotify(this);

		// 加载插播资源
		// SpotManager.getInstance(mContext).loadSpotAds();
		// 设置展示超时时间，加载超时则不展示广告，默认0，代表不设置超时时间
		//SpotManager.getInstance(mContext).setSpotTimeout(10000);//设置10秒
	}

	public void checkAppUpdate(final Context context, final boolean isShowNewHitm) {
		AdManager.getInstance(context).asyncCheckAppUpdate(new CheckAppUpdateCallBack() {
			public void onCheckAppUpdateFinish(
					AppUpdateInfo appUpdateInfo) {
				if (appUpdateInfo == null) {
					if(isShowNewHitm)
						Toast.makeText(mContext, "已经是最新版本！", Toast.LENGTH_SHORT).show();
				} else {

					// 获取版本号
					int versionCode = appUpdateInfo.getVersionCode();
					// 获取版本
					String versionName = appUpdateInfo.getVersionName();
					// 获取新版本的信息
					String updateTips = appUpdateInfo.getUpdateTips();
					// 获取apk下载地址
					final String downloadUrl = appUpdateInfo.getUrl();

					AlertDialog updateDialog = new AlertDialog.Builder(
							context)
							.setIcon(android.R.drawable.ic_dialog_info)
							.setTitle("发现新版本 " + versionName)
							.setMessage(updateTips)
							.setPositiveButton(
									"更新",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											try {
												Intent intent = Intent
														.parseUri(
																downloadUrl,
																Intent.FLAG_ACTIVITY_NEW_TASK);
												context.startActivity(intent);
											} catch (Exception e) {
												// TODO: handle
												// exception
											}
										}
									}).setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int which) {
								}
							})
							.create();
					updateDialog.show();
				}
			}
		});
	}

/*********+++++++++++++++++++++++++++自定义广告部分+++++++++++++++++++++*********/

	/**
	 * 初始化自定义广告
	 * @param context
	 */
	public void initDyAdObjects(){
		//DiyManager.initAdObjects(mContext);

	}

	/**
	 * 下载app
	 * @param context
	 * @param adId 服务器中的app唯一ID
	 */
	public void downloadAd(int adId){
		Log.d("TAG","YouMi downloadAd......");

		//DiyManager.downloadAd(mContext, adId);

	}

	/**
	 * 获取有米数据列表
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AdObject> getAdList(){
		return null;//DiyManager.getAdList(mContext);
	}


	public void initAdList(){

		new Thread(){
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();

				while(adList == null){
					//adList = DiyManager.getAdList(mContext);

					/******* start 测试*************/
					 	/*adList = new ArrayList<AdObject>();

				        AdObject mAdObject = new AdObject();
				        mAdObject.setAdId(168);
				        mAdObject.setAdText("这是一个传说中的软件！");
				        mAdObject.setAppName("GPRS流量计算器");
				        mAdObject.setIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
				        mAdObject.setSize("24.9M");
				        adList.add(mAdObject);*/
					/******* end  测试*************/
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Log.d("TAG", "adList == null adList == null");
				}
			}
		};//.start();
	}

	/**
	 * 获取应用app的信息列表
	 * @return
	 */
	public List<AppInfo> getAppInfo(List<AdObject> adList) throws Exception{
		List<AppInfo> itemList = new ArrayList<AppInfo>();

		final float JIAN_QU = 5.0f;//如：6到10作为一档即间距为5
		final float MAX_DOWNLOAD = 1000.0f;
		final float MIN_DOWNLOAD = 100.0f;

		//下载量通过函数      f(x) = a * x + b; 计算的
		float a = (MAX_DOWNLOAD - MIN_DOWNLOAD) / JIAN_QU;

		final float MAX_LEVEL = 10.0f;
		final float MIN_LEVEL = 6.6f;
		float level_a = (MAX_LEVEL - MIN_LEVEL) / JIAN_QU;
		int[] levelIconId = {
				R.drawable.rating_bg
				,R.drawable.rating_bg_1
				,R.drawable.rating_bg_2
				,R.drawable.rating_bg_3
				,R.drawable.rating_bg_4
				,R.drawable.rating_bg_5
				,R.drawable.rating_bg_6
				,R.drawable.rating_bg_7
				,R.drawable.rating_bg_8
				,R.drawable.rating_bg_9
				,R.drawable.rating_bg_10

		};


		if(adList == null)
			return null;

		for(AdObject list : adList){
			AppInfo appInfo = new AppInfo();

			appInfo.adId = list.getAdId();
			appInfo.versionName = list.getVersionName();
			appInfo.versionCode = list.getVersionCode();
			appInfo.packageName = list.getPackageName();
			appInfo.description = list.getDescription();
			appInfo.author = list.getAuthor();
			appInfo.iconUrl = list.getIconUrl();
			appInfo.category = list.getCategory();
			appInfo.appScreenShots = list.getScreenShortcuts();
			appInfo.icon = list.getIcon();
			appInfo.appName = list.getAppName();
			appInfo.adText = list.getAdText();

			appInfo.size = list.getSize();
			String[] str = appInfo.size.split("M");
			float sizeF = (float) Double.parseDouble(str[0]);

			//计算出下载次数
			float x = sizeF - ((int)sizeF - (int)sizeF % JIAN_QU + 1);
			int downloadCount = (int) (a * x + MIN_DOWNLOAD);
			if(downloadCount < 0)
				downloadCount = 137;
			appInfo.downloadCount = downloadCount+"万次下载";

			//计算出等级
			float levelF = level_a * x + MIN_LEVEL;
			String mBigDecimal = ""+new BigDecimal(""+levelF).setScale(0, BigDecimal.ROUND_HALF_UP);
			int levelI = Integer.parseInt(mBigDecimal);
			appInfo.levelIcon = BitmapFactory.decodeResource(mContext.getResources(), levelIconId[levelI]);

			appInfo.status = "下载";
			appInfo.frameIcon = null;

			itemList.add(appInfo);
		}
		appInfoList = itemList;

		return itemList;
	}

	/**
	 * 显示无积分推荐墙所有app而非可以自定义界面的app列表
	 */
	public void showRecommendWall(){
		DiyManager.showRecommendWall(mContext);
	}


/********------------------------- end 自定义广告部分----------------------------*************/

	/******************************** strat.....  积分墙广告 *********************************************/
	public void onDestroy(){
		// 如果使用积分广告，请务必调用积分广告的初始化接口:
		OffersManager.getInstance(mContext).onAppExit();
		// 注销积分监听-如果在onCreate注册了，那这里必须得注销
		PointsManager.getInstance(mContext).unRegisterNotify(this);
	}

	public void queryPoints(){
		//	return PointsManager.getInstance(mContext).queryPoints();// 查询积分余额
		onPointBalanceChange(PointsManager.getInstance(mContext).queryPoints());
	}

	/**
	 * 奖励积分
	 */
	public void awardPoints(int points){
		// demo 奖励10积分
		PointsManager.getInstance(mContext).awardPoints(points);
		// 注，调用该方法后，积分余额马上变更，可留意onPointBalanceChange是不是被调用了
	}

	/**
	 * 消费积分
	 * @param context
	 */
	public void spendPoints(int points){
		// demo 消费20积分
		PointsManager.getInstance(mContext).spendPoints(points);
		// 注，调用该方法后，积分余额马上变更，可留意onPointBalanceChange是不是被调用了
	}

	/**
	 * 显示积分墙
	 * @param context
	 */
	public void showOffersWall(){
		OffersManager.getInstance(mContext).showOffersWall();
	}

	public void onPointBalanceChange(int currentPoints) {
		// TODO Auto-generated method stub

		CommonUtil.currentPoints = currentPoints;

		if(mHandler == null)
			return;

		Message msg = new Message();
		msg.what = CommonUtil.CURRENT_POINTS;
		msg.arg1 = currentPoints;

		mHandler.sendMessage(msg);
	}
	/******************************** end.....  积分墙广告 *********************************************/


	/******************************** start.....  插播广告 *********************************************/
	// 展示插播广告，可以不调用loadSpot独立使用
	public void showSpotAds(){
		/*SpotManager.getInstance(mContext).showSpotAds(mContext, new SpotDialogListener() {
	        public void onShowSuccess() {
	            Log.i("YoumiAdDemo", "展示成功");
	        }

	        public void onShowFailed() {
	            Log.i("YoumiAdDemo", "展示失败");
	        }

	    });*/
	}
	/******************************** end.....  插播广告 *********************************************/

	/**
	 * 在线参数配置，获取参数值
	 * @param key  参数键
	 * @param onlineConfigCallBack  获取值的回调
	 */
	public void asyncGetOnlineConfig(String key, OnlineConfigCallBack onlineConfigCallBack){

		AdManager.getInstance(mContext).asyncGetOnlineConfig(key, onlineConfigCallBack);
	}

}
