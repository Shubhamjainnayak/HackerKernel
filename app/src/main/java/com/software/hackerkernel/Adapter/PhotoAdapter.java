package com.software.hackerkernel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dmallcott.dismissibleimageview.DismissibleImageView;
import com.software.hackerkernel.Model.PhotoModel;
import com.software.hackerkernel.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private Context mCtx;
    private List<PhotoModel> photoModels;
    public PhotoAdapter(Context mCtx, List<PhotoModel> photoModels) {
        this.mCtx = mCtx;
        this.photoModels = photoModels;
    }


    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PhotoViewHolder viewHolder;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_photos, parent, false);
        viewHolder = new PhotoViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        final PhotoModel photoModellist = photoModels.get(position);// call model
        holder.textpalaceno.setText(photoModellist.getTitle());
        Picasso.with(mCtx).load(photoModellist.getUrl()).into(holder.imageplace);
    }

    @Override
    public int getItemCount() {
        return photoModels.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        CardView cardarea;
        DismissibleImageView imageplace;
        TextView textpalaceno;
        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            cardarea=itemView.findViewById(R.id.cardarea);
            imageplace=itemView.findViewById(R.id.imageplace);
            textpalaceno=itemView.findViewById(R.id.textpalaceno);
        }
    }

}
