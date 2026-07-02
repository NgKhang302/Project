import { Link } from 'react-router-dom';
import { BASE, formatDate } from '../api.js';

export default function ArticleCard({ article, showAuthor = true }) {
  return (
    <Link to={`/article/${article.slug}`} className="card">
      {article.coverImageUrl
        ? <img src={`${BASE}${article.coverImageUrl}`} alt={article.title} />
        : <div className="card-no-img">📰</div>}
      <div className="card-body">
        <div className="card-category">{article.categoryName}</div>
        <div className="card-title">{article.title}</div>
        <div className="card-summary">{article.summary || ''}</div>
        <div className="card-meta">
          {formatDate(article.publishedAt)}
          {showAuthor && article.createdBy ? ` · ${article.createdBy}` : ''} · 👁 {article.viewCount}
        </div>
      </div>
    </Link>
  );
}
