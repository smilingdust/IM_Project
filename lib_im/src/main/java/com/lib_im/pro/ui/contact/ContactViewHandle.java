package com.lib_im.pro.ui.contact;

import android.content.Context;
import android.widget.TextView;

import com.lib_im.pro.R;
import com.lib_im.pro.entity.Contact;
import com.lib_im.pro.ui.widget.recyler.BaseRecyclerAdapter;
import com.lib_im.pro.ui.widget.recyler.LViewHolder;
import com.lib_im.pro.ui.widget.view.CircleImageView;
import com.lib_im.pro.utils.ImageLoader;

import java.util.List;

/**
 * Created by songgx on 2017/1/22.
 * contact设置view
 */

public class ContactViewHandle {

    private Context mContext;

    public ContactViewHandle(Context context) {
        this.mContext=context;
    }

    /**
    * @descript  联系人adapter设置view
    *
    * @param position
    *
    * @param list
    *
    * @param holder
    *
    */
    public void setView(int position, List<Contact> list, LViewHolder holder) {
        Contact bean = list.get(position);
        CircleImageView head = holder.getView(R.id.contact_head);
        TextView name = holder.getView(R.id.contact_name);
        ImageLoader.load(mContext,head,"",ImageLoader.getHeadOption());
        name.setText(bean.getNickname());
    }
}
