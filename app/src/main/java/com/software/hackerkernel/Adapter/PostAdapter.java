package com.software.hackerkernel.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.software.hackerkernel.Model.PostModel;
import com.software.hackerkernel.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context mCtx;
    private List<PostModel> postModels;

    public PostAdapter(Context mCtx, List<PostModel> postModels) {
        this.mCtx = mCtx;
        this.postModels = postModels;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostViewHolder viewHolder;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_post, parent, false);
        viewHolder = new PostViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
        final PostModel postModellist = postModels.get(position);// call model
        holder.body_post.setText(postModellist.getBody());
        holder.textpalaceno_post.setText(postModellist.getTitle());
        Picasso.with(mCtx).load(R.drawable.image).transform(new CircleTransform()).into(holder.imageplace_post);
    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        CardView cardpost;
        ImageView imageplace_post;
        TextView textpalaceno_post,body_post;
        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            cardpost=itemView.findViewById(R.id.cardpost);
            imageplace_post=itemView.findViewById(R.id.imageplace_post);
            textpalaceno_post=itemView.findViewById(R.id.textpalaceno_post);
            body_post=itemView.findViewById(R.id.body_post);
        }
    }
    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
