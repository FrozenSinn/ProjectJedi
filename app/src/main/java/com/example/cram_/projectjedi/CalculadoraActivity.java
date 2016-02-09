package com.example.cram_.projectjedi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CalculadoraActivity extends BaseActivity implements  View.OnClickListener {

    TextView display;
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, bAdd, bSub, bMul, bDiv, bEq, bCE, bAns;

    float lastNum = 0;
    float lastResult = 0;
    String lastOp = "=";
    boolean newNumber = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);
        currentView = R.id.activityCalc;

        display = (TextView) findViewById(R.id.textView);

        setButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                writeNumber("1");
                break;
            case R.id.button2:
                writeNumber("2");
                break;
            case R.id.button3:
                writeNumber("3");
                break;
            case R.id.button4:
                writeNumber("4");
                break;
            case R.id.button5:
                writeNumber("5");
                break;
            case R.id.button6:
                writeNumber("6");
                break;
            case R.id.button7:
                writeNumber("7");
                break;
            case R.id.button8:
                writeNumber("8");
                break;
            case R.id.button9:
                writeNumber("9");
                break;
            case R.id.button0:
                writeNumber("0");
                break;
            case R.id.buttonAdd:
                operate("+");
                break;
            case R.id.buttonSub:
                operate("-");
                break;
            case R.id.buttonMul:
                operate("*");
                break;
            case R.id.buttonDiv:
                operate("/");
                break;
            case R.id.buttonEq:
                operate("=");
                break;
            case R.id.buttonCE:
                erase();
                break;
            case R.id.buttonAns:
                writeNumber(Float.toString(lastResult));
                break;
        }
    }

    public void writeNumber(String number) {
        String displayText = display.getText().toString();
        if (displayText.length()<15) {
            if (newNumber) { //displayText.equals("0")
                display.setText(number);
                if(!number.equals("0"))
                    newNumber = false;
            }
            else
                display.setText(displayText + number);
        }
    }

    public void operate(String operation) {
        String displayText = display.getText().toString();
        float actualNum = Float.parseFloat(displayText);
        if(lastOp.equals("+"))
            lastNum += actualNum;
        else if(lastOp.equals("-"))
            lastNum -= actualNum;
        else if(lastOp.equals("*"))
            lastNum *= actualNum;
        else if(lastOp.equals("/"))
            lastNum /= actualNum;
        else if(lastOp.equals("=")) {
            lastResult = lastNum;
            lastNum = actualNum;
        }
        //new java.text.DecimalFormat("#").format(lastNum);
        display.setText(String.valueOf(lastNum));
        lastOp = operation;
        newNumber = true;
    }

    public void erase() {
        display.setText("0");
        newNumber = true;
    }

    public void setButtons() {
        b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(this);
        b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(this);
        b3 = (Button) findViewById(R.id.button3);
        b3.setOnClickListener(this);
        b4 = (Button) findViewById(R.id.button4);
        b4.setOnClickListener(this);
        b5 = (Button) findViewById(R.id.button5);
        b5.setOnClickListener(this);
        b6 = (Button) findViewById(R.id.button6);
        b6.setOnClickListener(this);
        b7 = (Button) findViewById(R.id.button7);
        b7.setOnClickListener(this);
        b8 = (Button) findViewById(R.id.button8);
        b8.setOnClickListener(this);
        b9 = (Button) findViewById(R.id.button9);
        b9.setOnClickListener(this);
        b0 = (Button) findViewById(R.id.button0);
        b0.setOnClickListener(this);
        bAdd = (Button) findViewById(R.id.buttonAdd);
        bAdd.setOnClickListener(this);
        bSub = (Button) findViewById(R.id.buttonSub);
        bSub.setOnClickListener(this);
        bMul = (Button) findViewById(R.id.buttonMul);
        bMul.setOnClickListener(this);
        bDiv = (Button) findViewById(R.id.buttonDiv);
        bDiv.setOnClickListener(this);
        bEq = (Button) findViewById(R.id.buttonEq);
        bEq.setOnClickListener(this);
        bCE = (Button) findViewById(R.id.buttonCE);
        bCE.setOnClickListener(this);
        bAns = (Button) findViewById(R.id.buttonAns);
        bAns.setOnClickListener(this);
    }
}
