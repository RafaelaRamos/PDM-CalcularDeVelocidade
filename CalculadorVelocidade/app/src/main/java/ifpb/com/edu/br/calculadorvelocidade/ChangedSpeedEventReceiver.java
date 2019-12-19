package ifpb.com.edu.br.calculadorvelocidade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public abstract class ChangedSpeedEventReceiver extends BroadcastReceiver {
    private static final String ACTION = "ag.ifpb.webmeter.receiver.ChangeSpeed";
    private static final String PARAM = "ag.ifpb.webmeter.receiver.ChangeSpeed.speed";

    protected abstract void onChangedSpeed(int speed);

    @Override
    public void onReceive(Context context, Intent intent) {
        int speed = intent.getIntExtra(PARAM, 0);
        onChangedSpeed(speed);
    }

    public static void sendBroadcast(Context ctx, int speedValue){
        //setup data
        Intent eventIntent = new Intent(ACTION);
        eventIntent.putExtra(PARAM, speedValue);
        //send by local broadcast
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(ctx);
        lbm.sendBroadcast(eventIntent);
    }

    public static void registerReceiver(Context ctx, ChangedSpeedEventReceiver receiver){
        //filter
        IntentFilter filter = new IntentFilter(ACTION);
        //registry broadcast
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(ctx);
        lbm.registerReceiver(receiver, filter);
    }

    public static void unregisterReceiver(Context ctx, ChangedSpeedEventReceiver receiver){
        //registry broadcast
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(ctx);
        lbm.unregisterReceiver(receiver);
    }
}
