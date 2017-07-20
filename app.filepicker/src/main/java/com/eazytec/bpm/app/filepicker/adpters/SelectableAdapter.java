package com.eazytec.bpm.app.filepicker.adpters;

import android.support.v7.widget.RecyclerView;

import com.eazytec.bpm.app.filepicker.models.BaseFile;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 * @version Id: SelectableAdapter, v 0.1 2017/7/19 9:39 Administrator Exp $$
 */
public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder, T extends BaseFile>
        extends RecyclerView.Adapter<VH> implements Selectable<T> {

    private static final String TAG = SelectableAdapter.class.getSimpleName();
    private List<T> items;

    protected List<T> selectedFiles;

    public SelectableAdapter(List<T> items, List<String> selectedPaths) {
        this.items = items;
        selectedFiles = new ArrayList<>();

        addPathsToSelections(selectedPaths);
    }

    private void addPathsToSelections(List<String> selectedPaths) {
        if(selectedPaths==null)
            return;

        for (int index = 0; index < items.size(); index++) {
            for (int jindex = 0; jindex < selectedPaths.size(); jindex++) {
                if(items.get(index).getPath().equals(selectedPaths.get(jindex)))
                {
                    selectedFiles.add(items.get(index));
                }
            }
        }
    }


    @Override
    public boolean isSelected(T photo) {
        return selectedFiles.contains(photo);
    }


    @Override
    public void toggleSelection(T photo) {
        if (selectedFiles.contains(photo)) {
            selectedFiles.remove(photo);
        } else {
            selectedFiles.add(photo);
        }
    }


    @Override
    public void clearSelection() {
        selectedFiles.clear();
    }

    @Override
    public int getSelectedItemCount() {
        return selectedFiles.size();
    }

    public void setData(List<T> items) {
        this.items = items;
    }

    public List<T> getItems()
    {
        return items;
    }

}
