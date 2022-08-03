CREATE TABLE users
(
    id              INT          NOT NULL PRIMARY KEY,
    name            VARCHAR(10)  NOT NULL,
    password_hashed VARCHAR(255) NOT NULL
);

CREATE TABLE posts
(
    id        INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    author_id INT         NOT NULL,
    title     VARCHAR(20) NOT NULL,
    content   TEXT        NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users (id)
);

CREATE TABLE comments
(
    id        INT  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    post_id   INT  NOT NULL,
    author_id INT  NOT NULL,
    content   TEXT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (author_id) REFERENCES users (id)
);

INSERT INTO users
VALUES (12191765, '박승재');
