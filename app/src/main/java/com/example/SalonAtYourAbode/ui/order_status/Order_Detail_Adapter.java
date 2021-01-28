package com.example.SalonAtYourAbode.ui.order_status;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SalonAtYourAbode.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Order_Detail_Adapter extends RecyclerView.Adapter<Order_Detail_Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<Order_Detail_Getter_Setter> orderDetailSetterSetters;
    Context context;
    int count_arrow = 1;
    Animation slideDown, slideUp;
    ArrayList<String> txnId1 = new ArrayList<String>();
    private static int currentPosition = 0;

    public Order_Detail_Adapter(Context ctx, List<Order_Detail_Getter_Setter> orderDetailSetterSetters) {
        this.inflater = LayoutInflater.from(ctx);
        context = ctx;
        this.orderDetailSetterSetters = orderDetailSetterSetters;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_order_status_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // bind the data
        holder.paymentStatus.setText(orderDetailSetterSetters.get(position).getPaymentStatus());
        holder.salonName.setText(orderDetailSetterSetters.get(position).getSalonName());
        holder.txnId.setText(orderDetailSetterSetters.get(position).getTxnId());
        holder.masterName.setText(orderDetailSetterSetters.get(position).getMasterName());
        holder.masterNumber.setText(orderDetailSetterSetters.get(position).getSalonContact());
        holder.payNow.setText(orderDetailSetterSetters.get(position).getPayNow());
        holder.payLater.setText(orderDetailSetterSetters.get(position).getPayLater());
        holder.orderStatus.setText(orderDetailSetterSetters.get(position).getOrderStatus());
        holder.orderList.setText(orderDetailSetterSetters.get(position).getOrderData());
        Picasso.get().load(orderDetailSetterSetters.get(position).getImageUrl()).into(holder.image);

        if (currentPosition == position && count_arrow == 1) {
//            slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            holder.linearLayout.setVisibility(View.GONE);
//            holder.linearLayout.startAnimation(slideUp);
        }

        if (currentPosition == position && count_arrow == 0) {
//            slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
            holder.linearLayout.setVisibility(View.VISIBLE);
//            holder.linearLayout.startAnimation(slideDown);
        }

    }

    @Override
    public int getItemCount() {
        return orderDetailSetterSetters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView paymentStatus, salonName, txnId, masterName, masterNumber, payNow, payLater, orderStatus, orderList;
        ImageView image, swipeDown, swipeUp;
        LinearLayout linearLayout;

        RecyclerView recyclerViewChild;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            salonName = itemView.findViewById(R.id.salonName);
            paymentStatus = itemView.findViewById(R.id.paymentResponse);
            masterName = itemView.findViewById(R.id.textViewMasterName);
            masterNumber = itemView.findViewById(R.id.textViewMasterNumber);
            payNow = itemView.findViewById(R.id.textViewPayBefore);
            payLater = itemView.findViewById(R.id.textViewPayAfter);
            orderStatus = itemView.findViewById(R.id.textViewOrderStatus);
            orderList = itemView.findViewById(R.id.textViewOrderList);
            txnId = itemView.findViewById(R.id.textViewTxnId);
            image = itemView.findViewById(R.id.coverImage);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            recyclerViewChild = itemView.findViewById(R.id.orderStatusRV);
            swipeDown = itemView.findViewById(R.id.swipeDown);
            swipeUp = itemView.findViewById(R.id.swipeUp);
            // handle onClick
            swipeUp.setOnClickListener(view -> {

                int position = getAdapterPosition();
                currentPosition = position;
                notifyDataSetChanged();
                count_arrow = 1;
                swipeDown.setVisibility(View.VISIBLE);
                swipeUp.setVisibility(View.GONE);
            });
            swipeDown.setOnClickListener(view -> {
                int position = getAdapterPosition();
                currentPosition = position;
                notifyDataSetChanged();
                count_arrow = 0;
                swipeDown.setVisibility(View.GONE);
                swipeUp.setVisibility(View.VISIBLE);
            });
        }
    }
}
