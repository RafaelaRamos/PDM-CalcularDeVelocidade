package ifpb.com.edu.br.calculadorvelocidade;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class SpeedCalculatorService extends IntentService {

    private Boolean condicao = true;
    private static final String TAG = "Download ";

    public SpeedCalculatorService() {
        super("SpeedCalculatorService");
    }

    private int calculator(int size, long time) {
        //dividindo tamanho (Mb)
        return Math.round(size * 1024/time);//in kbps
    }

    //TODO:Download dos arquivos
    private long download(int size){//em ms



        long  start = System.currentTimeMillis();

        try {
            URL url = new URL("http://212.183.159.230/"+size+"MB.zip");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.connect();

            if (http.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "Server returned HTTP " + http.getResponseCode() + " " + http.getResponseMessage());

            }
            File diretorio = new File(Environment.getExternalStorageDirectory() + "/ arquivos");

            if (!diretorio.exists()) {
                diretorio.mkdir();
                Log.e(TAG, "Directory Created.");
            }
            OutputStream saida = new FileOutputStream(new File(diretorio, "testeDownload"+size));
            InputStream is = http.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                saida.write(buffer, 0, len1);
            }

            saida.close();
            is.close();

        } catch (Exception e) {

            e.printStackTrace();

            Log.e(TAG, "Download falhou" + e.getMessage());
        }
        long elapsed = System.currentTimeMillis() - start;
        System.out.println( "Tempo de "+size+elapsed);
            return elapsed;

    }

    private void notifyUI(int speed){
        ChangedSpeedEventReceiver.sendBroadcast(getApplicationContext(), speed);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        condicao = (Boolean) intent.getBooleanExtra("condicao", true);


        //
        SignalEventReceiver.sendBroadcast(getApplicationContext(), SignalEventReceiver.SIGNAL_START);
        //simulando o download de 1M
        long t_1 = download(5);
        int s_1 = calculator(5, t_1);
        notifyUI(s_1);

        if (condicao == true) {
            //TODO: verificar se foi solicitado parada

            //simulando o download de 10M
            long t_5 = download(10);
            int s_5 = calculator(10, t_5);
            notifyUI(s_5);
        }

        //TODO: verificar se foi solicitado parada
        System.out.println(condicao);
        //simulando o download de 20M
        if (condicao == true) {
            long t_10 = download(20);
            int s_10 = calculator(20, t_10);
            notifyUI(s_10);

        }
        SignalEventReceiver.sendBroadcast(getApplicationContext(), SignalEventReceiver.SIGNAL_STOP);
    }

    /**
     * Inicializa o serviço de cálculo de download
     *
     * @see IntentService
     */
    public static void start(Context context) {
        Intent intent = new Intent(context, SpeedCalculatorService.class);
        context.startService(intent);
    }



   private void parar(int valor) {
       Intent intent = new Intent("notificacao");
       intent.putExtra ("cancela",valor);
       sendBroadcast(intent);
   }
}
