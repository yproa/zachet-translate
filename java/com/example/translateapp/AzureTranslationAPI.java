package com.example.translateapp;


import android.util.Log;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AzureTranslationAPI {
    String API_URL = "https://api.cognitive.microsofttranslator.com";
    // TODO: рекомендуется использовать свой ключ, чтобы получить доп. балл
    String key = "e85de764a1b8467f89377a8956bba378"; //
    // TODO: регион указать отдельной переменной
    String region="eastasia";

    // запрос языков работает без ключа
    @GET("/languages?api-version=3.0&scope=translation")
    Call<LanguagesResponse> getLanguages();

    @POST("/translate?api-version=3.0&") // путь к API
    @Headers({
            "Content-Type: application/json",
            "Ocp-Apim-Subscription-Key: " + key,
            "Ocp-Apim-Subscription-Region: " + region
            // TODO: указать ключ и регион
    })

        // TranslatedText - формат ответа от сервера
        // Тип ответа - TranslatedText, действие - translate, содержание запроса - пустое (нет полей формы)
        // TODO: с помощью аннотации @Body передать поля запроса к API (текст для перевода)
        // см. примеры https://square.github.io/retrofit/
    Call<TranslatedText[]> translate(@Query("to") String lang, @Body MainActivity.Message[] text);

    //Call<TranslatedText[]> translate(@Body MainActivity.Message[] text, @Query("to") String language);// тут опять же пригождается месседж из мейна, остальное вроде понятно
}