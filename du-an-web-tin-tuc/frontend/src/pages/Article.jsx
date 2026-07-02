import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { BASE, getArticleBySlug, formatDate } from '../api.js';
import Navbar from '../components/Navbar.jsx';

export default function Article() {
  const { slug } = useParams();
  const [article, setArticle] = useState(undefined);

  useEffect(() => {
    setArticle(undefined);
    getArticleBySlug(slug)
      .then(setArticle)
      .catch(() => setArticle(null));
  }, [slug]);

  useEffect(() => {
    if (!article) return;
    document.title = article.title + ' – SongNhip24';
    let meta = null;
    if (article.metaDescription) {
      meta = document.createElement('meta');
      meta.name = 'description';
      meta.content = article.metaDescription;
      document.head.appendChild(meta);
    }
    return () => {
      if (meta) document.head.removeChild(meta);
    };
  }, [article]);

  return (
    <>
      <Navbar />
      <div className="container">
        {article === undefined && <p className="loading">Đang tải bài viết...</p>}
        {article === null && <p className="empty">Không tìm thấy bài viết.</p>}
        {article && (
          <div className="article-detail">
            <Link className="card-category" to={`/category/${article.categorySlug}`} style={{ marginBottom: 8, display: 'inline-block' }}>
              {article.categoryName}
            </Link>
            <h1>{article.title}</h1>
            <div className="article-meta">
              <span>📅 {formatDate(article.publishedAt)}</span>
              <span>✍️ {article.createdBy}</span>
              <span>👁 {article.viewCount} lượt xem</span>
            </div>
            {article.coverImageUrl && (
              <img className="article-cover" src={`${BASE}${article.coverImageUrl}`} alt={article.title} />
            )}
            <div className="article-content">{article.content}</div>
            {article.tags && article.tags.length > 0 && (
              <div className="article-tags">
                {article.tags.map(t => <span className="tag" key={t}>{t}</span>)}
              </div>
            )}
          </div>
        )}
      </div>
    </>
  );
}
