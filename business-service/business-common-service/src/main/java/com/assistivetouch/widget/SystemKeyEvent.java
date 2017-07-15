package com.assistivetouch.widget;

import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.provider.Settings.System;
import android.widget.Toast;

public class SystemKeyEvent {

	private CommandShell cmd;
	private Context mContext;
	private String android_id;

	public SystemKeyEvent(Context context) {
		mContext = context;
		android_id = getAndroidId();
	}

	public CommandShell getCommandShell() {
		if (cmd == null) {
			if (android_id == null) {
				cmd = new CommandShell("sh");
			} else {  
				cmd = new CommandShell("su");
			}
		}
		return cmd;
	}

	class CommandShell {

		OutputStream mOutputStream;
		Process mProcess;

		public CommandShell(String cmd_super) {
			try {
				mProcess = Runtime.getRuntime().exec(cmd_super);
				mOutputStream = mProcess.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void system(String cmd) throws Exception {
			byte[] cmd_byte = (new StringBuilder(cmd)).append("\n").toString().getBytes("ASCII");
			mOutputStream.write(cmd_byte);
		}

		public void runCommand(String s) throws Exception {
			system(s);
		}

		public void close() throws Exception {
			if (mOutputStream != null) {
				mOutputStream.flush();
				mOutputStream.close();
			}
			if (mProcess != null) {
				mProcess.destroy();
			}
		}
	}

	public void sendKey(int key_code) {
		cmd = getCommandShell();
		try {
			cmd.runCommand("input keyevent " + key_code);
		} catch (Exception e) {
			Toast.makeText(mContext, "需Root权限支持！开权限见“设置中的Root权限说明”", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	public String getAndroidId() {
		String androidId = System.getString(mContext.getContentResolver(), System.ANDROID_ID);
		return androidId;
	}
}
