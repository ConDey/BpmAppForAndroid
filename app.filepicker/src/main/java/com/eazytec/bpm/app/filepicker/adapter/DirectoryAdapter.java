package com.eazytec.bpm.app.filepicker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eazytec.bpm.app.filepicker.R;
import com.eazytec.bpm.app.filepicker.utils.FileTypeUtils;
import com.eazytec.bpm.appstub.view.checkbox.SmoothCheckBox;

import java.io.File;
import java.util.List;

/**
 * @author Administrator
 * @version Id: DirectoryAdapter, v 0.1 2017/7/18 15:02 Administrator Exp $$
 */
public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.DirectoryViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class DirectoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView mFileImage;
        private TextView mFileTitle;
        private TextView mFileSubtitle;
        private SmoothCheckBox mFilecheckBox;

        public DirectoryViewHolder(View itemView, final OnItemClickListener clickListener) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, getAdapterPosition());
                }
            });

            mFileImage = (ImageView) itemView.findViewById(R.id.item_file_image);
            mFileTitle = (TextView) itemView.findViewById(R.id.item_file_title);
            mFileSubtitle = (TextView) itemView.findViewById(R.id.item_file_subtitle);
            mFilecheckBox = (SmoothCheckBox) itemView.findViewById(R.id.item_file_checkbox);
        }
    }

    private List<File> mFiles;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public DirectoryAdapter(Context context, List<File> files) {
        mContext = context;
        mFiles = files;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public DirectoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file, parent, false);

        return new DirectoryViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(DirectoryViewHolder holder, int position) {
        File currentFile = mFiles.get(position);

        FileTypeUtils.FileType fileType = FileTypeUtils.getFileType(currentFile);
        holder.mFileImage.setImageResource(fileType.getIcon());
        holder.mFileSubtitle.setText(fileType.getDescription());
        holder.mFileTitle.setText(currentFile.getName());
        //如果不是目录文件夹，就可以显示checkbox
        if(fileType.equals(FileTypeUtils.FileType.DIRECTORY)){
            holder.mFilecheckBox.setVisibility(View.GONE);
        }else{
            holder.mFilecheckBox.setVisibility(View.VISIBLE);//如果选中，把内容存入一个地方
        }
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public File getModel(int index) {
        return mFiles.get(index);
    }
}
