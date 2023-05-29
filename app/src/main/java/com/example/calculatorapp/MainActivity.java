package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView resultTv, solutionTv;
    MaterialButton buttonC;
    MaterialButton buttonBrackOpen;
    MaterialButton buttonBrackClose;
    MaterialButton buttonDivide, buttonMultiply, buttonPlus, buttonMinus, buttonEquals;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonAC, buttonDot;
    int leftBracket,rightBracket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);

        assignId(buttonC, R.id.button_c);
        assignId(buttonBrackOpen, R.id.button_open_bracket);
        assignId(buttonBrackClose, R.id.button_closed_bracket);
        assignId(buttonDivide, R.id.button_divide);
        assignId(buttonMultiply, R.id.button_multiply);
        assignId(buttonPlus, R.id.button_addition);
        assignId(buttonMinus, R.id.button_minus);
        assignId(buttonEquals, R.id.button_equal);
        assignId(button0, R.id.button_0);
        assignId(button1, R.id.button_1);
        assignId(button2, R.id.button_2);
        assignId(button3, R.id.button_3);
        assignId(button4, R.id.button_4);
        assignId(button5, R.id.button_5);
        assignId(button6, R.id.button_6);
        assignId(button7, R.id.button_7);
        assignId(button8, R.id.button_8);
        assignId(button9, R.id.button_9);
        assignId(buttonAC, R.id.button_ac);
        assignId(buttonDot, R.id.button_dot);

        leftBracket=0;
        rightBracket=0;
    }

    void assignId(MaterialButton btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTv.getText().toString();

        if(buttonText.equals("AC")){
            solutionTv.setText("");
            resultTv.setText("");
            return;
        }
        if(buttonText.equals("=")){
            if(resultTv.getText()!="")
                solutionTv.setText(resultTv.getText());
            else
                solutionTv.setText("");
            return;
        }
        if(dataToCalculate.length()==0){
            String op = "/*+)";
            if(op.contains(buttonText))
                return;
            if(buttonText.equals(".")){
                dataToCalculate = "0";
            }
        }

        if(dataToCalculate.length()>0){
            String num = "1234567890", op = "+/*)";
            if(buttonText.equals(".")&&!num.contains(""+dataToCalculate.charAt(dataToCalculate.length()-1))){
                dataToCalculate+="0";
            }
            else if(buttonText.equals("(")){
                char d = dataToCalculate.charAt(dataToCalculate.length()-1);
                if(num.contains(""+d)||d==')')return;

            }
            else if(buttonText.equals("-")){
                char d = dataToCalculate.charAt(dataToCalculate.length()-1);
                if(d=='-')return;
                if(d=='+')dataToCalculate = dataToCalculate.substring(0,dataToCalculate.length()-1);
            }
            else if(op.contains(buttonText)){
                switch (dataToCalculate.charAt(dataToCalculate.length()-1)){
                    case '+':
                    case '*':
                    case '/':
                    case '-':{
                        char d = dataToCalculate.charAt(dataToCalculate.length()-2);
                        if(d=='+'||d=='/'||d=='*')
                            dataToCalculate = dataToCalculate.substring(0,dataToCalculate.length()-1);
                        dataToCalculate = dataToCalculate.substring(0,dataToCalculate.length()-1);
                    }
                }
            }
        }
        if (buttonText.equals("C")){
            if(dataToCalculate.length()>0)
                dataToCalculate = dataToCalculate.substring(0,dataToCalculate.length()-1);
        }else{
            dataToCalculate = dataToCalculate+buttonText;
        }

        Pattern pattern = Pattern.compile("\\d+\\)*[+-/*]{1,2}\\(*\\d");
        Matcher matcher = pattern.matcher(dataToCalculate);
        solutionTv.setText(dataToCalculate);
        String finalResult="";
        if(dataToCalculate.length()>0 && matcher.find()){
            String op="+-/*";
            if(op.contains(""+dataToCalculate.charAt(dataToCalculate.length()-1))){
                if(op.contains(""+dataToCalculate.charAt(dataToCalculate.length()-2)))
                    finalResult = getResult(dataToCalculate.substring(0,dataToCalculate.length()-2));
                else
                    finalResult = getResult(dataToCalculate.substring(0,dataToCalculate.length()-1));
            }
            else
                finalResult = getResult(dataToCalculate);
        }
        else
            finalResult = "";

        if(!finalResult.equals("Err")){
            resultTv.setText(finalResult);
        }

    }
    String getResult(String data){
        try{
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult = context.evaluateString(scriptable,data,"Javascript",1,null).toString();

            if(finalResult.endsWith(".0")){
                finalResult = finalResult.replace(".0", "");
            }
            return finalResult;
        }catch(Exception e){
            return "Err";

        }

    }
}