import { createContext, useContext, useEffect, useState, useCallback } from "react";
import { api } from "../api/api";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    const refresh = useCallback(async () => {
        try {
            const current = await api.checkAuth();
            setUser(current);
        } catch {
            setUser(null);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        refresh();
    }, [refresh]);

    const login = async (usernameOrEmail, password) => {
        const { user: loggedInUser } = await api.login(usernameOrEmail, password);
        setUser(loggedInUser);
        return loggedInUser;
    };

    const register = async (username, email, password) => {
        return api.register(username, email, password);
    };

    const logout = async () => {
        await api.logout();
        setUser(null);
    };

    const value = { user, loading, isAuthenticated: !!user, isAdmin: user?.role === "ADMIN", login, register, logout, refresh };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
}
