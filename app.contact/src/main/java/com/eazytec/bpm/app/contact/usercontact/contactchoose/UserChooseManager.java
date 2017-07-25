package com.eazytec.bpm.app.contact.usercontact.contactchoose;

/**
 * @author Administrator
 * @version Id: UserChooseManager, v 0.1 2017/7/25 18:23 Administrator Exp $$
 */
public class UserChooseManager {

    private static UserChooseManager ourInstance = new UserChooseManager();

    private int maxCount = UserChooseConst.DEFAULT_MAX_COUNT;
    private int currentCount;

    public static UserChooseManager getOurInstance(){
        return ourInstance;
    }

    public void setMaxCount(int count) {
        clearSelections();
        this.maxCount = count;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void clearSelections() {
        currentCount = 0;
        maxCount = 0;
    }

    public int getCurrentCount() {
        return currentCount;
    }

}
