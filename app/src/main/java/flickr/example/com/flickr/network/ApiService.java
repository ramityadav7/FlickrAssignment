package flickr.example.com.flickr.network;

import flickr.example.com.flickr.network.model.Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("photos_public.gne")
    Call<Response> getImageList(
            @Query("format") String formatJson,
            @Query("nojsoncallback") String noJsonCallBack,
            @Query("id") String userId,
            @Query("tags") String tags
    );
}
