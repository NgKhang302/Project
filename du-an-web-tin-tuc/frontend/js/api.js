const BASE = 'http://localhost:8080';  //Tất cả URL API đều bắt đầu từ BASE

// Lấy token đang lưu trong localStorage  kho lưu data của browser
function getToken() {
  return localStorage.getItem('token');
}

// Header có Bearer token
function authHeaders() {
  return {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + getToken()
  };
}

// Nếu Chua login mà vào trans admin thì đẩy về login
function requireAuth() {
  if (!getToken()) {
    window.location.href = '../login.html';
  }
}

// ── AUTH ──────────────────────────────────────────
async function login(username, password) {
  const res = await fetch(`${BASE}/api/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });
  if (!res.ok) throw new Error('Sai tài khoản hoặc mật khẩu');
  return res.json(); // { token }
}

// ── PUBLIC ────────────────────────────────────────
async function getPublishedArticles() {
  const res = await fetch(`${BASE}/api/public/articles`);
  return res.json();
}

async function getArticleBySlug(slug) {
  const res = await fetch(`${BASE}/api/public/articles/${slug}`);
  if (!res.ok) throw new Error('Không tìm thấy bài viết');
  return res.json();
}

async function getPublicCategories() {
  const res = await fetch(`${BASE}/api/public/categories`);
  return res.json();
}

async function getArticlesByCategory(slug) {
  const res = await fetch(`${BASE}/api/public/categories/${slug}/articles`);
  return res.json();
}

async function subscribeNewsletter(email) {
  const res = await fetch(`${BASE}/api/public/newsletter`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email })
  });
  const data = await res.json();
  if (!res.ok) throw new Error(data.error || 'Đăng ký thất bại');
  return data;
}

// ── ADMIN ARTICLES ────────────────────────────────
async function adminGetArticles() {
  const res = await fetch(`${BASE}/api/admin/articles`, {
    headers: authHeaders()
  });
  if (res.status === 401 || res.status === 403) {
    localStorage.removeItem('token');
    window.location.href = '../login.html';
    return [];
  }
  if (!res.ok) throw new Error('Lỗi tải danh sách bài viết');
  const data = await res.json();
  return Array.isArray(data) ? data : [];
}

async function adminGetArticle(id) {
  const res = await fetch(`${BASE}/api/admin/articles/${id}`, {
    headers: authHeaders()
  });
  return res.json();
}

async function adminCreateArticle(data) {
  const res = await fetch(`${BASE}/api/admin/articles`, {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify(data)
  });
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.error || 'Lỗi tạo bài');
  }
  return res.json();
}

async function adminUpdateArticle(id, data) {
  const res = await fetch(`${BASE}/api/admin/articles/${id}`, {
    method: 'PUT',
    headers: authHeaders(),
    body: JSON.stringify(data)
  });
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.error || 'Lỗi cập nhật bài');
  }
  return res.json();
}

async function adminPublish(id) {
  const res = await fetch(`${BASE}/api/admin/articles/${id}/publish`, {
    method: 'PUT',
    headers: authHeaders()
  });
  if (!res.ok) throw new Error('Lỗi publish');
  return res.json();
}

async function adminUnpublish(id) {
  const res = await fetch(`${BASE}/api/admin/articles/${id}/unpublish`, {
    method: 'PUT',
    headers: authHeaders()
  });
  if (!res.ok) throw new Error('Lỗi unpublish');
  return res.json();
}

async function adminDeleteArticle(id) {
  const res = await fetch(`${BASE}/api/admin/articles/${id}`, {
    method: 'DELETE',
    headers: authHeaders()
  });
  if (!res.ok) throw new Error('Lỗi xoá bài');
}

// ── UPLOAD ────────────────────────────────────────
async function adminUpload(file) {
  const form = new FormData();
  form.append('file', file);
  const res = await fetch(`${BASE}/api/admin/upload`, {
    method: 'POST',
    headers: { 'Authorization': 'Bearer ' + getToken() },
    body: form
  });
  if (!res.ok) throw new Error('Upload thất bại');
  return res.json(); // { url }
}

// ── ADMIN CATEGORIES ─────────────────────────────
async function adminGetCategories() {
  const res = await fetch(`${BASE}/api/admin/categories`, {
    headers: authHeaders()
  });
  return res.json();
}

async function adminCreateCategory(data) {
  const res = await fetch(`${BASE}/api/admin/categories`, {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify(data)
  });
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.error || 'Lỗi tạo category');
  }
  return res.json();
}

async function adminDeleteCategory(id) {
  const res = await fetch(`${BASE}/api/admin/categories/${id}`, {
    method: 'DELETE',
    headers: authHeaders()
  });
  if (!res.ok) throw new Error('Lỗi xoá category');
}

// ── ADMIN TAGS ────────────────────────────────────
async function adminGetTags() {
  const res = await fetch(`${BASE}/api/admin/tags`, { headers: authHeaders() });
  return res.json();
}

async function adminCreateTag(data) {
  const res = await fetch(`${BASE}/api/admin/tags`, {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify(data)
  });
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.error || 'Lỗi tạo tag');
  }
  return res.json();
}

async function adminDeleteTag(id) {
  const res = await fetch(`${BASE}/api/admin/tags/${id}`, {
    method: 'DELETE',
    headers: authHeaders()
  });
  if (!res.ok) throw new Error('Lỗi xoá tag');
}

// ── HELPER ────────────────────────────────────────
function formatDate(dateStr) {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleDateString('vi-VN', {
    day: '2-digit', month: '2-digit', year: 'numeric'
  });
}
