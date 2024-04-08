package com.fy916.android_finger_paint_canvas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by 20215603 Feiyang Wang.
 * The Activity which connects to colorselection.xml that can allow users to change color
 * of the brush.
 */
public class ChangeColorActivity extends AppCompatActivity {
    int color = Color.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ChangeColorActivity", "OnCreate");

        setContentView(R.layout.colorselection); //Set the current view to colorselection.xml
        Bundle bundle = getIntent().getExtras();
        int color = bundle.getInt("color"); //Get the current color for drawing
        this.color = color;
        updateText(); //update color text
        updateSelection(); //update color selection button
    }

    // log onDestory status
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ChangeColorActivity", "onDestroy");
    }

    // log onPause status
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ChangeColorActivity", "onPause");
    }

    // log onResume status
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ChangeColorActivity", "onResume");
    }

    // log onStart status
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ChangeColorActivity", "onStart");
    }

    // log onStop status
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("ChangeColorActivity", "onStop");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("color", color); //Save the color selection
        Log.d("ChangeColorActivity", "State Saved");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        color = savedInstanceState.getInt("color"); //Restore the color selection
        Log.d("ChangeColorActivity", "State Restored");
    }

    private void updateText(){
        String color_string = String.valueOf(this.color); //get current color
        final TextView textView = (TextView) findViewById(R.id.selected_color_text);

        //set color text based on current color
        switch (this.color){
            case Color.RED:
                textView.setText("RED color selected");
                break;
            case Color.BLUE:
                textView.setText("BLUE color selected");
                break;
            case Color.GREEN:
                textView.setText("GREEN color selected");
                break;
            case Color.YELLOW:
                textView.setText("YELLOW color selected");
                break;
            case Color.BLACK:
                textView.setText("BLACK color selected");
                break;
            case Color.GRAY:
                textView.setText("GRAY color selected");
                break;
            default:
                textView.setText(color_string);
                break;
        }
    }

    private void updateSelection(){
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.colorgroup);

        //set color text based on current color
        switch (this.color){
            case Color.RED:
                radioGroup.check(R.id.select_red);
                break;
            case Color.BLUE:
                radioGroup.check(R.id.select_blue);
                break;
            case Color.GREEN:
                radioGroup.check(R.id.select_green);
                break;
            case Color.YELLOW:
                radioGroup.check(R.id.select_yellow);
                break;
            case Color.BLACK:
                radioGroup.check(R.id.select_black);
                break;
            case Color.GRAY:
                radioGroup.check(R.id.select_gray);
                break;
            default:
                radioGroup.check(R.id.select_red);
                break;
        }
    }


    //If red color radio is selected, update the color to red
    public void selectRed(View v){
        this.color = Color.RED;
        updateText();
    }

    //If green color radio is selected, update the color to green
    public void selectGreen(View v){
        this.color = Color.GREEN;
        updateText();
    }

    //If yellow color radio is selected, update the color to yellow
    public void selectYellow(View v){
        this.color = Color.YELLOW;
        updateText();
    }

    //If black color radio is selected, update the color to black
    public void selectBlack(View v){
        this.color = Color.BLACK;
        updateText();
    }

    //If gray color radio is selected, update the color to gray
    public void selectGray(View v){
        this.color = Color.GRAY;
        updateText();
    }

    //If blue color radio is selected, update the color to blue
    public void selectBlue(View v){
        this.color = Color.BLUE;
        updateText();
    }

    //When BACK button is clicked, start an Intent which gets the current color selection back
    public void onClickColorBack(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("color", this.color); //put color into the bundle

        Intent result = new Intent();
        result.putExtras(bundle);
        setResult(Activity.RESULT_OK, result); //set the result status
        finish();
    }
}
