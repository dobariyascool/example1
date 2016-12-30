package com.arraybit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.modal.MemberMaster;
import com.arraybit.mym.HomeActivity;
import com.arraybit.mym.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MemberViewHolder> {


    public boolean isItemAnimate;
    Context context;
    ArrayList<MemberMaster> alMemberMasters;
    LayoutInflater layoutInflater;
    View view;
    OnCardClickListener onCardClickListener;
    OnRequestListener onRequestListener;

    // Constructor
    public MemberListAdapter(Context context, OnCardClickListener onCardClickListener, OnRequestListener onRequestListener, ArrayList<MemberMaster> result) {
        this.context = context;
        this.alMemberMasters = result;
        this.layoutInflater = LayoutInflater.from(context);
        this.onCardClickListener = onCardClickListener;
        this.onRequestListener = onRequestListener;
    }


    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_contact, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MemberViewHolder holder, int position) {
        MemberMaster objMemberMaster = alMemberMasters.get(position);

        holder.txtContactName.setText(objMemberMaster.getMemberName());
        holder.txtEmail.setText(objMemberMaster.getEmail());
        holder.txtProfession.setText(objMemberMaster.getProfession());
        holder.txtContactNo.setText(objMemberMaster.getPhone1());
//        if (objMemberMaster.getMemberType() != null && objMemberMaster.getMemberType().equals("Admin")) {
//            holder.txtAdmin.setVisibility(View.VISIBLE);
//        } else {
//            holder.txtAdmin.setVisibility(View.GONE);
//        }

        if (objMemberMaster.getImageName() != null) {
            Glide.with(context).load(objMemberMaster.getImageName()).asBitmap().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(holder.ivContactImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    if (resource != null) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.ivContactImage.setImageDrawable(circularBitmapDrawable);
                    } else {
                        holder.ivContactImage.setImageResource(R.drawable.account_navigation);
                    }
                }
            });
        } else {
            holder.ivContactImage.setImageResource(R.drawable.account_navigation);
        }

        if (holder.ivContactImage.getDrawable() == null) {
            holder.ivContactImage.setImageResource(R.drawable.account_navigation);
        }

        if (HomeActivity.isHome) {
            holder.cvContact.setClickable(true);
            holder.llMemberRequest.setVisibility(View.GONE);
        } else {
            holder.cvContact.setClickable(false);
            holder.llMemberRequest.setVisibility(View.VISIBLE);
        }
    }

    public void MemberDataChanged(ArrayList<MemberMaster> result) {
        alMemberMasters.addAll(result);
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    public void MemberDataRemoved(int position) {
        alMemberMasters.remove(position);
        isItemAnimate = false;
        notifyItemRemoved(position);
    }

    public void UndoData(int position, MemberMaster objMemberMaster) {
        isItemAnimate = true;
        alMemberMasters.add(position, objMemberMaster);
        notifyItemInserted(position);
    }

    public void MemberTypechanged(int position, String memberType) {
        alMemberMasters.get(position).setMemberType(memberType);
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return alMemberMasters.size();
    }

    public void SetSearchFilter(ArrayList<MemberMaster> result) {
        isItemAnimate = false;
        alMemberMasters = new ArrayList<>();
        alMemberMasters.addAll(result);
        notifyDataSetChanged();
    }

    public interface OnCardClickListener {
        void OnCardClick(MemberMaster objMemberMaster, String Event, int position);
    }

    public interface OnRequestListener {
        void OnRequestStatus(MemberMaster objMemberMaster, int position, boolean isApproved);
    }

    class MemberViewHolder extends RecyclerView.ViewHolder {

        TextView txtContactName, txtContactNo, txtAdmin, txtEmail, txtProfession;
        CardView cvContact;
        ImageView ivContactImage;
        Button btnCancle, btnAccept;
        LinearLayout llMemberRequest;

        public MemberViewHolder(View itemView) {
            super(itemView);

            txtContactName = (TextView) itemView.findViewById(R.id.txtContactName);
            txtContactNo = (TextView) itemView.findViewById(R.id.txtContactNo);
            txtAdmin = (TextView) itemView.findViewById(R.id.txtAdmin);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);
            txtProfession = (TextView) itemView.findViewById(R.id.txtProfession);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            btnCancle = (Button) itemView.findViewById(R.id.btnCancle);
            btnAccept = (Button) itemView.findViewById(R.id.btnAccept);
            llMemberRequest = (LinearLayout) itemView.findViewById(R.id.llMemberRequest);
            cvContact = (CardView) itemView.findViewById(R.id.cvContact);

            cvContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClickListener.OnCardClick(alMemberMasters.get(getAdapterPosition()), null, getAdapterPosition());
                }
            });

            cvContact.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.add(0, 111, 100, context.getResources().getString(R.string.view_detail)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            onCardClickListener.OnCardClick(alMemberMasters.get(getAdapterPosition()), null, getAdapterPosition());
                            return false;
                        }
                    });
                    menu.add(0, 112, 200, context.getResources().getString(R.string.detail_save_contact)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            onCardClickListener.OnCardClick(alMemberMasters.get(getAdapterPosition()), context.getResources().getString(R.string.detail_save_contact), getAdapterPosition());
                            return false;
                        }
                    });
                    if (Globals.isAdmin) {
                        if (alMemberMasters.get(getAdapterPosition()).getMemberType().equals("Admin")) {
                            menu.add(0, 113, 300, context.getResources().getString(R.string.remove_admin)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    onCardClickListener.OnCardClick(alMemberMasters.get(getAdapterPosition()), context.getResources().getString(R.string.make_admin), getAdapterPosition());
                                    return false;
                                }
                            });
                        } else {
                            menu.add(0, 113, 300, context.getResources().getString(R.string.make_admin)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    onCardClickListener.OnCardClick(alMemberMasters.get(getAdapterPosition()), context.getResources().getString(R.string.make_admin), getAdapterPosition());
                                    return false;
                                }
                            });
                        }

                        menu.add(0, 114, 400, context.getResources().getString(R.string.delete_member)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                onCardClickListener.OnCardClick(alMemberMasters.get(getAdapterPosition()), context.getResources().getString(R.string.delete_member), getAdapterPosition());
                                return false;
                            }
                        });
                    }
                    menu.add(0, 115, 500, context.getResources().getString(R.string.call)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            onCardClickListener.OnCardClick(alMemberMasters.get(getAdapterPosition()), context.getResources().getString(R.string.call), getAdapterPosition());
                            return false;
                        }
                    });
//                    menu.add(0, 114, 400, context.getResources().getString(R.string.message)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//                            onCardClickListener.OnCardClick(alMemberMasters.get(getAdapterPosition()), context.getResources().getString(R.string.message), getAdapterPosition());
//                            return false;
//                        }
//                    });
                    menu.add(0, 116, 600, context.getResources().getString(R.string.email)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            onCardClickListener.OnCardClick(alMemberMasters.get(getAdapterPosition()), context.getResources().getString(R.string.email), getAdapterPosition());
                            return false;
                        }
                    });

                }


            });

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRequestListener.OnRequestStatus(alMemberMasters.get(getAdapterPosition()), getAdapterPosition(), true);
                }
            });

            btnCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRequestListener.OnRequestStatus(alMemberMasters.get(getAdapterPosition()), getAdapterPosition(), false);
                }
            });

        }
    }
}
