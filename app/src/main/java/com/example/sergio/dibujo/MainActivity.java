package com.example.sergio.dibujo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements ColorPicker.OnColorChangedListener{
    private Paint pincel;
    private TextView tvGrosor;
    private Button dialogButton;
    private SeekBar sk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pincel = new Paint();
        pincel.setColor(Color.BLACK);

        pincel.setStrokeWidth(8);
        pincel.setAntiAlias(true);

        pincel.setStyle(Paint.Style.STROKE);
        setContentView(new Vista(this));
        //Toast.makeText(this, "COLOR POR DEFECTO BLANCO",Toast.LENGTH_LONG).show();
    }
    public void getColor() {
        new ColorPicker(this,MainActivity.this, Color.WHITE)
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.pickColor){
            getColor();
        }
        if(id == R.id.brush) {
            Vista.herramienta = id;
            cambiarGrosor();
        }
        if(id == R.id.circulo){
            Vista.herramienta = id;
        }
        if(id == R.id.rectangulo){
            Vista.herramienta = id;
        }
        if(id == R.id.triangulo){
            Vista.herramienta = id;
            Toast.makeText(this, R.string.versionPRO,Toast.LENGTH_LONG).show();
        }
        if(id == R.id.guardar){
            exportar();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void colorChanged(int color) {
        //MainActivity.this.findViewById(android.R.id.content).setBackgroundColor(color); ESTO ES PARA EL FONDO

        Vista.colorElegido = color;

    }
    public void cambiarGrosor(){
        LayoutInflater li = LayoutInflater.from(this);
        View myView = li.inflate(R.layout.dialogogrosor, null);

        final AlertDialog.Builder cDialog = new AlertDialog.Builder(this);
        cDialog.setView(myView);
        cDialog.setTitle(R.string.tituloDialogo);
        tvGrosor = (TextView)myView.findViewById(R.id.tvGrosor);
        tvGrosor.setText(String.valueOf(Vista.grosor));
        sk = (SeekBar)myView.findViewById(R.id.seekBar);
        sk.setProgress(Vista.grosor);
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvGrosor.setText(String.valueOf(progress));
                Vista.grosor = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        cDialog.setPositiveButton(R.string.btCerrarDialogo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        cDialog.create();
        cDialog.show();


        }

    public  void exportar() {
        Bitmap exp = Vista.mapaDeBits;
        File root = Environment.getExternalStorageDirectory();
        File myDir = new File(root.getAbsolutePath() + "/DCIM/Paint");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        Log.i("", "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            exp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
