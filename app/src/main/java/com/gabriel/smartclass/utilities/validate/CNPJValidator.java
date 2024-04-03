package com.gabriel.smartclass.utilities.validate;

import java.util.ArrayList;
import java.util.List;

public abstract class CNPJValidator {

    public static boolean validateCNPJ(String cpnj){
        if(cpnj.equals("")){
            return false;
        } else if (cpnj.equals("  .   .   /    .  ")) {
            return false;
        } else if (cpnj.equals("00.000.000/0000.00")) {
            return false;
        } else if (cpnj.equals("11.111.111/1111.00")) {
            return false;
        } else if (cpnj.equals("22.222.222/2222.22")) {
            return false;
        } else if (cpnj.equals("33.333.333/3333.33")) {
            return false;
        } else if (cpnj.equals("44.444.444/4444.44")) {
            return false;
        } else if (cpnj.equals("55.555.555/5555.55")) {
            return false;
        } else if (cpnj.equals("66.666.666/6666.66")) {
            return false;
        } else if (cpnj.equals("77.777.777/7777.77")) {
            return false;
        } else if (cpnj.equals("88.888.888/8888.88")) {
            return false;
        } else if (cpnj.equals("99.999.999/9999.99")) {
            return false;
        }else{
            List<Integer> cnpjNumbers = new ArrayList<>();
            List<Integer> numerals = new ArrayList<>();
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

        }



        return false;
    }
}
