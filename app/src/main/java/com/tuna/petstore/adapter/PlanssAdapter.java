package com.tuna.petstore.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuna.petstore.R;
import com.tuna.petstore.database.HistoryDAO;
import com.tuna.petstore.database.PlanssDAO;
import com.tuna.petstore.inter.OnClick;
import com.tuna.petstore.inter.OnClick1;
import com.tuna.petstore.model.History;
import com.tuna.petstore.model.Planss;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

public class PlanssAdapter extends RecyclerView.Adapter<PlanssAdapter.MyViewHolder> {

    private final Context context;
    private List<Planss> list;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private final OnClick onClick;
    private final OnClick1 onClick1;
    private HistoryDAO historyDAO;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView planss;
        final TextView day;
        final ImageView avatar;
        final CheckBox checkBox;
        final RelativeLayout viewBackground;
        public final RelativeLayout viewForeground;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tvNamepet);
            planss = view.findViewById(R.id.tvplanpet);
            day = view.findViewById(R.id.tvday);
            avatar = view.findViewById(R.id.avatar);
            checkBox = view.findViewById(R.id.cbo);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public PlanssAdapter(Context mContext, List<Planss> albumList, OnClick onClick, OnClick1 onClick1) {
        this.context = mContext;
        this.list = albumList;
        this.onClick = onClick;
        this.onClick1 = onClick1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_planss, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Planss planss = list.get(position);
        holder.name.setText(planss.getIdpet());
        holder.planss.setText(planss.getName());
        holder.day.setText(planss.getTime() + " | " + sdf.format(planss.getDay()));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClick.onItemClickClicked(position);
                return false;
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    historyDAO = new HistoryDAO(context);
                    Random random = new Random();
                    String id = String.valueOf(random.nextInt());
                    History histories = new History(id, planss.getName(), planss.getIdpet(), planss.getDay(), planss.getTime());
                    if (historyDAO.insertHistory(histories) > 0) {
                        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                        builder.setTitle("Message");
                        builder.setMessage("Have you completed this plan yet ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onClick1.onItemClickClicked(position);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                holder.checkBox.setChecked(false);
                            }
                        });
                        builder.show();
                    }

                }
            }
        });
        holder.checkBox.setChecked(false);
        switch (planss.getName()) {
            case "Đi bộ":
                Glide.with(context).load(R.drawable.dibo).into(holder.avatar);
                break;
            case "Cho ăn":
                Glide.with(context).load(R.drawable.doan).into(holder.avatar);
                break;
            case "Tắm rửa":
                Glide.with(context).load(R.drawable.tam).into(holder.avatar);
                break;
            case "Đi khám thú y":
                Glide.with(context).load(R.drawable.kham).into(holder.avatar);
                break;
            case "Mua thức ăn, phụ kiện":
                Glide.with(context).load(R.drawable.shop).into(holder.avatar);
                break;
            case "Khác":
                Glide.with(context).load(R.drawable.cander).into(holder.avatar);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void changeDataset(List<Planss> items) {
        this.list = items;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        onClick1.onItemClickClicked(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Planss item, int position) {
        PlanssDAO planssDAO = new PlanssDAO(context);
        list.add(position, item);
        // notify item added by position
        planssDAO.insertPlanss(item);
        notifyItemInserted(position);
    }
}
