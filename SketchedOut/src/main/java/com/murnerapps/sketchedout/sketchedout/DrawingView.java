package com.murnerapps.sketchedout.sketchedout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Mark on 5/24/14.
 */
public class DrawingView extends View {


    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF000000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;
    private int brushSize = 20;
    private List<Path> moveList = new ArrayList<Path>(), undoList = new ArrayList<Path>(), currentMoveList = new ArrayList<Path>();
    private List<Paint> moveListPaint = new ArrayList<Paint>(), currentMoveListPaint = new ArrayList<Paint>();
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    public void setupDrawing(){
        //setup the drawing area for interaction
        drawPath = new Path();

        setupPaint();

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }
    protected void setupPaint(){
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);

        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//view given size
        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override

    protected void onDraw(Canvas canvas) {
        //draw view

        for(int i = 0; i< moveList.size(); i++){
            canvas.drawPath(moveList.get(i), moveListPaint.get(i));
        }
        for(int i = 0; i< currentMoveList.size(); i++){
            canvas.drawPath(currentMoveList.get(i), currentMoveListPaint.get(i));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//detect user touch
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                drawPath.lineTo(touchX, touchY + .1f);
                currentMoveList.add(drawPath);
                currentMoveListPaint.add(drawPaint);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                currentMoveList.add(drawPath);
                currentMoveListPaint.add(drawPaint);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                currentMoveList.add(drawPath);
                moveList.add(drawPath);
                moveListPaint.add(drawPaint);
                currentMoveList.clear();
                currentMoveListPaint.clear();
                drawPath = new Path();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void setColor(String newColor){
//set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        setupPaint();
    }

    public void deleteDrawing(){
        moveList = new ArrayList<Path>();
        moveListPaint = new ArrayList<Paint>();
        currentMoveList = new ArrayList<Path>();
        currentMoveListPaint = new ArrayList<Paint>();
        undoList = new ArrayList<Path>();
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void undo() {
        if (moveList != null && moveList.size() > 0) {
            undoList.add(moveList.remove(moveList.size() - 1));
            moveListPaint.remove(moveListPaint.size() -1);
            invalidate();
        }

    }

    public boolean isEmpty(){
        return moveList.size() == 0 && currentMoveList.size() == 0;
    }
}
