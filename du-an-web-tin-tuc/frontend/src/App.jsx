import { Routes, Route } from 'react-router-dom';
import Home from './pages/Home.jsx';
import Article from './pages/Article.jsx';
import Category from './pages/Category.jsx';
import Login from './pages/Login.jsx';
import RequireAuth from './components/RequireAuth.jsx';
import AdminArticles from './pages/admin/AdminArticles.jsx';
import AdminForm from './pages/admin/AdminForm.jsx';
import AdminCategories from './pages/admin/AdminCategories.jsx';
import AdminTags from './pages/admin/AdminTags.jsx';

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/category/:slug" element={<Category />} />
      <Route path="/article/:slug" element={<Article />} />
      <Route path="/login" element={<Login />} />

      <Route path="/admin" element={<RequireAuth><AdminArticles /></RequireAuth>} />
      <Route path="/admin/articles/new" element={<RequireAuth><AdminForm /></RequireAuth>} />
      <Route path="/admin/articles/:id/edit" element={<RequireAuth><AdminForm /></RequireAuth>} />
      <Route path="/admin/categories" element={<RequireAuth><AdminCategories /></RequireAuth>} />
      <Route path="/admin/tags" element={<RequireAuth><AdminTags /></RequireAuth>} />
    </Routes>
  );
}
