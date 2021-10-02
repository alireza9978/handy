package ir.coleo.handy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;

import ir.coleo.handy.R;
import ir.coleo.handy.adapters.RecyclerViewAdapterAngleList;
import ir.coleo.handy.models.Angle;
import ir.coleo.handy.models.AngleItem;

import static ir.coleo.handy.constant.Constants.ANGLE_INTENT_DATA;

public class AngleSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angle_selection);

        RecyclerView recyclerView = findViewById(R.id.angle_list_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapterAngleList adapter = new RecyclerViewAdapterAngleList(AngleItem.getAll());
        recyclerView.setAdapter(adapter);

        Button submit = findViewById(R.id.start_process_button);
        submit.setOnClickListener(v -> {
            ArrayList<AngleItem> items = (ArrayList<AngleItem>) adapter.getItems();
            ArrayList<Angle> selected = new ArrayList<>();
            for (AngleItem item: items){
                if (item.isSelected()){
                    selected.add(item.getAngle());
                }
            }
            Intent result = new Intent();
            result.putExtra(ANGLE_INTENT_DATA, selected);
            setResult(Activity.RESULT_OK, result);
            finish();
        });

    }
}