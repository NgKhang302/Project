import { useEffect, useState } from 'react';
import { adminGetTags, adminCreateTag, adminDeleteTag, slugify } from '../../api.js';
import AdminNav from '../../components/AdminNav.jsx';
import AdminSidebar from '../../components/AdminSidebar.jsx';

export default function AdminTags() {
  const [tags, setTags] = useState(null);
  const [alert, setAlert] = useState(null);
  const [name, setName] = useState('');
  const [slug, setSlug] = useState('');

  function loadTags() {
    adminGetTags()
      .then(setTags)
      .catch(() => setAlert({ type: 'error', msg: 'Lỗi tải danh sách' }));
  }

  useEffect(loadTags, []);

  function showAlert(type, msg) {
    setAlert({ type, msg });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      await adminCreateTag({ name: name.trim(), slug: slug.trim() });
      setName(''); setSlug('');
      showAlert('success', 'Đã thêm tag!');
      loadTags();
    } catch (err) {
      showAlert('error', err.message);
    }
  }

  async function handleDelete(id, tagName) {
    if (!confirm(`Xoá tag "${tagName}"?`)) return;
    try {
      await adminDeleteTag(id);
      loadTags();
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
            <h2>Quản lý Tags</h2>
          </div>

          {alert && <div className={`alert alert-${alert.type}`}>{alert.msg}</div>}

          <div className="admin-form" style={{ marginBottom: 24 }}>
            <h2 style={{ fontSize: '1rem', marginBottom: 16 }}>Thêm tag mới</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-row">
                <div className="form-group">
                  <label>Tên *</label>
                  <input type="text" placeholder="Trí tuệ nhân tạo" required
                    value={name} onChange={e => { setName(e.target.value); setSlug(slugify(e.target.value)); }} />
                </div>
                <div className="form-group">
                  <label>Slug *</label>
                  <input type="text" placeholder="tri-tue-nhan-tao" required
                    value={slug} onChange={e => setSlug(e.target.value)} />
                </div>
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
                  <th>Hành động</th>
                </tr>
              </thead>
              <tbody>
                {tags === null && <tr><td colSpan={4} className="loading">Đang tải...</td></tr>}
                {tags && tags.length === 0 && <tr><td colSpan={4} className="empty">Chưa có tag nào.</td></tr>}
                {tags && tags.map(t => (
                  <tr key={t.id}>
                    <td>{t.id}</td>
                    <td><strong>{t.name}</strong></td>
                    <td>{t.slug}</td>
                    <td>
                      <button className="btn btn-danger btn-sm" onClick={() => handleDelete(t.id, t.name)}>Xoá</button>
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
