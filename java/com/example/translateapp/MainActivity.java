package com.example.translateapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;





public class MainActivity extends AppCompatActivity {


    class Message {// для отправки запроса проще создать класс чтобы упаковывать в него
        String text;
    }

    // Экземпляр библиотеки и интерфейса можно использовать для всех обращений к сервису
    // формируем экземпляр библиотеки
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()) // ответ от сервера в виде строки
            .baseUrl(AzureTranslationAPI.API_URL) // адрес API сервера
            .build();

    AzureTranslationAPI api = retrofit.create(AzureTranslationAPI.class); // описываем, какие функции реализованы


    ArrayList<String> langs;
    String lng;
    Spinner spinner;
    ArrayAdapter adapter;
    String[] str;
    String item;
    String currLang;

    EditText inputText;
    TextView outText;
    //Начиная с версии 0.0 входной язык определяется автоматически
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        inputText = findViewById(R.id.inputText);
        spinner = findViewById(R.id.spinner);
        outText = findViewById(R.id.outText);


        Call<LanguagesResponse> call = api.getLanguages(); // создаём объект-вызов
        call.enqueue(new LanguagesCallback());


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                lng = (String)parent.getItemAtPosition(selectedItemPosition);
                Log.d("lngchoos", lng );
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Ваш выбор: " + lng.split("/")[0], Toast.LENGTH_SHORT);
                toast.show();


            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });






    }
    public void onClick(View v) {//онклик для кнопки
        Toast.makeText(getApplicationContext(), "Btn click", Toast.LENGTH_SHORT).show();
        Message[] message = new Message[1];// may be neverno
        Message msg = new Message();
        msg.text=String.valueOf(inputText.getText());


        message[0] = msg;

        Call<TranslatedText[]> call = api.translate(lng,message);
        call.enqueue(new TranslateCallback());

        Toast.makeText(getApplicationContext(), "Try to send", Toast.LENGTH_SHORT).show();
    }
    // TODO: создать аналогичным образом класс для ответа сервера при переводе текста
    class LanguagesCallback implements Callback<LanguagesResponse> {

        @Override
        public void onResponse(Call<LanguagesResponse> call, Response<LanguagesResponse> response) {
            if (response.isSuccessful()) {
                //langs = (ArrayList<String>)Arrays.asList(response.body().toString().split(":"));
                langs = response.body().toText();
                //langs = response.body().toString().split(":");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, langs);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);


                //ArrayAdapter<String> ad1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lang1);


                // TODO: response.body() содержит массив языков, доступных для перевода
                Log.d("mytag", "response: " + response.body());
            } else {
                // TODO: выводить Toast и сообщение в журнал в случае ошибки
            }
        }


        @Override
        public void onFailure(Call<LanguagesResponse> call, Throwable t) {
            // TODO: выводить Toast и сообщение в журнал в случае ошибки
            Toast.makeText(getApplicationContext(), "Не удалось загрузить языки! Проблемы с интернетом.", Toast.LENGTH_SHORT).show();

        }
    }


    class TranslateCallback implements Callback<TranslatedText[]> {

        @Override
        public void onResponse(Call<TranslatedText[]> call, Response<TranslatedText[]> response) {
            if (response.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "finish?", Toast.LENGTH_SHORT).show();
                String str = response.body()[0].toString();
                outText.setText(str);
                Log.d("mytag", "response: " + str);
            } else {
                Log.d("mytag", "error" + response.code());
                Toast.makeText(getApplicationContext(), "error "+response.code(), Toast.LENGTH_SHORT).show();
                outText.setText(lng+String.valueOf(inputText.getText()));
            }
        }

        @Override
        public void onFailure(Call<TranslatedText[]> call, Throwable t) {
            Log.d("mytag", "Translation error ;(" + t.getLocalizedMessage());
        }
    }
}