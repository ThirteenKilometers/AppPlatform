package com.yw.platform.yhtext.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yw.platform.R;
import com.yw.platform.yhtext.beans.MessageEvent;
import com.yw.platform.yhtext.beans.accept_bean.AcceptQueryDocumentListBean;
import com.yw.platform.yhtext.beans.accept_bean.AcceptQueryDocumentPreviewUrl;
import com.yw.platform.yhtext.beans.commonbeans.DocumentBean;
import com.yw.platform.yhtext.beans.commonbeans.PagingInfoBean;
import com.yw.platform.yhtext.beans.send_bean.SendQueryDocumentListBean;
import com.yw.platform.yhtext.beans.send_bean.SendQueryDocumentPreviewUrl;
import com.yw.platform.yhtext.beans.send_bean.base.BaseSendMsgBean;
import com.yw.platform.yhtext.netty.client.Const;
import com.yw.platform.yhtext.utils.BaseUtils;
import com.yw.platform.yhtext.utils.DocumentFormatUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import lzhs.com.library.utils.ToastUtils;
import lzhs.com.library.wedgit.comm_adapter.absrecyclerview.CommonAdapter;
import lzhs.com.library.wedgit.comm_adapter.absrecyclerview.base.ViewHolder;

public class DocumentListActivity extends AppCompatActivity {
    TwinklingRefreshLayout mRefreshLayout;
    RecyclerView mRecycleList;
    int currPageNum = 1;
    PagingInfoBean pagingInfoBean = null;
    List<DocumentBean> mDatas = new ArrayList<>();
    CommonAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_list);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        mRecycleList = (RecyclerView) findViewById(R.id.mRecycleList);
        mRecycleList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DocAdapter(this, R.layout.item_list, mDatas);
        mRecycleList.setAdapter(mAdapter);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        //  mRefreshLayout.setHeaderView(new SSSRefreshHeader(this));
        mRefreshLayout.setAutoLoadMore(true);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {//刷新
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                currPageNum = 1;
               BaseUtils.sendMessage(creatwQueryDocumentList());
            }//加载更多

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (pagingInfoBean != null) {
                    if ((pagingInfoBean.getTotalPageNum() - currPageNum) > 0) {
                        ++currPageNum;//有下一页的话
                      BaseUtils.sendMessage(creatwQueryDocumentList());
                    } else {
                        ToastUtils.showShort("已经没有更多的数据了");
                        mRefreshLayout.finishLoadmore();
                    }
                }
            }
        });
       BaseUtils.sendMessage(creatwQueryDocumentList());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    /**
     * 获取文档列表接口：method=”queryDocumentList”
     */
    private String creatwQueryDocumentList(){
        // BaseSendMsgBean sendMsgBean = createDefaultSendBean();
        BaseSendMsgBean sendMsgBean = BaseUtils.createDefaultSendBean();

        SendQueryDocumentListBean sendQueryDocumentListBean = new SendQueryDocumentListBean();
        sendQueryDocumentListBean.setNotification("REQUEST");
        sendQueryDocumentListBean.setUserCode(BaseUtils.getUserCode());//当前登录账号getUserCode()
        sendQueryDocumentListBean.setCurrPageNum(currPageNum);// 当前页数
        sendMsgBean.setContent(sendQueryDocumentListBean);
        sendMsgBean.setMethod(Const.METHER_QUERYDOCUMENTLIST);
        sendMsgBean.setRequestId(Const.METHER_QUERYDOCUMENTLIST + "");
        return JSON.toJSONString(sendMsgBean);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptMsg(MessageEvent event) {
        switch (event.getCode()) {
            case Const.METHER_QUERYDOCUMENTLIST_CODE://获取文档列表接口
                AcceptQueryDocumentListBean acceptQueryDocumentListBean =
                        JSON.parseObject((String) event.getDataContent(), AcceptQueryDocumentListBean.class);
                //CodeUtils.initCode(acceptQueryDocumentListBean.getCode());
              //  CodeUtils.initCode(DocumentListActivity.this,10003);
                // 获取 分页信息
                pagingInfoBean = (PagingInfoBean) acceptQueryDocumentListBean.getPagingInfo();
                if (currPageNum == 1) mDatas.clear();
                mDatas.addAll(acceptQueryDocumentListBean.getDocuments());
                mAdapter.notifyDataSetChanged();

                break;
            case Const.METHER_PUSH_QUERYDOCUMENTLIST_CODE://自动刷新文档列表接口
                currPageNum = 1;
                BaseUtils.sendMessage(creatwQueryDocumentList());
                break;
            case Const.METHER_QUERYDOCUMENTPREVIEWURL_CODE://获取文档在线预览路径接口：method=”queryDocumentPreviewUrl”
                AcceptQueryDocumentPreviewUrl acceptQueryDocumentPreviewUrl =
                        JSON.parseObject((String) event.getDataContent(), AcceptQueryDocumentPreviewUrl.class);
               // CodeUtils.initCode(DocumentListActivity.this,acceptQueryDocumentPreviewUrl.getCode());

                String srrr = acceptQueryDocumentPreviewUrl.getPreviewUrl();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(srrr));
                startActivity(intent);
               /* 如有要求则可以用webview 加载
               Intent intent = new Intent(DocumentListActivity.this, WebViewActivity.class);
                intent.putExtra("key", srrr);
                startActivity(intent);*/
                //http://192.168.2.196:8200/preview/previewPrivateFile?code=37c2475b17da447b9c99d815ce63867c
                break;
            default:
                if (mRefreshLayout != null) {
                    mRefreshLayout.finishRefreshing();
                    mRefreshLayout.finishLoadmore();
                }
                break;
        }
    }

    class DocAdapter extends CommonAdapter<DocumentBean> {
        public DocAdapter(Context context, int layoutId, List<DocumentBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, final DocumentBean item, int position) {
            String filename = item.getFileName();
            holder.setText(R.id.fileName, filename);
            holder.setText(R.id.fileDate, item.getCreateDate() + "");
            holder.setText(R.id.FileDescribetion, item.getDescribetion() + "");//描述item.getFileName()
            String subfilename = filename.substring(filename.lastIndexOf(".") + 1);
            DocumentFormatUtils.documentFormat(subfilename, holder);
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /**
                     *获取文档在线预览路径接口：method=”queryDocumentPreviewUrl”
                     */
                    BaseSendMsgBean sendMsgBean = BaseUtils.createDefaultSendBean();
                    SendQueryDocumentPreviewUrl sendQueryDocumentPreviewUrl = new SendQueryDocumentPreviewUrl();
                    sendQueryDocumentPreviewUrl.setNotification("REQUEST");
                    sendQueryDocumentPreviewUrl.setFileId(item.getFileId());
                    sendMsgBean.setContent(sendQueryDocumentPreviewUrl);
                    sendMsgBean.setMethod(Const.METHER_QUERYDOCUMENTPREVIEWURL);
                    sendMsgBean.setRequestId(Const.METHER_QUERYDOCUMENTPREVIEWURL + "");
                   BaseUtils.sendMessage(JSON.toJSONString(sendMsgBean));
                }
            });
        }
    }

    public void onBack(View view) {
        this.finish();
    }
}
