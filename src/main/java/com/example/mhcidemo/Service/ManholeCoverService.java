package com.example.mhcidemo.Service;


import com.example.mhcidemo.domain.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ManholeCoverService {

    // 方法声明，用于处理图片上传和分析
    Result<List<String>> uploadAndAnalyzeImage(MultipartFile image, Long userId);
}

