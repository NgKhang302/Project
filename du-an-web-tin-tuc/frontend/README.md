# Frontend — SongNhip24

Giao diện web tin tức SongNhip24 viết bằng React (Vite) + React Router.

## Cấu trúc

```
frontend/
├── index.html              Entry HTML (Vite)
├── vite.config.js          Cấu hình Vite, proxy /api và /uploads sang backend khi dev
├── src/
│   ├── main.jsx             Bootstrap React + Router
│   ├── App.jsx               Khai báo route
│   ├── api.js                 Tất cả hàm gọi API
│   ├── style.css              Toàn bộ CSS
│   ├── components/
│   │   ├── Navbar.jsx         Nav trang công khai
│   │   ├── AdminNav.jsx       Nav khu vực admin (có nút đăng xuất)
│   │   ├── AdminSidebar.jsx   Sidebar admin
│   │   ├── ArticleCard.jsx    Card bài viết dùng chung
│   │   └── RequireAuth.jsx    Route guard kiểm tra session cookie
│   └── pages/
│       ├── Home.jsx           Trang chủ — tin mới nhất + newsletter
│       ├── Category.jsx       Trang chuyên mục
│       ├── Article.jsx        Chi tiết bài viết
│       ├── Login.jsx          Đăng nhập admin
│       └── admin/
│           ├── AdminArticles.jsx    Danh sách bài viết
│           ├── AdminForm.jsx        Tạo / chỉnh sửa bài viết
│           ├── AdminCategories.jsx  Quản lý chuyên mục
│           └── AdminTags.jsx        Quản lý tags
```

## Route

| Path | Trang |
|---|---|
| `/` | Trang chủ |
| `/category/:slug` | Chuyên mục |
| `/article/:slug` | Chi tiết bài viết |
| `/login` | Đăng nhập admin |
| `/admin` | Danh sách bài viết (cần đăng nhập) |
| `/admin/articles/new` | Tạo bài mới (cần đăng nhập) |
| `/admin/articles/:id/edit` | Sửa bài (cần đăng nhập) |
| `/admin/categories` | Quản lý category (cần đăng nhập) |
| `/admin/tags` | Quản lý tag (cần đăng nhập) |

## Chạy dev

```
npm install
npm run dev
```

Yêu cầu backend đang chạy tại `http://localhost:8080`; Vite dev server proxy sẵn `/api` và `/uploads` sang đó.

## Build production

```
npm run build
```

Output ở `dist/`, được Dockerfile build và serve qua nginx (xem `Dockerfile`, `nginx.conf`).

## api.js

Tất cả hàm gọi API được tập trung trong `src/api.js`:

| Hàm | Mô tả |
|---|---|
| `getPublishedArticles()` | Lấy bài đã published |
| `getArticleBySlug(slug)` | Chi tiết bài viết |
| `getPublicCategories()` | Danh sách chuyên mục |
| `getArticlesByCategory(slug)` | Bài theo chuyên mục |
| `getPublicTags()` | Danh sách tags |
| `subscribeNewsletter(email)` | Đăng ký bản tin |
| `checkAuth()` | Kiểm tra session hiện tại |
| `login(username, password)` | Đăng nhập admin |
| `logout()` | Đăng xuất |
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
