package com.arraybit.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.global.Globals;
import com.arraybit.modal.AdvertiseMaster;
import com.arraybit.mym.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rey.material.widget.TextView;

import java.util.ArrayList;


public class AdvertiseAdapter extends RecyclerView.Adapter<AdvertiseAdapter.AdvertiseViewHolder> {

    public boolean isItemAnimate;
    Context context;
    ArrayList<AdvertiseMaster> alAdvertiseMaster;
    LayoutInflater layoutInflater;
    View view;
    int previousPosition;
    AdvertiseListener objAdvertiseListener;

    public AdvertiseAdapter(Context context, ArrayList<AdvertiseMaster> result, AdvertiseListener objAdvertiseListener) {
        this.context = context;
        this.alAdvertiseMaster = result;
        this.layoutInflater = LayoutInflater.from(context);
        this.objAdvertiseListener = objAdvertiseListener;
    }

    @Override
    public AdvertiseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_advertise, parent, false);
        return new AdvertiseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdvertiseViewHolder holder, int position) {
        AdvertiseMaster objAdvertiseMaster = alAdvertiseMaster.get(position);
        holder.txtNotificationTime.setText(objAdvertiseMaster.getCreateDateTime());
        if (objAdvertiseMaster.getAdvertisementType().equals("Text")) {
            if (objAdvertiseMaster.getAdvertiseText() == null || objAdvertiseMaster.getAdvertiseText().equals("")) {
                holder.txtNotificationText.setVisibility(View.GONE);
            } else {
                holder.txtNotificationText.setVisibility(View.VISIBLE);
                holder.txtNotificationText.setText(objAdvertiseMaster.getAdvertiseText());
            }
        } else {
            holder.txtNotificationText.setVisibility(View.GONE);
        }
        if (objAdvertiseMaster.getAdvertisementType().equals("Image")) {
            if (objAdvertiseMaster.getAdvertiseImageName() != null && !objAdvertiseMaster.getAdvertiseImageName().equals("")) {
                holder.ivNotificationImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(objAdvertiseMaster.getAdvertiseImageName()).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.ivNotificationImage);
            } else {
                holder.ivNotificationImage.setVisibility(View.GONE);
            }
        } else {
            holder.ivNotificationImage.setVisibility(View.GONE);
        }

        if (objAdvertiseMaster.getIsEnabled()) {
            holder.txtNotificationEnable.setText("Active");
            holder.txtNotificationEnable.setTextColor(context.getResources().getColor(R.color.green));
            holder.ivProfile1.setBackground(new ColorDrawable(context.getResources().getColor(R.color.card_disable)));
        } else {
            holder.txtNotificationEnable.setText("Inactive");
            holder.txtNotificationEnable.setTextColor(context.getResources().getColor(R.color.grey_500));
            holder.ivProfile1.setBackground(new ColorDrawable(context.getResources().getColor(R.color.card_enable1)));
        }

        if (isItemAnimate) {
            if (position > previousPosition) {
                Globals.SetItemAnimator(holder);
            }
            previousPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return alAdvertiseMaster.size();
    }

    public void AdvertiseDataRemove(int position) {
        isItemAnimate = true;
        alAdvertiseMaster.remove(position);
        notifyItemRemoved(position);
    }

    public void AdvretiseDisable(int position, boolean isEnable) {
        isItemAnimate = false;
        alAdvertiseMaster.get(position).setIsEnabled(isEnable);
        notifyItemChanged(position);
    }

    public void AdvertiseDataChanged(ArrayList<AdvertiseMaster> result) {
        alAdvertiseMaster.addAll(result);
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    public interface AdvertiseListener {
        void OnCardClick(AdvertiseMaster objAdvertiseMaster, String Event, int position);
    }

    class AdvertiseViewHolder extends RecyclerView.ViewHolder {

        TextView txtNotificationTime, txtNotificationEnable, txtNotificationText;
        CardView cvItem;
        ImageView ivNotificationImage, ivProfile1;

        public AdvertiseViewHolder(View itemView) {
            super(itemView);

            txtNotificationTime = (TextView) itemView.findViewById(R.id.txtNotificationTime);
            txtNotificationEnable = (TextView) itemView.findViewById(R.id.txtNotificationEnable);
            txtNotificationText = (TextView) itemView.findViewById(R.id.txtNotificationText);
            ivNotificationImage = (ImageView) itemView.findViewById(R.id.ivNotificationImage);
            ivProfile1 = (ImageView) itemView.findViewById(R.id.ivProfile1);
            cvItem = (CardView) itemView.findViewById(R.id.cvItem);

            cvItem.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    if (alAdvertiseMaster.get(getAdapterPosition()).getIsEnabled()) {
                        menu.add(0, 111, 100, context.getResources().getString(R.string.action_disable)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                objAdvertiseListener.OnCardClick(alAdvertiseMaster.get(getAdapterPosition()), context.getResources().getString(R.string.action_disable), getAdapterPosition());
                                return false;
                            }
                        });
                    } else {
                        menu.add(0, 111, 100, context.getResources().getString(R.string.action_enable)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                objAdvertiseListener.OnCardClick(alAdvertiseMaster.get(getAdapterPosition()), context.getResources().getString(R.string.action_enable), getAdapterPosition());
                                return false;
                            }
                        });
                    }
                    menu.add(0, 112, 200, context.getResources().getString(R.string.action_edit)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            objAdvertiseListener.OnCardClick(alAdvertiseMaster.get(getAdapterPosition()), context.getResources().getString(R.string.action_edit), getAdapterPosition());
                            return false;
                        }
                    });
                    menu.add(0, 113, 300, context.getResources().getString(R.string.action_delete)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            objAdvertiseListener.OnCardClick(alAdvertiseMaster.get(getAdapterPosition()), context.getResources().getString(R.string.action_delete), getAdapterPosition());
                            return false;
                        }
                    });
                }


            });
        }
    }
}
