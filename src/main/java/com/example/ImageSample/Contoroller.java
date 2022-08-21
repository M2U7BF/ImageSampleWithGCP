package com.example.ImageSample;

import com.google.api.services.storage.model.StorageObject;
import com.google.common.io.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
public class Contoroller {
    @Autowired
    GoogleStorageClientAdapter googleStorageClientAdapter;

    //テスト用
    @Autowired
    ImageDataRepository imageDataRepository;

    /**
     * アップロード
     *
     * @param files
     * @return
     */
    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Boolean uploadFile(@RequestPart(value = "file", required = true) MultipartFile files)  {
        try {
            ImageData imageData = new ImageData();
            imageData.setName(files.getOriginalFilename());
            System.out.println("getName : "+files.getOriginalFilename());
            imageDataRepository.save(imageData);

            return  googleStorageClientAdapter.upload(files, "prefix");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ダウンロード
     *
     * @param request
     * @param path
     * @param response
     * @return
     */
    @RequestMapping(name = "file-download", path = "download",
            method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> fileDownload(HttpServletRequest request,
                                                          @RequestParam(value = "file", required = false) String path,
                                                          HttpServletResponse response
    ) {
        System.out.println(path);
        try {
            StorageObject object = googleStorageClientAdapter.download(path);

            byte[] res = Files.toByteArray((File) object.get("file"));
            ByteArrayResource resource = new ByteArrayResource(res);

            return ResponseEntity.ok()
                    .contentLength(res.length)
                    .header("Content-type", "application/octet-stream")
                    .header("Content-disposition", "attachment; filename=\"" + path + "\"")
                    .body(resource);
        }catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No such file or directory");
        }
    }
}
