package com.ndp.flazzbca.Paxstore;

import android.content.Context;

import java.util.LinkedHashSet;
import java.util.Set;

//
//import android.content.Context;
//
//import java.util.LinkedHashSet;
//import java.util.Set;
//
public class DownloadManager {
    //    private List<AbstractDocument> documentList = new ArrayList<>();
    private Set<DocumentBase> documentList = new LinkedHashSet<>();

    private String appKey;
    private String appSecret;
    private String sn;
    private String filePath;
    private static DownloadManager instance;

    public static synchronized DownloadManager getInstance() {
        if (instance == null) {
            instance = new DownloadManager();
        }
        return instance;
    }

    public DownloadManager addDocument(DocumentBase document) {
        documentList.add(document);
        return instance;
    }

    public void updateData(Context context) {
        for (DocumentBase document : documentList) {
            if (document.parse() == 0) {
                document.save(context);
            }
            document.delete();
        }
    }


    private void deleteAll() {
        for (DocumentBase document : documentList) {
            document.delete();
        }
    }

//    public void remove(AbstractDocument document) {
//        documentList.remove(document);
//    }
//
//    public void clear() {
//        documentList.clear();
//    }
//
//    public String getAppKey() {
//        return appKey;
//    }
//
//    public void setAppKey(String appKey) {
//        this.appKey = appKey;
//    }
//
//    public String getAppSecret() {
//        return appSecret;
//    }
//
//    public void setAppSecret(String appSecret) {
//        this.appSecret = appSecret;
//    }
//
//    public String getSn() {
//        return sn;
//    }
//
//    public void setSn(String sn) {
//        this.sn = sn;
//    }
//
//    public boolean hasUpdateParam() {
//        for (DocumentBase document : documentList) {
//            if (document.isExit()) {
//                return true;
//            }
//        }
//        return false;
//    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
//
