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
    public ResponseEntity<ArticleResponse> create(
            HttpServletRequest req,
            @RequestBody ArticleRequest request) { //json body > java object
        //Lấy username từ request (JwtFilter đã lưu)
        String username = (String) req.getAttribute("username");
        //  Gọi service tạo bài viết
        ArticleResponse articleResponse = service.create(request, username);
        // Tạo bài + wrap vào 200 OK + return
        return ResponseEntity.ok(service.create(request, username));
    }

    // PUT /api/admin/articles/{id} — cập nhật nội dung
    @PutMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> update(@PathVariable Integer id, // lấy id từ url
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
        return ResponseEntity.noContent().build(); //build là bước cúi hoàn thiện và tạo ra object responseEtity nếu k sài thì nó chỉ là bản nháp chưa có repEtity thật để return
    }

    // POST /api/admin/upload — upload ảnh bìa
    // Trả về đường dẫn để gán vào coverImageUrl khi tạo bài
    @PostMapping("/upload")                     // láy từ from data  kiểu dữ liệu cho file
    public ResponseEntity<Map<String, String>> upload(@RequestParam MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }
        // lấy time  hiện tại mily s      tên file từ fontend
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();   // dùng time v để tránh trùng tên file
        File uploadDir = new File("uploads");
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {  // tồn tại && tạo foder uploaDir
            throw new IllegalStateException("Could not create uploads directory");
        }
        // lưu file vào server
        file.transferTo(new File(uploadDir.getAbsolutePath() + "/" + filename));

        // Trả về URL để frontend điền vào coverImageUrl
        String url = "/uploads/" + filename;
        return ResponseEntity.ok(Map.of("url", url)); //Tạo Map với 1 entry:{ "url": "/uploads/1715770800000_avatar.png" }
    }                                                      // Map.of(key, value)
}                                                           // key   = "url"
                                                           // value = "/uploads/1715770800000_avatar.png"

