import { Link } from 'react-router-dom';

export default function Navbar({ categories = [] }) {
  return (
    <nav>
      <Link className="logo" to="/">SongNhip24</Link>
      <div className="nav-links">
        <Link to="/">Trang chủ</Link>
        {categories.map(c => (
          <Link key={c.slug} to={`/category/${c.slug}`}>{c.name}</Link>
        ))}
        <Link to="/admin">Admin</Link>
      </div>
    </nav>
  );
}
