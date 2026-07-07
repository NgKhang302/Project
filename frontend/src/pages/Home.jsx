import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { api } from "../api/api";
import Alert from "../components/Alert";

const SKILLS = [
    {
        icon: "📖",
        title: "READING",
        lines: [
            "Bài tập đọc hiểu phong phú theo nhiều chủ đề.",
            "Mở rộng từ vựng qua ngữ cảnh thực tế và bài kiểm tra.",
        ],
    },
    {
        icon: "✍️",
        title: "VIẾT",
        lines: [
            "Bài tập luyện viết đa dạng, phản hồi tự động tức thì và bắt lỗi sai.",
            "Cải thiện ngữ pháp và diễn đạt tự nhiên hơn.",
        ],
    },
    {
        icon: "🎧",
        title: "NGHE",
        lines: [
            "Nghe hội thoại và bài nói với nhiều cấp độ khác nhau.",
            "Luyện phát âm chuẩn với phụ đề hỗ trợ.",
        ],
    },
];

export default function Home() {
    const [categories, setCategories] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        api.getCategories()
            .then(setCategories)
            .catch((err) => setError(err.message))
            .finally(() => setLoading(false));
    }, []);

    return (
        <div className="page container">
            <section className="hero">
                <h1>Learn English, one lesson at a time</h1>
                <p>Reading, writing, and listening practice with instant feedback.</p>
            </section>

            <h2 className="section-title">Kỹ Năng</h2>
            <div className="skills-grid">
                {SKILLS.map((skill) => (
                    <div key={skill.title} className="skill-card">
                        <div className="skill-card-icon">{skill.icon}</div>
                        <div className="skill-card-title">{skill.title}</div>
                        {skill.lines.map((line) => (
                            <p key={line}>{line}</p>
                        ))}
                    </div>
                ))}
            </div>

            <h2 className="section-title">Danh mục bài học</h2>
            <Alert type="error">{error}</Alert>

            {loading ? (
                <p>Loading categories...</p>
            ) : (
                <div className="category-grid">
                    {categories.map((category) => (
                        <Link key={category.id} to={`/categories/${category.id}`} className="category-card">
                            <h3>{category.name}</h3>
                            <p>{category.description}</p>
                            <span className="category-card-count">{category.lessonCount} lessons</span>
                        </Link>
                    ))}
                    {categories.length === 0 && <p>No categories yet.</p>}
                </div>
            )}
        </div>
    );
}
