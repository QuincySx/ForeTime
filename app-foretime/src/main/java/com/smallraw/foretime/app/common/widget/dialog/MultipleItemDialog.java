package com.smallraw.foretime.app.common.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smallraw.foretime.app.R;
import com.smallraw.foretime.app.base.BaseDialogView;

import java.util.List;

public class MultipleItemDialog extends BaseDialogView {
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;
    private OnDialogChangeSelectListener mOnChangeSelectListener;

    private MultipleItemDialog(Builder context) {
        super(context.mContext);
        mOnChangeSelectListener = context.mOnChangeSelectListener;
        mMyAdapter = new MyAdapter(context.list, new OnChangeSelectListener() {
            @Override
            public void onChange(int index) {
                mOnChangeSelectListener.onChange(MultipleItemDialog.this, index);
            }
        });
        mMyAdapter.mCurrentSelect = context.mSelectItem;
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

    public static final class Builder {
        private Context mContext;
        private List<String> list;
        private int mSelectItem;
        private OnDialogChangeSelectListener mOnChangeSelectListener;

        public Builder(Context context) {
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

        public MultipleItemDialog.Builder setSelectItem(OnDialogChangeSelectListener onChangeSelectListener) {
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
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_multipele_select, viewGroup, false);
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
            viewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
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