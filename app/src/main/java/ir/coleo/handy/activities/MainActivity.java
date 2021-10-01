package ir.coleo.handy.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import ir.coleo.handy.R;
import ir.coleo.handy.constant.Constants;
import ir.coleo.handy.models.Angle;
import ir.coleo.handy.models.InputSource;
import ir.coleo.handy.models.Usages;

import static ir.coleo.handy.constant.Constants.ANGLE_INTENT_DATA;
import static ir.coleo.handy.constant.Constants.createImageFromBitmap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";
    private ActivityResultLauncher<Intent> firstImageGetter;
    private ActivityResultLauncher<Intent> secondImageGetter;
    private ActivityResultLauncher<Intent> videoGetter;

    private Bitmap firstImage;
    private Bitmap secondImage;
    private Uri videoUri;

    private Button loadFirstImageButton;
    private Button loadSecondImageButton;
    private ImageView firstImageView;
    private ImageView secondImageView;

    private Button loadVideoButton;
    private VideoView videoView;

    private Button startProcessButton;
    private ArrayList<Angle> selectedAngle;

    private InputSource inputSource = InputSource.Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defineProcessViews();
        defineImageViews();
        defineVideoViews();
        setInputSourceDropDownValue();
        setUsageDropDownValue();
        setAngleDropDownValue();
        showImageInputState();
    }

    private void defineProcessViews() {
        startProcessButton = findViewById(R.id.start_process_button);

        startProcessButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, DetectionActivity.class);
            intent.putExtra(Constants.INPUT_METHOD, inputSource);
            switch (inputSource) {
                case Image: {
                    if (firstImage != null && secondImage != null) {
                        intent.putExtra(Constants.INPUT_IMAGE_ONE, createImageFromBitmap(firstImage, MainActivity.this, true));
                        intent.putExtra(Constants.INPUT_IMAGE_TWO, createImageFromBitmap(secondImage, MainActivity.this, false));
                    } else {
                        Toast.makeText(MainActivity.this, R.string.input_image_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                }
                case Video:
                    if (videoUri != null) {
                        intent.putExtra(Constants.INPUT_VIDEO, videoUri);
                    }else{
                        Toast.makeText(MainActivity.this, R.string.input_video_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                case Camera:
                    break;
            }
            if (selectedAngle != null) {
                intent.putExtra(Constants.DETECTION_ANGLE, selectedAngle);
            } else {
                Toast.makeText(MainActivity.this, R.string.input_selected_angle_error, Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(intent);
        });

    }

    private void defineVideoViews() {
        loadVideoButton = findViewById(R.id.button_load_video);
        videoView = findViewById(R.id.video_view);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        // The Intent to access gallery and read a video file.
        videoGetter = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    Intent resultIntent = result.getData();
                    if (resultIntent != null) {
                        if (result.getResultCode() == RESULT_OK) {
                            videoUri = resultIntent.getData();
                            videoView.setMediaController(mediaController);
                            videoView.setVideoURI(videoUri);
                            videoView.requestFocus();
                            videoView.start();
                        }
                    }
                });

    }

    private void defineImageViews() {
        firstImageView = findViewById(R.id.first_image_imageview);
        secondImageView = findViewById(R.id.second_image_imageview);
        loadFirstImageButton = findViewById(R.id.first_button_load_picture);
        loadSecondImageButton = findViewById(R.id.second_button_load_picture);
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
                    hideVideoInputState();
                    showImageInputState();
                    inputSource = InputSource.Image;
                }
                if (id == 1) {
                    hideImageInputState();
                    showVideoInputState();
                    inputSource = InputSource.Video;
                }
                if (id == 2) {
                    hideImageInputState();
                    hideVideoInputState();
                    inputSource = InputSource.Camera;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setAngleDropDownValue() {
        Button angleSelectionButton = findViewById(R.id.angle_selection_button);
        ActivityResultLauncher<Intent> angleSelectionResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    Intent resultIntent = result.getData();
                    if (resultIntent != null) {
                        if (result.getResultCode() == RESULT_OK) {
                            selectedAngle = null;
                            Serializable serializable = resultIntent.getExtras().getSerializable(ANGLE_INTENT_DATA);
                            if (serializable instanceof ArrayList) {
                                selectedAngle = (ArrayList<Angle>) serializable;
                            }
                        }
                    }
                });


        angleSelectionButton.setOnClickListener(v -> {
            Intent gallery = new Intent(this, AngleSelectionActivity.class);
            angleSelectionResult.launch(gallery);
        });
    }

    private void setUsageDropDownValue() {
        AppCompatSpinner dropdown = findViewById(R.id.usage_spinner);
        ArrayList<String> usages = Usages.getStrings(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, usages);
        dropdown.setAdapter(adapter);
    }


    private void hideImageInputState() {
        loadFirstImageButton.setVisibility(View.GONE);
        loadSecondImageButton.setVisibility(View.GONE);
        firstImageView.setVisibility(View.GONE);
        secondImageView.setVisibility(View.GONE);

        firstImage = null;
        secondImage = null;

        firstImageView.setImageBitmap(null);
        secondImageView.setImageBitmap(null);
    }

    private void hideVideoInputState() {
        loadVideoButton.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        videoView.setVideoURI(null);
        videoView.stopPlayback();
    }

    private void showImageInputState() {
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
        loadVideoButton.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.VISIBLE);

        loadVideoButton.setOnClickListener(
                v -> {
                    Intent gallery = new Intent(Intent.ACTION_PICK,
                            MediaStore.Video.Media.INTERNAL_CONTENT_URI);
                    videoGetter.launch(gallery);
                });
    }

}