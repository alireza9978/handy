package ir.coleo.handy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import ir.coleo.handy.R;
import ir.coleo.handy.models.AngleItem;

/**
 * کلاس مدیریت لیست گاوداری‌ها در صفحه‌ی انتخاب گاوداری
 */
public class RecyclerViewAdapterAngleList extends RecyclerView.Adapter<RecyclerViewAdapterAngleList.Holder> {

    private final ArrayList<AngleItem> items;

    public RecyclerViewAdapterAngleList(ArrayList<AngleItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.angle_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        AngleItem item = items.get(position);
        holder.checkBox.setText(item.getAngle().getName());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.select();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public Holder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.item_check_box);
        }
    }

    public ArrayList<AngleItem> getItems() {
        return items;
    }
}
