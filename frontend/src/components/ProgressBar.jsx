export default function ProgressBar({ label, value, max = 100 }) {
    const percent = max > 0 ? Math.min(100, Math.round((value / max) * 100)) : 0;

    return (
        <div className="progress-bar-wrapper">
            {label && (
                <div className="progress-bar-label">
                    <span>{label}</span>
                    <span>{percent}%</span>
                </div>
            )}
            <div className="progress-bar-track">
                <div className="progress-bar-fill" style={{ width: `${percent}%` }} />
            </div>
        </div>
    );
}
