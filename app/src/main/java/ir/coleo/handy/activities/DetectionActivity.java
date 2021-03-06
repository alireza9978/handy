package ir.coleo.handy.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.mediapipe.formats.proto.LandmarkProto;
import com.google.mediapipe.solutions.hands.HandLandmark;
import com.google.mediapipe.solutions.hands.Hands;
import com.google.mediapipe.solutions.hands.HandsOptions;
import com.google.mediapipe.solutions.hands.HandsResult;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import ir.coleo.handy.R;
import ir.coleo.handy.adapters.RecyclerViewAdapterAngleCalculation;
import ir.coleo.handy.constant.Constants;
import ir.coleo.handy.constant.ImageUtil;
import ir.coleo.handy.customViews.HandsResultImageView;
import ir.coleo.handy.models.Angle;
import ir.coleo.handy.models.CalculatedAngle;
import ir.coleo.handy.models.CalculatedAngleItem;
import ir.coleo.handy.models.InputSource;

public class DetectionActivity extends AppCompatActivity {
    private static final String TAG = "DetectionActivity";

    private final boolean RUN_ON_GPU = true;
    private InputSource inputSource;
    private ArrayList<Angle> selectedAngle;
    private Bitmap firstImage;
    private Bitmap secondImage;
    private Uri videoUri;

    private RecyclerViewAdapterAngleCalculation adapter;
    private boolean firstDone = false;
    private boolean secondDone = false;

    private ProgressBar progressBar;
    private int deactivationCount = 0;
    private int maxDeactivationCount = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        RecyclerView angleList = findViewById(R.id.angle_info_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        angleList.setHasFixedSize(true);
        angleList.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapterAngleCalculation();
        angleList.setAdapter(adapter);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        inputSource = (InputSource) getIntent().getSerializableExtra(Constants.INPUT_METHOD);
        selectedAngle = (ArrayList<Angle>) getIntent().getSerializableExtra(Constants.DETECTION_ANGLE);
        switch (inputSource) {
            case Image: {
                //here context can be anything like getActivity() for fragment, this or MainActivity.this
                try {
                    firstImage = BitmapFactory.decodeStream(openFileInput(getIntent().getStringExtra(Constants.INPUT_IMAGE_ONE)));
                    secondImage = BitmapFactory.decodeStream(openFileInput(getIntent().getStringExtra(Constants.INPUT_IMAGE_TWO)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            }
            case Video:
                videoUri = getIntent().getParcelableExtra(Constants.INPUT_VIDEO);
                break;
            case Camera:
                break;
        }
        activateProgressBar(inputSource);
    }

    @Override
    protected void onPostCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        switch (inputSource) {
            case Image: {
                detectPointImage(ImageUtil.scale(firstImage, 1000), R.id.hand_result_image_one, true);
                detectPointImage(ImageUtil.scale(secondImage, 1000), R.id.hand_result_image_two, false);
                break;
            }
            case Video:
            case Camera:
                break;
        }
    }

    private synchronized void detectPointImage(Bitmap image, @IdRes int id, boolean first) {
        Hands hands = new Hands(this, HandsOptions.builder()
                .setMode(HandsOptions.STATIC_IMAGE_MODE)
                .setMaxNumHands(1)
                .setRunOnGpu(RUN_ON_GPU)
                .build());
        HandsResultImageView imageView = new HandsResultImageView(this, selectedAngle);

        // Connects MediaPipe Hands to the user-defined HandsResultImageView.
        hands.setResultListener(
                handsResult -> {
                    logWristLandmark(handsResult /*showPixelValues=*/);
                    imageView.setHandsResult(handsResult);
                    runOnUiThread(imageView::update);
                    deactivateProgressBar();
                    showAngleList(imageView.getCalculatedAngles(), first);
                });
        hands.setErrorListener((message, e) -> Log.e(TAG, "MediaPipe Hands error:" + message));

        // Updates the preview layout.
        FrameLayout frameLayout = findViewById(id);
        frameLayout.removeAllViewsInLayout();
        imageView.setImageDrawable(null);
        frameLayout.addView(imageView);
        imageView.setVisibility(View.VISIBLE);
        hands.send(image);
    }

    private void logWristLandmark(HandsResult result) {
        LandmarkProto.NormalizedLandmark wristLandmark = Hands.getHandLandmark(result, 0, HandLandmark.WRIST);
        // For Bitmaps, show the pixel values. For texture inputs, show the normalized coordinates.

        Log.i(TAG, String.format("MediaPipe Hand wrist normalized coordinates (value range: [0, 1]): x=%f, y=%f",
                wristLandmark.getX(), wristLandmark.getY()));

    }

    private void activateProgressBar(InputSource source) {
        deactivationCount = 0;
        if (source == InputSource.Image) {
            maxDeactivationCount = 2;
        } else {
            maxDeactivationCount = 1;
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    private void deactivateProgressBar() {
        deactivationCount++;
        if (deactivationCount == maxDeactivationCount) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private ArrayList<CalculatedAngle> otherCalculatedAngles;

    private void showAngleList(ArrayList<CalculatedAngle> calculatedAngles, boolean first) {
        if (first) {
            firstDone = true;
        } else {
            secondDone = true;
        }

        if (firstDone && secondDone) {
            ArrayList<CalculatedAngleItem> calculatedAngleItems = new ArrayList<>();
            if (first) {
                for (int i = 0; i < calculatedAngles.size(); i++) {
                    CalculatedAngle firstAngle = calculatedAngles.get(i);
                    CalculatedAngle secondAngle = otherCalculatedAngles.get(i);
                    calculatedAngleItems.add(new CalculatedAngleItem(firstAngle.getAngle(), firstAngle.getImageAngle(), secondAngle.getImageAngle()));
                }
            } else {
                for (int i = 0; i < calculatedAngles.size(); i++) {
                    CalculatedAngle firstAngle = otherCalculatedAngles.get(i);
                    CalculatedAngle secondAngle = calculatedAngles.get(i);
                    calculatedAngleItems.add(new CalculatedAngleItem(firstAngle.getAngle(), firstAngle.getImageAngle(), secondAngle.getImageAngle()));
                }
            }
            adapter.addItem(calculatedAngleItems);
            adapter.notifyDataSetChanged();
        } else {
            this.otherCalculatedAngles = calculatedAngles;
        }

    }

}