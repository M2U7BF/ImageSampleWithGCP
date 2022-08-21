package com.example.ImageSample;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.StorageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

//サブ処理
@Component
public class GoogleStorageClientAdapter {
    Storage storage;
    String bucketName;

    //@Valueはプロパティから設定を取得する

    public GoogleStorageClientAdapter(@Autowired Storage storage, @Value("${bucket}") String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    /**
     * ファイルを受け取り、アップロード
     *
     * @param file
     * @param prefixName
     * @return
     * @throws IOException
     */
    public Boolean upload(MultipartFile file, String prefixName) throws IOException {
        StorageObject object = new StorageObject();
        object.setName(file.getOriginalFilename());//元々のファイル名を設定

        InputStream targetStream = new ByteArrayInputStream(file.getBytes());

        storage.objects().insert(bucketName, object, new AbstractInputStreamContent(file.getOriginalFilename()) { //新しいオブジェクトとメタデータを保存します。
            @Override
            public long getLength() throws IOException {
                return file.getSize();
            }

            @Override
            public boolean retrySupported() {
                return false;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return targetStream;
            }
        }).execute();
        return true;
    }

    /**
     * ダウンロード
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public StorageObject download(String fileName) throws IOException {
        StorageObject object = storage.objects().get(bucketName, fileName).execute();

        File file = new File("./" + fileName); //保存するため、Fileオブジェクト生成
        FileOutputStream os = new FileOutputStream(file);

        storage.getRequestFactory()
                .buildGetRequest(new GenericUrl(object.getMediaLink()))
                .execute()
                .download(os);
        object.set("file", file);
        return object;
    }
}