package com.example.leechaelin.webview_hw;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
        dialog= new ProgressDialog(this);
        listview = (ListView)findViewById(R.id.listview);
        adapter = new ArrayAdapter<Data>(this,android.R.layout.simple_list_item_1,data);
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

                return false;
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
        switch (item.getItemId()){
            case R.id.add:
                webview.setVisibility(View.VISIBLE);
                listview.setVisibility(View.INVISIBLE);
                break;
            case R.id.list:
                webview.setVisibility(View.INVISIBLE);
                listview.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

}
