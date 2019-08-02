package cn.kingtous.diplomagenerator.Tools;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;

import cn.kingtous.diplomagenerator.R;

/**
 * Created by panyi on 16/10/23.
 */
public class FileUtil {
    public static boolean checkFileExist(final String path) {
        if (TextUtils.isEmpty(path))
            return false;

        File file = new File(path);
        return file.exists();
    }

    // 获取文件扩展名
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    /**
     * 将图片文件加入到相册
     *
     * @param context
     * @param dstPath
     */
    public static void ablumUpdate(final Context context, final String dstPath) {
        if (TextUtils.isEmpty(dstPath) || context == null)
            return;

        File file = new File(dstPath);
        //System.out.println("panyi  file.length() = "+file.length());
        if (!file.exists() || file.length() == 0) {//文件若不存在  则不操作
            return;
        }

        ContentValues values = new ContentValues(2);
        String extensionName = getExtensionName(dstPath);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + (TextUtils.isEmpty(extensionName) ? "jpeg" : extensionName));
        values.put(MediaStore.Images.Media.DATA, dstPath);
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static void sendFiles(Context context,String savePath){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        // 获得拓展名
        String type=getFormatNameOfFile(savePath);
        File file=new File(savePath);
        Uri contentUri= FileProvider.getUriForFile(context,context.getPackageName()+".fileprovider",file);
        intent.putExtra(Intent.EXTRA_STREAM,contentUri);
        intent.setType(type);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e){
            new QMUITipDialog.Builder(context).setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO).setTipWord(context.getString(R.string.no_such_format)).create().show();
        }
    }

    public static void processFile(Context context,String savePath){
        Intent intent=new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        // 获得拓展名
        String type=getFormatNameOfFile(savePath);
        File file=new File(savePath);
        Uri contentUri= FileProvider.getUriForFile(context,context.getPackageName()+".fileprovider",file);
        intent.setDataAndType(contentUri,type);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e){
            new QMUITipDialog.Builder(context).setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO).setTipWord("没有打开此格式的应用").create().show();
        }
    }

    public static String getFormatNameOfFile(String filePath){
        String type="*/*";
        String[] tmp=filePath.split("\\.");
        if (tmp.length!=1){
            for (String [] pair:MIME_MapTable){
                if (pair[0].equals(tmp[tmp.length-1])) {
                    type=pair[1];
                    break;
                }
            }
        }
        return type;
    }

    public static final String[][] MIME_MapTable={
            //{后缀名，MIME类型}
            {"3gp",    "video/3gpp"},
            {"apk",    "application/vnd.android.package-archive"},
            {"asf",    "video/x-ms-asf"},
            {"avi",    "video/x-msvideo"},
            {"bin",    "application/octet-stream"},
            {"bmp",    "image/bmp"},
            {"c",  "text/plain"},
            {"class",  "application/octet-stream"},
            {"conf",   "text/plain"},
            {"cpp",    "text/plain"},
            {"doc",    "application/msword"},
            {"docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {"xls",    "application/vnd.ms-excel"},
            {"xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {"exe",    "application/octet-stream"},
            {"gif",    "image/gif"},
            {"gtar",   "application/x-gtar"},
            {"gz", "application/x-gzip"},
            {"h",  "text/plain"},
            {"htm",    "text/html"},
            {"html",   "text/html"},
            {"jar",    "application/java-archive"},
            {"java",   "text/plain"},
            {"jpeg",   "image/jpeg"},
            {"jpg",    "image/jpeg"},
            {"js", "application/x-javascript"},
            {"log",    "text/plain"},
            {"m3u",    "audio/x-mpegurl"},
            {"m4a",    "audio/mp4a-latm"},
            {"m4b",    "audio/mp4a-latm"},
            {"m4p",    "audio/mp4a-latm"},
            {"m4u",    "video/vnd.mpegurl"},
            {"m4v",    "video/x-m4v"},
            {"mov",    "video/quicktime"},
            {"mp2",    "audio/x-mpeg"},
            {"mp3",    "audio/x-mpeg"},
            {"mp4",    "video/mp4"},
            {"mpc",    "application/vnd.mpohun.certificate"},
            {"mpe",    "video/mpeg"},
            {"mpeg",   "video/mpeg"},
            {"mpg",    "video/mpeg"},
            {"mpg4",   "video/mp4"},
            {"mpga",   "audio/mpeg"},
            {"msg",    "application/vnd.ms-outlook"},
            {"ogg",    "audio/ogg"},
            {"pdf",    "application/pdf"},
            {"png",    "image/png"},
            {"pps",    "application/vnd.ms-powerpoint"},
            {"ppt",    "application/vnd.ms-powerpoint"},
            {"pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {"prop",   "text/plain"},
            {"rc", "text/plain"},
            {"rmvb",   "audio/x-pn-realaudio"},
            {"rtf",    "application/rtf"},
            {"sh", "text/plain"},
            {"tar",    "application/x-tar"},
            {"tgz",    "application/x-compressed"},
            {"txt",    "text/plain"},
            {"wav",    "audio/x-wav"},
            {"wma",    "audio/x-ms-wma"},
            {"wmv",    "audio/x-ms-wmv"},
            {"wps",    "application/vnd.ms-works"},
            {"xml",    "text/plain"},
            {"z",  "application/x-compress"},
            {"zip",    "application/x-zip-compressed"},
            {"",        "*/*"}
    };

}//end class