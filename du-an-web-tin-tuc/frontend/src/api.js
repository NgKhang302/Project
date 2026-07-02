export const BASE = '';

// ── AUTH ──────────────────────────────────────────
export async function checkAuth() {
  const res = await fetch(`${BASE}/api/auth/check`, { credentials: 'include' });
  return res.ok;
}

export async function login(username, password) {
  const res = await fetch(`${BASE}/api/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify({ username, password })
  });
  if (!res.ok) throw new Error('Sai tài khoản hoặc mật khẩu');
  return res.json();
}

export async function logout() {
  await fetch(`${BASE}/api/auth/logout`, {
    method: 'POST',
    credentials: 'include'
  });
}

// ── PUBLIC ────────────────────────────────────────
export async function getPublishedArticles() {
  const res = await fetch(`${BASE}/api/public/articles`);
  return res.json();
}

export async function getArticleBySlug(slug) {
  const res = await fetch(`${BASE}/api/public/articles/${slug}`);
  if (!res.ok) throw new Error('Không tìm thấy bài viết');
  return res.json();
}

export async function getPublicCategories() {
  const res = await fetch(`${BASE}/api/public/categories`);
  return res.json();
}

export async function getPublicTags() {
  const res = await fetch(`${BASE}/api/public/tags`);
  return res.json();
}

export async function getArticlesByCategory(slug) {
  const res = await fetch(`${BASE}/api/public/categories/${slug}/articles`);
  return res.json();
}

export async function subscribeNewsletter(email) {
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
export async function adminGetArticles() {
  const res = await fetch(`${BASE}/api/admin/articles`, { credentials: 'include' });
  return res.json();
}

export async function adminGetArticle(id) {
  const res = await fetch(`${BASE}/api/admin/articles/${id}`, { credentials: 'include' });
  return res.json();
}

export async function adminCreateArticle(data) {
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

export async function adminUpdateArticle(id, data) {
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

export async function adminPublish(id) {
  const res = await fetch(`${BASE}/api/admin/articles/${id}/publish`, {
    method: 'PUT',
    credentials: 'include'
  });
  if (!res.ok) throw new Error('Lỗi publish');
  return res.json();
}

export async function adminUnpublish(id) {
  const res = await fetch(`${BASE}/api/admin/articles/${id}/unpublish`, {
    method: 'PUT',
    credentials: 'include'
  });
  if (!res.ok) throw new Error('Lỗi unpublish');
  return res.json();
}

export async function adminDeleteArticle(id) {
  const res = await fetch(`${BASE}/api/admin/articles/${id}`, {
    method: 'DELETE',
    credentials: 'include'
  });
  if (!res.ok) throw new Error('Lỗi xoá bài');
}

// ── UPLOAD ────────────────────────────────────────
export async function adminUpload(file) {
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
export async function adminGetCategories() {
  const res = await fetch(`${BASE}/api/admin/categories`, { credentials: 'include' });
  return res.json();
}

export async function adminCreateCategory(data) {
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

export async function adminDeleteCategory(id) {
  const res = await fetch(`${BASE}/api/admin/categories/${id}`, {
    method: 'DELETE',
    credentials: 'include'
  });
  if (!res.ok) throw new Error('Lỗi xoá category');
}

// ── ADMIN TAGS ────────────────────────────────────
export async function adminGetTags() {
  const res = await fetch(`${BASE}/api/admin/tags`, { credentials: 'include' });
  return res.json();
}

export async function adminCreateTag(data) {
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

export async function adminDeleteTag(id) {
  const res = await fetch(`${BASE}/api/admin/tags/${id}`, {
    method: 'DELETE',
    credentials: 'include'
  });
  if (!res.ok) throw new Error('Lỗi xoá tag');
}

// ── HELPER ────────────────────────────────────────
export function formatDate(dateStr) {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleDateString('vi-VN', {
    day: '2-digit', month: '2-digit', year: 'numeric'
  });
}

export function slugify(text) {
  return text
    .toLowerCase()
    .normalize('NFD').replace(/[̀-ͯ]/g, '')
    .replace(/đ/g, 'd').replace(/Đ/g, 'd')
    .replace(/[^a-z0-9\s-]/g, '')
    .trim().replace(/\s+/g, '-');
}
