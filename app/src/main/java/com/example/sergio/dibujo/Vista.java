package com.example.sergio.dibujo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 2dam on 12/01/2015.
 */
public class Vista extends View{
    static int herramienta = R.id.brush;
    float x1 = 10;
    float y1 = 10;
    float x2 = 200;
    float y2 = 200;

    private float radio = 0;
    private int alto, ancho;
    private Paint pincel;
    public static int colorElegido = Color.BLACK;
    public static int grosor;
    private List<Recta> rectas = new ArrayList<Recta>();

    public static Bitmap mapaDeBits;
    private Canvas lienzoFondo;

    private Path rectaPoligonal = new Path();

    private MainActivity ma = new MainActivity();
    class Recta{
        public float x0, y0, xi, yi;

        Recta(float x0, float y0, float xi, float yi) {
            this.x0 = x0;
            this.y0 = y0;
            this.xi = xi;
            this.yi = yi;
        }
    }

    public Vista(Context context) {

        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        alto = h;
        ancho = w;
        mapaDeBits = Bitmap.createBitmap(w, h,Bitmap.Config.ARGB_8888); // ESTAS DOS LINEAS DE ABAJO SIRVEN PARA CREAR UN FONDO
        lienzoFondo = new Canvas(mapaDeBits);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pincel = new Paint();
        pincel.setColor(colorElegido);
        pincel.setStrokeWidth(grosor);
        pincel.setAntiAlias(true);

        pincel.setStyle(Paint.Style.STROKE); // PARA PINTAR LINEAS Y CURVAS CON PATH
        if(herramienta == R.id.brush){
            canvas.drawPath(rectaPoligonal,pincel);
        }
        if(herramienta == R.id.circulo){
            canvas.drawCircle(x0,y0,radio,pincel);
        }
        if(herramienta == R.id.rectangulo){
            canvas.drawRect(Math.min(x0,xi),Math.min(y0, yi),Math.max(x0, xi),Math.max(y0, yi), pincel);
        }
        if(herramienta == R.id.triangulo){

        }

        canvas.drawBitmap(mapaDeBits, 0, 0, null);
// PINTAR RECTANGULOS
        /*if(xi<0 || yi<0){
            lienzoFondo.drawRect(xi, yi, x0, y0, pincel);
        }else {
            canvas.drawRect(x0, y0, xi, yi, pincel);
        }*/
      /*  for(Recta r : rectas) {
            canvas.drawLine(r.x0, r.y0, r.xi, r.yi, pincel);
        }*/

        // PARA DESHACER LO MISMO DEL FOR PERO CON LIENZO DE FONDO
        //canvas.drawLine(x0, y0, xi, yi, pincel);
        // DIBUJAR UN CIRCULO
        //canvas.drawCircle(x0,y0,radio,pincel);

    }

    private float x0=-1, y0=-1, xi=-1, yi=-1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x0=x;
                y0=y;
                rectaPoligonal.reset();
                rectaPoligonal.moveTo(x0, y0);
                break;
            case MotionEvent.ACTION_MOVE:
                xi=x;
                yi=y;
                x1 = x;
                y1 = y;
                //radio = (float) Math.sqrt((Math.pow(x0-y0,2))+(Math.pow(xi-yi,2)));
                 //lienzoFondo.drawRect(x0, y0, xi, yi, pincel);
                //lienzoFondo.drawCircle(x0, y0, 2, pincel); // PARA PINTAR ESTILO SPRAY O DIFUSO (FLUJO)
                if(herramienta == R.id.brush) {
                    rectaPoligonal.quadTo(xi, yi, (x + xi) / 2, (y + yi) / 2); // RECTA CUADRATICA
                    lienzoFondo.drawPath(rectaPoligonal, pincel);
                }
                if(herramienta == R.id.circulo){
                    x1 = x;
                    y1 = y;
                    radio = (float) Math.sqrt((Math.pow(x-x0,2))+(Math.pow(y-y0,2)));
                }
                if(herramienta == R.id.rectangulo){
                    x1 = x;
                    y1 = y;

                }

                 invalidate(); // MUY IMPORTANTE, HACE QUE SEA LLAMADO EL MÉTODO onDraw
                break;
            case MotionEvent.ACTION_UP:
                xi=x;
                yi=y;
                if(herramienta == R.id.brush) {
                    lienzoFondo.drawPath(rectaPoligonal, pincel); // NO SE PIRDE LA RECTA CON PATH
                    rectaPoligonal.reset();
                }
                if(herramienta == R.id.circulo){
                    radio = (float) Math.sqrt((Math.pow(x-x0,2))+(Math.pow(y-y0,2)));
                    lienzoFondo.drawCircle(x0,y0,radio,pincel);
                }
                if(herramienta == R.id.rectangulo){
                    x1 = x;
                    y1 = y;
                    lienzoFondo.drawRect(x0, y0, xi, yi, pincel);
                }

//                radio = (float) Math.sqrt((Math.pow(x0-y0,2))+(Math.pow(xi-yi,2)));
                /*if(xi<0 || yi<0){
                    lienzoFondo.drawRect(xi, yi, x0, y0, pincel);
                }else {
                    lienzoFondo.drawRect(x0, y0, xi, yi, pincel);
                }// ASI NO SE PIERDEn LAS LINEAS CON BITMAP*/
                //rectas.add(new Recta(x0,y0,xi,yi)); ESTO PARA Q NO SE PIERDA (ES PEOR)
                invalidate(); // MUY IMPORTANTE, HACE QUE SEA LLAMADO EL MÉTODO onDraw
                x0=y0=xi=yi=-1;
                radio = 0;
                break;
        }
        return true;


    }





}
