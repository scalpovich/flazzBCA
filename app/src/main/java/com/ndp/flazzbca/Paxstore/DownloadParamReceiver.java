package com.ndp.flazzbca.Paxstore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.slf4j.LoggerFactory;
import static com.pax.market.android.app.sdk.PushConstants.ACTION_DATA_MESSAGE_RECEIVED;
import static com.pax.market.android.app.sdk.PushConstants.ACTION_NOTIFICATION_CLICK;
import static com.pax.market.android.app.sdk.PushConstants.ACTION_NOTIFICATION_MESSAGE_RECEIVED;
import static com.pax.market.android.app.sdk.PushConstants.ACTION_NOTIFY_DATA_MESSAGE_RECEIVED;
import static com.pax.market.android.app.sdk.PushConstants.ACTION_NOTIFY_MEDIA_MESSAGE_RECEIVED;
import static com.pax.market.android.app.sdk.PushConstants.EXTRA_MEIDA;
import static com.pax.market.android.app.sdk.PushConstants.EXTRA_MESSAGE_CONTENT;
import static com.pax.market.android.app.sdk.PushConstants.EXTRA_MESSAGE_DATA;
import static com.pax.market.android.app.sdk.PushConstants.EXTRA_MESSAGE_NID;
import static com.pax.market.android.app.sdk.PushConstants.EXTRA_MESSAGE_TITLE;
import java.util.logging.Logger;

public class DownloadParamReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        //todo add log to see if the broadcast is received, if not, please check whether the bradcast config is correct
//        Log.i("DownloadParamReceiver", "broadcast received");
//        //todo receive the broadcast from paxstore, start a service to download parameter files
//        context.startService(new Intent(context, DownloadParamService.class));
//    }

    private static final Logger logger = (Logger) LoggerFactory.getLogger(DownloadParamReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_NOTIFY_DATA_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e("Param","### NOTIFY_DATA_MESSAGE_RECEIVED ###");
            String title = intent.getStringExtra(EXTRA_MESSAGE_TITLE);
            String content = intent.getStringExtra(EXTRA_MESSAGE_CONTENT);
            Log.e("Param","ASdasd");
            Log.e("Param","### notification title={}, content={} ###"+ title+" "+ content);
            String dataJson = intent.getStringExtra(EXTRA_MESSAGE_DATA);
            Log.e("Param","### data json={} ###"+ dataJson);
            Toast.makeText(context, "  data=" + dataJson, Toast.LENGTH_SHORT).show();
        } else if (ACTION_DATA_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e("Param","### NOTIFY_DATA_MESSAGE_RECEIVED ###");
            String dataJson = intent.getStringExtra(EXTRA_MESSAGE_DATA);
            Log.e("Param","### data json={} ###"+ dataJson);
            Toast.makeText(context, "  data=" + dataJson, Toast.LENGTH_SHORT).show();
        } else if (ACTION_NOTIFICATION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e("Param","### NOTIFICATION_MESSAGE_RECEIVED ###");
            String title = intent.getStringExtra(EXTRA_MESSAGE_TITLE);
            String content = intent.getStringExtra(EXTRA_MESSAGE_CONTENT);
            Log.e("Param","### notification title={}, content={} ###"+ title+", "+ content);
        } else if (ACTION_NOTIFICATION_CLICK.equals(intent.getAction())) {
            Log.e("Param","### NOTIFICATION_CLICK ###");
            int nid = intent.getIntExtra(EXTRA_MESSAGE_NID, 0);
            String title = intent.getStringExtra(EXTRA_MESSAGE_TITLE);
            String content = intent.getStringExtra(EXTRA_MESSAGE_CONTENT);
            String dataJson = intent.getStringExtra(EXTRA_MESSAGE_DATA);
            Log.e("Param","### notification nid={}, title={}, content={}, dataJson={} ###"+ nid+", "+ title+", "+ content+", "+ dataJson);
        } else if (ACTION_NOTIFY_MEDIA_MESSAGE_RECEIVED.equals(intent.getAction())) {
            //You can decide when to show this media notification, immediately or later.
            Log.e("Param","### ACTION_NOTIFY_MEDIA_MESSAGE_RECEIVED ###");
            String mediaJson = intent.getStringExtra(EXTRA_MEIDA);
            Log.e("Param","### media json={} ###"+ mediaJson);
            Toast.makeText(context, "  mediaJson=" + mediaJson, Toast.LENGTH_SHORT).show();
        }
    }
}
