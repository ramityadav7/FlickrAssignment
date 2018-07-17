package flickr.example.com.flickr.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.webkit.WebView;

import flickr.example.com.flickr.BaseActivity;
import flickr.example.com.flickr.R;
import flickr.example.com.flickr.constants.AppConstants;

public class DetailsActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String url = getIntent().getExtras().getString(AppConstants.HOME_BUNDLE_DATA_KEY);
        WebView myWebView = findViewById(R.id.webView);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.setInitialScale(1);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.loadUrl(url);
    }
}
