package com.nowakowski.krzysztof95.navigationdrawerapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


class VievPagerAdapter extends FragmentPagerAdapter {
        private Context ctxt=null;

        VievPagerAdapter(Context ctxt, FragmentManager mgr) {
            super(mgr);
            this.ctxt=ctxt;
        }

        @Override
        public int getCount() {
            return(2);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return (JoinedEventsFragment.newInstance(position));
                case 1:
                    return (MyEventsFragment.newInstance(position));

            }
            return JoinedEventsFragment.newInstance(position);
        }

    @Override
    public String getPageTitle(int position) {
        switch (position) {
            case 0:
                return (JoinedEventsFragment.getTitle(ctxt, position));
            case 1:
                return (MyEventsFragment.getTitle(ctxt ,position));

        }
        return (JoinedEventsFragment.getTitle(ctxt, position));
    }
}

