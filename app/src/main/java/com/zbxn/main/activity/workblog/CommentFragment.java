package com.zbxn.main.activity.workblog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.pub.base.BaseFragment;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.CommentAdapter;
import com.zbxn.main.entity.Comment;
import com.zbxn.main.entity.Reply;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * 评论fragment
 * <p/>
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends BaseFragment implements
        AdapterView.OnItemClickListener, CommentAdapter.CallBackClick {

    @BindView(R.id.mListView)
    ListView mListView;

    private int mDataId;//日志或公告id
    private int mType;//类型 1--公告 2--日志
    private int mPage = 1;
    private int mRows = 10;

    private List<Comment> mList;

    private CommentAdapter mAdapter;
    private CommentFooterView mFooterView;
    CallBackComment callBackComment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callBackComment = (CallBackComment) getActivity();
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        mList = new ArrayList<>();
        mAdapter = new CommentAdapter(getActivity(), mList, this);
        mListView.setFocusable(false);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mFooterView = new CommentFooterView(this, mListView);
    }


    public void reloadData(int dataId, int type) {
        this.mDataId = dataId;
        this.mType = type;
        getCommentList();
    }

    //回复某条评论
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        //??
        ImageView imgComment = (ImageView) view.findViewById(R.id.img_comment);
        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //删除
        ImageView imgDelete = (ImageView) view.findViewById(R.id.img_delete);
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Comment comment = mList.get(position);
        //获取当前登录人id
        int loginId = Integer.decode(PreferencesUtils.getString(getContext(), LoginActivity.FLAG_INPUT_ID));
        //MyToast.showToast("" + comment.getCreateuserid());
        if (loginId == comment.getCreateuserid()) {
            MyToast.showToast("不能回复自己");
        } else {
            callBackComment.sendComment(comment.getCreateUserName(), comment.getId(), comment.getCreateuserid());
        }
    }

    /**
     * 回复评论的点击事件
     */
    @Override
    public void onViewClick(List<Reply> list, int position, Comment comment) {
        //获取当前登录人id
        String loginId = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_INPUT_ID);
        if (Integer.decode(loginId) == list.get(position).getCreateuserid()) {
            MyToast.showToast("不能回复自己");
        } else {
            Reply reply = list.get(position);
            callBackComment.sendComment(reply.getCreateUserName(), comment.getId(), reply.getCreateuserid());
        }
    }

    /**
     * 获取评论列表
     */
    public void getCommentList() {
        String ssid = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().getCommentList(ssid, mType, mDataId, mPage, mRows);
        callRequest(call, new HttpCallBack(Comment.class, getContext(), false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    List<Comment> list = mResult.getRows();
                    if (mPage == 1) {
                        mList.clear();
                    }
                    mPage++;
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    setMore(list);
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
    }

    /**
     * 显示加载更多
     *
     * @param mResult
     */
    private void setMore(List mResult) {
        if (mResult == null) {
            mFooterView.loadMoreComplete(true);
            return;
        }
        int pageTotal = mResult.size();
        if (pageTotal >= mRows) {
            mFooterView.loadMoreComplete(true);
        } else {
            mFooterView.loadMoreComplete(false);
        }
    }

    //目前使用<fragment/>加载时，必须实现这个接口,否则会蹦
    public interface CallBackComment {
        void sendComment(String name, int commentId, int replyToId);
    }

    /**
     * 刷新评论列表
     */
    public void refreshUI() {
        mPage = 1;
        getCommentList();
    }

}
