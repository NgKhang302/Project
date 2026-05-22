const BASE = 'http://localhost:8080';

// Kiểm tra session còn hợp lệ không (cookie được gửi tự động)
// Nếu không hợp lệ thì chuyển về trang login
async function requireAuth() {
  const res = await fetch(`${BASE}/api/auth/check`, { credentials: 'include' });
  if (!res.ok) {
    window.location.href = '/login.html';
    return Promise.reject('Not authenticated');
  }
}

// ── AUTH ──────────────────────────────────────────
async function login(username, password) {
  const res = await fetch(`${BASE}/api/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify({ username, password })
  });
  if (!res.ok) throw new Error('Sai tài khoản hoặc mật khẩu');
  return res.json();
}

async function logout() {
  await fetch(`${BASE}/api/auth/logout`, {
    method: 'POST',
    credentials: 'include'
  });
  window.location.href = '/login.html';
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

async function getPublicTags() {
  const res = await fetch(`${BASE}/api/public/tags`);
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
  const res = await fetch(`${BASE}/api/admin/articles`, { credentials: 'include' });
  return res.json();
}

async function adminGetArticle(id) {
  const res = await fetch(`${BASE}/api/admin/articles/${id}`, { credentials: 'include' });
  return res.json();
}

async function adminCreateArticle(data) {
  const res = await fetch(`${BASE}/api/admin/articles`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
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
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
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
    credentials: 'include'
  });
  if (!res.ok) throw new Error('Lỗi publish');
  return res.json();
}

async function adminUnpublish(id) {
  const res = await fetch(`${BASE}/api/admin/articles/${id}/unpublish`, {
    method: 'PUT',
    credentials: 'include'
  });
  if (!res.ok) throw new Error('Lỗi unpublish');
  return res.json();
}

async function adminDeleteArticle(id) {
  const res = await fetch(`${BASE}/api/admin/articles/${id}`, {
    method: 'DELETE',
    credentials: 'include'
  });
  if (!res.ok) throw new Error('Lỗi xoá bài');
}

// ── UPLOAD ────────────────────────────────────────
async function adminUpload(file) {
  const form = new FormData();
  form.append('file', file);
  const res = await fetch(`${BASE}/api/admin/upload`, {
    method: 'POST',
    credentials: 'include',
    body: form
  });
  if (!res.ok) throw new Error('Upload thất bại');
  return res.json();
}

// ── ADMIN CATEGORIES ─────────────────────────────
async function adminGetCategories() {
  const res = await fetch(`${BASE}/api/admin/categories`, { credentials: 'include' });
  return res.json();
}

async function adminCreateCategory(data) {
  const res = await fetch(`${BASE}/api/admin/categories`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
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
    credentials: 'include'
  });
  if (!res.ok) throw new Error('Lỗi xoá category');
}

// ── ADMIN TAGS ────────────────────────────────────
async function adminGetTags() {
  const res = await fetch(`${BASE}/api/admin/tags`, { credentials: 'include' });
  return res.json();
}

async function adminCreateTag(data) {
  const res = await fetch(`${BASE}/api/admin/tags`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
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
    credentials: 'include'
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
