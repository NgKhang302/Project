import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import {
  BASE, getPublicCategories, getPublishedArticles, getArticlesByCategory,
  subscribeNewsletter, formatDate
} from '../api.js';
import Navbar from '../components/Navbar.jsx';
import ArticleCard from '../components/ArticleCard.jsx';

export default function Home() {
  const [categories, setCategories] = useState([]);
  const [hero, setHero] = useState(null);
  const [gridArticles, setGridArticles] = useState(null);
  const [categorySections, setCategorySections] = useState([]);
  const [email, setEmail] = useState('');
  const [alert, setAlert] = useState(null);

  useEffect(() => {
    getPublicCategories().then(setCategories).catch(() => setCategories([]));
    getPublishedArticles().then(articles => {
      setHero(articles[0] || null);
      setGridArticles(articles.slice(1, 7));
    }).catch(() => setGridArticles([]));
  }, []);

  useEffect(() => {
    if (categories.length === 0) return;
    let cancelled = false;
    const topCats = categories.slice(0, 3);
    Promise.all(topCats.map(async cat => {
      try {
        const articles = await getArticlesByCategory(cat.slug);
        return { cat, articles: articles.slice(0, 3) };
      } catch {
        return { cat, articles: [] };
      }
    })).then(sections => {
      if (!cancelled) setCategorySections(sections.filter(s => s.articles.length > 0));
    });
    return () => { cancelled = true; };
  }, [categories]);

  async function handleNewsletterSubmit(e) {
    e.preventDefault();
    try {
      await subscribeNewsletter(email.trim());
      setAlert({ type: 'success', msg: 'Đăng ký thành công! Cảm ơn bạn.' });
      setEmail('');
    } catch (err) {
      setAlert({ type: 'error', msg: err.message });
    }
  }

  return (
    <>
      <Navbar categories={categories} />
      <div className="container">
        {hero && (
          <section className="section-block">
            <Link to={`/article/${hero.slug}`} className="hero-card">
              <div className="hero-img">
                {hero.coverImageUrl
                  ? <img src={`${BASE}${hero.coverImageUrl}`} alt={hero.title} />
                  : <div className="hero-no-img">📰</div>}
                <div className="hero-overlay" />
              </div>
              <div className="hero-body">
                <span className="card-category">{hero.categoryName}</span>
                <h1 className="hero-title">{hero.title}</h1>
                <p className="hero-summary">{hero.summary || ''}</p>
                <div className="card-meta">{formatDate(hero.publishedAt)} · {hero.createdBy} · 👁 {hero.viewCount}</div>
              </div>
            </Link>
          </section>
        )}

        <section className="section-block">
          <h2 className="section-title">Bài mới nhất</h2>
          <div className="article-grid">
            {gridArticles === null && <p className="loading">Đang tải...</p>}
            {gridArticles && gridArticles.length === 0 && <p className="empty">Chưa có bài viết nào khác.</p>}
            {gridArticles && gridArticles.map(a => <ArticleCard key={a.id} article={a} />)}
          </div>
        </section>

        {categorySections.map(({ cat, articles }) => (
          <section className="section-block" key={cat.slug}>
            <h2 className="section-title">
              {cat.name}
              <Link to={`/category/${cat.slug}`} className="section-more">Xem tất cả →</Link>
            </h2>
            <div className="article-grid">
              {articles.map(a => <ArticleCard key={a.id} article={a} showAuthor={false} />)}
            </div>
          </section>
        ))}

        <div className="newsletter-box">
          <h3>Nhận bản tin mỗi tuần</h3>
          <p>Cập nhật tin công nghệ, AI, việc làm ngay trong hộp thư của bạn.</p>
          {alert && (
            <div className={`alert alert-${alert.type}`}>{alert.msg}</div>
          )}
          <form className="newsletter-form" onSubmit={handleNewsletterSubmit}>
            <input
              type="email"
              placeholder="email@example.com"
              required
              value={email}
              onChange={e => setEmail(e.target.value)}
            />
            <button type="submit" className="btn btn-primary">Đăng ký</button>
          </form>
        </div>
      </div>
    </>
  );
}
