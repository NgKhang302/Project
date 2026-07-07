import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { api } from "../api/api";
import Alert from "../components/Alert";

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
