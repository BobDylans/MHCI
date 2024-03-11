package com.example.mhcidemo.Service.ServiceImpl;


import com.example.mhcidemo.Mapper.ManholeCoverMapper;
import com.example.mhcidemo.Service.ManholeCoverService;
import com.example.mhcidemo.domain.ManholeCover;
import com.example.mhcidemo.domain.Result;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ManholeCoverServiceImpl implements ManholeCoverService {

    @Resource
    private  ManholeCoverMapper manholeCoverMapper;
    private final String uploadDir = "D:\\staticResource\\ManholeCover";
    private final String analysisServiceUrl = "http://localhost:5000/api/upload"; // 图片分析服务URL
    @Resource
    private  RestTemplate restTemplate ;

    public Result<List<String>> uploadAndAnalyzeImage(MultipartFile image, Long userId) {
        try {
            // 保存图片
            String imagePath = saveImage(image);

            // 分析图片
            List<String> analysisResults = analyzeImage(imagePath);

            // 保存井盖信息到数据库
            ManholeCover manholeCover = new ManholeCover();
            // 设置manholeCover的属性...
            manholeCoverMapper.insert(manholeCover);

            return Result.success(analysisResults);
        } catch (Exception e) {
            // 异常处理
            return Result.error(500, "图片上传或分析失败: " + e.getMessage());
        }
    }

    private String saveImage(MultipartFile image) throws IOException {
        // 确保上传目录存在
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            boolean wasDirectoryMade = directory.mkdirs();
            if (!wasDirectoryMade) {
                throw new IOException("Failed to create directory for image storage.");
            }
        }

        // 构建图片保存的完整路径
        String originalFilename = image.getOriginalFilename();
        String filename = UUID.randomUUID() + (originalFilename != null ? "_" + originalFilename : "");
        Path path = Paths.get(uploadDir + File.separator + filename);

        // 保存图片
        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // 返回图片的路径
        return path.toString();
    }


    private List<String> analyzeImage(String imagePath) {
        FileSystemResource fileResource = new FileSystemResource(imagePath);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<List<String>> response = restTemplate.exchange(
                analysisServiceUrl,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<String>>() {
                });

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to analyze image");
        }

        return response.getBody();
    }
}


