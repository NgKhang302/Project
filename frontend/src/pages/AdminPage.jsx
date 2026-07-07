import { useEffect, useState } from "react";
import { api } from "../api/api";
import Alert from "../components/Alert";

const TABS = ["Categories", "Lessons", "Quizzes"];
const CONTENT_TYPES = ["READING", "WRITING", "LISTENING"];

export default function AdminPage() {
    const [tab, setTab] = useState("Categories");
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const notify = (message) => {
        setSuccess(message);
        setTimeout(() => setSuccess(null), 2000);
    };

    return (
        <div className="page container">
            <h1>Admin</h1>
            <div className="filter-tabs">
                {TABS.map((t) => (
                    <button key={t} className={tab === t ? "filter-tab active" : "filter-tab"} onClick={() => setTab(t)}>
                        {t}
                    </button>
                ))}
            </div>

            <Alert type="error" onClose={() => setError(null)}>{error}</Alert>
            <Alert type="success">{success}</Alert>

            {tab === "Categories" && <CategoriesTab onError={setError} onSuccess={notify} />}
            {tab === "Lessons" && <LessonsTab onError={setError} onSuccess={notify} />}
            {tab === "Quizzes" && <QuizzesTab onError={setError} onSuccess={notify} />}
        </div>
    );
}

function CategoriesTab({ onError, onSuccess }) {
    const [categories, setCategories] = useState([]);
    const [form, setForm] = useState({ id: null, name: "", slug: "", description: "" });

    const load = () => api.adminGetCategories().then(setCategories).catch((e) => onError(e.message));

    useEffect(() => {
        load();
    }, []);

    const resetForm = () => setForm({ id: null, name: "", slug: "", description: "" });

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const payload = { name: form.name, slug: form.slug, description: form.description };
            if (form.id) {
                await api.adminUpdateCategory(form.id, payload);
                onSuccess("Category updated");
            } else {
                await api.adminCreateCategory(payload);
                onSuccess("Category created");
            }
            resetForm();
            load();
        } catch (err) {
            onError(err.message);
        }
    };

    const handleDelete = async (id) => {
        try {
            await api.adminDeleteCategory(id);
            onSuccess("Category deleted");
            load();
        } catch (err) {
            onError(err.message);
        }
    };

    return (
        <div className="admin-tab">
            <form className="form inline-form" onSubmit={handleSubmit}>
                <input placeholder="Name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
                <input placeholder="Slug (optional)" value={form.slug} onChange={(e) => setForm({ ...form, slug: e.target.value })} />
                <input
                    placeholder="Description"
                    value={form.description}
                    onChange={(e) => setForm({ ...form, description: e.target.value })}
                />
                <button type="submit">{form.id ? "Update" : "Create"}</button>
                {form.id && (
                    <button type="button" className="secondary" onClick={resetForm}>
                        Cancel
                    </button>
                )}
            </form>

            <table className="admin-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Slug</th>
                        <th>Lessons</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {categories.map((c) => (
                        <tr key={c.id}>
                            <td>{c.name}</td>
                            <td>{c.slug}</td>
                            <td>{c.lessonCount}</td>
                            <td>
                                <button className="link-button" onClick={() => setForm(c)}>
                                    Edit
                                </button>
                                <button className="link-button danger" onClick={() => handleDelete(c.id)}>
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

function LessonsTab({ onError, onSuccess }) {
    const [categories, setCategories] = useState([]);
    const [categoryId, setCategoryId] = useState("");
    const [lessons, setLessons] = useState([]);
    const [form, setForm] = useState({ id: null, categoryId: "", title: "", slug: "", content: "", contentType: "READING" });

    useEffect(() => {
        api.adminGetCategories().then((data) => {
            setCategories(data);
            if (data.length > 0) setCategoryId(String(data[0].id));
        });
    }, []);

    const load = (catId) => {
        if (!catId) return;
        api.adminGetLessons(catId).then(setLessons).catch((e) => onError(e.message));
    };

    useEffect(() => {
        load(categoryId);
    }, [categoryId]);

    const resetForm = () => setForm({ id: null, categoryId, title: "", slug: "", content: "", contentType: "READING" });

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const payload = {
                categoryId: Number(form.categoryId || categoryId),
                title: form.title,
                slug: form.slug,
                content: form.content,
                contentType: form.contentType,
            };
            if (form.id) {
                await api.adminUpdateLesson(form.id, payload);
                onSuccess("Lesson updated");
            } else {
                await api.adminCreateLesson(payload);
                onSuccess("Lesson created");
            }
            resetForm();
            load(categoryId);
        } catch (err) {
            onError(err.message);
        }
    };

    const handleDelete = async (id) => {
        try {
            await api.adminDeleteLesson(id);
            onSuccess("Lesson deleted");
            load(categoryId);
        } catch (err) {
            onError(err.message);
        }
    };

    return (
        <div className="admin-tab">
            <select value={categoryId} onChange={(e) => setCategoryId(e.target.value)}>
                {categories.map((c) => (
                    <option key={c.id} value={c.id}>
                        {c.name}
                    </option>
                ))}
            </select>

            <form className="form" onSubmit={handleSubmit}>
                <input placeholder="Title" value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} required />
                <input placeholder="Slug (optional)" value={form.slug} onChange={(e) => setForm({ ...form, slug: e.target.value })} />
                <select value={form.contentType} onChange={(e) => setForm({ ...form, contentType: e.target.value })}>
                    {CONTENT_TYPES.map((t) => (
                        <option key={t} value={t}>
                            {t}
                        </option>
                    ))}
                </select>
                <textarea
                    placeholder="Content (HTML supported)"
                    value={form.content}
                    onChange={(e) => setForm({ ...form, content: e.target.value })}
                    rows={5}
                />
                <button type="submit">{form.id ? "Update" : "Create"}</button>
                {form.id && (
                    <button type="button" className="secondary" onClick={resetForm}>
                        Cancel
                    </button>
                )}
            </form>

            <table className="admin-table">
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Type</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {lessons.map((l) => (
                        <tr key={l.id}>
                            <td>{l.title}</td>
                            <td>{l.contentType}</td>
                            <td>
                                <button className="link-button" onClick={() => setForm({ ...l, categoryId: String(l.categoryId) })}>
                                    Edit
                                </button>
                                <button className="link-button danger" onClick={() => handleDelete(l.id)}>
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

function QuizzesTab({ onError, onSuccess }) {
    const [categories, setCategories] = useState([]);
    const [categoryId, setCategoryId] = useState("");
    const [lessons, setLessons] = useState([]);
    const [lessonId, setLessonId] = useState("");
    const [quizzes, setQuizzes] = useState([]);
    const [form, setForm] = useState({ id: null, question: "", options: "", correctAnswer: "", explanation: "" });

    useEffect(() => {
        api.adminGetCategories().then((data) => {
            setCategories(data);
            if (data.length > 0) setCategoryId(String(data[0].id));
        });
    }, []);

    useEffect(() => {
        if (!categoryId) return;
        api.adminGetLessons(categoryId).then((data) => {
            setLessons(data);
            setLessonId(data.length > 0 ? String(data[0].id) : "");
        });
    }, [categoryId]);

    const load = (lid) => {
        if (!lid) return;
        api.adminGetQuizzes(lid).then(setQuizzes).catch((e) => onError(e.message));
    };

    useEffect(() => {
        load(lessonId);
    }, [lessonId]);

    const resetForm = () => setForm({ id: null, question: "", options: "", correctAnswer: "", explanation: "" });

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const payload = {
                lessonId: Number(lessonId),
                question: form.question,
                options: form.options.split("|").map((o) => o.trim()).filter(Boolean),
                correctAnswer: form.correctAnswer,
                explanation: form.explanation,
            };
            if (form.id) {
                await api.adminUpdateQuiz(form.id, payload);
                onSuccess("Quiz updated");
            } else {
                await api.adminCreateQuiz(payload);
                onSuccess("Quiz created");
            }
            resetForm();
            load(lessonId);
        } catch (err) {
            onError(err.message);
        }
    };

    const handleDelete = async (id) => {
        try {
            await api.adminDeleteQuiz(id);
            onSuccess("Quiz deleted");
            load(lessonId);
        } catch (err) {
            onError(err.message);
        }
    };

    return (
        <div className="admin-tab">
            <div className="inline-form">
                <select value={categoryId} onChange={(e) => setCategoryId(e.target.value)}>
                    {categories.map((c) => (
                        <option key={c.id} value={c.id}>
                            {c.name}
                        </option>
                    ))}
                </select>
                <select value={lessonId} onChange={(e) => setLessonId(e.target.value)}>
                    {lessons.map((l) => (
                        <option key={l.id} value={l.id}>
                            {l.title}
                        </option>
                    ))}
                </select>
            </div>

            <form className="form" onSubmit={handleSubmit}>
                <textarea
                    placeholder="Question"
                    value={form.question}
                    onChange={(e) => setForm({ ...form, question: e.target.value })}
                    required
                />
                <input
                    placeholder="Options separated by | (e.g. A. cat | B. dog | C. bird)"
                    value={form.options}
                    onChange={(e) => setForm({ ...form, options: e.target.value })}
                    required
                />
                <input
                    placeholder="Correct answer (must match one option exactly)"
                    value={form.correctAnswer}
                    onChange={(e) => setForm({ ...form, correctAnswer: e.target.value })}
                    required
                />
                <input
                    placeholder="Explanation (optional)"
                    value={form.explanation}
                    onChange={(e) => setForm({ ...form, explanation: e.target.value })}
                />
                <button type="submit" disabled={!lessonId}>
                    {form.id ? "Update" : "Create"}
                </button>
                {form.id && (
                    <button type="button" className="secondary" onClick={resetForm}>
                        Cancel
                    </button>
                )}
            </form>

            <table className="admin-table">
                <thead>
                    <tr>
                        <th>Question</th>
                        <th>Correct answer</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {quizzes.map((q) => (
                        <tr key={q.id}>
                            <td>{q.question}</td>
                            <td>{q.correctAnswer}</td>
                            <td>
                                <button
                                    className="link-button"
                                    onClick={() =>
                                        setForm({
                                            id: q.id,
                                            question: q.question,
                                            options: q.options.join(" | "),
                                            correctAnswer: q.correctAnswer,
                                            explanation: q.explanation || "",
                                        })
                                    }
                                >
                                    Edit
                                </button>
                                <button className="link-button danger" onClick={() => handleDelete(q.id)}>
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
