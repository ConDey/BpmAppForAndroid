package com.eazytec.bpm.app.message;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.eazytec.bpm.app.message.detail.ReadedFragment;
import com.eazytec.bpm.app.message.detail.UnReadedFragment;

/**
 * @author Beckett_W
 * @version Id: MessageMainTabAdapter, v 0.1 2017/11/3 16:35 Beckett_W Exp $$
 */
public class MessageMainTabAdapter extends FragmentPagerAdapter {

    private Context context = null;

    private ReadedFragment readedFragment = new ReadedFragment();
    private UnReadedFragment unReadedFragment = new UnReadedFragment();

    private static final int TAB_INDEX_COUNT = 2;

    private static final int TAB_INDEX_ONE = 0;
    private static final int TAB_INDEX_TWO = 1;


    public MessageMainTabAdapter(FragmentManager fm , Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case TAB_INDEX_ONE:
                return unReadedFragment;
            case TAB_INDEX_TWO:
                return readedFragment;
        }
        throw new IllegalStateException("No fragment at position " + position);
    }

    @Override
    public int getCount() {
        return TAB_INDEX_COUNT;
    }

    //tab选项卡名字
    @Override
    public CharSequence getPageTitle(int position) {
        String tabLabel = null;
        switch (position){
            case TAB_INDEX_ONE:
                tabLabel = this.context.getString(R.string.message_tab_one);
                break;
            case TAB_INDEX_TWO:
                tabLabel = this.context.getString(R.string.message_tab_two);
                break;
        }
        return tabLabel;
    }

    private int mChildCount = 0;

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object)   {
        if ( mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

}
