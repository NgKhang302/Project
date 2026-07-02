import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { checkAuth, login } from '../api.js';

export default function Login() {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);

  useEffect(() => {
    checkAuth().then(ok => { if (ok) navigate('/admin', { replace: true }); }).catch(() => {});
  }, [navigate]);

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);
    try {
      await login(username.trim(), password);
      navigate('/admin');
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div className="login-wrap">
      <div className="login-box">
        <h2>🔐 Đăng nhập Admin</h2>

        {error && <div className="alert alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Tài khoản</label>
            <input type="text" placeholder="admin" required value={username} onChange={e => setUsername(e.target.value)} />
          </div>
          <div className="form-group">
            <label>Mật khẩu</label>
            <input type="password" placeholder="••••••" required value={password} onChange={e => setPassword(e.target.value)} />
          </div>
          <button type="submit" className="btn btn-primary btn-block">Đăng nhập</button>
        </form>
      </div>
    </div>
  );
}
