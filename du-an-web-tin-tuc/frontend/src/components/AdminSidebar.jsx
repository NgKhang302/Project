import { Link, useLocation } from 'react-router-dom';

export default function AdminSidebar() {
  const { pathname } = useLocation();
  const isActive = (path) => pathname === path;

  return (
    <aside className="sidebar">
      <Link to="/admin" className={isActive('/admin') ? 'active' : ''}>📄 Bài viết</Link>
      <Link to="/admin/categories" className={isActive('/admin/categories') ? 'active' : ''}>🗂 Category</Link>
      <Link to="/admin/tags" className={isActive('/admin/tags') ? 'active' : ''}>🏷 Tags</Link>
      <Link to="/admin/articles/new" className={isActive('/admin/articles/new') ? 'active' : ''}>✏️ Tạo bài mới</Link>
    </aside>
  );
}
