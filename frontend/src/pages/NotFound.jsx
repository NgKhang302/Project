import { Link } from "react-router-dom";

export default function NotFound() {
    return (
        <div className="page container narrow" style={{ textAlign: "center" }}>
            <h1 style={{ fontSize: "4rem", margin: "1rem 0" }}>404</h1>
            <p>Trang bạn tìm không tồn tại.</p>
            <Link to="/">
                <button>Về trang chủ</button>
            </Link>
        </div>
    );
}
