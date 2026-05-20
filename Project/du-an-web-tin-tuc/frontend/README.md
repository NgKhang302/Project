# Frontend — SongNhip24

Giao diện web tin tức SongNhip24 viết bằng HTML + CSS + Vanilla JS thuần.

## Cấu trúc

```
frontend/
├── index.html          Trang chủ — tin mới nhất + newsletter
├── category.html       Trang chuyên mục — lọc bài theo category
├── article.html        Trang chi tiết bài viết
├── login.html          Đăng nhập admin
├── admin/
│   ├── index.html      Danh sách bài viết (admin)
│   ├── form.html       Tạo / chỉnh sửa bài viết
│   ├── categories.html Quản lý chuyên mục
│   └── tags.html       Quản lý tags
├── css/
│   └── style.css       Toàn bộ CSS
└── js/
    └── api.js          Tất cả hàm gọi API
```

## Cách dùng

Không cần build, mở thẳng file HTML trên trình duyệt.  
Yêu cầu backend đang chạy tại `http://localhost:8080`.

## api.js

Tất cả hàm gọi API được tập trung trong `js/api.js`:

| Hàm | Mô tả |
|---|---|
| `getPublishedArticles()` | Lấy bài đã published |
| `getArticleBySlug(slug)` | Chi tiết bài viết |
| `getPublicCategories()` | Danh sách chuyên mục |
| `getArticlesByCategory(slug)` | Bài theo chuyên mục |
| `getPublicTags()` | Danh sách tags |
| `subscribeNewsletter(email)` | Đăng ký bản tin |
| `login(username, password)` | Đăng nhập admin |
| `adminGetArticles()` | Lấy tất cả bài (admin) |
| `adminCreateArticle(data)` | Tạo bài mới |
| `adminUpdateArticle(id, data)` | Cập nhật bài |
| `adminPublish(id)` | Publish bài |
| `adminUnpublish(id)` | Đưa về draft |
| `adminDeleteArticle(id)` | Xóa bài |
| `adminUpload(file)` | Upload ảnh bìa |
| `adminGetCategories()` | Danh sách category (admin) |
| `adminCreateCategory(data)` | Tạo category |
| `adminDeleteCategory(id)` | Xóa category |
| `adminGetTags()` | Danh sách tag (admin) |
| `adminCreateTag(data)` | Tạo tag |
| `adminDeleteTag(id)` | Xóa tag |
