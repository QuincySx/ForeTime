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
package com.smallraw.foretime.app.common.widget.dialog;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smallraw.foretime.app.R;
import com.smallraw.foretime.app.base.BaseDialogView;

public class MultipleItemDialog extends BaseDialogView {
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;
    private OnDialogChangeSelectListener mOnChangeSelectListener;

    private MultipleItemDialog(Builder builder) {
        super(builder);
        mOnChangeSelectListener = builder.mOnChangeSelectListener;
        mMyAdapter =
                new MyAdapter(
                        builder.list,
                        new OnChangeSelectListener() {
                            @Override
                            public void onChange(int index) {
                                mOnChangeSelectListener.onChange(MultipleItemDialog.this, index);
                            }
                        });
        mMyAdapter.mCurrentSelect = builder.mSelectItem;
        mRecyclerView.setAdapter(mMyAdapter);
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected View setRootView() {
        View view = getLayoutInflater().inflate(R.layout.dialog_multiple_item_select, null, false);
        return view;
    }

    public static final class Builder extends BaseDialogView.Builder {
        private Context mContext;
        private List<String> list;
        private int mSelectItem;
        private OnDialogChangeSelectListener mOnChangeSelectListener;

        public Builder(Context context) {
            super(context);
            mContext = context;
        }

        public MultipleItemDialog.Builder setDate(List<String> list) {
            this.list = list;
            return this;
        }

        public MultipleItemDialog.Builder setSelectItem(int index) {
            this.mSelectItem = index;
            return this;
        }

        public MultipleItemDialog.Builder setSelectItem(
                OnDialogChangeSelectListener onChangeSelectListener) {
            this.mOnChangeSelectListener = onChangeSelectListener;
            return this;
        }

        public MultipleItemDialog build() {
            return new MultipleItemDialog(this);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_item);
        }
    }

    public static class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<String> list;
        private int mCurrentSelect = 0;
        private OnChangeSelectListener mOnChangeSelectListener;

        public MyAdapter(List<String> list, OnChangeSelectListener onChangeSelectListener) {
            this.list = list;
            mOnChangeSelectListener = onChangeSelectListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view =
                    LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.item_multipele_select, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            if (mCurrentSelect == viewHolder.getAdapterPosition()) {
                viewHolder.mTextView.setSelected(true);
            } else {
                viewHolder.mTextView.setSelected(false);
            }
            viewHolder.mTextView.setText(list.get(i));
            viewHolder.mTextView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int temp = mCurrentSelect;
                            mCurrentSelect = viewHolder.getAdapterPosition();
                            notifyItemChanged(temp);
                            notifyItemChanged(mCurrentSelect);
                            if (mOnChangeSelectListener != null) {
                                mOnChangeSelectListener.onChange(viewHolder.getAdapterPosition());
                            }
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }
    }

    public interface OnChangeSelectListener {
        void onChange(int index);
    }

    public interface OnDialogChangeSelectListener {
        void onChange(MultipleItemDialog dialog, int index);
    }
}
