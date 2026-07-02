import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { adminGetArticles, adminPublish, adminUnpublish, adminDeleteArticle, formatDate } from '../../api.js';
import AdminNav from '../../components/AdminNav.jsx';
import AdminSidebar from '../../components/AdminSidebar.jsx';

export default function AdminArticles() {
  const [articles, setArticles] = useState(null);
  const [error, setError] = useState(null);

  function loadArticles() {
    adminGetArticles()
      .then(setArticles)
      .catch(e => setError('Lỗi tải danh sách: ' + e.message));
  }

  useEffect(loadArticles, []);

  async function handlePublish(id) {
    if (!confirm('Publish bài này?')) return;
    try {
      await adminPublish(id);
      loadArticles();
    } catch (e) { alert(e.message); }
  }

  async function handleUnpublish(id) {
    if (!confirm('Đưa về Draft?')) return;
    try {
      await adminUnpublish(id);
      loadArticles();
    } catch (e) { alert(e.message); }
  }

  async function handleDelete(id) {
    if (!confirm('Xoá bài viết này?')) return;
    try {
      await adminDeleteArticle(id);
      loadArticles();
    } catch (e) { alert(e.message); }
  }

  return (
    <>
      <AdminNav />
      <div className="admin-layout">
        <AdminSidebar />
        <main className="admin-content">
          <div className="top-bar">
            <h2>Quản lý bài viết</h2>
            <Link to="/admin/articles/new" className="btn btn-primary">+ Tạo bài mới</Link>
          </div>

          {error && <div className="alert alert-error">{error}</div>}

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Tiêu đề</th>
                  <th>Category</th>
                  <th>Trạng thái</th>
                  <th>👁 Views</th>
                  <th>Ngày tạo</th>
                  <th>Hành động</th>
                </tr>
              </thead>
              <tbody>
                {articles === null && (
                  <tr><td colSpan={7} className="loading">Đang tải...</td></tr>
                )}
                {articles && articles.length === 0 && (
                  <tr><td colSpan={7} className="empty">Chưa có bài viết nào.</td></tr>
                )}
                {articles && articles.map(a => (
                  <tr key={a.id}>
                    <td>{a.id}</td>
                    <td><strong>{a.title}</strong></td>
                    <td>{a.categoryName}</td>
                    <td><span className={`badge badge-${a.status.toLowerCase()}`}>{a.status}</span></td>
                    <td>{a.viewCount}</td>
                    <td>{formatDate(a.createdAt)}</td>
                    <td style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
                      <Link to={`/admin/articles/${a.id}/edit`} className="btn btn-warning btn-sm">Sửa</Link>
                      {a.status === 'DRAFT'
                        ? <button className="btn btn-success btn-sm" onClick={() => handlePublish(a.id)}>Publish</button>
                        : <button className="btn btn-secondary btn-sm" onClick={() => handleUnpublish(a.id)}>Draft</button>}
                      <button className="btn btn-danger btn-sm" onClick={() => handleDelete(a.id)}>Xoá</button>
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
