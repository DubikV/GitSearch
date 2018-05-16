package com.gmail.vanyadubik.gitsearch.service.sync;

import com.gmail.vanyadubik.gitsearch.model.json.DownloadResponse;
import com.gmail.vanyadubik.gitsearch.model.json.OwnerDTO;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

import static com.gmail.vanyadubik.gitsearch.common.Consts.CONNECT_PATTERN_URL;

public interface SyncService {

    @GET(CONNECT_PATTERN_URL)
    Call<DownloadResponse> search(@QueryMap Map<String, String> params);

    @GET
    Call<OwnerDTO> getOwner(@Url String url);

}
