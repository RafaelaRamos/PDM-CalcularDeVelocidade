package ifpb.com.edu.br.calculadorvelocidade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //widgets
    Button btnCancel;
    Button btnStart;
    //others
    ChangedSpeedEventReceiver speedEventReceiver;
    SignalEventReceiver signalEventReceiver;
    SpeedometerJsHandler jsHandler;

    //TODO: tarefa 3 - melhorar a manipulação de botões
    private void handleButtons(boolean starting) {
        btnStart.setEnabled(!starting);
        btnCancel.setEnabled(starting);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get webview and create jshandler
        WebView wv = findViewById(R.id.speedometer);
        jsHandler = new SpeedometerJsHandler(wv);
        //configure webview
        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("file:///android_asset/www/index.html");
        wv.addJavascriptInterface(jsHandler, "ctl");
        //buttons
        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpeedCalculatorService.start(MainActivity.this);
            }
        });
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: tarefa 2 - cancelar o serviço via broadcast
                Intent intent = new Intent(MainActivity.this, SpeedCalculatorService.class);
                intent.putExtra("condicao", false);
                stopService(intent);


            }
        });
        //receivers
        speedEventReceiver = new ChangedSpeedEventReceiver() {
            @Override
            protected void onChangedSpeed(int speed) {
                jsHandler.speed(speed);
            }
        };
        signalEventReceiver = new SignalEventReceiver() {
            @Override
            protected void onStart() {
                jsHandler.speed(0);
                handleButtons(true);
            }

            @Override
            protected void onStop() {
                handleButtons(false);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        //
        ChangedSpeedEventReceiver.registerReceiver(this, speedEventReceiver);
        SignalEventReceiver.registerReceiver(this, signalEventReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //
        ChangedSpeedEventReceiver.unregisterReceiver(this, speedEventReceiver);
        SignalEventReceiver.unregisterReceiver(this, signalEventReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int teste=0;

            int cancela  = intent.getIntExtra("cancela",teste);
            btnStart.setEnabled(true);
            btnCancel.setEnabled(false);
        }


    };
}

