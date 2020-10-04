package com.example.calculator1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

public class MainActivity extends AppCompatActivity {
    private EditText result;
    private EditText newNum;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button butsin;
    private  Button butcos;
    private Button  buttan;
    private  Button butlog;
    private  Button butsquare;
    private  Button butroot;
    private  Button buttondecimal;
    private Double num1 = null;
    private Double num2 = null;
    private TextView operation1;
    private String pendingoperation = "=";
    private  static final String state_pending="pendingoperation";
    private static  final String state_operand1="operand1";
    private static final int SPEECH_REQUEST_CODE = 0;



    // Create an intent that can start the Speech Recognizer activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button voice=(Button) findViewById(R.id.voice);
        result = (EditText) findViewById(R.id.editText);
        newNum = (EditText) findViewById(R.id.newNumber);
        operation1 = (TextView) findViewById(R.id.operation);
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
         button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        butsin=(Button) findViewById(R.id.butsin);
        butcos=(Button)findViewById(R.id.btncos);
        butlog=(Button) findViewById(R.id.btnlog);
        butroot=(Button) findViewById(R.id.btnroot) ;
        buttan=(Button) findViewById(R.id.btntan);
        butsquare=(Button)findViewById(R.id.btnsquare) ;
       Button buttonadd = (Button) findViewById(R.id.add);
       Button buttonminus = (Button) findViewById(R.id.sub);
        Button buttonmultiply = (Button) findViewById(R.id.multiply);
        Button buttondivide = (Button) findViewById(R.id.divide);
        buttondecimal = (Button) findViewById(R.id.decimal);
        Button buttonequal = (Button) findViewById(R.id.equal);
        Button buttonclear = (Button) findViewById(R.id.clear);

        View.OnClickListener ourOnClickListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                newNum.append(b.getText().toString());


            }
        };
        View.OnClickListener voicelistner=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySpeechRecognizer();

            }
        };
        voice.setOnClickListener(voicelistner);

        button0.setOnClickListener(ourOnClickListner);
        button1.setOnClickListener(ourOnClickListner);
        button2.setOnClickListener(ourOnClickListner);
        button3.setOnClickListener(ourOnClickListner);
        button4.setOnClickListener(ourOnClickListner);
        button5.setOnClickListener(ourOnClickListner);
        button6.setOnClickListener(ourOnClickListner);
        button7.setOnClickListener(ourOnClickListner);
        button8.setOnClickListener(ourOnClickListner);
        button9.setOnClickListener(ourOnClickListner);
        buttondecimal.setOnClickListener(ourOnClickListner);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button B = (Button) v;
                String op = B.getText().toString();
                String value = newNum.getText().toString();
                try {
                    performOperation(value, op);
                } catch (NumberFormatException e) {
                    newNum.setText("");
                }

                pendingoperation = op;
                operation1.setText(pendingoperation);
            }
        };
        View.OnClickListener operation=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                String st = b.getText().toString();
                perform(st);
            }
        };

        butsin.setOnClickListener(operation);
        butcos.setOnClickListener(operation);
        butsquare.setOnClickListener(operation);
        buttan.setOnClickListener(operation);
        butlog.setOnClickListener(operation);
        butroot.setOnClickListener(operation);
        buttan.setOnClickListener(operation);
        buttonadd.setOnClickListener(listener);
        buttonminus.setOnClickListener(listener);
        buttonmultiply.setOnClickListener(listener);
        buttondivide.setOnClickListener(listener);
        buttonequal.setOnClickListener(listener);

        View.OnClickListener clear1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("");
                newNum.setText("");
                num1 = null;
                num2 = null;
                pendingoperation = "=";
                operation1.setText("");
            }
        };
        buttonclear.setOnClickListener(clear1);
        Button Neg=(Button) findViewById(R.id.neg);

        View.OnClickListener negative=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value=newNum.getText().toString();
                if(value.length()==0){
                    newNum.setText("-");
                }else{
                    try {
                        Double DOUBLEVALUE=Double.valueOf(value);
                        DOUBLEVALUE*=-1;
                        newNum.setText(DOUBLEVALUE.toString());
                    }catch (NumberFormatException E){
                        newNum.setText("");
                    }
                }


            }
        };
        Neg.setOnClickListener(negative);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(state_pending,pendingoperation);
        if (num1!=null){
            outState.putDouble(state_operand1,num1);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pendingoperation=savedInstanceState.getString(state_pending);
        num1=savedInstanceState.getDouble(state_operand1);
        operation1.setText(pendingoperation);
    }

    private void performOperation(String value, String op) {
        if (num1 == null) {
            num1 = Double.valueOf(value);

        } else {

            num2 = Double.valueOf(value);
            if (pendingoperation.equals("=")) {
                pendingoperation = op;
            }
            switch (pendingoperation) {
                case "/":
                    if (num2 == 0) {
                        num1 = 0.0;
                    } else {
                        num1 = num1 / num2;
                    }
                    break;
                case "*":
                    num1 = num1 * num2;
                    break;
                case "+":
                    num1 = num1 + num2;
                    break;
                case "-":
                    num1 = num1 - num2;
                    break;
                case "=":
                    num1 = Double.valueOf(value);
                    break;
                case "%":
                    num1=num1%num2;
                    break;

            }
        }
        result.setText(num1.toString());
        newNum.setText("");


    }
    private void perform(String st){
            if(st.equalsIgnoreCase("Sin")){

                try {
                    Double d = Double.valueOf(newNum.getText().toString());
                    d = Math.toRadians(d);
                    d = Math.sin(d);
                    newNum.setText(d.toString());

                } catch (NumberFormatException E) {
                    newNum.setText(" ");

                }
            }else  if(st.equalsIgnoreCase("Cos")){

                try {
                    Double d = Double.valueOf(newNum.getText().toString());
                    d = Math.toRadians(d);
                    d = Math.cos(d);
                    newNum.setText(d.toString());

                } catch (NumberFormatException E) {
                    newNum.setText(" ");

                }
            }else  if(st.equalsIgnoreCase("Tan")){

                try {
                    Double d = Double.valueOf(newNum.getText().toString());
                    d = Math.toRadians(d);
                    d = Math.tan(d);
                    newNum.setText(d.toString());

                } catch (NumberFormatException E) {
                    newNum.setText(" ");

                }
            }else  if(st.equalsIgnoreCase("Log")){

                try {
                    Double d = Double.valueOf(newNum.getText().toString());

                    d = Math.log(d);
                    newNum.setText(d.toString());


                } catch (NumberFormatException E) {
                    newNum.setText(" ");

                }
            }else  if(st.equalsIgnoreCase("Square")){

                try {
                    Double d = Double.valueOf(newNum.getText().toString());

                    d = d*d;
                    newNum.setText(d.toString());

                } catch (NumberFormatException E) {
                    newNum.setText(" ");

                }
            }else  if(st.equalsIgnoreCase("Root")){

                try {
                    Double d = Double.valueOf(newNum.getText().toString());
                    num1 = Math.sqrt(d);
                    newNum.setText(d.toString());

                } catch (NumberFormatException E) {
                    newNum.setText(" ");

                }
            }
        }
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
             if(spokenText.equalsIgnoreCase("stop")||spokenText.equalsIgnoreCase("Stop")){
                Toast.makeText(this,"Back to Keyboard",Toast.LENGTH_LONG).show();
            }
            else if(spokenText.equalsIgnoreCase("Zero")||spokenText.equalsIgnoreCase("0")){
                displaySpeechRecognizer();
                newNum.append(button0.getText().toString());

            }else if(spokenText.equalsIgnoreCase("1")||spokenText.equalsIgnoreCase("One")||spokenText.equalsIgnoreCase("one")){
                newNum.append((button1.getText().toString()));
                displaySpeechRecognizer();

            }else if(spokenText.equalsIgnoreCase("2")||spokenText.equalsIgnoreCase("Two")||spokenText.equalsIgnoreCase("two")){
                newNum.append((button2.getText().toString()));
                displaySpeechRecognizer();

            }else if(spokenText.equalsIgnoreCase("3")||spokenText.equalsIgnoreCase("Three")||spokenText.equalsIgnoreCase("two")){
                newNum.append((button3.getText().toString()));
                displaySpeechRecognizer();

            }else if(spokenText.equalsIgnoreCase("4")||spokenText.equalsIgnoreCase("Four")){
                newNum.append((button4.getText().toString()));
                displaySpeechRecognizer();

            }else if(spokenText.equalsIgnoreCase("5")||spokenText.equalsIgnoreCase("Five")||spokenText.equalsIgnoreCase("v")){
                newNum.append((button5.getText().toString()));
                displaySpeechRecognizer();

            }else if(spokenText.equalsIgnoreCase("6")||spokenText.equalsIgnoreCase("Six")){
                newNum.append((button6.getText().toString()));
                displaySpeechRecognizer();

            }else if(spokenText.equalsIgnoreCase("7")||spokenText.equalsIgnoreCase("Seven")){
                newNum.append((button7.getText().toString()));
                displaySpeechRecognizer();

            }else if(spokenText.equalsIgnoreCase("8") ||spokenText.equalsIgnoreCase("Eight")){
                newNum.append((button8.getText().toString()));
                displaySpeechRecognizer();

            }else if(spokenText.equalsIgnoreCase("9")||spokenText.equalsIgnoreCase("Nine")){
                newNum.append((button9.getText().toString()));
                displaySpeechRecognizer();

            }
            else if(spokenText.equalsIgnoreCase("plus")||spokenText.equalsIgnoreCase("+")){
                String value = newNum.getText().toString();
                try {
                    performOperation(value, "+");
                } catch (NumberFormatException e) {
                    newNum.setText("");
                }

                pendingoperation = "+";
                operation1.setText(pendingoperation);
                displaySpeechRecognizer();

            }
            else if(spokenText.equalsIgnoreCase("minus")||spokenText.equalsIgnoreCase("-")){
                String value = newNum.getText().toString();
                try {
                    performOperation(value, "-");
                } catch (NumberFormatException e) {
                    newNum.setText("");
                }

                pendingoperation = "-";
                operation1.setText(pendingoperation);
displaySpeechRecognizer();

            }
            else if(spokenText.equalsIgnoreCase("answer")){
                String value = newNum.getText().toString();
                try {
                    performOperation(value, "=");
                } catch (NumberFormatException e) {
                    newNum.setText("");
                }

                pendingoperation = "=";
                operation1.setText(pendingoperation);

            }
            else if(spokenText.equalsIgnoreCase("multiply")||spokenText.equalsIgnoreCase("*")){
                String value = newNum.getText().toString();
                try {
                    performOperation(value, "*");
                } catch (NumberFormatException e) {
                    newNum.setText("");
                }

                pendingoperation = "*";
                operation1.setText(pendingoperation);
displaySpeechRecognizer();
            }  else if( spokenText.equalsIgnoreCase("divide")||spokenText.equalsIgnoreCase("/"))
            {
                String value = newNum.getText().toString();
            try {
                performOperation(value, "/");
            } catch (NumberFormatException e) {
                newNum.setText("");
            }

            pendingoperation = "/";
            operation1.setText(pendingoperation);

        }else if(spokenText.equalsIgnoreCase(("modulas"))){
                String value = newNum.getText().toString();
                try {
                    performOperation(value, "%");
                } catch (NumberFormatException e) {
                    newNum.setText("");
                }

                pendingoperation = "%";
                operation1.setText(pendingoperation);
displaySpeechRecognizer();
            }else if(spokenText.equalsIgnoreCase("decimal")||spokenText.equalsIgnoreCase("Decimal")){
                newNum.append((buttondecimal.getText().toString()));
                displaySpeechRecognizer();

            }

            else if(spokenText.equalsIgnoreCase("erase")||spokenText.equalsIgnoreCase("E")){
                result.setText("");
                newNum.setText("");
                num1 = null;
                num2 = null;
                pendingoperation = "=";
                operation1.setText("");
            }
            else if(spokenText.equalsIgnoreCase("negative")||spokenText.equalsIgnoreCase("Negative")){
                String value=newNum.getText().toString();
                if(value.length()==0){
                    newNum.setText("-");
                }else{
                    try {
                        Double DOUBLEVALUE=Double.valueOf(value);
                        DOUBLEVALUE*=-1;
                        newNum.setText(DOUBLEVALUE.toString());
                    }catch (NumberFormatException E){
                        newNum.setText("");
                    }
                }
                displaySpeechRecognizer();
            }if(spokenText.equalsIgnoreCase("Sin theta")||spokenText.equalsIgnoreCase("SIN theta".substring(0,3)))
            {

                try {
                    Double d = Double.valueOf(newNum.getText().toString());
                    d = Math.toRadians(d);
                    d = Math.sin(d);
                    newNum.setText(d.toString());

                } catch (NumberFormatException E) {
                    newNum.setText(" ");

                }
                displaySpeechRecognizer();
            }else  if(spokenText.equalsIgnoreCase("Cos theata")){

                try {
                    Double d = Double.valueOf(newNum.getText().toString());
                    d = Math.toRadians(d);
                    d = Math.cos(d);
                    newNum.setText(d.toString());

                } catch (NumberFormatException E) {
                    newNum.setText(" ");

                }
                displaySpeechRecognizer();
            }else  if(spokenText.equalsIgnoreCase("Tan theta")){

                try {
                    Double d = Double.valueOf(newNum.getText().toString());
                    d = Math.toRadians(d);
                    d = Math.tan(d);
                    newNum.setText(d.toString());

                } catch (NumberFormatException E) {
                    newNum.setText(" ");
                }


                displaySpeechRecognizer();
            }else  if(spokenText.equalsIgnoreCase("Log")){

                try {
                    Double d = Double.valueOf(newNum.getText().toString());

                    d = Math.log(d);
                    newNum.setText(d.toString());


                } catch (NumberFormatException E) {
                    newNum.setText(" ");

                }
                displaySpeechRecognizer();
            }else  if(spokenText.equalsIgnoreCase("Square ")){

                try {
                    Double d = Double.valueOf(newNum.getText().toString());

                    d = d*d;
                    newNum.setText(d.toString());

                } catch (NumberFormatException E) {
                    newNum.setText(" ");

                }
                displaySpeechRecognizer();
            }else  if(spokenText.equalsIgnoreCase(" Square Root")){

                try {
                    Double d = Double.valueOf(newNum.getText().toString());
                    num1 = Math.sqrt(d);
                    newNum.setText(d.toString());

                } catch (NumberFormatException E) {
                    newNum.setText(" ");

                }
                displaySpeechRecognizer();
            }


          else  {


                ErrorListener errorListener=new ErrorListener() {
                @Override
                public void warning(TransformerException exception) throws TransformerException {
                    displaySpeechRecognizer();
                }

                @Override
                public void error(TransformerException exception) throws TransformerException {
                  displaySpeechRecognizer();
                }

                @Override
                public void fatalError(TransformerException exception) throws TransformerException {
displaySpeechRecognizer();
                }
            };
               // Toast.makeText(this,"Voice Cannnot regonize",Toast.LENGTH_LONG).show();

                displaySpeechRecognizer();

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}