package com.example.kisileruygulamas;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface KisilerDaoInterface {

    @GET("bayrakquiz/tum_kisiler.php")
    Call<KisilerCevap> tumKisiler();

    @POST("bayrakquiz/tum_kisiler_arama.php")
    @FormUrlEncoded
    Call<KisilerCevap> kisiArama(@Field("kisi_ad") String kisi_ad);

    @POST("bayrakquiz/delete_kisiler.php")
    @FormUrlEncoded
    Call<KisilerCevap> kisiSil(@Field("kisi_id") int kisi_id);

    @POST("bayrakquiz/update_kisiler.php")
    @FormUrlEncoded
    Call<KisilerCevap> kisiGuncelle(@Field("kisi_id") int kisi_id
            ,@Field("kisi_ad") String kisi_ad
            ,@Field("kisi_tel") String kisi_tel);

    @POST("bayrakquiz/insert_kisiler.php")
    @FormUrlEncoded
    Call<KisilerCevap> kisiEkle(@Field("kisi_ad") String kisi_ad
            ,@Field("kisi_tel") String kisi_tel);
}
