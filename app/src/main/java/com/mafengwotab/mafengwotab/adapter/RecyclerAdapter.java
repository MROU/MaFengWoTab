package com.mafengwotab.mafengwotab.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mafengwotab.mafengwotab.R;
import java.util.List;
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements View.OnClickListener{
    private Context mContext;
    private List<String> mInfos;
    LayoutInflater layoutInflator;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public RecyclerAdapter(Context context, List<String> info) {
        this.mContext = context;
        this.mInfos = info;
        this.layoutInflator= LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mInfos.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_type, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder,int pos){
        String info = mInfos.get(pos);
        viewHolder.itemView.setTag(R.id.postition,pos);
        viewHolder.tvTitle.setText(info);
    }


    public static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView tvTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle= (TextView) itemView.findViewById(R.id.tv_title);

        }
    }


    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view, String vid, int pos);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
      //设置点击事件
            mOnItemClickListener.onItemClick(v,(String)v.getTag(),(int)v.getTag(R.id.postition));
        }
    }
}
