import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getArticlesByCategory } from '../api.js';
import Navbar from '../components/Navbar.jsx';
import ArticleCard from '../components/ArticleCard.jsx';

export default function Category() {
  const { slug } = useParams();
  const [articles, setArticles] = useState(null);

  useEffect(() => {
    setArticles(null);
    getArticlesByCategory(slug)
      .then(setArticles)
      .catch(() => setArticles([]));
  }, [slug]);

  useEffect(() => {
    if (articles && articles.length > 0) {
      document.title = articles[0].categoryName + ' – SongNhip24';
    }
  }, [articles]);

  const title = articles && articles.length > 0 ? articles[0].categoryName : (articles ? 'Chuyên mục' : 'Đang tải...');

  return (
    <>
      <Navbar />
      <div className="container">
        <h1 className="page-title">{title}</h1>
        <div className="article-grid">
          {articles === null && <p className="loading">Đang tải...</p>}
          {articles && articles.length === 0 && <p className="empty">Chưa có bài viết nào trong chuyên mục này.</p>}
          {articles && articles.map(a => <ArticleCard key={a.id} article={a} />)}
        </div>
      </div>
    </>
  );
}
