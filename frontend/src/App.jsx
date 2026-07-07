import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./hooks/useAuth";
import Navbar from "./components/Navbar";
import RequireAuth from "./components/RequireAuth";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import CategoryPage from "./pages/CategoryPage";
import LessonPage from "./pages/LessonPage";
import QuizPage from "./pages/QuizPage";
import QuizResult from "./pages/QuizResult";
import ProfilePage from "./pages/ProfilePage";
import AdminPage from "./pages/AdminPage";
import SearchResults from "./pages/SearchResults";
import NotFound from "./pages/NotFound";

export default function App() {
    return (
        <BrowserRouter>
            <AuthProvider>
                <Navbar />
                <main>
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />
                        <Route path="/categories/:id" element={<CategoryPage />} />
                        <Route path="/lessons/:id" element={<LessonPage />} />
                        <Route path="/search" element={<SearchResults />} />

                        <Route element={<RequireAuth />}>
                            <Route path="/dashboard" element={<Dashboard />} />
                            <Route path="/profile" element={<ProfilePage />} />
                            <Route path="/lessons/:id/quiz" element={<QuizPage />} />
                            <Route path="/lessons/:id/quiz/result" element={<QuizResult />} />
                        </Route>

                        <Route element={<RequireAuth adminOnly />}>
                            <Route path="/admin" element={<AdminPage />} />
                        </Route>

                        <Route path="*" element={<NotFound />} />
                    </Routes>
                </main>
            </AuthProvider>
        </BrowserRouter>
    );
}
