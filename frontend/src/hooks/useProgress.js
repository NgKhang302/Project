import { useCallback, useEffect, useState } from "react";
import { api } from "../api/api";
import { useAuth } from "./useAuth";

export function useProgress() {
    const { isAuthenticated } = useAuth();
    const [progress, setProgress] = useState([]);
    const [dashboard, setDashboard] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const refresh = useCallback(async () => {
        if (!isAuthenticated) {
            setProgress([]);
            setDashboard(null);
            setLoading(false);
            return;
        }
        setLoading(true);
        try {
            const [progressData, dashboardData] = await Promise.all([
                api.getUserProgress(),
                api.getDashboard(),
            ]);
            setProgress(progressData);
            setDashboard(dashboardData);
            setError(null);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }, [isAuthenticated]);

    useEffect(() => {
        refresh();
    }, [refresh]);

    return { progress, dashboard, loading, error, refresh };
}
