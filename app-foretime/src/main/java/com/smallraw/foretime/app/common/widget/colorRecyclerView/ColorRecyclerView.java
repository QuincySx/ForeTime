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
package com.smallraw.foretime.app.common.widget.colorRecyclerView;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import com.smallraw.foretime.app.R;
import com.smallraw.foretime.app.common.widget.SpacesItemDecoration;

public class ColorRecyclerView extends RecyclerView {
    private List<String> mColors;
    private ColorAdapter mColorAdapter;

    public void setColors(List<String> colors) {
        mColors = colors;
        mColorAdapter = new ColorAdapter(mColors);
        setAdapter(mColorAdapter);
    }

    public String getSelectColor() {
        return mColors.get(mColorAdapter.mCurrentSelectIndex);
    }

    public ColorRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        addItemDecoration(new SpacesItemDecoration(AutoSizeUtils.dp2px(getContext(), 13)));
    }

    private static class ColorViewHolder extends ViewHolder {
        private final ImageView mImageView;

        private ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_color);
        }
    }

    public static class ColorAdapter extends Adapter<ColorViewHolder> {
        private List<String> mColors;
        private OnItemClickListener mOnItemClickListener;
        private int mCurrentSelectIndex;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        public ColorAdapter(List<String> colors) {
            mColors = colors;
        }

        @NonNull
        @Override
        public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = View.inflate(viewGroup.getContext(), R.layout.item_color_select, null);
            return new ColorViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ColorViewHolder colorViewHolder, int i) {
            Context context = colorViewHolder.mImageView.getContext();
            if (mCurrentSelectIndex == colorViewHolder.getAdapterPosition()) {
                colorViewHolder.mImageView.setSelected(true);
            } else {
                colorViewHolder.mImageView.setSelected(false);
            }

            int clolr = Color.parseColor(mColors.get(i));
            if (colorViewHolder.mImageView.getDrawable() != null
                    && colorViewHolder.mImageView.getDrawable() instanceof ColorItemDrawable) {
                ((ColorItemDrawable) colorViewHolder.mImageView.getDrawable()).setColor(clolr);
            } else {
                ColorItemDrawable colorItemDrawable = new ColorItemDrawable(context);
                colorItemDrawable.setColor(clolr);
                colorViewHolder.mImageView.setImageDrawable(colorItemDrawable);
            }

            colorViewHolder.mImageView.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.setSelected(true);

                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onClick(
                                        v, colorViewHolder.getAdapterPosition());
                            }

                            int temp = mCurrentSelectIndex;
                            mCurrentSelectIndex = colorViewHolder.getAdapterPosition();
                            notifyItemChanged(temp);
                            notifyItemChanged(mCurrentSelectIndex);
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return mColors == null ? 0 : mColors.size();
        }
    }

    private interface OnItemClickListener {
        public void onClick(View view, int postion);
    }
}
