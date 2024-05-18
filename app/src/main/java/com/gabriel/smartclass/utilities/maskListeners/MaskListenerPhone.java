package com.gabriel.smartclass.utilities.maskListeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MaskListenerPhone implements TextWatcher {
    private final EditText editTextMasked;
    private String lastCharacter;
    public MaskListenerPhone(EditText editTextMasked) {
        this.editTextMasked = editTextMasked;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (s != null) {
            int editTextSize = editTextMasked.getText().toString().length(); /*--> toda vez que o usuário for digitar ele pega qual o tamanho do editText*/
            if (editTextSize >= 1) {
                lastCharacter = editTextMasked.getText().toString().substring(editTextSize - 1); /*quando  o usuário digitar o segundo caracter ele identifica qual o primeiro*/
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null) {
            int editTextSize = editTextMasked.getText().toString().length(); /*--> toda vez que o usuário for digitar ele pega qual o tamanho do editText*/
            if (lastCharacter != null) {
                if (editTextSize == 1) {
                    if (!lastCharacter.equals("(")) {
                        editTextMasked.append("(");
                    } else {
                        editTextMasked.getText().delete(editTextSize - 1, editTextSize);
                    }
                } else if (editTextSize == 4) {
                    if (!lastCharacter.equals("")) {
                        editTextMasked.append(".");
                    } else {
                        editTextMasked.getText().delete(editTextSize - 1, editTextSize);
                    }

                } else if (editTextSize == 10) {
                    if (!lastCharacter.equals("/")) {
                        editTextMasked.append("/");
                    } else {
                        editTextMasked.getText().delete(editTextSize - 1, editTextSize);
                    }

                } else if (editTextSize == 15) {
                    if (!lastCharacter.equals("-")) {
                        editTextMasked.append("-");
                    } else {
                        editTextMasked.getText().delete(editTextSize - 1, editTextSize);
                    }
                }
            }

        }


    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
