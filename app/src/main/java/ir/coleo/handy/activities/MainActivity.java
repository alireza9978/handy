package ir.coleo.handy.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import java.io.IOException;
import java.util.ArrayList;

import ir.coleo.handy.R;
import ir.coleo.handy.models.Angle;
import ir.coleo.handy.models.InputSource;
import ir.coleo.handy.models.Usages;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";
    private ActivityResultLauncher<Intent> firstImageGetter;
    private ActivityResultLauncher<Intent> secondImageGetter;
    private Bitmap firstImage;
    private Bitmap secondImage;
    private ImageView firstImageView;
    private ImageView secondImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInputSourceDropDownValue();
        defineImageViews();
        setUsageDropDownValue();
        setAngleDropDownValue();
        showImageInputState();
    }

    private void defineImageViews(){
        firstImageView = findViewById(R.id.first_image_imageview);
        secondImageView = findViewById(R.id.second_image_imageview);
        // The Intent to access gallery and read images as bitmap.
        firstImageGetter = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent resultIntent = result.getData();
                    if (resultIntent != null) {
                        if (result.getResultCode() == RESULT_OK) {
                            try {
                                firstImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultIntent.getData());
                                firstImageView.setImageBitmap(firstImage);
                            } catch (IOException e) {
                                Log.e(TAG, "Bitmap reading error:" + e);
                            }
                        }
                    }
                });

        secondImageGetter = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent resultIntent = result.getData();
                    if (resultIntent != null) {
                        if (result.getResultCode() == RESULT_OK) {
                            try {
                                secondImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultIntent.getData());
                                secondImageView.setImageBitmap(secondImage);
                            } catch (IOException e) {
                                Log.e(TAG, "Bitmap reading error:" + e);
                            }
                        }
                    }
                });
    }

    private void setInputSourceDropDownValue() {
        AppCompatSpinner dropdown = findViewById(R.id.input_source_spinner);
        ArrayList<String> usages = InputSource.getStrings(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, usages);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    showImageInputState();
                }
                if (id == 1) {
                    showVideoInputState();
                }
                if (id == 2) {
                    showCameraInputState();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setAngleDropDownValue() {
        AppCompatSpinner dropdown = findViewById(R.id.angle_spinner);
        ArrayList<String> usages = Angle.getStrings(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, usages);
        dropdown.setAdapter(adapter);
    }

    private void setUsageDropDownValue() {
        AppCompatSpinner dropdown = findViewById(R.id.usage_spinner);
        ArrayList<String> usages = Usages.getStrings(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, usages);
        dropdown.setAdapter(adapter);
    }

    private void showImageInputState() {

        Button loadFirstImageButton = findViewById(R.id.first_button_load_picture);
        Button loadSecondImageButton = findViewById(R.id.second_button_load_picture);

        loadFirstImageButton.setVisibility(View.VISIBLE);
        loadSecondImageButton.setVisibility(View.VISIBLE);
        firstImageView.setVisibility(View.VISIBLE);
        secondImageView.setVisibility(View.VISIBLE);

        loadFirstImageButton.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            firstImageGetter.launch(gallery);
        });
        loadSecondImageButton.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            secondImageGetter.launch(gallery);
        });
    }


    private void showVideoInputState() {

    }


    private void showCameraInputState() {

    }

}