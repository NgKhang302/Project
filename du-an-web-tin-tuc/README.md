cd /home/nguyen-khang/Intenr/du-an-web-tin-tuc/backend
  mvn spring-boot:run
# SongNhip24 — Web Tin Tức

Web tin tức tập trung vào công nghệ, AI, việc làm và kiếm tiền online.

## Cấu trúc dự án

```
du-an-web-tin-tuc/
├── backend/     Java 17 + Spring Boot + PostgreSQL
└── frontend/    HTML + CSS + Vanilla JS
```

## Tính năng hiện có

### Public
- Trang chủ — danh sách bài đã published, lượt xem
- Trang chuyên mục — lọc bài theo category
- Trang chi tiết bài viết — tự động tăng lượt xem, hiển thị tags, SEO meta description
- Đăng ký nhận bản tin email

### Admin (yêu cầu đăng nhập)
- Đăng nhập bằng JWT
- CRUD bài viết — tạo, sửa, publish/draft, xóa
- CRUD chuyên mục (category)
- CRUD tags — gắn nhiều tag cho bài viết
- Upload ảnh bìa
- Xem lượt xem từng bài

## Công nghệ

| Phần | Stack |
|---|---|
| Backend | Java 17, Spring Boot, Spring Data JPA, PostgreSQL, JWT |
| Frontend | HTML, CSS, Vanilla JS |
| API Docs | Swagger UI tại `/swagger-ui.html` |

## Cách chạy

1. Tạo database PostgreSQL tên `songnhip24_db`
2. Chạy `schema.sql` để tạo bảng
3. Tạo file `.env` theo `.env.example`
4. Chạy backend: `./mvnw spring-boot:run` trong thư mục `backend/`
5. Mở `frontend/index.html` trên trình duyệt

## Mô hình kiếm tiền (roadmap)

- Google AdSense khi có traffic
- Booking bài viết PR / tài trợ
- Affiliate cho tool, khóa học, hosting
- Gói membership đọc bài chuyên sâu
