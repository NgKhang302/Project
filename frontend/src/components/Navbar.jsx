import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

export default function Navbar() {
    const { user, isAuthenticated, isAdmin, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = async () => {
        await logout();
        navigate("/login");
    };

    return (
        <nav className="navbar">
            <Link to="/" className="navbar-brand">
                EduApp
            </Link>
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
