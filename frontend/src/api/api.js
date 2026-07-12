const BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

async function request(path, { method = "GET", body, params } = {}) {
    let url = `${BASE_URL}${path}`;
    if (params) {
        const query = new URLSearchParams(
            Object.entries(params).filter(([, v]) => v !== undefined && v !== null)
        ).toString();
        if (query) url += `?${query}`;
    }

    const response = await fetch(url, {
        method,
        credentials: "include",
        headers: body ? { "Content-Type": "application/json" } : undefined,
        body: body ? JSON.stringify(body) : undefined,
    });

    if (response.status === 204) return null;

    const isJson = response.headers.get("content-type")?.includes("application/json");
    const data = isJson ? await response.json() : await response.text();

    if (!response.ok) {
        const message = (isJson && data?.message) || response.statusText || "Request failed";
        throw new Error(message);
    }

    return data;
}

export const api = {
    // Auth
    login: (usernameOrEmail, password) =>
        request("/auth/login", { method: "POST", body: { usernameOrEmail, password } }),
    register: (username, email, password) =>
        request("/auth/register", { method: "POST", body: { username, email, password } }),
    logout: () => request("/auth/logout", { method: "POST" }),
    checkAuth: () => request("/auth/check"),

    // Public
    getCategories: () => request("/public/categories"),
    getLessonsByCategory: (categoryId, contentType, level) =>
        request("/public/lessons", { params: { categoryId, contentType, level } }),
    getPublicLesson: (id) => request(`/public/lessons/${id}`),
    searchLessons: (q) => request("/public/lessons/search", { params: { q } }),

    // Lessons (authenticated)
    getLesson: (id) => request(`/lessons/${id}`),
    completeLesson: (id, score) => request(`/lessons/${id}/complete`, { method: "POST", body: { score } }),

    // Quizzes
    getQuizzesByLesson: (lessonId) => request(`/quizzes/lesson/${lessonId}`),
    submitQuizAnswer: (quizId, answer) => request("/quizzes/submit", { method: "POST", body: { quizId, answer } }),

    // Writing practice
    submitWriting: (lessonId, content) => request("/writing/submit", { method: "POST", body: { lessonId, content } }),
    getWritingHistory: (lessonId) => request(`/writing/lesson/${lessonId}`),
    checkWriting: (lessonId, content) => request("/writing/check", { method: "POST", body: { lessonId, content } }),

    // Dictionary lookup
    lookupWord: (word) => request("/public/dictionary", { params: { word } }),

    // User
    getProfile: () => request("/user/profile"),
    updateProfile: (email) => request("/user/profile", { method: "PUT", body: { email } }),
    getUserProgress: () => request("/user/progress"),
    getDashboard: () => request("/user/dashboard"),

    // Admin - categories
    adminGetCategories: () => request("/admin/categories"),
    adminCreateCategory: (payload) => request("/admin/categories", { method: "POST", body: payload }),
    adminUpdateCategory: (id, payload) => request(`/admin/categories/${id}`, { method: "PUT", body: payload }),
    adminDeleteCategory: (id) => request(`/admin/categories/${id}`, { method: "DELETE" }),

    // Admin - lessons
    adminGetLessons: (categoryId) => request("/admin/lessons", { params: { categoryId } }),
    adminCreateLesson: (payload) => request("/admin/lessons", { method: "POST", body: payload }),
    adminUpdateLesson: (id, payload) => request(`/admin/lessons/${id}`, { method: "PUT", body: payload }),
    adminDeleteLesson: (id) => request(`/admin/lessons/${id}`, { method: "DELETE" }),
    adminGetDialogueLines: (lessonId) => request(`/admin/lessons/${lessonId}/dialogue-lines`),
    adminSaveDialogueLines: (lessonId, lines) =>
        request(`/admin/lessons/${lessonId}/dialogue-lines`, { method: "PUT", body: lines }),

    // Admin - uploads
    adminUploadAudio: async (file) => {
        const formData = new FormData();
        formData.append("file", file);
        const response = await fetch(`${BASE_URL}/admin/upload/audio`, {
            method: "POST",
            credentials: "include",
            body: formData,
        });
        const data = await response.json();
        if (!response.ok) throw new Error(data?.message || "Upload failed");
        return data;
    },

    // Admin - quizzes
    adminGetQuizzes: (lessonId) => request("/admin/quizzes", { params: { lessonId } }),
    adminCreateQuiz: (payload) => request("/admin/quizzes", { method: "POST", body: payload }),
    adminUpdateQuiz: (id, payload) => request(`/admin/quizzes/${id}`, { method: "PUT", body: payload }),
    adminDeleteQuiz: (id) => request(`/admin/quizzes/${id}`, { method: "DELETE" }),
};
