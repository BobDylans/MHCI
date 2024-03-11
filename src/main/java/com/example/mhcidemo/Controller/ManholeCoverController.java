package com.example.mhcidemo.Controller;

import com.example.mhcidemo.Service.ServiceImpl.ManholeCoverServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.mhcidemo.domain.Result; // 确保这是正确的导入语句

import java.util.List;

@RestController
@RequestMapping("/api/manholeCovers")
public class ManholeCoverController {

    @Autowired
    private ManholeCoverServiceImpl manholeCoverService;

    @PostMapping("/upload")
    public ResponseEntity<Result<List<String>>> uploadAndAnalyze(@RequestParam("image") MultipartFile image, @RequestParam("userId") Long userId) {
        // 调用服务层处理图片上传和分析
        Result<List<String>> result = manholeCoverService.uploadAndAnalyzeImage(image, userId);

        // 根据结果返回响应
        if (result.getStatus() == 200) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(result.getStatus()).body(result);
        }
    }
}
