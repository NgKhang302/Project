import { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';
import { checkAuth } from '../api.js';

export default function RequireAuth({ children }) {
  const [status, setStatus] = useState('checking');

  useEffect(() => {
    let cancelled = false;
    checkAuth().then(ok => {
      if (!cancelled) setStatus(ok ? 'ok' : 'denied');
    });
    return () => { cancelled = true; };
  }, []);

  if (status === 'checking') return null;
  if (status === 'denied') return <Navigate to="/login" replace />;
  return children;
}
