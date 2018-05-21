package com.yw.platform.yhtext.utils;

import com.yw.platform.R;

import lzhs.com.library.wedgit.comm_adapter.absrecyclerview.base.ViewHolder;

/**
 * 文档格式
 */

public class DocumentFormatUtils {
     public  static void documentFormat(String subfilename, ViewHolder holder) {
        switch (subfilename) {
            case ".xls":
            case ".xlsx":
            case ".xlsb":
                holder.setImageResource(R.id.FileImage, R.drawable.excel);
                break;
            case "bmp":
            case "gif":
            case "jpg":
            case "pic":
            case "png":
            case "tif":
                holder.setImageResource(R.id.FileImage, R.drawable.pic);
                break;
            case "ppt":
            case "pptx":
                holder.setImageResource(R.id.FileImage, R.drawable.ppt);
                break;
            case "txt":
                holder.setImageResource(R.id.FileImage, R.drawable.txt);
                break;
            case "doc":
            case "docx":
                holder.setImageResource(R.id.FileImage, R.drawable.word);
                break;
            default:
                holder.setImageResource(R.id.FileImage, R.drawable.unknow);

                break;
        }
    }
}
