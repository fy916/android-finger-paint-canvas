package com.fy916.android_finger_paint_canvas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by 20215603 Feiyang Wang.
 * The Activity which connects to brushselection.xml that can allow users to change brush settings.
 */
public class ChangeBrushActivity extends AppCompatActivity {
    int curr_brush = 1; //1 is round, 2 is square, 3 is butt
    int curr_brush_width = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ChangeBrushActivity", "OnCreate");

        setContentView(R.layout.brushselection); //Set the current view to brushselection.xml
        Bundle bundle = getIntent().getExtras();
        curr_brush_width = bundle.getInt("curr_brush_width"); //Get the current brush width for drawing
        curr_brush = bundle.getInt("curr_brush"); //Get the current brush type for drawing

        updateTextSelection(); //update the selection
    }

    // log onDestory status
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ChangeBrushActivity", "onDestroy");
    }

    // log onPause status
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ChangeBrushActivity", "onPause");
    }

    // log onResume status
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ChangeBrushActivity", "onResume");
    }

    // log onStart status
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ChangeBrushActivity", "onStart");
    }

    // log onStop status
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("ChangeBrushActivity", "onStop");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curr_brush", curr_brush); //save the current brush type
        outState.putInt("curr_brush_width", curr_brush_width); //save the current brush width
        Log.d("ChangeBrushActivity", "State Saved");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        curr_brush = savedInstanceState.getInt("curr_brush"); //restore the current brush type
        curr_brush_width = savedInstanceState.getInt("curr_brush_width"); //restore the current brush width
        Log.d("ChangeBrushActivity", "State Restored");
    }

    private void updateTextSelection() {
        final RadioGroup brushtypes = (RadioGroup) findViewById(R.id.brushtypes);
        final TextView textViewBrushType = (TextView) findViewById(R.id.brushType);
        final RadioGroup brushsizes = (RadioGroup) findViewById(R.id.brushsizes);

        //set brush type based on current brush settings
        switch (curr_brush) {
            case 1:
                brushtypes.check(R.id.brushRound);
                textViewBrushType.setText("Brush Shape is: Round");
                break;
            case 2:
                brushtypes.check(R.id.brushSquare);
                textViewBrushType.setText("Brush Shape is: Square");
                break;
            default:
                brushtypes.check(R.id.brushButt);
                textViewBrushType.setText("Brush Shape is: Butt");
                break;
        }

        //set brush width based on current brush settings
        switch (curr_brush_width) {
            case 1:
                brushsizes.check(R.id.size1);
                break;
            case 10:
                brushsizes.check(R.id.size10);
                break;
            case 25:
                brushsizes.check(R.id.size25);
                break;
            case 50:
                brushsizes.check(R.id.size50);
                break;
            case 100:
                brushsizes.check(R.id.size100);
                break;
            case 200:
                brushsizes.check(R.id.size200);
                break;
            default:
                brushsizes.check(R.id.size10);
                break;
        }

        final TextView textViewBrushSize = (TextView) findViewById(R.id.brushSize);
        textViewBrushSize.setText("Brush Size is: " + String.valueOf(curr_brush_width));
    }

    //Update selection when ROUND brush is selected
    public void selectRound(View v) {
        this.curr_brush = 1;
        updateTextSelection();
    }

    //Update selection when SQUARE brush is selected
    public void selectSquare(View v) {
        this.curr_brush = 2;
        updateTextSelection();
    }

    //Update selection when BUTT brush is selected
    public void selectButt(View v) {
        this.curr_brush = 3;
        updateTextSelection();
    }

    //Update size selection
    public void selectSize1(View v) {
        this.curr_brush_width = 1;
        updateTextSelection();
    }

    public void selectSize10(View v) {
        this.curr_brush_width = 10;
        updateTextSelection();
    }

    public void selectSize25(View v) {
        this.curr_brush_width = 25;
        updateTextSelection();
    }

    public void selectSize50(View v) {
        this.curr_brush_width = 50;
        updateTextSelection();
    }

    public void selectSize100(View v) {
        this.curr_brush_width = 100;
        updateTextSelection();
    }

    public void selectSize200(View v) {
        this.curr_brush_width = 200;
        updateTextSelection();
    }

    //When BACK button is clicked, start an Intent which gets the current brush selection back
    public void onClickBrushBack(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("curr_brush_width", this.curr_brush_width); //put brush width into the bundle
        bundle.putInt("curr_brush", this.curr_brush); //put brush shape into the bundle

        Intent result = new Intent();
        result.putExtras(bundle);
        setResult(Activity.RESULT_OK, result); //set the result status
        finish();
    }

}
