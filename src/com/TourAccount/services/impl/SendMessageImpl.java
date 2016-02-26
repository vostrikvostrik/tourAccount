package com.TourAccount.services.impl;

import android.widget.EditText;
import com.TourAccount.services.SendMessage;

/**
 * User: User
 * Date: 29.04.15
 * Time: 16:31
 */
public class SendMessageImpl implements SendMessage, Runnable{

    private EditText textView;

    @Override
    public void run() {
        //Фоновая работа

        //Отправить в UI поток новый Runnable
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText("Сообщение отправлено!");
            }
        });
    }

}
