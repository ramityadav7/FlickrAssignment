package flickr.example.com.flickr.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import flickr.example.com.flickr.application.FlickrApplication;
import flickr.example.com.flickr.constants.AppConstants;
import flickr.example.com.flickr.data.HomeData;
import flickr.example.com.flickr.data.HomeDataItem;
import flickr.example.com.flickr.network.model.ItemsItem;
import flickr.example.com.flickr.network.model.Media;
import flickr.example.com.flickr.network.model.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class HomeViewModel extends ViewModel{

    private FlickrApplication flickrApplication;
    private MutableLiveData<HomeData> homeLiveData;
    private String user;
    private String tag;

    public void setApplication(FlickrApplication flickrApplication) {
        this.flickrApplication = flickrApplication;
    }

    public void setUser(String user) {
        if(TextUtils.isEmpty(user)) {
            this.user = null;
        } else {
            this.user = user;
        }
    }

    public void setTag(String tag) {
        if(TextUtils.isEmpty(tag)) {
            this.tag = null;
        } else {
            this.tag = tag;
        }
    }

    public void setSearch(boolean isSearch) {
        if(homeLiveData != null)
            homeLiveData.getValue().setSearch(isSearch);
    }

    public LiveData<HomeData> getItems() {
        if (homeLiveData == null || homeLiveData.getValue().isShowError() || homeLiveData.getValue().isSearch() ) {
            if(homeLiveData == null)
                homeLiveData = new MutableLiveData<>();
            HomeData homeData = new HomeData();
            homeData.setShowProgress(true);
            homeData.setShowError(false);
            homeLiveData.setValue(homeData);

            loadHomeData();
        }

        return homeLiveData;
    }


    private void loadHomeData() {
        Call<Response> call = flickrApplication.getApiService().getImageList(AppConstants.FORMAT_JSON, AppConstants.NO_JSON_CALLBACK, user, tag);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()) {
                    Response response1 = response.body();
                    HomeData homeData = homeLiveData.getValue();
                    homeData.setShowProgress(false);
                    homeData.setSearch(false);
                    homeLiveData.setValue(parseHomeData(response1.getItems(), homeData));
                    Log.d("Ramit", response1.toString());
                } else {
                    handleFailure();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                handleFailure();
            }
        });


    }

    private void handleFailure() {
        HomeData homeData = new HomeData();
        homeData.setShowProgress(false);
        homeData.setShowError(true);
        homeData.setSearch(false);
        homeLiveData.setValue(homeData);
        Log.d("Ramit", "Failure");
    }

    private HomeData parseHomeData(List<ItemsItem> resultsItems, HomeData homeData) {
        List<HomeDataItem> homeDataItems = new ArrayList<>();

        for(ItemsItem resultsItem: resultsItems) {
            HomeDataItem homeDataItem = new HomeDataItem();

            Media media = resultsItem.getMedia();
            if(null != media) {
                String thumbnail = media.getM();
                if(!TextUtils.isEmpty(thumbnail)) {
                    homeDataItem.setThumbnail(thumbnail.replace("_m", "_z"));
                    homeDataItem.setImageUrl(thumbnail.replace("_m", "_b"));

                    homeDataItems.add(homeDataItem);
                }
            }
        }

        homeData.setHomeDataItems(homeDataItems);

        return homeData;
    }
}
