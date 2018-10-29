package com.smallraw.foretime.app.ui.musicListActivity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smallraw.foretime.app.R;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
  private List<MusicBean> mData;

  public MusicAdapter(List<MusicBean> data) {
    mData = data;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_music, viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
    MusicBean musicBean = mData.get(i);
    viewHolder.mContentTextView.setText(musicBean.content);
    viewHolder.mTitleTextView.setText(musicBean.title);
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView mImageView;
    private TextView mTitleTextView;
    private TextView mContentTextView;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      mImageView = itemView.findViewById(R.id.ivMusic);
      mTitleTextView = itemView.findViewById(R.id.tvTitle);
      mContentTextView = itemView.findViewById(R.id.tvContent);
    }
  }
}
