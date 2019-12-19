package ifpb.com.edu.br.calculadorvelocidade;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class SpeedometerJsHandler {
    final WebView webView;

    private String callback = null;

    public SpeedometerJsHandler(WebView wv){
        this.webView = wv;
    }

    public void speed(long value){
        StringBuilder sb = new StringBuilder("javascript:(function(){");
        sb.append("var callback = " + callback + ";");
        sb.append("callback(" + value + ");");
        sb.append("})();");
        webView.loadUrl(sb.toString());
    }

    public void cancel(){
        speed(0);
    }

    @JavascriptInterface
    public void onStart(String callback){
        Log.d("AGDEBUG", "Callback value: " + callback);
        this.callback = callback;
    }

}
