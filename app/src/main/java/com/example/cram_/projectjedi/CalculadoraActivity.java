package com.example.cram_.projectjedi;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class CalculadoraActivity extends BaseActivity implements  View.OnClickListener {

    TextView display;
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, bAdd, bSub, bMul, bDiv, bEq, bCE, bAns, bPoint;
    boolean notif = false;

    double lastNum = 0;
    double lastResult = 0;
    String lastOp = "=";
    boolean newNumber = true;
    boolean dot = false;

    MenuItem alert;
    MenuItem notification;
    Users users;

    SharedPreferences settings;
    public static final String PREFS_NAME = "prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);

        display = (TextView) findViewById(R.id.textView);
        users = new Users(getApplicationContext());

        if (savedInstanceState != null) {
            notif = savedInstanceState.getBoolean("notif");
            lastNum = savedInstanceState.getDouble("lastNum");
            lastResult = savedInstanceState.getDouble("lastResult");
            lastOp = savedInstanceState.getString("lastOp");
            newNumber = savedInstanceState.getBoolean("newNumber");
            dot = savedInstanceState.getBoolean("dot");
            display.setText(savedInstanceState.getString("displayText"));
        }

        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Cursor c = users.getNotif(settings.getString("user", "no name"));
        String typeNotification = "alert";
        if(c.moveToFirst())
            typeNotification = c.getString(c.getColumnIndex("notif"));
        notif=typeNotification!="alert";
        setButtons();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("notif", notif);
        outState.putDouble("lastNum", lastNum);
        outState.putDouble("lastResult",lastResult);
        outState.putString("lastOp", lastOp);
        outState.putBoolean("newNumber", newNumber);
        outState.putBoolean("dot", dot);
        outState.putString("displayText", display.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        String typeNotification = "alert";
        Cursor c = users.getNotif(settings.getString("user","no name"));
        if(c.moveToFirst())
            typeNotification = c.getString(c.getColumnIndex("notif"));
        if(typeNotification.equals("alert")) {
            notification.setChecked(false);
            alert.setChecked(true);
        }
        else {
            alert.setChecked(false);
            notification.setChecked(true);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calculadora, menu);

        notification = menu.findItem(R.id.notification);
        alert = menu.findItem(R.id.dialog);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.apply();

                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.call:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:972340598"));
                startActivity(intent);
                return true;
            case R.id.navegator:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(intent);
                return true;
            case R.id.notification:
                users.setNotif("notification", settings.getString("user", "no name"));
                notif = true;
                return true;
            case R.id.dialog:
                users.setNotif("alert", settings.getString("user", "no name"));
                notif = false;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                writeNumber(Double.toString(lastResult));
                break;
            case R.id.buttonDot:
                if(!dot) {
                    String displayText = display.getText().toString();
                    if(newNumber) {
                        displayText = "0";
                        newNumber = false;
                    }
                    display.setText(displayText + ".");
                    dot = true;
                }
        }
    }

    public void writeNumber(String number) {
        String displayText = display.getText().toString();
        if (newNumber) { //displayText.equals("0")
            display.setText(number);
            if(!number.equals("0"))
                newNumber = false;
        }
        else if (displayText.length()<15)
            display.setText(displayText + number);
    }

    public void operate(String operation) {
        String displayText = display.getText().toString();
        double actualNum = Double.parseDouble(displayText);
        if(lastOp.equals("+"))
            lastNum += actualNum;
        else if(lastOp.equals("-"))
            lastNum -= actualNum;
        else if(lastOp.equals("*"))
            lastNum *= actualNum;
        else if(lastOp.equals("/")) {
            if (actualNum == 0)
                showError();
            else
                lastNum /= actualNum;
        }
        else if(lastOp.equals("=")) {
            lastResult = lastNum;
            lastNum = actualNum;
        }

        String displayedText;
        DecimalFormat DECIMAL_FORMATER = new DecimalFormat("##.##");
        DecimalFormat SCIENTIFIC_FORMATER = new DecimalFormat("0.##########E0");
        displayedText = DECIMAL_FORMATER.format(lastNum).replace(",",".");
        if(displayedText.length()>14)
            displayedText = SCIENTIFIC_FORMATER.format(lastNum).replace(",",".");
        display.setText(displayedText);
        lastOp = operation;
        newNumber = true;
        dot = false;
    }

    public void erase() {
        display.setText("0");
        newNumber = true;
    }

    public void showError() {
        if(notif) {
            int mId = 1;
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.jediimage)
                            .setContentTitle("Error")
                            .setContentText("Has dividido entre 0");


            /*Intent resultIntent = new Intent(getApplicationContext(), CalculadoraActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addParentStack(CalculadoraActivity.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);*/

            mNotificationManager.notify(mId, mBuilder.build());
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(CalculadoraActivity.this);

            builder.setTitle("Error");
            builder.setMessage("Has dividido un número entre 0");

            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
                        }
                    }
            );
            AlertDialog dialog = builder.create();
            dialog.show();
        }
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
        bPoint = (Button) findViewById(R.id.buttonDot);
        bPoint.setOnClickListener(this);
    }
}
