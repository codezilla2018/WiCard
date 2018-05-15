package me.morasquad.wicard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.morasquad.wicard.models.MyWicard;

/**
 * Created by Sandun Isuru Niraj on 15/05/2018.
 */

public class WiCardRecyclerAdapter extends RecyclerView.Adapter<WiCardRecyclerAdapter.WiCardViewHolder> {

    private ArrayList<MyWicard> myWicards;
    private Context mContext;
    private ArrayList<MyWicard> mFilteredList;

    public WiCardRecyclerAdapter(ArrayList<MyWicard> myWicards, Context mContext) {
        this.myWicards = myWicards;
        this.mContext = mContext;
        this.mFilteredList = myWicards;
    }

    @Override
    public WiCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wicard_recycler, parent, false);

        return new WiCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WiCardViewHolder holder, int position) {

        holder.fullName.setText(myWicards.get(position).getFullName());
        holder.emailAddress.setText(myWicards.get(position).getEmail());
        holder.Address.setText(myWicards.get(position).getAddress());
        holder.mobileNumber.setText(myWicards.get(position).getMobileNumber());
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public class WiCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView fullName;
        public TextView Address;
        public TextView emailAddress;
        public TextView mobileNumber;
        private ItemClickListener clickListener;


        public WiCardViewHolder(View itemView) {
            super(itemView);
            fullName = (TextView) itemView.findViewById(R.id.fullNameTxt);
            Address = (TextView) itemView.findViewById(R.id.addressTxt);
            emailAddress = (TextView) itemView.findViewById(R.id.emailTxt);
            mobileNumber = (TextView) itemView.findViewById(R.id.mobileNumTxt);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

            @Override
        public void onClick(View view) {

            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }


}
