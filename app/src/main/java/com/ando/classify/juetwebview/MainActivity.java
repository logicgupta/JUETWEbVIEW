package com.ando.classify.juetwebview;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    WebView webView;
    WebView webView2;
    private HttpClient httpclient = null;
    private HttpClient httpClient_get=null;
    String json=null;
       HttpContext localContext = new BasicHttpContext();
    InputStream inputStream=null;
    String name1;
    String father_name;
    String course;
    String pmobile,smobile;
    String pemail,semail;
    String paddress,pdistrict,pin,state;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        webView2 = findViewById(R.id.webView2);
        webView2.setVisibility(View.GONE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView2.getSettings().setJavaScriptEnabled(true);

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true); // allow pinch to zooom
        webView.getSettings().setDisplayZoomControls(false);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e(TAG, "onPageStarted: " + url);
                String cookies = CookieManager.getInstance().getCookie(url);

                Log.e(TAG, "onPageStarted:1 " + cookies);
                Toast.makeText(MainActivity.this, "" + cookies, Toast.LENGTH_SHORT).show();
                if (url.equalsIgnoreCase("https://webkiosk.juet.ac.in/StudentFiles/StudentPage.jsp")) {
                    webView.setVisibility(View.GONE);
                    webView2.setVisibility(View.VISIBLE);
                    webView2.loadUrl("https://webkiosk.juet.ac.in//StudentFiles/PersonalFiles/StudPersonalInfo.jsp");
                    new X().execute();

                }


            }


            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);

                if (url.equalsIgnoreCase("https://webkiosk.juet.ac.in/StudentFiles/StudentPage.jsp")) {

                    webView.setVisibility(View.GONE);
                }
                String cookies = CookieManager.getInstance().getCookie(url);

            }
        });
        webView.loadUrl("https://webkiosk.juet.ac.in/");


        webView2.evaluateJavascript("(function(){return window.document.body.outerHTML})();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {

                        Log.e(TAG, "onReceiveValue: "+html );
                    }
                });
    }

    class X extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            String cookies = CookieManager.getInstance().getCookie("https://webkiosk.juet.ac.in/StudentFiles/StudentPage.jsp");
            Document doc2 = null;
            try {
               doc2 = Jsoup.connect("https://webkiosk.juet.ac.in/StudentFiles/PersonalFiles/StudPersonalInfo.jsp")
                        .cookie("SESSIONID", cookies)
                        .get();
                Log.e(TAG, "doInBackground: "+doc2.html() );

            } catch (IOException e) {
                e.printStackTrace();
            }
            Document document= Jsoup.parse(doc2.html().toString());
            Log.d("Website Login "," Personal "+document);
            Element table1=document.select("table").get(1);
            Elements rows=table1.select("tr");
            for(int i=0;i<rows.size();i++){
                Element row =rows.get(i);
                Elements cols=row.select("td");
                if(cols.get(0).text().equals("Name")){
                    name1=cols.get(1).text();
                    Log.d("Username ","My NAME IS "+name1);
                }
                if(cols.get(0).text().equals("Course")){
                    course=cols.get(1).text();
                    Log.d("Username ","My  Course IS "+course);

                }
                if(cols.get(0).text().equals("Father's Name")){
                    father_name=cols.get(1).text();
                    Log.d("Username ","My  Father NAME IS "+father_name);

                }
                if(cols.get(0).text().equals("Cell/Mobile")){
                    smobile=cols.get(2).text();
                    pmobile=cols.get(3).text();
                    Log.d("Username ","My  Mobile Number IS "+smobile);
                    Log.d("Username ","My   Parent Mobile number  IS "+smobile);
                }
                if(cols.get(0).text().equals("E-Mail")){
                    semail=cols.get(1).text();
                    pemail=cols.get(3).text();
                    Log.d("Username ","My   Email IS "+semail);
                    Log.d("Username ","My  Father Email IS "+pemail);
                }
                if (cols.get(0).text().equals("Address")){
                    paddress=cols.get(1).text();
                    Log.e("address",""+paddress);
                }
                if (cols.get(0).text().equals("District")){
                    pdistrict=cols.get(1).text();
                    Log.e("District"," "+pdistrict);
                }if (cols.get(0).text().equals("City/PIN")){
                    pin=cols.get(1).text();
                    Log.e("City/PIN"," "+pin);
                }
                if (cols.get(0).text().equals("State")){
                    state=cols.get(1).text();
                    Log.e("City/PIN"," "+state);
                }


            }
            return null;
        }
    }

}
