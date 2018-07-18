package flickr.example.com.flickr.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import flickr.example.com.flickr.BaseActivity;
import flickr.example.com.flickr.R;
import flickr.example.com.flickr.application.FlickrApplication;
import flickr.example.com.flickr.constants.AppConstants;
import flickr.example.com.flickr.data.HomeData;
import flickr.example.com.flickr.data.HomeDataItem;
import flickr.example.com.flickr.detail.DetailsActivity;
import flickr.example.com.flickr.utils.AppUtil;
import flickr.example.com.flickr.utils.UiUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener, HomeAdapter.HomeItemClickHandler{

    private RelativeLayout relativeLayoutHome;
    private RecyclerView recyclerViewHome;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayoutError;
    private Button buttonReload;
    private FloatingActionButton fab;
    private LinearLayout linearLayoutSearch;
    private EditText editTextUser;
    private EditText editTextTag;
    private Button buttonSearch;

    private HomeData homeData;
    private HomeAdapter homeAdapter;

    private static final String BUNDLE_KEY_USER = "BUNDLE_KEY_USER";
    private static final String BUNDLE_KEY_TAG = "BUNDLE_KEY_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initView();
        if(savedInstanceState != null) {
            editTextUser.setText(savedInstanceState.getString(BUNDLE_KEY_USER));
            editTextTag.setText(savedInstanceState.getString(BUNDLE_KEY_TAG));
        }
        loadData(false);
    }

    private void initView() {
        recyclerViewHome = findViewById(R.id.recyclerViewHome);
        relativeLayoutHome = findViewById(R.id.relativeLayoutHome);
        relativeLayoutError = findViewById(R.id.relativeLayoutError);
        progressBar = findViewById(R.id.progressBar);
        buttonReload = findViewById(R.id.buttonReload);
        fab = findViewById(R.id.fab);
        linearLayoutSearch = findViewById(R.id.linearLayoutSearch);
        editTextTag = findViewById(R.id.editTextTag);
        editTextUser = findViewById(R.id.editTextUser);
        buttonSearch = findViewById(R.id.buttonSearch);

        homeData = new HomeData();
        List<HomeDataItem> homeDataItems = new ArrayList<>();
        homeAdapter = new HomeAdapter(homeDataItems, this);
        homeData.setHomeDataItems(homeDataItems);

        int orientation = this.getResources().getConfiguration().orientation;
        int spanCount;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = 2;
        } else {
            spanCount = 3;
        }

        final RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewHome.setLayoutManager(mLayoutManager);
        recyclerViewHome.setAdapter(homeAdapter);


        buttonReload.setOnClickListener(this);
        fab.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
    }

    private void loadData(boolean isSearch)
    {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.setApplication((FlickrApplication) getApplication());
        homeViewModel.setTag(editTextTag.getText().toString().trim());
        homeViewModel.setUser(editTextUser.getText().toString().trim());
        homeViewModel.setSearch(isSearch);
        homeViewModel.getItems().observe(this, new Observer<HomeData>() {
            @Override
            public void onChanged(@Nullable HomeData homeData) {
                MainActivity.this.homeData.setShowProgress(homeData.isShowProgress());
                MainActivity.this.homeData.setShowError(homeData.isShowError());

                updateView();

                List<HomeDataItem> homeDataItems = homeData.getHomeDataItems();
                if(!AppUtil.isCollectionEmpty(homeDataItems)) {
                    List<HomeDataItem> data = MainActivity.this.homeData.getHomeDataItems();
                    data.clear();
                    data.addAll(homeDataItems);
                    homeAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void updateView() {
        updateErrorView();
        updateProgressView();
    }

    private void updateErrorView() {
        if(homeData.isShowError()) {
            editTextTag.setText("");
            editTextUser.setText("");
            relativeLayoutError.setVisibility(View.VISIBLE);
            relativeLayoutHome.setVisibility(View.GONE);
        } else {
            relativeLayoutError.setVisibility(View.GONE);
            relativeLayoutHome.setVisibility(View.VISIBLE);
        }
    }

    private void updateProgressView() {
        if(homeData.isShowProgress()) {
            recyclerViewHome.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            recyclerViewHome.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSearch: {
                handleSearch();
                break;
            }
            case R.id.buttonReload: {
                loadData(false);
                break;
            }

            case R.id.fab: {
                handleFabFilter();
                break;
            }
        }
    }

    private void handleSearch() {
        UiUtil.hideSoftKeyboard(this);
        linearLayoutSearch.setVisibility(View.GONE);
        loadData(true);
    }

    private void handleFabFilter() {
        if(linearLayoutSearch.getVisibility() == View.VISIBLE) {
            linearLayoutSearch.setVisibility(View.GONE);
        } else {
            linearLayoutSearch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHomeItemClick(int position) {
        String imageUrl = homeData.getHomeDataItems().get(position).getThumbnail();

        Intent myIntent = new Intent(MainActivity.this, DetailsActivity.class);
        myIntent.putExtra(AppConstants.HOME_BUNDLE_DATA_KEY, imageUrl);
        startActivity(myIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(BUNDLE_KEY_TAG, editTextTag.getText().toString().trim());
        outState.putString(BUNDLE_KEY_USER, editTextUser.getText().toString().trim());
    }
}
