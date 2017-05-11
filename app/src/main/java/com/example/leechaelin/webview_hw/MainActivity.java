package com.example.leechaelin.webview_hw;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ProgressDialog dialog;
    ListView listview;
    WebView webview;
    EditText edittext;
    Animation ani;
    LinearLayout l;

    ArrayList<Data> data = new ArrayList<Data>();
    ArrayAdapter<Data>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l = (LinearLayout)findViewById(R.id.linear1);
        edittext=(EditText)findViewById(R.id.editText);
        webview=(WebView)findViewById(R.id.webView);
        ani= AnimationUtils.loadAnimation(this,R.anim.anim);
        dialog= new ProgressDialog(this);
        listview = (ListView)findViewById(R.id.listview);
        adapter = new ArrayAdapter<Data>(this,android.R.layout.simple_list_item_1,data);
        webview.addJavascriptInterface(new JavascriptMethod(),"Myapp");
        listview.setAdapter(adapter);

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dlg= new AlertDialog.Builder(MainActivity.this);
                dlg.setMessage("정말로 삭제하시겠습니까? ")
                        .setNegativeButton("닫기",null)
                        .setPositiveButton("확인 ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                data.remove(position);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(),"삭제하였습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }).show();

                return true;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                l.setVisibility(View.VISIBLE);
                webview.setVisibility(View.VISIBLE);
                listview.setVisibility(View.INVISIBLE);
                String url=data.get(position).getUrl();
                webview.loadUrl(url);
            }
        });
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                dialog.setMessage("Loading~!");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                super.onPageStarted(view,url,favicon);
            }
            @Override
            public void onPageFinished(WebView view,String url){
                edittext.setText(url);
                super.onPageFinished(view,url);
            }

        });
        //webview.loadUrl("http://www.naver.com");
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress>=100)dialog.dismiss();
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return super.onJsAlert(view, url, message, result);
            }
        });

        WebSettings webSettings= webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                l.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    public void goURL(View v){
        String url=edittext.getText().toString();
        webview.loadUrl(url);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.add){
            l.setAnimation(ani);
            ani.start();
            l.setVisibility(View.INVISIBLE);
            webview.setVisibility(View.VISIBLE);
            listview.setVisibility(View.INVISIBLE);
            webview.loadUrl("file:///android_asset/www/urladd.html");

        }else if(item.getItemId()==R.id.list){
            l.setVisibility(View.GONE);
            webview.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.VISIBLE);

        }

        return super.onOptionsItemSelected(item);
    }
    Handler myhandler = new Handler();

    private class JavascriptMethod {

        @JavascriptInterface
        public void visible(){
            myhandler.post(new Runnable() {
                @Override
                public void run() {
                    l.setVisibility(View.VISIBLE);
                }
            });
        }
        @JavascriptInterface
        public void saveurl(final String sitename,final String url){
            myhandler.post(new Runnable(){
                @Override
                public void run(){
                    boolean Isin = true;
                    for(int i=0;i<data.size();i++){
                        if(data.get(i).getUrl().equals(url)) {
                            Log.d("tag", data.get(i).getUrl());
                            Isin = false;
                            break;
                        }
                    }
                    if(Isin){
                        data.add(new Data(sitename,url));
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),"즐겨찾기에 추가되었습니다. ",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"즐겨찾기에 이미 있는 항목입니다 ",Toast.LENGTH_SHORT).show();
                        webview.loadUrl("javascript:displayMsg()");
                    }
                }
            });

        }
    }
}
