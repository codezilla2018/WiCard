package me.morasquad.wicard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    public void onBindViewHolder(final WiCardViewHolder holder, int position) {

        holder.fullName.setText(myWicards.get(position).getFullName());
        holder.emailAddress.setText(myWicards.get(position).getEmail());
        holder.Address.setText(myWicards.get(position).getAddress());
        holder.mobileNumber.setText(myWicards.get(position).getMobileNumber());

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, final int position, boolean isLongClick) {
                if (isLongClick) {

                    PopupMenu popup = new PopupMenu(mContext, view);
                    popup.getMenuInflater().inflate(R.menu.view_wicard_popup, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()){

                                case R.id.delete:
                                    ShowDeleteAlertBox(position);
                                    break;

                                case R.id.edit:
                                    Toast.makeText(mContext, "Edit", Toast.LENGTH_SHORT).show();
                                    break;

                                case R.id.send:
                                    Toast.makeText(mContext, "Send", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            return true;
                        }
                    });

                    popup.show();
                } else {



                }
            }
        });
    }

    private void ShowDeleteAlertBox(final int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:

                        SqliteHelper sqliteHelper = new SqliteHelper(mContext);
                        sqliteHelper.deleteUser(myWicards.get(position).getEmail());
                        Toast.makeText(mContext, "Your WiCard is Deleted!", Toast.LENGTH_SHORT).show();
                        myWicards.remove(position);
                        notifyDataSetChanged();

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Delete this image?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
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
