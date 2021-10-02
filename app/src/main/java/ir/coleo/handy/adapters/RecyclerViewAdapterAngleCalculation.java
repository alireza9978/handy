package ir.coleo.handy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.coleo.handy.R;
import ir.coleo.handy.models.CalculatedAngleItem;

/**
 * کلاس مدیریت لیست گاوداری‌ها در صفحه‌ی انتخاب گاوداری
 */
public class RecyclerViewAdapterAngleCalculation extends RecyclerView.Adapter<RecyclerViewAdapterAngleCalculation.Holder> {

    private final ArrayList<CalculatedAngleItem> items = new ArrayList<>();

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.angle_calculation_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        CalculatedAngleItem item = items.get(position);
        holder.angleName.setText(item.getAngle().getName());
        holder.firstImage.setText(item.getFirstImageAngleString());
        holder.secondImage.setText(item.getSecondImageAngleString());
        holder.imagesDifferent.setText(item.anglesDifferent());
        holder.naturalAngle.setText(item.getAngle().getNatural());
        holder.naturalAngleDifferent.setText(item.getAngle().getNaturalDifferent(item.anglesDifferentValue()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView angleName;
        TextView firstImage;
        TextView secondImage;
        TextView imagesDifferent;
        TextView naturalAngle;
        TextView naturalAngleDifferent;

        public Holder(@NonNull View itemView) {
            super(itemView);
            angleName = itemView.findViewById(R.id.angle_name_text);
            firstImage = itemView.findViewById(R.id.first_angle_text);
            secondImage = itemView.findViewById(R.id.second_angle_text);
            imagesDifferent = itemView.findViewById(R.id.different_angle_value);
            naturalAngle = itemView.findViewById(R.id.normal_angle_value);
            naturalAngleDifferent = itemView.findViewById(R.id.different_to_normal_angle_value);
        }
    }

    public void addItem(ArrayList<CalculatedAngleItem> calculatedAngles) {
        items.addAll(calculatedAngles);
    }

}
