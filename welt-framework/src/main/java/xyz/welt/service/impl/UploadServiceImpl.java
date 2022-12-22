package xyz.welt.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.welt.bean.ResponseResult;
import xyz.welt.enums.AppHttpCodeEnum;
import xyz.welt.exception.SystemException;
import xyz.welt.service.UploadService;
import xyz.welt.utils.PathUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class UploadServiceImpl implements UploadService {

    // private String accessKey;
    // private String secretKey;
    // private String bucket;

    private String accessKeyId;
    private String accessKeySecret;
    private String bucket;

    @Override
    public ResponseResult uploadImg(MultipartFile img) throws IOException{
        //判断文件类型
        //获取原石文件名
        String fileName = img.getOriginalFilename();
        //对原始文件名进行判断
        if (!(fileName.endsWith(".jpg")||fileName.endsWith(".png"))){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        //如果判断通过上传文件到OSS
        String filePath = PathUtils.generateFilePath(fileName);
        // String url = uploadOSS(img,filePath);
        String url = uploadFile(filePath,img.getInputStream());
        return ResponseResult.okResult(url);
    }

    private String uploadFile(String filePath, InputStream inputStream) {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = "oss-cn-shenzhen.aliyuncs.com";

        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        // String accessKeyId = "weqeq";
        // String accessKeySecret = "wqeqrqrqwe";
        // String bucketName = "sg-blog-oss";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 创建PutObjectRequest对象。
        // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, filePath, inputStream);


        // 上传字符串。
        ossClient.putObject(putObjectRequest);

        // 关闭OSSClient。
        ossClient.shutdown();
        return "https://"+bucket+"."+endpoint+"/"+filePath;
    }

    // private String uploadOSS(MultipartFile imgFile,String filePath){
    //     //构造一个带指定 Region 对象的配置类
    //     Configuration cfg = new Configuration(Region.autoRegion());
    //     cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
    //
    //     UploadManager uploadManager = new UploadManager(cfg);
    //     String key = filePath;
    //     try {
    //
    //         InputStream inputStream = imgFile.getInputStream();
    //
    //         Auth auth = Auth.create(accessKey, secretKey);
    //         String upToken = auth.uploadToken(bucket);
    //         try {
    //             Response response = uploadManager.put(inputStream,key,upToken,null, null);
    //             //解析上传成功的结果
    //             DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
    //             System.out.println(putRet.key);
    //             System.out.println(putRet.hash);
    //
    //             return "http://rl68ym0il.hn-bkt.clouddn.com/"+key;
    //         } catch (QiniuException ex) {
    //             Response r = ex.response;
    //             System.err.println(r.toString());
    //             try {
    //                 System.err.println(r.bodyString());
    //             } catch (QiniuException ex2) {
    //                 //ignore
    //             }
    //         }
    //     } catch (FileNotFoundException ex) {
    //         //ignore
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //
    //     return "www";
    // }


}
