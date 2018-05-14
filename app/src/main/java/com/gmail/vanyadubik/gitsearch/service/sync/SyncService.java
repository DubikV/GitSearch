package com.gmail.vanyadubik.gitsearch.service.sync;

import com.gmail.vanyadubik.gitsearch.model.json.DownloadResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

import static com.gmail.vanyadubik.gitsearch.common.Consts.CONNECT_PATTERN_URL;


public interface SyncService {

    @GET(CONNECT_PATTERN_URL)
    Call<DownloadResponse> download1(@QueryMap Map<String, String> params);

    @GET(CONNECT_PATTERN_URL)
    Observable<DownloadResponse> download(@QueryMap Map<String, String> params);

}
