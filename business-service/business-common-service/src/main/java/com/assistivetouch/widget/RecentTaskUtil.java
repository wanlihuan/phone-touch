package com.assistivetouch.widget;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Handler;

public class RecentTaskUtil{
	Context mContext;
	
	public RecentTaskUtil(Context context) {
	//	this(context, null);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	/**
	 *       
	 * @param taskParentView   加载任务到taskParentView父控件中
	 */
	public void loadRecentTask(FolderTaskView taskParentView){
		taskParentView.removeAllViews();
		(new Handler()).post(new LoadTaskRunnable(mContext, taskParentView));
	}
	
	/**
     * 加载最近任务线程
     * @author wanlh
     *
     */
	private class LoadTaskRunnable implements Runnable{
		private FolderTaskView parentView;
		private IconUtilities iconUtilities;
		private PackageManager pm;
		ActivityInfo homeInfo;
		List<ActivityManager.RecentTaskInfo> recentTasks;
		int minCount = 0;
		
		public LoadTaskRunnable(final Context context, FolderTaskView parentView){
			this.parentView = parentView;
			
			iconUtilities = new IconUtilities(context);
	        pm = context.getPackageManager();
	        final ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
	        recentTasks = am.getRecentTasks(16, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
	        homeInfo = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).resolveActivityInfo(pm, 0);
	        int numTasks = recentTasks.size();
	        minCount = Math.min(numTasks, 8);
	       // parentView.setlhCount(Math.min(numTasks, 8));
		}
		
		public void run() {  
			// TODO Auto-generated method stub
			  //reloadRecentTask
			  // Performance note:  Our android performance guide says to prefer Iterator when
	          // using a List class, but because we know that getRecentTasks() always returns
	          // an ArrayList<>, we'll use a simple index instead.
	         // int index = 0;
	         int visibilityCount = 0;
	          for (int i = 0; i < minCount; i++) {
	              final ActivityManager.RecentTaskInfo info = recentTasks.get(i);

	              final Intent intent = new Intent(info.baseIntent);
	              if (info.origActivity != null) {
	                  intent.setComponent(info.origActivity);
	              }

	              // Skip the current home activity.
	              if (homeInfo != null) {
	                  if (homeInfo.packageName.equals(
	                          intent.getComponent().getPackageName())
	                          && homeInfo.name.equals(
	                                  intent.getComponent().getClassName())) {
	                      continue;
	                  }
	              }
	              
	              visibilityCount++;
	              
	              intent.setFlags((intent.getFlags()&~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
	                      | Intent.FLAG_ACTIVITY_NEW_TASK);
	              final ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
	              
	              if (resolveInfo != null) {
	                  final ActivityInfo activityInfo = resolveInfo.activityInfo;
	                  final String title = activityInfo.loadLabel(pm).toString();
	                  Drawable icon = activityInfo.loadIcon(pm);
	                  
	                  icon = iconUtilities.createIconDrawable(icon);
	                  
	                  if (title != null && title.length() > 0 && icon != null) {
	                	  ShortcutInfo shortcutInfo = new ShortcutInfo();
	                	  shortcutInfo.title = title;
	                	  shortcutInfo.icon = icon;
	                	  shortcutInfo.intent = intent;
	                	  parentView.createShortcutTask(shortcutInfo);
	                  }
	              }
	          }
	          parentView.setlhCount(visibilityCount);
		 }
	}
}
