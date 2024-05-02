package com.gabriel.smartclass.utilities.listeners;

import android.view.View;
import android.widget.EditText;

public class CapacityTextListener implements View.OnFocusChangeListener {
    private EditText editText;

    public CapacityTextListener(EditText editText){
        this.editText = editText;
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            if(editText.getText().toString().equals("")){
                editText.setText("0");
            }
        }
    }
}
