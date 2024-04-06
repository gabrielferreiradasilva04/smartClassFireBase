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
            List<Integer> numerals1 = new ArrayList<>();
            List<Integer> numerals2 = new ArrayList<>();
            List<Integer> results1 = new ArrayList<>();
            List<Integer> results2 = new ArrayList<>();
            StringBuilder sbCnpj = new StringBuilder(cnpj);
            StringBuilder sbExpectedCnpj;
            List<Integer> expectedCnpjNumbers = new ArrayList<>();
            /*validação do primeiro digito*/
            numerals1.add(5);
            numerals1.add(4);
            numerals1.add(3);
            numerals1.add(2);
            numerals1.add(9);
            numerals1.add(8);
            numerals1.add(7);
            numerals1.add(6);
            numerals1.add(5);
            numerals1.add(4);
            numerals1.add(3);
            numerals1.add(2);
            /*CNPJ numbers*/
            int i = 0;
            while(i < cnpj.length()){
                if(!Character.isDigit(cnpj.charAt(i))){
                    sbCnpj.deleteCharAt(i);
                    cnpj = sbCnpj.toString();
                }
                i++;
            }
            String cnpjWithoutCharacters = sbCnpj.toString(); /*String com somente os numeros do CNPJ*/
            sbExpectedCnpj = new StringBuilder(sbCnpj.delete(12,14).toString());
            for(int j = 0 ; j < cnpjWithoutCharacters.length() ; j++ ){
                cnpjNumbers.add(Integer.parseInt(Character.toString(cnpjWithoutCharacters.charAt(j)))); /*converte os numeros do CNPJ*/
            }
            for(int j = 0; j < numerals1.size() ; j++){
                int result = numerals1.get(j) * cnpjNumbers.get(j);
                results1.add(result);
            }
            int sumResult1 = 0;
            for(int j = 0 ; j < results1.size() ; j++){
                sumResult1 += results1.get(j);
            }

            int digit1 = cnpjNumbers.get(12);
            int digit2 = cnpjNumbers.get(13);

            int resultDigit1;

            int restOfDivision1 = (sumResult1 % 11);
            if(restOfDivision1 < 2){
                resultDigit1 = 0;
                String resultDigitStr = Integer.toString(resultDigit1);
                Character character = resultDigitStr.charAt(0);
                sbExpectedCnpj.append(character);
            }else{
                resultDigit1 = 11 - restOfDivision1;
                String resultDigitStr = Integer.toString(resultDigit1);
                Character character = resultDigitStr.charAt(0);
                sbExpectedCnpj.append(character);
            }
            /*Validação do segundo digito*/
            String cnpjWithFirstValidatorDigit = sbExpectedCnpj.toString();
            for(int j = 0 ; j < cnpjWithFirstValidatorDigit.length() ; j++ ){
                expectedCnpjNumbers.add(Integer.parseInt(Character.toString(cnpjWithFirstValidatorDigit.charAt(j))));
            }
            numerals2.add(6);
            numerals2.add(5);
            numerals2.add(4);
            numerals2.add(3);
            numerals2.add(2);
            numerals2.add(9);
            numerals2.add(8);
            numerals2.add(7);
            numerals2.add(6);
            numerals2.add(5);
            numerals2.add(4);
            numerals2.add(3);
            numerals2.add(2);

            for(int j = 0; j < numerals2.size() ; j++){
                int result = numerals2.get(j) * expectedCnpjNumbers.get(j);
                results2.add(result);
            }

            int sumResult2 = 0;
            for(int j = 0 ; j < results2.size() ; j++){
                sumResult2 += results2.get(j);
            }

            int resultDigit2;
            int restOfDivision2 = (sumResult2 % 11);

            if (restOfDivision2 < 2){
                resultDigit2 = 0;
                String resultDigitStr = Integer.toString(resultDigit2);
                Character character = resultDigitStr.charAt(0);
                sbExpectedCnpj.append(character);
            }else{
                resultDigit2 = 11 - restOfDivision2;
                String resultDigitStr = Integer.toString(resultDigit2);
                Character character = resultDigitStr.charAt(0);
                sbExpectedCnpj.append(character);
            }
            ;
            String expectedCnpj = sbExpectedCnpj.toString();

            return expectedCnpj.equals(cnpjWithoutCharacters);

        }
    }
}
