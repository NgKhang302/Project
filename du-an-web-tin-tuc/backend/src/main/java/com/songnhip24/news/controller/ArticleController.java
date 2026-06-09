package com.songnhip24.news.controller;

import com.songnhip24.news.dto.ArticleRequest;
import com.songnhip24.news.dto.ArticleResponse;
import com.songnhip24.news.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    // GET /api/admin/articles — toàn bộ bài (draft + published)
    @GetMapping("/articles")
    public List<ArticleResponse> getAll() {
        return service.getAll();
    }

    // GET /api/admin/articles/{id}
    @GetMapping("/articles/{id}")
    public ArticleResponse getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    // POST /api/admin/articles — tạo bài mới (mặc định DRAFT)
    @PostMapping("/articles")
    public ResponseEntity<ArticleResponse> create(HttpServletRequest req, //lấy username từ JwtFilter
                                                  @RequestBody ArticleRequest request) {
        String username = (String) req.getAttribute("username");
        return ResponseEntity.ok(service.create(request, username));
    }

    // PUT /api/admin/articles/{id} — cập nhật nội dung
    @PutMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> update(@PathVariable Integer id,
                                                  @RequestBody ArticleRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    // PUT /api/admin/articles/{id}/publish — xuất bản bài
    @PutMapping("/articles/{id}/publish")
    public ResponseEntity<ArticleResponse> publish(@PathVariable Integer id) {
        return ResponseEntity.ok(service.publish(id));
    }

    // PUT /api/admin/articles/{id}/unpublish — đưa về draft
    @PutMapping("/articles/{id}/unpublish")
    public ResponseEntity<ArticleResponse> unpublish(@PathVariable Integer id) {
        return ResponseEntity.ok(service.unpublish(id));
    }

    // DELETE /api/admin/articles/{id}
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of("jpg", "jpeg", "png", "webp", "gif");
    // POST /api/admin/upload — upload ảnh bìa
    // Trả về đường dẫn để gán vào coverImageUrl khi tạo bài
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload(@RequestParam MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        String original = file.getOriginalFilename();
        if (original == null || !original.contains(".")) {
            throw new IllegalArgumentException("Invalid file name");
        }                                                 //cắt đui file ra +1 là bỏ dấu chấm lấy đui
        String ext = original.substring(original.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED_IMAGE_TYPES.contains(ext)) {
            throw new IllegalArgumentException("Only image files are allowed (jpg, jpeg, png, webp, gif)");
        }                                                    //đui file
        String filename = System.currentTimeMillis() + "." + ext;  //lấy time làm thành tên file để tránh trùng
        File uploadDir = new File("uploads");//tạo object cho biết đường dẫn
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {//uploadDir.mkdirs dùng đường dẫn đó để tạo thật 
            throw new IllegalStateException("Could not create uploads directory");
        }
               // lưu vào đường dẫn
        file.transferTo(new File(uploadDir.getAbsolutePath() + "/" + filename));

        // Trả về URL để frontend điền vào coverImageUrl
        String url = "/uploads/" + filename;
        return ResponseEntity.ok(Map.of("url", url));  //URL để frontend truy cập file vừa upload
    }
}
