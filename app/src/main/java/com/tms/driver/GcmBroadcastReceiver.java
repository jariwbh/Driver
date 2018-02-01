package com.tms.driver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.BufferedWriter;

/**
 * This Receiver is used to get the broadcast for the GCM
 * 
 * @author amit.singh
 * 
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		String messageType = gcm.getMessageType(intent);
		// StringBuffer Sbuf = new StringBuffer();
		// Sbuf.append(messageType+"_"+extras.getString("message").toString());
//		try {
//			createFileOnDevice(extras.getString("message").toString());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// Utilities.getInstance(context).writetoFile("AbcGCMLog", "GCMLOG", );
		// Explicitly specify that GcmIntentService will handle the intent.
		ComponentName comp = new ComponentName(context.getPackageName(),
				GcmIntentService.class.getName());

		// Start the service, keeping the device awake while it is launching.
		startWakefulService(context, (intent.setComponent(comp)));

		setResultCode(Activity.RESULT_OK);
	}

	public static BufferedWriter out;

//	private void createFileOnDevice(String message) throws IOException {
//		/*
//		 * Function to initially create the log file and it also writes the time
//		 * of creation to file.
//		 */
//		File Root = Environment.getExternalStorageDirectory();
//		if (Root.canWrite()) {
//			File LogFile = new File(Root, "Log.txt");
//
//			FileWriter LogWriter = new FileWriter(LogFile, true);
//			out = new BufferedWriter(LogWriter);
//			out.write(Calendar.getInstance().getTime() + "--" + message);
//			out.close();
//			// Date date = new Date();
//			// out.write("Logged at"
//			// + String.valueOf(date.getHours() + ":" + date.getMinutes()
//			// + ":" + date.getSeconds() + "\n"));
//		}
//	}
}
