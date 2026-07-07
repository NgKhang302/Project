import { Link } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import { useProgress } from "../hooks/useProgress";
import StatCard from "../components/StatCard";
import ProgressBar from "../components/ProgressBar";
import Alert from "../components/Alert";

const SKILLS = ["READING", "WRITING", "LISTENING"];

export default function Dashboard() {
    const { user } = useAuth();
    const { dashboard, progress, loading, error } = useProgress();

    if (loading) return <div className="page container">Loading dashboard...</div>;

    return (
        <div className="page container">
            <h1>Welcome back, {user?.username}</h1>
            <Alert type="error">{error}</Alert>

            {dashboard && (
                <>
                    <div className="stat-grid">
                        <StatCard title="Completed lessons" value={dashboard.completedLessons} />
                        <StatCard title="In progress" value={dashboard.inProgressLessons} />
                        <StatCard title="Average score" value={`${dashboard.averageScore.toFixed(1)}%`} />
                    </div>

                    <section className="dashboard-section">
                        <h2>Progress by skill</h2>
                        {SKILLS.map((skill) => (
                            <ProgressBar
                                key={skill}
                                label={skill}
                                value={dashboard.completedByContentType?.[skill] || 0}
                                max={dashboard.completedLessons || 1}
                            />
                        ))}
                    </section>

                    <section className="dashboard-section">
                        <h2>Recent activity</h2>
                        {dashboard.recentActivity?.length ? (
                            <ul className="activity-list">
                                {dashboard.recentActivity.map((item) => (
                                    <li key={item.id}>
                                        <Link to={`/lessons/${item.lessonId}`}>{item.lessonTitle}</Link>
                                        <span> — {item.status}</span>
                                    </li>
                                ))}
                            </ul>
                        ) : (
                            <p>No completed lessons yet.</p>
                        )}
                    </section>
                </>
            )}

            {!dashboard && progress.length === 0 && <p>Start a lesson to see your progress here.</p>}
        </div>
    );
}
