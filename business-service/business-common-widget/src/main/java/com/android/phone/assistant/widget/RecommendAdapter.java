package com.android.phone.assistant.widget;

import java.util.List;

import com.android.phone.assistant.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * app列表适配器
 * @author wanlh
 *
 */
public class RecommendAdapter extends BaseAdapter{
    
    Context context;
    List<AppInfo> list;
    LayoutInflater mInflater;

    public RecommendAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }
    
    public RecommendAdapter(Context context , List<AppInfo> arrayList) {
        this.context = context;
        this.list = arrayList;
        this.mInflater = LayoutInflater.from(context);
    }
    
    public void setList(List<AppInfo> arrayList){
    	this.list = arrayList;
    }
    
    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AppInfo itemObject = list.get(position);
        
        ViewHolder viewHolder = null;
        if(convertView==null){
            convertView =  mInflater.inflate(R.layout.app_list_item_ext, null);
           
            viewHolder = new ViewHolder();
            viewHolder.iconFrameView = (ImageView) convertView.findViewById(R.id.icon_frame);
            viewHolder.iconView = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.appNameView = (TextView) convertView.findViewById(R.id.name);
            viewHolder.appLevelView = (ImageView) convertView.findViewById(R.id.app_level);
            viewHolder.appPromptView = (TextView) convertView.findViewById(R.id.app_prompt);
            viewHolder.appSizeView = (TextView) convertView.findViewById(R.id.size);
            viewHolder.downloadCountView = (TextView) convertView.findViewById(R.id.download_count);
            viewHolder.appStatus = (TextView) convertView.findViewById(R.id.app_status);
            
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        viewHolder.iconFrameView.setImageBitmap(itemObject.frameIcon);
        viewHolder.iconView.setImageBitmap(itemObject.icon);
        viewHolder.appNameView.setText(itemObject.appName);
        viewHolder.appLevelView.setImageBitmap(itemObject.levelIcon);
        viewHolder.appPromptView.setText(itemObject.adText);
        
        viewHolder.appSizeView.setText(itemObject.size);
        viewHolder.downloadCountView.setText(itemObject.downloadCount);
        viewHolder.appStatus.setText(itemObject.status);
        
        viewHolder.appStatus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            
            	mOnClickStatusListener.OnClickStatus(0, itemObject.adId);
            }
        });

        return convertView;
    }

    OnClickStatusListener mOnClickStatusListener;
    public void setOnClickStatusListener(OnClickStatusListener onClickStatusListener){
    	mOnClickStatusListener = onClickStatusListener;
    }
    public interface OnClickStatusListener{
    	void OnClickStatus(int status, int adId);
    }
    
    class ViewHolder{
    	
    	ImageView iconFrameView;
    	/**
    	 * app图标
    	 */
        ImageView iconView;
        /**
         * app名字
         */
        TextView appNameView;
        /**
         * app大小
         */
        TextView appSizeView;
        /**
         * 介绍
         */
        TextView appPromptView;
        /**
         * 下载等状态
         */
        TextView appStatus;
        /**
         * app等级
         */
        ImageView appLevelView;
        /**
         * 下载量
         */
        TextView downloadCountView;
    }

    
}
