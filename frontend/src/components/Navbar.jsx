import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

export default function Navbar() {
    const { user, isAuthenticated, isAdmin, logout } = useAuth();
    const navigate = useNavigate();
    const [query, setQuery] = useState("");

    const handleLogout = async () => {
        await logout();
        navigate("/login");
    };

    const handleSearch = (e) => {
        e.preventDefault();
        if (query.trim()) {
            navigate(`/search?q=${encodeURIComponent(query.trim())}`);
        }
    };

    return (
        <nav className="navbar">
            <Link to="/" className="navbar-brand">
                EduApp
            </Link>
            <form className="navbar-search" onSubmit={handleSearch}>
                <input
                    type="search"
                    placeholder="Tìm bài học..."
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                />
            </form>
            <div className="navbar-links">
                <Link to="/">Home</Link>
                {isAuthenticated && <Link to="/dashboard">Dashboard</Link>}
                {isAdmin && <Link to="/admin">Admin</Link>}
                {isAuthenticated ? (
                    <>
                        <Link to="/profile">{user.username}</Link>
                        <button className="navbar-logout" onClick={handleLogout}>
                            Logout
                        </button>
                    </>
                ) : (
                    <>
                        <Link to="/login">Login</Link>
                        <Link to="/register">Register</Link>
                    </>
                )}
            </div>
        </nav>
    );
}
