package com.example.it_food.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.activity.ConfirmOrderActivity;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.Address;
import com.example.it_food.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressItemAdapter extends RecyclerView.Adapter<AddressItemAdapter.AddressItemViewHolder>{

    private List<Address> mListAddress;
    private Context mContext;
    private OnAddressRemovedListener listener;
    private OnButtonClickListener mbtnListener;

    public AddressItemAdapter(List<Address> mListAddress, Context mContext, OnAddressRemovedListener listener, OnButtonClickListener btnlistener) {
        this.mListAddress = mListAddress;
        this.mContext = mContext;
        this.listener = listener;
        this.mbtnListener = btnlistener;
    }

    @NonNull
    @Override
    public AddressItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_address, parent, false);
        return new AddressItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressItemViewHolder holder, int position) {
        Address address = mListAddress.get(position);
        if (address == null){
            return;
        }
        User user = SharedPreferences.getInstance(mContext).getUser();

        holder.btnAddress.setText(address.getAddress());
        holder.btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                mbtnListener.onButtonClicked(pos);
            }
        });
        holder.btnDeleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> body = new HashMap<>();
                body.put("id", address.get_id());
                body.put("userId", user.getId());
                APIService.apiService.deleteCart(body).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.d("", "success");
                            int pos = holder.getAdapterPosition();
                            listener.onAddressRemoved(pos);
                        }else {
                            Log.d("", "response error");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("", "error");
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListAddress != null)
            return mListAddress.size();
        return 0;
    }
    public interface OnAddressRemovedListener {
        void onAddressRemoved(int position);
    }

    public interface OnButtonClickListener {
        void onButtonClicked(int position);
    }


    public class AddressItemViewHolder extends RecyclerView.ViewHolder{

        private RadioButton btnAddress;
        private LinearLayout btnDeleteAddress;
        public AddressItemViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAddress = itemView.findViewById(R.id.radioAddress);
            btnDeleteAddress = itemView.findViewById(R.id.btnDeleteAddress);
        }
    }
}
