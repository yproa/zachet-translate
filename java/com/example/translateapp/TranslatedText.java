package com.example.translateapp;


import java.util.Map;

class TranslatedText {
    Map<String, Language> translation;
    // TODO: указать необходимые поля хранения ответа от API при переводе текста

    String[] translations;

    @Override
    public String toString() {
        String str = " ";
        for (String result : translations) {
            str += result + "\n";
        }
        return str;
    }







}