package com.fy916.android_finger_paint_canvas;

import static java.lang.Math.min;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by 20215603 Feiyang Wang.
 * Main entry of the software, which allows the user to draw, select brush, color and save the drawing
 */
public class MainActivity extends AppCompatActivity {
    int height = 1920;
    int width = 1080;
    int status_bar_height = 100;

    int curr_color = Color.BLACK;
    int curr_brush = 1; //1 is round, 2 is square, 3 is butt
    int curr_brush_width = 25;
    int drawing_canvas_size = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide the title
        //https://developer.android.com/reference/androidx/appcompat/app/ActionBar#hide()
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main); //set the layout to activity_main.xml

        // get the current phone resolution, without the status bar
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //https://developer.android.com/reference/android/view/WindowManager
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        status_bar_height = getStatusBarHeight();
        /**
         * Calculate the suitable square canvas size. The canvas should be square to be
         * maximized for a phone in both portrait and landscape mode.
         * Assume the phone's resolution without status bar is 1920*1000,
         * then the canvas should be 1000 * 1000.
         * Here the size also deducts 10 to ensure the framework (outline) of the drawing space
         * to be shown.
         */
        drawing_canvas_size = min(width, height) - status_bar_height - 10;

        //log the screen size
        Log.d("MainActivity", "Height is: " + String.valueOf(height));
        Log.d("MainActivity", "Width is: " + String.valueOf(width));
        Log.d("MainActivity", "Status bar height is: " + String.valueOf(status_bar_height));

        // get the painterview's frame to adjust its size
        // the painter view fills its frame
        ConstraintLayout fingerPainterFrame = findViewById(R.id.painterViewFrame);

        // set a square frame
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                min(width, height) - status_bar_height - 10, 0);

        // Get the current screen orientation
        int orientation = getResources().getConfiguration().orientation;

        //if the device is current in portrait mode
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // set the constraint layout of the frame of the painter view
            layoutParams.topToBottom = R.id.color;
            layoutParams.bottomToTop = R.id.save;
            layoutParams.dimensionRatio = "1:1";
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        //if the device is current in landscape mode
        } else {
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.dimensionRatio = "1:1";
            layoutParams.endToStart = R.id.color;
            layoutParams.startToEnd = R.id.brush;
        }

        // set the layout for the frame, which contains the drawing canvas
        fingerPainterFrame.setLayoutParams(layoutParams);

        // get the intent from outside the software if the software is opened via other application.
        // e.g. a file browser selecting an image
        // the application accepts any image from outside the device via uri
        Intent intent = getIntent();
        Uri uri = intent.getData(); // get the uri for the source
        FingerPainterView fingerPainterView = findViewById(R.id.fingerPainterView);

        // if the software is opened with an uri, load the image file to the canvas
        if (uri != null) {
            fingerPainterView.load_file(this.getContentResolver(), uri, drawing_canvas_size, drawing_canvas_size);
        }
    }

    // log onDestory status
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "onDestroy");
    }

    // log onPause status
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onPause");
    }

    // log onResume status
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");
    }

    // log onStart status
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "onStart");
    }

    // log onStop status
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "onStop");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save the current color, brush shape, brush width
        outState.putInt("curr_color", curr_color);
        outState.putInt("curr_brush", curr_brush);
        outState.putInt("curr_brush_width", curr_brush_width);
        Log.d("MainActivity", "State Saved");
    }


    // reference: https://stackoverflow.com/questions/3141996/android-how-to-override-the-back-button-so-it-doesnt-finish-my-activity
    @Override
    public void onBackPressed() {
        // move the app to background when the back button is clicked
        moveTaskToBack(true);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //restore the current color, brush shape, brush width
        super.onRestoreInstanceState(savedInstanceState);
        curr_color = savedInstanceState.getInt("curr_color");
        curr_brush = savedInstanceState.getInt("curr_brush");
        curr_brush_width = savedInstanceState.getInt("curr_brush_width");
        syncWithPainter(); //sync with the painter view
        Log.d("MainActivity", "State Restored");
    }

    // define different request code for intent to use
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int PICK_COLOR_REQUEST = 2;
    public static final int PICK_BRUSH_REQUEST = 3;
    public static final int SAVE_FILE_REQUEST = 4;

    // when open file button is clicked, prompt the file browser to ask user to select an image file
    public void onClickOpenFile(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //https://developer.android.com/reference/android/content/Intent#setType(java.lang.String)
        intent.setType("image/*"); // select any type of image file
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // ask the file chooser to get an uri
        startActivityForResult(Intent.createChooser(intent, "Select image file"), PICK_IMAGE_REQUEST);
    }


    // when pick color button is clicked, ask the user to choose color
    public void onClickPickColor(View v) {
        Intent intent = new Intent(MainActivity.this, ChangeColorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("color", this.curr_color); // pass the current selection to the color selection page
        intent.putExtras(bundle);
        // ask the user to choose a new color
        startActivityForResult(intent, PICK_COLOR_REQUEST);
    }

    // when pick brush button is clicked, ask the user to choose brush shape and size
    public void onClickPickBrush(View v) {
        Intent intent = new Intent(MainActivity.this, ChangeBrushActivity.class);
        Bundle bundle = new Bundle();
        // pass the current brush size and shape to the brush selection page
        bundle.putInt("curr_brush_width", this.curr_brush_width);
        bundle.putInt("curr_brush", this.curr_brush);
        intent.putExtras(bundle);
        // ask the user to choose a new brush shape or size
        startActivityForResult(intent, PICK_BRUSH_REQUEST);
    }


    // when save file button is clicked, ask the user to enter the file name and save file
    public void onClickSaveFile(View v) throws IOException {
        FingerPainterView fingerPainterView = findViewById(R.id.fingerPainterView);
        String temp_filepath = fingerPainterView.get_bitmap(); // get the current drawing's temp file path

        //Create an alert dialog for the user to input file name and save file
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter File Name (10 characters MAX)");

        // Set up the input
        EditText user_input_box = new EditText(this);
        user_input_box.setSingleLine(true);
        // https://developer.android.com/reference/android/app/AlertDialog.Builder#setView(android.view.View)
        builder.setView(user_input_box);

        // Set up the buttons
        // https://developer.android.com/reference/android/app/AlertDialog
        // https://developer.android.com/reference/android/app/AlertDialog.Builder#setPositiveButton(int,%20android.content.DialogInterface.OnClickListener)
        // https://developer.android.com/reference/android/app/AlertDialog.Builder#setNegativeButton(java.lang.CharSequence,%20android.content.DialogInterface.OnClickListener)
        // https://developer.android.com/reference/android/content/DialogInterface.OnClickListener
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // if user clicks on the save button, check input, if valid, save file
                String userInput = String.valueOf(user_input_box.getText());
                saveCanvas(userInput, temp_filepath);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // if user clicks cancel, back to main page
                dialog.cancel();
            }
        });
        builder.show();
    }

    //Saves the bitmap into the given path
    public boolean saveCanvas(String fileName, String bitmap_path) {
        if (fileName.length() > 10) { //if the user input exceeds 10 characters (the limit)
            //Shows the toast message to tell user it cannot be saved
            Toast.makeText(this, "Number of characters are more than the maximum limit (10 Characters), FILE NOT SAVED! ", Toast.LENGTH_LONG).show();
            return false;
        }

        //if the user input is valid, get the Download folder of the device for saving
        // https://developer.android.com/reference/android/os/Environment#DIRECTORY_DOWNLOADS
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // if there is no Download folder, make one
        if (!folder.exists()) {
            folder.mkdir();
        }

        // Get the saved file
        File saved_file = new File(folder, fileName + ".png");
        write_files(saved_file, bitmap_path); //Write to the file
        return true;
    }

    public void write_files(File saved_file, String bitmap_path) {
        try {
            //read the temp file from the temp path
            File f = new File(bitmap_path);
            // copy the temp file to the memory for writing
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            Bitmap bitmap = b.copy(b.getConfig(), true);

            // Get an output stream to write
            FileOutputStream outputStream = new FileOutputStream(saved_file);
            // Write the bitmap to the file
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Save file page Error", String.valueOf(e));
        }
        // Tell the user the file is saved to the path
        Toast.makeText(this, "Drawing is successfully saved to file" + saved_file.getAbsolutePath(), Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the user successfully picks an image from the file browser
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getData() != null){
                // Get the image URI from the result data
                Uri uri = data.getData();
                Log.d("MainActivity", "User picks image from uri: "+ String.valueOf(uri));

                // Get a reference to the FingerPainterView
                FingerPainterView fingerPainterView = findViewById(R.id.fingerPainterView);
                // Set the URI on the FingerPainterView, load the given file
                fingerPainterView.load_file(this.getContentResolver(), uri, drawing_canvas_size, drawing_canvas_size);
            }
        }


        // if the user successfully picks a color
        if (requestCode == PICK_COLOR_REQUEST && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            this.curr_color = bundle.getInt("color"); // get the user picked color
            syncWithPainter(); // sync the color with the painter
        }


        if (requestCode == PICK_BRUSH_REQUEST && resultCode == RESULT_OK && data != null) {
            // if the user successfully picks a brush size or shape
            Bundle bundle = data.getExtras();
            // get the user picked brush settings
            curr_brush_width = bundle.getInt("curr_brush_width");
            curr_brush = bundle.getInt("curr_brush");
            syncWithPainter(); // sync the brush with the painter
        }
    }


    private void syncWithPainter() { // sync the current class variables with the painter view
        FingerPainterView fingerPainterView = findViewById(R.id.fingerPainterView);
        fingerPainterView.setColour(this.curr_color); //sync color
        fingerPainterView.setBrushWidth(this.curr_brush_width); //sync brush width

        // sync the brush based on brush code
        //1 is round, 2 is square, 3 is butt
        switch (curr_brush) {
            case 1:
                fingerPainterView.setBrush(Paint.Cap.ROUND);
                break;
            case 2:
                fingerPainterView.setBrush(Paint.Cap.SQUARE);
                break;
            default:
                fingerPainterView.setBrush(Paint.Cap.BUTT);
                break;
        }
    }

    // Get the height of the status bar
    private int getStatusBarHeight() {
        // https://developer.android.com/reference/android/content/res/Resources
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }
}


