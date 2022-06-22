package com.example.newsfresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private Context context;
    private ArrayList<News> items=new ArrayList<News>();
    private NewsItemClicked listener;

    public NewsListAdapter(Context context, NewsItemClicked listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        NewsViewHolder viewHolder=new NewsViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClicked(items.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News currentItem= items.get(position);
        holder.titleView.setText(currentItem.title);
        holder.author.setText(currentItem.author);
        Glide.with(holder.imageView.getContext()).load(currentItem.imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateNews(ArrayList<News> updatedNews)
    {
        items.clear();
        items.addAll(updatedNews);
        notifyDataSetChanged();
    }

}

class NewsViewHolder extends RecyclerView.ViewHolder{
    TextView titleView;
    ImageView imageView;
    TextView author;

    public NewsViewHolder(@NonNull View itemView)
    {
        super(itemView);

        titleView= itemView.findViewById(R.id.title);
        imageView=itemView.findViewById(R.id.image);
        author=itemView.findViewById(R.id.author);
    }
}

interface NewsItemClicked{
    void OnItemClicked(News item);
}