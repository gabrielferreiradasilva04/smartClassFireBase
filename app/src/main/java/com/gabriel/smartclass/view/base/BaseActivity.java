package com.gabriel.smartclass.view.base;

import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public void hideKeyboard(){
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(), 0);
        }
    }
}
