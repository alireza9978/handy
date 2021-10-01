// Copyright 2021 The MediaPipe Authors.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package ir.coleo.handy.customViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.appcompat.widget.AppCompatImageView;

import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmark;
import com.google.mediapipe.solutions.hands.HandsResult;

import java.util.ArrayList;
import java.util.List;

import ir.coleo.handy.models.Angle;
import ir.coleo.handy.models.AngleConnection;

/**
 * An ImageView implementation for displaying MediaPipe Hands results.
 */
public class HandsResultImageView extends AppCompatImageView {
    private static final String TAG = "HandsResultImageView";

    private static final int ANGLE_COLOR = Color.DKGRAY;
    private static final int LANDMARK_COLOR = Color.RED;
    private static final int LANDMARK_RADIUS = 5;
    private static final int CONNECTION_COLOR = Color.GREEN;
    private static final int CONNECTION_THICKNESS = 5;
    private Bitmap latest;
    private ArrayList<Angle> angleArrayList;

    public HandsResultImageView(Context context) {
        super(context);
        setScaleType(ScaleType.FIT_CENTER);
    }

    public HandsResultImageView(Context context, ArrayList<Angle> angleArrayList) {
        super(context);
        setScaleType(ScaleType.FIT_CENTER);
        this.angleArrayList = angleArrayList;
    }

    /**
     * Sets a {@link HandsResult} to render.
     *
     * @param result a {@link HandsResult} object that contains the solution outputs and the input
     *               {@link Bitmap}.
     */
    public void setHandsResult(HandsResult result) {
        if (result == null) {
            return;
        }
        Bitmap bmInput = result.inputBitmap();
        int width = bmInput.getWidth();
        int height = bmInput.getHeight();
        latest = Bitmap.createBitmap(width, height, bmInput.getConfig());
        Canvas canvas = new Canvas(latest);

        canvas.drawBitmap(bmInput, new Matrix(), null);
        int numHands = result.multiHandLandmarks().size();
        for (int i = 0; i < numHands; ++i) {
            drawLandmarksOnCanvas(
                    result.multiHandLandmarks().get(i).getLandmarkList(), canvas, width, height);
        }
    }

    /**
     * Updates the image view with the latest hands result.
     */
    public void update() {
        postInvalidate();
        if (latest != null) {
            setImageBitmap(latest);
        }
    }

    private void drawLandmarksOnCanvas(List<NormalizedLandmark> handLandmarkList, Canvas canvas, int width, int height) {

        Paint anglePaint = new Paint();
        anglePaint.setColor(ANGLE_COLOR);

        Paint landmarkPaint = new Paint();
        landmarkPaint.setColor(LANDMARK_COLOR);

        if (angleArrayList != null) {
            for (Angle angle : angleArrayList) {
                // Draw connections.
                for (AngleConnection c : angle.getAngleConnections()) {
                    Paint connectionPaint = new Paint();
                    connectionPaint.setColor(CONNECTION_COLOR);
                    connectionPaint.setStrokeWidth(CONNECTION_THICKNESS);
                    NormalizedLandmark start = handLandmarkList.get(c.getStart());
                    NormalizedLandmark end = handLandmarkList.get(c.getEnd());
                    canvas.drawLine(
                            start.getX() * width,
                            start.getY() * height,
                            end.getX() * width,
                            end.getY() * height,
                            connectionPaint);
                }


                // Draw landmarks.
                for (NormalizedLandmark landmark : angle.getHandLandmarkList(handLandmarkList)) {
                    canvas.drawCircle(
                            landmark.getX() * width, landmark.getY() * height, LANDMARK_RADIUS, landmarkPaint);
                }


                NormalizedLandmark landmark = handLandmarkList.get(angle.getTwo());
                String angleText = angle.calculateAngle(handLandmarkList);
                setTextSizeForWidth(anglePaint, angleText);
                float x = landmark.getX() * width + width * 0.02f;
                float y = landmark.getY() * height - height * 0.02f;
                canvas.drawText(angleText, x, y, anglePaint);
            }
        }

    }

    /**
     * Sets the text size for a Paint object so a given string of text will be a
     * given width.
     *  @param paint         the Paint to set the text size for
     * @param text          the text that should be that width
     */
    private static void setTextSizeForWidth(Paint paint, String text) {

        final float testTextSize = 48f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = testTextSize * 45f / bounds.height();

        // Set the paint for that size.
        paint.setTextSize(desiredTextSize);
    }

}
