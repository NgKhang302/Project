import { useEffect, useRef, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import {
  BASE, adminGetArticle, adminCreateArticle, adminUpdateArticle,
  adminGetCategories, adminGetTags, adminUpload, adminPublish, adminUnpublish,
  slugify
} from '../../api.js';
import AdminNav from '../../components/AdminNav.jsx';
import AdminSidebar from '../../components/AdminSidebar.jsx';

export default function AdminForm() {
  const { id: articleId } = useParams();
  const navigate = useNavigate();

  const [categories, setCategories] = useState([]);
  const [tags, setTags] = useState([]);
  const [status, setStatus] = useState(null);
  const [alert, setAlert] = useState(null);

  const [title, setTitle] = useState('');
  const [slug, setSlug] = useState('');
  const [categoryId, setCategoryId] = useState('');
  const [summary, setSummary] = useState('');
  const [metaDescription, setMetaDescription] = useState('');
  const [content, setContent] = useState('');
  const [coverImageUrl, setCoverImageUrl] = useState('');
  const [selectedTagIds, setSelectedTagIds] = useState([]);
  const fileInputRef = useRef(null);

  useEffect(() => {
    Promise.all([adminGetCategories(), adminGetTags()]).then(([cats, tgs]) => {
      setCategories(cats);
      setTags(tgs);
    });
  }, []);

  const [articleTagNames, setArticleTagNames] = useState(null);

  useEffect(() => {
    if (!articleId) return;
    adminGetArticle(articleId).then(a => {
      setTitle(a.title);
      setSlug(a.slug);
      setSummary(a.summary || '');
      setMetaDescription(a.metaDescription || '');
      setContent(a.content);
      setCoverImageUrl(a.coverImageUrl || '');
      setCategoryId(a.categoryId);
      setStatus(a.status);
      setArticleTagNames(a.tags || []);
    });
  }, [articleId]);

  // đánh dấu tag đã chọn dựa trên tên tag khi cả article và tags đã load
  useEffect(() => {
    if (!articleTagNames || tags.length === 0) return;
    setSelectedTagIds(tags.filter(t => articleTagNames.includes(t.name)).map(t => t.id));
  }, [articleTagNames, tags]);

  function handleTitleChange(value) {
    setTitle(value);
    if (!articleId) setSlug(slugify(value));
  }

  function toggleTag(id) {
    setSelectedTagIds(prev =>
      prev.includes(id) ? prev.filter(t => t !== id) : [...prev, id]
    );
  }

  async function handleFileChange(e) {
    const file = e.target.files[0];
    if (!file) return;
    try {
      const res = await adminUpload(file);
      setCoverImageUrl(res.url);
    } catch (err) {
      showAlert('error', 'Upload thất bại: ' + err.message);
    }
  }

  function getFormData() {
    return {
      title: title.trim(),
      slug: slug.trim(),
      summary: summary.trim(),
      metaDescription: metaDescription.trim() || null,
      content: content.trim(),
      coverImageUrl: coverImageUrl || null,
      categoryId: parseInt(categoryId),
      tagIds: selectedTagIds
    };
  }

  function showAlert(type, msg) {
    setAlert({ type, msg });
    window.scrollTo(0, 0);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    const data = getFormData();
    try {
      if (articleId) {
        await adminUpdateArticle(articleId, data);
        showAlert('success', 'Đã cập nhật bài viết!');
      } else {
        const created = await adminCreateArticle(data);
        showAlert('success', 'Đã tạo bài viết! Chuyển sang trang sửa...');
        setTimeout(() => navigate(`/admin/articles/${created.id}/edit`), 1000);
      }
    } catch (err) {
      showAlert('error', err.message);
    }
  }

  async function handlePublishToggle() {
    try {
      if (status === 'PUBLISHED') {
        await adminUnpublish(articleId);
        setStatus('DRAFT');
        showAlert('success', 'Đã đưa về Draft.');
      } else {
        await adminPublish(articleId);
        setStatus('PUBLISHED');
        showAlert('success', 'Bài viết đã được Publish!');
      }
    } catch (err) {
      showAlert('error', err.message);
    }
  }

  return (
    <>
      <AdminNav />
      <div className="admin-layout">
        <AdminSidebar />
        <main className="admin-content">
          <div className="admin-form">
            <h2>{articleId ? 'Chỉnh sửa bài viết' : 'Tạo bài viết mới'}</h2>
            {alert && <div className={`alert alert-${alert.type}`}>{alert.msg}</div>}

            <form onSubmit={handleSubmit}>
              <div className="form-row">
                <div className="form-group">
                  <label>Tiêu đề *</label>
                  <input type="text" placeholder="Nhập tiêu đề..." required
                    value={title} onChange={e => handleTitleChange(e.target.value)} />
                </div>
                <div className="form-group">
                  <label>Slug *</label>
                  <input type="text" placeholder="tieu-de-bai-viet" required
                    value={slug} onChange={e => setSlug(e.target.value)} />
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Category *</label>
                  <select required value={categoryId} onChange={e => setCategoryId(e.target.value)}>
                    <option value="">-- Chọn category --</option>
                    {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label>Ảnh bìa</label>
                  <div className="upload-area" onClick={() => fileInputRef.current.click()}>
                    {!coverImageUrl && <span>📷 Nhấn để chọn ảnh</span>}
                    {coverImageUrl && <img className="upload-preview" src={`${BASE}${coverImageUrl}`} alt="" />}
                  </div>
                  <input ref={fileInputRef} type="file" accept="image/*" style={{ display: 'none' }} onChange={handleFileChange} />
                </div>
              </div>

              <div className="form-group">
                <label>Tóm tắt</label>
                <textarea rows={3} placeholder="Mô tả ngắn về bài viết..."
                  value={summary} onChange={e => setSummary(e.target.value)} />
              </div>

              <div className="form-group">
                <label>Meta Description (SEO)</label>
                <textarea rows={2} placeholder="Mô tả hiển thị trên Google, tối đa 160 ký tự..."
                  value={metaDescription} onChange={e => setMetaDescription(e.target.value)} />
              </div>

              <div className="form-group">
                <label>Tags</label>
                <div className="tags-container">
                  {tags.map(t => (
                    <label className="tag-checkbox" key={t.id}>
                      <input type="checkbox" checked={selectedTagIds.includes(t.id)} onChange={() => toggleTag(t.id)} /> {t.name}
                    </label>
                  ))}
                </div>
              </div>

              <div className="form-group">
                <label>Nội dung *</label>
                <textarea rows={12} placeholder="Nội dung đầy đủ..." required
                  value={content} onChange={e => setContent(e.target.value)} />
              </div>

              <div className="form-actions">
                <button type="submit" className="btn btn-primary">💾 Lưu Draft</button>
                {articleId && (
                  <button type="button"
                    className={status === 'PUBLISHED' ? 'btn btn-secondary' : 'btn btn-success'}
                    onClick={handlePublishToggle}>
                    {status === 'PUBLISHED' ? '↩️ Đưa về Draft' : '🚀 Publish'}
                  </button>
                )}
                <Link to="/admin" className="btn btn-secondary">Huỷ</Link>
              </div>
            </form>
          </div>
        </main>
      </div>
    </>
  );
}
