import { useEffect, useState } from "react";
import { useAuth } from "../hooks/useAuth";
import { useProgress } from "../hooks/useProgress";
import StatCard from "../components/StatCard";
import Alert from "../components/Alert";
import { api } from "../api/api";

export default function ProfilePage() {
    const { user, refresh } = useAuth();
    const { dashboard } = useProgress();
    const [email, setEmail] = useState("");
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        if (user) setEmail(user.email);
    }, [user]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setSuccess(null);
        setSaving(true);
        try {
            await api.updateProfile(email);
            await refresh();
            setSuccess("Profile updated");
        } catch (err) {
            setError(err.message);
        } finally {
            setSaving(false);
        }
    };

    if (!user) return null;

    return (
        <div className="page container narrow">
            <h1>My Profile</h1>

            <div className="stat-grid">
                <StatCard title="Completed lessons" value={dashboard?.completedLessons ?? "-"} />
                <StatCard title="Average score" value={dashboard ? `${dashboard.averageScore.toFixed(1)}%` : "-"} />
            </div>

            <Alert type="error">{error}</Alert>
            <Alert type="success">{success}</Alert>

            <form className="form" onSubmit={handleSubmit}>
                <label>
                    Username
                    <input type="text" value={user.username} disabled />
                </label>
                <label>
                    Email
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                </label>
                <button type="submit" disabled={saving}>
                    {saving ? "Saving..." : "Save changes"}
                </button>
            </form>
        </div>
    );
}
