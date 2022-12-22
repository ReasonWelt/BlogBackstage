package xyz.welt.service;

import org.springframework.web.multipart.MultipartFile;
import xyz.welt.bean.ResponseResult;

import java.io.IOException;

public interface UploadService {
    ResponseResult uploadImg(MultipartFile img) throws IOException;
}
