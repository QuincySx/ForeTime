/*
 * Copyright 2021 Smallraw Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smallraw.foretime.app.ui.musicListActivity;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smallraw.foretime.app.R;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private List<MusicBean> mData;

    public MusicAdapter(List<MusicBean> data) {
        mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_music, viewGroup, false));
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
