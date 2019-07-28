package com.yueer.farmshop.kaoshitest.fragment.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yueer.farmshop.kaoshitest.R;
import com.yueer.farmshop.kaoshitest.base.BaseActivity;
import com.yueer.farmshop.kaoshitest.base.BasePresenter;
import com.yueer.farmshop.kaoshitest.fragment.home.bean.DataBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends BaseActivity {


    @BindView(R.id.web_web)
    WebView webWeb;

    @Override
    protected BasePresenter initBasePresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }
    @SuppressLint("JavascriptInterface")
    @Override
    protected void initData() {






        Intent intent = getIntent();
        ArrayList<DataBean.BannerBeanBean> data = (ArrayList<DataBean.BannerBeanBean>) intent.getSerializableExtra("data");
        int position = intent.getIntExtra("position", 0);



//        String name = getIntent().getStringExtra("url");
        webWeb.loadUrl(data.get(position).getBannerWeb_url());
        // 启用javascript
        webWeb.getSettings().setJavaScriptEnabled(true);
        webWeb.setWebViewClient(new WebViewClient());
        webWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                result.confirm();
                Toast.makeText(WebActivity.this,message,Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                    ActivityCompat.requestPermissions(WebActivity.this, mPermissionList, 123);
                }
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "share");
                intent.putExtra(Intent.EXTRA_TEXT, "【" + "分享" + "】  ：" + message);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
                return true;
            }

        });









//        webWeb.getSettings().setJavaScriptEnabled(true);
//        webWeb.setWebViewClient(new WebViewClient());
//        //Toast.makeText(this, mText, Toast.LENGTH_SHORT).show();
//        webWeb.loadUrl(data.get(position).getBannerWeb_url());

//
//        //解析Html
////方法外    @SuppressLint("JavascriptInterface")
//
//        // 启用javascript
//        webWeb.getSettings().setJavaScriptEnabled(true);
//        webWeb.setWebViewClient(new WebViewClient());
//        webWeb.setWebChromeClient(new WebChromeClient());
//// 从assets目录下面的加载html
//        webWeb.getSettings().setJavaScriptEnabled(true);
//        webWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webWeb.getSettings().setAllowFileAccess(true);
//        webWeb.getSettings().setDefaultTextEncodingName("UTF-8");
//        webWeb.getSettings().setLoadWithOverviewMode(true);
//        webWeb.getSettings().setUseWideViewPort(true);
//        webWeb.getSettings().setDomStorageEnabled(true);
//        webWeb.loadUrl(data.get(position).getBannerWeb_url());



//        WebSettings settings = webWeb.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        settings.setSupportZoom(true);
//        webWeb.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
//                webView.loadUrl(s);
//                return true;
//            }
//        });
//        String htmlData = HtmlUtil.createHtmlData(bean.getBody(), bean.getCss(), (List<String>) bean.getJs());
//        webWeb.loadData(htmlData,HtmlUtil.MIME_TYPE,HtmlUtil.ENCODING);

    }

    @Override
    protected void initView() {
//        //单个微信分享
//        Intent intent=new Intent(Intent.ACTION_SEND);
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
//        intent.putExtra(Intent.EXTRA_TEXT, "终于可以了!!!");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(Intent.createChooser(intent, getTitle()));
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

        init();
    }

    private void init() {
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");

    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.e("tag", "开始分享");
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Log.e("tag", "开始成功");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Log.e("tag", "分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Log.e("tag", "分享取消");
        }
    };
}


