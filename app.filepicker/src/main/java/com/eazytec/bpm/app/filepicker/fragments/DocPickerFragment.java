package com.eazytec.bpm.app.filepicker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.internal.util.Predicate;
import com.eazytec.bpm.app.filepicker.R;
import com.eazytec.bpm.app.filepicker.adpters.SectionsPagerAdapter;
import com.eazytec.bpm.app.filepicker.cursors.loadercallbacks.FileResultCallback;
import com.eazytec.bpm.app.filepicker.filepicker.FilePickerManager;
import com.eazytec.bpm.app.filepicker.models.Document;
import com.eazytec.bpm.app.filepicker.models.FileType;
import com.eazytec.bpm.app.filepicker.utils.MediaStoreHelper;
import com.eazytec.bpm.app.filepicker.utils.TabLayoutHelper;
import com.eazytec.bpm.app.filepicker.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Administrator
 * @version Id: DocPickerFragment, v 0.1 2017/7/19 10:14 Administrator Exp $$
 */
public class DocPickerFragment extends BaseFragment {

    private static final String TAG = DocPickerFragment.class.getSimpleName();

    TabLayout tabLayout;

    ViewPager viewPager;
    private ArrayList<String> selectedPaths;
    private ProgressBar progressBar;
    private DocPickerFragmentListener mListener;

    public DocPickerFragment() {
        // Required empty public constructor
    }

    public interface DocPickerFragmentListener{
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_picker, container, false);
    }

    public static DocPickerFragment newInstance(ArrayList<String> selectedPaths) {
        DocPickerFragment docPickerFragment = new DocPickerFragment();
        docPickerFragment.selectedPaths = selectedPaths;
        return  docPickerFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DocPickerFragmentListener) {
            mListener = (DocPickerFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DocPickerFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews(view);
        initView();
    }

    private void initView() {
        setUpViewPager();
        setData();
    }

    private void setViews(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.file_picker_tabs);
        viewPager = (ViewPager) view.findViewById(R.id.file_picker_viewPager);
        progressBar = (ProgressBar) view.findViewById(R.id.file_picker_progress_bar);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void setData() {
        MediaStoreHelper.getDocs(getActivity(), new FileResultCallback<Document>() {
            @Override
            public void onResultCallback(List<Document> files) {
                if(!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                setDataOnFragments(files);
            }
        });
    }

    private void setDataOnFragments(List<Document> files) {
        SectionsPagerAdapter sectionsPagerAdapter = (SectionsPagerAdapter) viewPager.getAdapter();
        if(sectionsPagerAdapter!=null)
        {
            for (int index = 0; index < sectionsPagerAdapter.getCount(); index++) {
                DocFragment docFragment = (DocFragment) getChildFragmentManager()
                        .findFragmentByTag(
                                "android:switcher:" + R.id.file_picker_viewPager + ":"+index);
                if(docFragment!=null)
                {
                    FileType fileType = docFragment.getFileType();
                    if(fileType!=null)
                        docFragment.updateList(filterDocuments(fileType.extensions, files));
                }
            }
        }
    }

    private void setUpViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        ArrayList<FileType> supportedTypes = FilePickerManager.getInstance().getFileTypes();
        for (int index = 0; index < supportedTypes.size(); index++) {
            adapter.addFragment(DocFragment.newInstance(supportedTypes.get(index)),supportedTypes.get(index).title);
        }

        viewPager.setOffscreenPageLimit(supportedTypes.size());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        TabLayoutHelper mTabLayoutHelper = new TabLayoutHelper(tabLayout, viewPager);
        mTabLayoutHelper.setAutoAdjustTabModeEnabled(true);
    }

    private ArrayList<Document> filterDocuments(final String[] type, List<Document> documents)
    {
        final Predicate<Document> docType = new Predicate<Document>() {
            public boolean apply(Document document) {
                return document.isThisType(type);
            }
        };

        return new ArrayList<>(Utils.filter(new HashSet<>(documents),docType));
    }
}

