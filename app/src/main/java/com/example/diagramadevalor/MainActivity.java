package com.example.diagramadevalor;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;

import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.*;
import android.graphics.Color;
import static com.example.diagramadevalor.R.*;

public class MainActivity extends Activity {
    long startTime;
    long endTime;
    float totalSegundos;//tiempo por actividad en segundos
    float totalFinalSegundos;//tiempo total de todo
    int actividad=000;
    ArrayList<Float> Verde=new ArrayList<Float>();
    ArrayList<Float> Amarillo=new ArrayList<Float>();
    ArrayList<Float> Rojo=new ArrayList<Float>();
    TextView grabar;
    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;
    PieChartView pieChartView;//la grafica1
    List pieData=new ArrayList<>();//los datos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grabar = (TextView) findViewById(R.id.TextView);
        pieChartView=findViewById(R.id.grafica1);
        startService(new Intent(this, Servicio.class));
    }
    @Override
    public void onStop() {
        super.onStop();
        stopService(new Intent(this, Servicio.class));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RECOGNIZE_SPEECH_ACTIVITY:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String strSpeech2Text = speech.get(0);
                    grabar.setText(strSpeech2Text);
                }
                if(grabar.getText().equals("amarillo")){
                    actividad=010;
                    startTime = System.currentTimeMillis();
                }else if(grabar.getText().equals("verde")){
                    actividad=100;
                    startTime = System.currentTimeMillis();
                }else if(grabar.getText().equals("rojo")) {
                    actividad = 001;
                    startTime = System.currentTimeMillis();
                }else{
                    actividad=000;
                    //actividad no reconocida
                    grabar.setText("Actividad no reconocida");
                }
                break;
            default:
                break;
        }
    }

    public void onClickImgBtnHablar(View v) {

        Intent intentActionRecognizeSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Configura el Lenguaje (Español-México)
        intentActionRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-MX");
        try {
            startActivityForResult(intentActionRecognizeSpeech,RECOGNIZE_SPEECH_ACTIVITY);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),"Tú dispositivo no soporta el reconocimiento por voz",Toast.LENGTH_SHORT).show();
        }
    }
    public void onClickBtnTerminar(View v){
       endTime=System.currentTimeMillis()-startTime;
       totalSegundos= ((float)endTime/1000);
        grabar.setText(grabar.getText()+" "+Float.toString(totalSegundos));
        grabar.clearComposingText();
        if(actividad==100){
            Verde.add(totalSegundos);
            totalFinalSegundos+=totalSegundos;
        }else if(actividad==010){
            Amarillo.add(totalSegundos);
            totalFinalSegundos+=totalSegundos;
        }else if(actividad==1){
            Rojo.add(totalSegundos);
            totalFinalSegundos+=totalSegundos;
        }else{
            //no existe esta actividad
            grabar.setText("No se puede medir una actividad no reconocida");
        }

    }

    public float sumarArray(ArrayList<Float> array){
        float suma=0.0f;
        for(int i=0;i< array.size();i++){
            suma+=array.get(i);
        }
        return suma;
    }

    public void onCLickBtnFinaliazar(View v){
        float verde=sumarArray(Verde);
        float amarillo=sumarArray(Amarillo);
        float rojo=sumarArray(Rojo);
        String cadena="Actividad de valor: "+String.format("%.02f",verde/60)+" min\n"
                        +"Actividad no necesaria: "+String.format("%.02f",amarillo/60)+" min\n"
                    +"Actividad desperdiciada: "+String.format("%.02f",rojo/60)+" min \n"
                +"Tiempo total: "+String.format("%.02f",totalFinalSegundos/60)+"min";
        grabar.setText(cadena);
        pieData.clear();
        pieData.add(new SliceValue(verde,Color.GREEN).setLabel(" Actividad de valor "+String.format("%.02f",(verde/totalFinalSegundos)*100)+"%"));
        pieData.add(new SliceValue(amarillo,Color.YELLOW).setLabel(" Actividad no necesaria "+String.format("%.02f",(amarillo/totalFinalSegundos)*100)+"%"));
        pieData.add(new SliceValue(rojo,Color.RED).setLabel(" Actividad desperdiciada "+String.format("%.02f",(rojo/totalFinalSegundos)*100)+"%"));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(12);
        pieChartData.setHasCenterCircle(true).setCenterText1("Diagrama de valor").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);
    }
    public void onClickBtnReiniciar(View v){
        grabar.clearComposingText();
        pieData.clear();
        Verde.clear();
        Amarillo.clear();
        Rojo.clear();
        totalFinalSegundos=0.0f;
        grabar.setText("");
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartView.setPieChartData(pieChartData);

    }

}