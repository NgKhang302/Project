export default function CategorySelector({ categories, value, onChange }) {
    return (
        <select className="category-selector" value={value ?? ""} onChange={(e) => onChange(e.target.value)}>
            <option value="">All categories</option>
            {categories.map((category) => (
                <option key={category.id} value={category.id}>
                    {category.name}
                </option>
            ))}
        </select>
    );
}
