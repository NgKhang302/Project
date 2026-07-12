-- Reference schema for the EduApp database.
-- Hibernate (ddl-auto=update) manages the schema at runtime; this file is used
-- to pre-initialize a fresh PostgreSQL instance (e.g. via docker-compose) and
-- as authoritative documentation of the data model.

CREATE TABLE IF NOT EXISTS users (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    email         VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'USER',
    created_at    TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS categories (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    slug        VARCHAR(120) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS lessons (
    id           BIGSERIAL PRIMARY KEY,
    category_id  BIGINT       NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    title        VARCHAR(200) NOT NULL,
    slug         VARCHAR(220) NOT NULL UNIQUE,
    content      TEXT,
    content_type VARCHAR(20)  NOT NULL,
    audio_url    VARCHAR(500),
    cefr_level   VARCHAR(5),
    created_at   TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_lessons_category_id ON lessons(category_id);
ALTER TABLE lessons ADD COLUMN IF NOT EXISTS cefr_level VARCHAR(5);
CREATE INDEX IF NOT EXISTS idx_lessons_cefr_level ON lessons(cefr_level);

CREATE TABLE IF NOT EXISTS dialogue_lines (
    id           BIGSERIAL PRIMARY KEY,
    lesson_id    BIGINT NOT NULL REFERENCES lessons(id) ON DELETE CASCADE,
    speaker      VARCHAR(100) NOT NULL,
    text         TEXT NOT NULL,
    order_index  INT NOT NULL DEFAULT 0
);
CREATE INDEX IF NOT EXISTS idx_dialogue_lines_lesson_id ON dialogue_lines(lesson_id);

CREATE TABLE IF NOT EXISTS quizzes (
    id              BIGSERIAL PRIMARY KEY,
    lesson_id       BIGINT NOT NULL REFERENCES lessons(id) ON DELETE CASCADE,
    question        TEXT   NOT NULL,
    options         TEXT   NOT NULL,
    correct_answer  VARCHAR(500) NOT NULL,
    explanation     TEXT
);
CREATE INDEX IF NOT EXISTS idx_quizzes_lesson_id ON quizzes(lesson_id);

CREATE TABLE IF NOT EXISTS user_progress (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    lesson_id     BIGINT NOT NULL REFERENCES lessons(id) ON DELETE CASCADE,
    status        VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
    score         DOUBLE PRECISION,
    completed_at  TIMESTAMP,
    UNIQUE (user_id, lesson_id)
);
CREATE INDEX IF NOT EXISTS idx_user_progress_user_id ON user_progress(user_id);

CREATE TABLE IF NOT EXISTS user_quiz_results (
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    quiz_id      BIGINT NOT NULL REFERENCES quizzes(id) ON DELETE CASCADE,
    user_answer  VARCHAR(500),
    is_correct   BOOLEAN NOT NULL,
    "timestamp"  TIMESTAMP NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_user_quiz_results_user_id ON user_quiz_results(user_id);

CREATE TABLE IF NOT EXISTS writing_submissions (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    lesson_id     BIGINT NOT NULL REFERENCES lessons(id) ON DELETE CASCADE,
    content       TEXT   NOT NULL,
    word_count    INT    NOT NULL DEFAULT 0,
    errors_json   TEXT,
    error_count   INT    NOT NULL DEFAULT 0,
    submitted_at  TIMESTAMP NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_writing_submissions_user_lesson ON writing_submissions(user_id, lesson_id);
ALTER TABLE writing_submissions ADD COLUMN IF NOT EXISTS errors_json TEXT;
ALTER TABLE writing_submissions ADD COLUMN IF NOT EXISTS error_count INT NOT NULL DEFAULT 0;
