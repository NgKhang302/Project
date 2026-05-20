# Backend — SongNhip24

Spring Boot REST API cho web tin tức SongNhip24.

## Stack

- Java 17
- Spring Boot 4
- Spring Data JPA
- PostgreSQL
- JWT (jjwt 0.12.7)
- Swagger UI (springdoc-openapi)

## Cấu trúc package

```
com.songnhip24.news/
├── config/        Cấu hình CORS, JWT filter, static files
├── controller/    Nhận HTTP request
├── service/       Xử lý logic nghiệp vụ
├── repository/    Truy vấn database
├── model/         Entity ánh xạ với bảng DB
├── dto/           Object truyền dữ liệu vào/ra API
├── security/      JWT filter và service
└── exception/     Xử lý lỗi toàn cục
```

## Database

Chạy `schema.sql` để tạo toàn bộ bảng:

| Bảng | Mô tả |
|---|---|
| `users` | Tài khoản admin |
| `categories` | Chuyên mục (id, name, slug, description) |
| `tags` | Nhãn bài viết (id, name, slug) |
| `articles` | Bài viết — trạng thái DRAFT / PUBLISHED, có `meta_description` cho SEO |
| `article_tags` | Quan hệ nhiều-nhiều article ↔ tag, cascade xóa theo article/tag |
| `article_views` | Lượt xem từng bài, cascade xóa theo article |
| `newsletter_subscribers` | Email đăng ký bản tin |

**Quan hệ:**
- `articles.category_id` → `categories.id`
- `articles.created_by` → `users.id`
- `article_tags.article_id` → `articles.id` (ON DELETE CASCADE)
- `article_tags.tag_id` → `tags.id` (ON DELETE CASCADE)
- `article_views.article_id` → `articles.id` (ON DELETE CASCADE)

## Cấu hình môi trường

Tạo file `.env` theo `.env.example`:

```bash
DB_URL=jdbc:postgresql://localhost:5432/songnhip24_db
DB_USERNAME=postgres
DB_PASSWORD=mat_khau_cua_ban
```

## Chạy

```bash
./mvnw spring-boot:run
```

Backend chạy tại `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

## API chính

### Public (không cần token)
| Method | URL | Mô tả |
|---|---|---|
| GET | `/api/public/articles` | Danh sách bài đã published |
| GET | `/api/public/articles/{slug}` | Chi tiết bài (tăng lượt xem) |
| GET | `/api/public/categories` | Danh sách chuyên mục |
| GET | `/api/public/categories/{slug}/articles` | Bài viết theo chuyên mục |
| GET | `/api/public/tags` | Danh sách tags |
| POST | `/api/public/newsletter` | Đăng ký nhận bản tin |

### Admin (cần Bearer token)
| Method | URL | Mô tả |
|---|---|---|
| POST | `/api/auth/login` | Đăng nhập, trả về JWT |
| GET | `/api/admin/articles` | Tất cả bài (cả draft) |
| POST | `/api/admin/articles` | Tạo bài mới |
| PUT | `/api/admin/articles/{id}` | Cập nhật bài |
| PUT | `/api/admin/articles/{id}/publish` | Publish bài |
| PUT | `/api/admin/articles/{id}/unpublish` | Đưa về draft |
| DELETE | `/api/admin/articles/{id}` | Xóa bài |
| POST | `/api/admin/upload` | Upload ảnh bìa |
| GET/POST/DELETE | `/api/admin/categories` | Quản lý chuyên mục |
| GET/POST/DELETE | `/api/admin/tags` | Quản lý tags |
