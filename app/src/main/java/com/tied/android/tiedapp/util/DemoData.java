package com.tied.android.tiedapp.util;

import com.tied.android.tiedapp.R;

/**
 * Created by yuweichen on 16/5/3.
 */
public class DemoData {

    public static int[] covers = {R.mipmap.walkthrough1, R.mipmap.walkthrough2, R.mipmap.walkthrough3, R.mipmap.walkthrough4};

    public static int[] profile_layout = {R.layout.profile1 /*, R.layout.profile2, R.layout.profile3*/ };

    public static int[] select_client_layout = {R.layout.schedule_select_client_distance_view, R.layout.schedule_select_client_map_view, R.layout.schedule_select_client_list};
    public static int[] activity_layout = {R.layout.fragment_schedule_create_employee, R.layout.fragment_you_timeline};


//    public void selectTab(LinearLayout tab_bar, int position){
//        int index = 0;
//        for(int i = 0; i < tab_bar.getChildCount(); i++){
//            if(tab_bar.getChildAt(i) instanceof LinearLayout){
//                LinearLayout child = (LinearLayout) tab_bar.getChildAt(i);
//                Log.d(TAG, "am here != position "+child.getChildAt(i));
//                TextView title = (TextView) child.getChildAt(0);
//                TextView indicator = (TextView) child.getChildAt(1);
//                if(position != index){
//                    indicator.setVisibility(View.GONE);
//                    title.setTextColor(getResources().getColor(R.color.semi_transparent_black));
//                }else{
//                    indicator.setVisibility(View.VISIBLE);
//                    title.setTextColor(getResources().getColor(R.color.button_bg));
//                }
//                index++;
//            }
//        }
//    }
}
