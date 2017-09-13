package com.haoyu.app.adapter;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoyu.app.basehelper.BaseArrayRecyclerAdapter;
import com.haoyu.app.entity.MFileInfo;
import com.haoyu.app.lingnan.student.R;
import com.haoyu.app.utils.Common;
import com.haoyu.app.utils.MediaFile;

import java.util.List;

/**
 * Created by acer1 on 2017/2/6.
 * 研讨文件列表
 */
public class DiscussFileAdapter extends BaseArrayRecyclerAdapter<MFileInfo> {
    private Activity mContext;

    public DiscussFileAdapter(Activity context, List<MFileInfo> mDatas) {
        super(mDatas);
        this.mContext = context;

    }

    @Override
    public void onBindHoder(RecyclerHolder holder, final MFileInfo entity, int position) {
        ImageView iv_type = holder.obtainView(R.id.resourcesType);
        TextView resourcesName = holder.obtainView(R.id.resourcesName);
        TextView resourcesSize = holder.obtainView(R.id.resourcesSize);
        resourcesName.setText(entity.getFileName());
        if (MediaFile.isImageFileType(entity.getUrl())) {
            iv_type.setImageResource(R.drawable.resources_jpg);
        } else if (MediaFile.isPdfFileType(entity.getUrl())) {
            iv_type.setImageResource(R.drawable.resources_pdf);
        } else if (MediaFile.isOfficeFileType(entity.getUrl())) {
            String type = Common.getFileType(entity.getUrl());
            if (type.equals(".PPT") || type.equals(".PPTX")) {
                iv_type.setImageResource(R.drawable.resources_ppt);
            } else if (type.equals(".DOC") || type.equals("DOCX")) {
                iv_type.setImageResource(R.drawable.resources_doc);
            } else if (type.equals(".XLS") || type.equals(".XLSX")) {
                iv_type.setImageResource(R.drawable.resources_xls);
            } else {
                iv_type.setImageResource(R.drawable.resources_unknown);
            }
        } else if (Common.getFileType(entity.getUrl()).equals(".TXT")) {
            iv_type.setImageResource(R.drawable.resources_txt);
        } else {
            iv_type.setImageResource(R.drawable.resources_unknown);
        }
        resourcesSize.setText(Common.FormetFileSize(entity.getFileSize()));
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.resources_item;
    }

    public interface OpenResourceCallBack {
        void open(MFileInfo mFileInfo);

    }
}
