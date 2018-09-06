package com.example.lancer.starnote.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lancer.starnote.R;
import com.example.lancer.starnote.bean.NoteBookData;
import com.example.lancer.starnote.ui.fragment.NoteBookEditFragment;
import com.example.lancer.starnote.util.SystemUtils;
import com.example.lancer.starnote.widget.DragGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author: Lancer
 * date：2018/9/6
 * des:
 * email:tyk790406977@126.com
 */

public class DragAdapter extends BaseAdapter implements DragGridView.GridBaseAdapter {
    private int currentHidePosition = -1;
    private List<NoteBookData> mLists;
    private int height;
    private int width;
    private Activity mActivity;
    private boolean DataChange=false;

    public DragAdapter(Activity aty, List<NoteBookData> datas) {
        super();
        Collections.sort(datas);
        this.mLists = datas;
        this.mActivity = aty;
        width = new SystemUtils(aty).getScreenW(aty) / 2;
        height = (int) aty.getResources().getDimension(R.dimen.space_35);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * 数据是否发生了改变
     *
     * @return
     */
    public boolean getDataChange() {
        return DataChange;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        NoteBookData data = mLists.get(position);
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.litem_dragadapter, null);
            viewHolder = new ViewHolder();
            viewHolder.titleBar = convertView.findViewById(R.id.item_note_titlebar);
            viewHolder.date = convertView.findViewById(R.id.item_note_tv_date);
            viewHolder.content = convertView.findViewById(R.id.item_note_content);
            viewHolder.thumbtack = convertView.findViewById(R.id.item_note_img_thumbtack);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ViewGroup.LayoutParams layoutParams = viewHolder.content.getLayoutParams();
        layoutParams.height = (layoutParams.width - height);
        layoutParams.width = width;
        viewHolder.content.setLayoutParams(layoutParams);

        viewHolder.titleBar
                .setBackgroundColor(NoteBookEditFragment.sTitleBackGrounds[data
                        .getColor()]);
        viewHolder.date.setText(data.getDate());
        viewHolder.thumbtack.setImageResource(NoteBookEditFragment.sThumbtackImgs[data.getColor()]);
        viewHolder.content.setText(Html.fromHtml(data.getContent()));
        viewHolder.content.setBackgroundColor(NoteBookEditFragment.sBackGrounds[data.getColor()]);
        if (position == currentHidePosition) {
            convertView.setVisibility(View.GONE);
        } else {
            convertView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public void refurbishData(List<NoteBookData> datas) {
        if (datas == null) {
            datas = new ArrayList<NoteBookData>(1);
        }
        Collections.sort(datas);
        this.mLists = datas;
        notifyDataSetChanged();
    }
    private class ViewHolder {
        TextView date;
        ImageView state;
        ImageView thumbtack;
        View titleBar;
        TextView content;
    }

    @Override
    public void reorderItems(int oldPosition, int newPosition) {
        DataChange = true;
        if (oldPosition >= mLists.size() || oldPosition < 0) {
            return;
        }
        NoteBookData temp = mLists.get(oldPosition);
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(mLists, i, i + 1);
            }
        } else if (oldPosition > newPosition) {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(mLists, i, i - 1);
            }
        }
        mLists.set(newPosition, temp);
    }

    @Override
    public void setHideItem(int hidePosition) {
        this.currentHidePosition = hidePosition;
        notifyDataSetChanged();
    }
}
