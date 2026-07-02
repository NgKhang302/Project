import { useEffect, useState } from 'react';
import { adminGetCategories, adminCreateCategory, adminDeleteCategory, slugify } from '../../api.js';
import AdminNav from '../../components/AdminNav.jsx';
import AdminSidebar from '../../components/AdminSidebar.jsx';

export default function AdminCategories() {
  const [categories, setCategories] = useState(null);
  const [alert, setAlert] = useState(null);
  const [name, setName] = useState('');
  const [slug, setSlug] = useState('');
  const [description, setDescription] = useState('');

  function loadCategories() {
    adminGetCategories()
      .then(setCategories)
      .catch(() => setAlert({ type: 'error', msg: 'Lỗi tải danh sách' }));
  }

  useEffect(loadCategories, []);

  function showAlert(type, msg) {
    setAlert({ type, msg });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      await adminCreateCategory({ name: name.trim(), slug: slug.trim(), description: description.trim() });
      setName(''); setSlug(''); setDescription('');
      showAlert('success', 'Đã thêm category!');
      loadCategories();
    } catch (err) {
      showAlert('error', err.message);
    }
  }

  async function handleDelete(id, catName) {
    if (!confirm(`Xoá category "${catName}"?`)) return;
    try {
      await adminDeleteCategory(id);
      loadCategories();
    } catch (e) {
      showAlert('error', 'Xoá thất bại: ' + e.message);
    }
  }

  return (
    <>
      <AdminNav />
      <div className="admin-layout">
        <AdminSidebar />
        <main className="admin-content">
          <div className="top-bar">
            <h2>Quản lý Category</h2>
          </div>

          {alert && <div className={`alert alert-${alert.type}`}>{alert.msg}</div>}

          <div className="admin-form" style={{ marginBottom: 24 }}>
            <h2 style={{ fontSize: '1rem', marginBottom: 16 }}>Thêm category mới</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-row">
                <div className="form-group">
                  <label>Tên *</label>
                  <input type="text" placeholder="Công nghệ" required
                    value={name} onChange={e => { setName(e.target.value); setSlug(slugify(e.target.value)); }} />
                </div>
                <div className="form-group">
                  <label>Slug *</label>
                  <input type="text" placeholder="cong-nghe" required
                    value={slug} onChange={e => setSlug(e.target.value)} />
                </div>
              </div>
              <div className="form-group">
                <label>Mô tả</label>
                <input type="text" placeholder="Mô tả ngắn..."
                  value={description} onChange={e => setDescription(e.target.value)} />
              </div>
              <button type="submit" className="btn btn-primary">Thêm</button>
            </form>
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Tên</th>
                  <th>Slug</th>
                  <th>Mô tả</th>
                  <th>Hành động</th>
                </tr>
              </thead>
              <tbody>
                {categories === null && <tr><td colSpan={5} className="loading">Đang tải...</td></tr>}
                {categories && categories.length === 0 && <tr><td colSpan={5} className="empty">Chưa có category nào.</td></tr>}
                {categories && categories.map(c => (
                  <tr key={c.id}>
                    <td>{c.id}</td>
                    <td><strong>{c.name}</strong></td>
                    <td>{c.slug}</td>
                    <td>{c.description || ''}</td>
                    <td>
                      <button className="btn btn-danger btn-sm" onClick={() => handleDelete(c.id, c.name)}>Xoá</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </main>
      </div>
    </>
  );
}
