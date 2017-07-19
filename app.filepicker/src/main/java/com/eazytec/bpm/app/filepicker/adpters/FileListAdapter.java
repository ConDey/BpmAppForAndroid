package com.eazytec.bpm.app.filepicker.adpters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eazytec.bpm.app.filepicker.R;
import com.eazytec.bpm.app.filepicker.filepicker.FilePickerConst;
import com.eazytec.bpm.app.filepicker.filepicker.FilePickerManager;
import com.eazytec.bpm.app.filepicker.models.Document;
import com.eazytec.bpm.appstub.view.checkbox.SmoothCheckBox;

import java.util.List;

/**
 * @author Administrator
 * @version Id: FileListAdapter, v 0.1 2017/7/19 9:36 Administrator Exp $$
 */
public class FileListAdapter extends SelectableAdapter<FileListAdapter.FileViewHolder, Document>{


    private final Context context;
    private final FileAdapterListener mListener;

    public FileListAdapter(Context context, List<Document> items, List<String> selectedPaths, FileAdapterListener fileAdapterListener) {
        super(items, selectedPaths);
        this.context = context;
        this.mListener = fileAdapterListener;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false);

        return new FileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, int position) {
        final Document document = getItems().get(position);

        holder.imageView.setImageResource(document.getFileType().getDrawable());
        holder.fileNameTextView.setText(document.getTitle());
        holder.fileSizeTextView.setText(Formatter.formatShortFileSize(context, Long.parseLong(document.getSize())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicked(document,holder);
            }
        });

        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClicked(document,holder);
            }
        });

        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(isSelected(document));

        holder.itemView.setBackgroundResource(isSelected(document)?R.color.grey_300:android.R.color.white);
        holder.checkBox.setVisibility(isSelected(document) ? View.VISIBLE : View.GONE);

        holder.checkBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                toggleSelection(document);
                holder.itemView.setBackgroundResource(isChecked?R.color.grey_300:android.R.color.white);

                if(mListener!=null)
                    mListener.onItemSelected();
            }
        });
    }

    private void onItemClicked(Document document, FileViewHolder holder)
    {
        if(FilePickerManager.getInstance().getMaxCount()==1)
        {
            FilePickerManager.getInstance().add(document.getPath(), FilePickerConst.FILE_TYPE_DOCUMENT);
            if(mListener!=null)
                mListener.onItemSelected();
        }
        else {
            if (holder.checkBox.isChecked()) {
                FilePickerManager.getInstance().remove(document.getPath(), FilePickerConst.FILE_TYPE_DOCUMENT);
                holder.checkBox.setChecked(!holder.checkBox.isChecked(), true);
                holder.checkBox.setVisibility(View.GONE);
            } else if (FilePickerManager.getInstance().shouldAdd()) {
                FilePickerManager.getInstance().add(document.getPath(), FilePickerConst.FILE_TYPE_DOCUMENT);
                holder.checkBox.setChecked(!holder.checkBox.isChecked(), true);
                holder.checkBox.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        SmoothCheckBox checkBox;

        ImageView imageView;

        TextView fileNameTextView;

        TextView fileSizeTextView;

        public FileViewHolder(View itemView) {
            super(itemView);
            checkBox = (SmoothCheckBox) itemView.findViewById(R.id.item_file_checkbox);
            imageView = (ImageView) itemView.findViewById(R.id.item_file_image);
            fileNameTextView = (TextView) itemView.findViewById(R.id.item_file_title);
            fileSizeTextView = (TextView) itemView.findViewById(R.id.item_file_subtitle);
        }
    }
}

