package com.sp.video.yi.demo;

import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Weichao Wang on 2016/4/27.
 */
public class MainTestActivity extends ListActivity {
    public static final String[] options = {
            "PlayerTestActivity",
            "RetrofitTestActivity",
            "YiTestActivity"};
    @Override
    protected void onResume() {
        super.onResume();
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = null;
        switch (position) {
            default:
                break;
            case 0:
                intent = new Intent(this,PlayerTestActivity.class);
                break;
            case 1:
                intent = new Intent(this,RetrofitTestActivity.class);
                break;
            case 2:
                intent = new Intent(this,YiTestActivity.class);
                break;
        }
        startActivity(intent);
    }
}
