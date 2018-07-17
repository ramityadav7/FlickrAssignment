package flickr.example.com.flickr.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import flickr.example.com.flickr.BaseActivity;
import flickr.example.com.flickr.R;
import flickr.example.com.flickr.application.FlickrApplication;
import flickr.example.com.flickr.data.HomeData;
import flickr.example.com.flickr.data.HomeDataItem;
import flickr.example.com.flickr.utils.AppUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener, HomeAdapter.HomeItemClickHandler{

    private RelativeLayout relativeLayoutHome;
    private RecyclerView recyclerViewHome;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayoutError;
    private Button buttonReload;

    private HomeData homeData;
    private HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initView();
        loadData();
    }

    private void initView() {
        recyclerViewHome = findViewById(R.id.recyclerViewHome);
        relativeLayoutHome = findViewById(R.id.relativeLayoutHome);
        relativeLayoutError = findViewById(R.id.relativeLayoutError);
        progressBar = findViewById(R.id.progressBar);
        buttonReload = findViewById(R.id.buttonReload);

        homeData = new HomeData();
        List<HomeDataItem> homeDataItems = new ArrayList<>();
        homeAdapter = new HomeAdapter(homeDataItems, this);
        homeData.setHomeDataItems(homeDataItems);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewHome.setLayoutManager(mLayoutManager);
        recyclerViewHome.setAdapter(homeAdapter);


        buttonReload.setOnClickListener(this);
    }

    private void loadData()
    {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.setApplication((FlickrApplication) getApplication());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonReload: {
                break;
            }
        }
    }

    @Override
    public void onHomeItemClick(int position) {

    }
}
