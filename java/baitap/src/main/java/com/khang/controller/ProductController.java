package com.khang.controller;

import com.khang.dto.ProductCreateRequest;
import com.khang.model.Product;
import com.khang.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;  // Bearer
import jakarta.servlet.http.HttpServletRequest;  // lấy dữ liệu từ req
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; //upload file ảnh

import java.io.File;  //lưu file ảnh
import java.util.List;

@RestController // trả json
@RequestMapping("/api/products")   //báo url
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service){
        this.service = service;
    }

    // CREATE (có auth)
    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public Product create(HttpServletRequest request, @RequestBody ProductCreateRequest productRequest){
        String username = (String) request.getAttribute("username"); //username mà JWT fillter nhéc vào req
        return service.create(productRequest, username);
    }

    // GET ALL
    @GetMapping
    public List<Product> getAll(){
        return service.getAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Product get(@PathVariable Integer id){
        return service.getById(id);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        service.delete(id);
    }

    // SEARCH
    @GetMapping("/search")
    public List<Product> search(@RequestParam String name){
        return service.search(name);
    }

    // UPLOAD FILE
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {  //isEmpty rỗng
            throw new IllegalArgumentException("File is required");
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename(); //đổi tên file thành timestamp + tên gốc để tránh bị trùng
        String path = "uploads/" + filename;
        File uploadDir = new File("uploads");
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {  //Nếu chưa có thư mục uploads và cũng không tạo được thì báo lỗi
            throw new IllegalStateException("Could not create uploads directory");
        }

        file.transferTo(new File(path));  //Copy file từ request → lưu vào server

        return path;
    }
}
//Đảm bảo thư mục uploads tồn tại, nếu chưa có thì tạo nó, nếu tạo thất bại thì báo lỗi