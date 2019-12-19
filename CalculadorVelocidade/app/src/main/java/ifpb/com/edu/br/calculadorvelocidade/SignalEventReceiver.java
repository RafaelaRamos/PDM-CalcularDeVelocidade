package ifpb.com.edu.br.calculadorvelocidade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public abstract class SignalEventReceiver extends BroadcastReceiver {
    private static final String ACTION = "ag.ifpb.webmeter.receiver.Signal";
    private static final String PARAM = "ag.ifpb.webmeter.receiver.Signal.value";

    public static int SIGNAL_START = 1;
    public static int SIGNAL_STOP = 2;

    protected abstract void onStart();

    protected abstract void onStop();

    @Override
    public void onReceive(Context context, Intent intent) {
        int signal = intent.getIntExtra(PARAM, 0);
        if (signal == SIGNAL_START) onStart();
        else if (signal == SIGNAL_STOP) onStop();
    }

    public static void sendBroadcast(Context ctx, int signal){
        //setup data
        Intent eventIntent = new Intent(ACTION);
        eventIntent.putExtra(PARAM, signal);
        //send by local broadcast
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(ctx);
        lbm.sendBroadcast(eventIntent);
    }

    public static void registerReceiver(Context ctx, SignalEventReceiver receiver){
        //filter
        IntentFilter filter = new IntentFilter(ACTION);
        //registry broadcast
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(ctx);
        lbm.registerReceiver(receiver, filter);
    }

    public static void unregisterReceiver(Context ctx, SignalEventReceiver receiver){
        //registry broadcast
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(ctx);
        lbm.unregisterReceiver(receiver);
    }
}
