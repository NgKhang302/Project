import { Link, useNavigate } from 'react-router-dom';
import { logout } from '../api.js';

export default function AdminNav() {
  const navigate = useNavigate();

  async function handleLogout(e) {
    e.preventDefault();
    await logout();
    navigate('/login');
  }

  return (
    <nav>
      <Link className="logo" to="/">SongNhip24</Link>
      <div className="nav-links">
        <Link to="/">Xem trang chủ</Link>
        <a href="#" onClick={handleLogout}>Đăng xuất</a>
      </div>
    </nav>
  );
}
