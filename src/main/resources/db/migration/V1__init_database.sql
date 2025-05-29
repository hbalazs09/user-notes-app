CREATE TABLE "user"
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted    BOOLEAN   DEFAULT FALSE,
    version    BIGINT    DEFAULT 0
);

CREATE TABLE note
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT        NOT NULL,
    text       VARCHAR(1000) NOT NULL,
    completed  BOOLEAN   DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted    BOOLEAN   DEFAULT FALSE,
    version    BIGINT    DEFAULT 0,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user" (id)
);


INSERT INTO "user" (name, deleted)
VALUES ('Alice', false);
INSERT INTO "user" (name, deleted)
VALUES ('Bob', false);
INSERT INTO "user" (name, deleted)
VALUES ('Charlie', false);
INSERT INTO "user" (name, deleted)
VALUES ('Diana', false);
INSERT INTO "user" (name, deleted)
VALUES ('Eve', false);

INSERT INTO note (user_id, text, completed, deleted)
VALUES (1, 'Buy milk', false, false);
INSERT INTO note (user_id, text, completed, deleted)
VALUES (1, 'Send email', true, false);
INSERT INTO note (user_id, text, completed, deleted)
VALUES (2, 'Call mom', false, false);
INSERT INTO note (user_id, text, completed, deleted)
VALUES (3, 'Finish project', true, false);
INSERT INTO note (user_id, text, completed, deleted)
VALUES (4, 'Go to the gym', false, false);
INSERT INTO note (user_id, text, completed, deleted)
VALUES (5, 'Book flight tickets', true, false);
INSERT INTO note (user_id, text, completed, deleted)
VALUES (5, 'Read a book', false, false);