package musuapp.com.musu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PersonalFragment personalTab = new PersonalFragment();
                return personalTab;
            case 1:
                LatestFragment latestTab = new LatestFragment();
                return latestTab;
            case 2:
                GroupsFragment groupsTab = new GroupsFragment();
                return groupsTab;
            case 3:
                SettingsFragment settingsTab = new SettingsFragment();
                return settingsTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}