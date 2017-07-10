package com.zbxn.crm.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pub.utils.ConfigUtils;
import com.pub.utils.DateUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.crm.entity.FollowCommentEntity;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author: ysj
 * @date: 2017-01-23 16:58
 */
public class FollowCommentAdapter extends BaseAdapter {

    private Context mContext;
    private List<FollowCommentEntity> mList;

    public FollowCommentAdapter(Context mContext, List<FollowCommentEntity> list) {
        this.mContext = mContext;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item_comment, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FollowCommentEntity entity = mList.get(position);
        holder.commentsMemberid.setText(entity.getCreateUserName());
        holder.commentsCreatetime.setText((DateUtils.fromTodaySimple(StringUtils.convertToDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), entity.getCreateTimeStr()))));
        if (entity.getReplyToID().equals(entity.getFollowRecordID())) {//这俩相同 是评论
            holder.commentsContent.setText(entity.getReplyContent());
        } else {//否则是回复
            String text = "回复@" + entity.getReplyToUserName() + ": " + entity.getReplyContent();
            SpannableString spanText = new SpannableString(text);
            spanText.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.orange)), 2, entity.getReplyToUserName().length() + 3,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.commentsContent.setText(spanText);
        }
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        ImageLoader.getInstance().displayImage(server + entity.getCreateUserIcon(), holder.imageViewPortrait);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iamgeView_portrait)
        CircleImageView imageViewPortrait;
        @BindView(R.id.comments_memberid)
        TextView commentsMemberid;
        @BindView(R.id.comments_createtime)
        TextView commentsCreatetime;
        @BindView(R.id.img_comment)
        ImageView imgComment;
        @BindView(R.id.img_delete)
        ImageView imgDelete;
        @BindView(R.id.comment_reply)
        TextView commentReply;
        @BindView(R.id.comments_content)
        TextView commentsContent;
        @BindView(R.id.comment_layout)
        LinearLayout commentLayout;
        @BindView(R.id.layout)
        LinearLayout layout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
