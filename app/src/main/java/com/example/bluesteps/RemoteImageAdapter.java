package com.example.bluesteps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class RemoteImageAdapter extends RecyclerView.Adapter<RemoteImageAdapter.ViewHolder> {

    private final Context context;
    private final List<String> imageUrls;

    public RemoteImageAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fish_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);

        // İnternet var mı kontrol et
        boolean hasInternet = checkInternetConnection.isInternetAvailable(context);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.fish) // Yüklenirken gösterilecek
                .error(R.drawable.fish)             // Hata olursa gösterilecek
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();

        if (hasInternet) {
            // İnternet varsa normal yükle
            Glide.with(context)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(holder.imageView);
        } else {
            // İnternet yoksa sadece cache’den al, yoksa default göster
            Glide.with(context)
                    .load(imageUrl)
                    .apply(requestOptions.onlyRetrieveFromCache(true))
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
