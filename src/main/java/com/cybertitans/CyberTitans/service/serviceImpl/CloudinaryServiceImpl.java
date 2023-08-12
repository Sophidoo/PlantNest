package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cybertitans.CyberTitans.exception.Exception;
import com.cybertitans.CyberTitans.service.CloudinaryService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadFIle(MultipartFile image) {
        try{
            File uploadedFile = convertMultipartToFile(image);
            Map uploadedFileResult = cloudinary.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
            boolean isDeleted =uploadedFile.delete();
            if(isDeleted){
                System.out.println("File Successfully deleted");
            }else{
                System.out.println("File doesn't exist");
            }
            return uploadedFileResult.get("url").toString();
        }catch (IOException e){
            return new RuntimeException(e).getMessage();
        }
    }

    private File convertMultipartToFile(MultipartFile image) throws IOException {
        String file = image.getOriginalFilename();
        assert file != null;
        File convertFile = new File(file);
        FileOutputStream fileOutputStream = new FileOutputStream(convertFile);
        fileOutputStream.write(image.getBytes());
        fileOutputStream.close();
        return convertFile;
    }
}
