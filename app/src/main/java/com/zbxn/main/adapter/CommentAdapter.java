package com.zbxn.main.adapter;

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

import com.pub.utils.DateUtils;
import com.pub.utils.MyToast;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.entity.Comment;
import com.zbxn.main.entity.Member;
import com.zbxn.main.entity.Reply;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.pub.base.BaseApp.getContext;

/**
 * Created by ysj on 2016/8/10.
 */
public class CommentAdapter extends BaseAdapter {

    private Context mContext;
    private List<Comment> mList;
    private CallBackClick callBackClick;

    public CommentAdapter(Context mContext, List<Comment> mList, CallBackClick callBackClick) {
        this.mContext = mContext;
        this.mList = mList;
        this.callBackClick = callBackClick;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item_comment, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Comment comment = mList.get(position);
        holder.commentsMemberid.setText(comment.getCreateUserName());
        holder.commentsContent.setText(comment.getCommentcontent().toString());
        holder.commentsCreatetime.setText((DateUtils.fromTodaySimple(comment.getCreatetime())));

        final List<Reply> list = comment.getReplyList();
        int count = list.size();
        holder.layout.removeAllViews();
        if (count != 0) {
            for (int i = 0; i < count; i++) {
                TextView t = new TextView(getContext());
                t.setLayoutParams(new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)));
                t.setTextColor(mContext.getResources().getColor(R.color.tvc6));
                t.setTextSize(14f);

                String replyTo = list.get(i).getReplyToUserName();
                String replyContent = list.get(i).getReplycontent();
                final String creatName = list.get(i).getCreateUserName();
                if (Member.isLogin()) {
                    String name = Member.get().getUserName().equals(creatName) ? "我" : creatName;
                    String data = DateUtils.fromTodaySimple(list.get(i).getCreatetime());
                    if (StringUtils.isBlank(name)) {
                        name = "";
                    }
                    String text = name + "  回复  " + replyTo + ": " + replyContent + "   " + data;
                    if (text != null) {
                        SpannableString spanText = new SpannableString(text);
                        spanText.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.orange)), 0, name.length(),
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        spanText.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.orange)), name.length() + 6, name.length() + 6 + replyTo.length(),
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        t.append(spanText);
                    }
                }
                holder.layout.addView(t);

                //回复评论中的回复
                final int finalI = i;
                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBackClick.onViewClick(list, finalI, comment);
                    }
                });
            }
        }
//        holder.imgComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.showToast("删除");
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iamgeView_portrait)
        CircleImageView iamgeViewPortrait;
        @BindView(R.id.comments_memberid)
        TextView commentsMemberid;
        @BindView(R.id.comments_createtime)
        TextView commentsCreatetime;
        @BindView(R.id.comments_content)
        TextView commentsContent;
        @BindView(R.id.comment_reply)
        TextView commentReply;
        @BindView(R.id.layout)
        LinearLayout layout;
        @BindView(R.id.img_delete)
        ImageView imgDelete;
        @BindView(R.id.img_comment)
        ImageView imgComment;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface CallBackClick {
        void onViewClick(List<Reply> list, int position, Comment comment);
    }

}
