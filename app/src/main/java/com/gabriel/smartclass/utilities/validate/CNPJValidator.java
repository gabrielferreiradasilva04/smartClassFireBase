package com.gabriel.smartclass.utilities.validate;

import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public abstract class CNPJValidator {

    public static boolean validateCNPJ(String cnpj) throws ParseException, RuntimeException, Exception, NumberFormatException, NullPointerException {
        if("".equals(cnpj)){
            return false;
        } else if (cnpj.equals("  .   .   /    .  ")) {
            return false;
        } else if (cnpj.equals("00.000.000/0000.00")) {
            return false;
        } else if (cnpj.equals("11.111.111/1111.11")) {
            return false;
        } else if (cnpj.equals("22.222.222/2222.22")) {
            return false;
        } else if (cnpj.equals("33.333.333/3333.33")) {
            return false;
        } else if (cnpj.equals("44.444.444/4444.44")) {
            return false;
        } else if (cnpj.equals("55.555.555/5555.55")) {
            return false;
        } else if (cnpj.equals("66.666.666/6666.66")) {
            return false;
        } else if (cnpj.equals("77.777.777/7777.77")) {
            return false;
        } else if (cnpj.equals("88.888.888/8888.88")) {
            return false;
        } else if (cnpj.equals("99.999.999/9999.99")) {
            return false;
        }else{
            List<Integer> cnpjNumbers = new ArrayList<>();
            List<Integer> numerals = new ArrayList<>();
            StringBuilder sb = new StringBuilder(cnpj);
            numerals.add(5);
            numerals.add(4);
            numerals.add(3);
            numerals.add(2);
            numerals.add(9);
            numerals.add(8);
            numerals.add(7);
            numerals.add(6);
            numerals.add(5);
            numerals.add(4);
            numerals.add(3);
            numerals.add(2);
            /*CNPJ numbers*/
            int i = 0;
            while(i < cnpj.length()){
                if(!Character.isDigit(cnpj.charAt(i))){
                    sb.deleteCharAt(i); /*Pegar somente os numeros do CNPJ*/
                    cnpj = sb.toString();
                }
                i++;
            }
            String cnpjWithoutCharacters = sb.toString(); /*String com somente os numeros do CNPJ*/


            for(int j = 0 ; j < cnpjWithoutCharacters.length() ; j++ ){
                cnpjNumbers.add(Integer.parseInt(Character.toString(cnpjWithoutCharacters.charAt(j)))); /*converte os numeros do CNPJ*/
            }
            return true;
        }
    }
}
