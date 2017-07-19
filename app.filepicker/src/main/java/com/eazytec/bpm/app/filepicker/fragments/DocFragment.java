package com.eazytec.bpm.app.filepicker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eazytec.bpm.app.filepicker.R;
import com.eazytec.bpm.app.filepicker.adpters.FileAdapterListener;
import com.eazytec.bpm.app.filepicker.adpters.FileListAdapter;
import com.eazytec.bpm.app.filepicker.filepicker.FilePickerManager;
import com.eazytec.bpm.app.filepicker.models.Document;
import com.eazytec.bpm.app.filepicker.models.FileType;

import java.util.List;

/**
 * @author Administrator
 * @version Id: DocFragment, v 0.1 2017/7/19 10:11 Administrator Exp $$
 */
public class DocFragment extends BaseFragment implements FileAdapterListener {

    private static final String TAG = DocFragment.class.getSimpleName();
    RecyclerView recyclerView;

    LinearLayout emptyView;

    private DocFragmentListener mListener;
    private FileListAdapter fileListAdapter;
    private FileType fileType;

    public DocFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_list, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DocFragmentListener) {
            mListener = (DocFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PhotoPickerFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static DocFragment newInstance(FileType fileType) {
        DocFragment photoPickerFragment = new DocFragment();
        Bundle bun = new Bundle();
        bun.putParcelable(FILE_TYPE, fileType);
        photoPickerFragment.setArguments(bun);
        return  photoPickerFragment;
    }

    public FileType getFileType() {
        return getArguments().getParcelable(FILE_TYPE);
    }

    @Override
    public void onItemSelected() {
        mListener.onItemSelected();
    }

    public interface DocFragmentListener {
        void onItemSelected();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        fileType =  getArguments().getParcelable(FILE_TYPE);

        recyclerView = (RecyclerView) view.findViewById(R.id.file_list_recyclerview);
        emptyView = (LinearLayout) view.findViewById(R.id.file_list_empty_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setVisibility(View.GONE);
    }

    public void updateList(List<Document> dirs) {
        if(getView()==null)
            return;

        if(dirs.size()>0)
        {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);

            FileListAdapter fileListAdapter = (FileListAdapter) recyclerView.getAdapter();
            if(fileListAdapter==null) {
                fileListAdapter = new FileListAdapter(getActivity(), dirs, FilePickerManager.getInstance().getSelectedFiles(), this);

                recyclerView.setAdapter(fileListAdapter);
            }
            else
            {
                fileListAdapter.setData(dirs);
                fileListAdapter.notifyDataSetChanged();
            }
        }
        else
        {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

}
