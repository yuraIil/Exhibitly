
-- SEQUENCES
CREATE SEQUENCE category_id_seq START 1;
CREATE SEQUENCE exhibit_id_seq START 1;
CREATE SEQUENCE multimedia_id_seq START 1;
CREATE SEQUENCE report_id_seq START 1;
CREATE SEQUENCE users_id_seq START 1;

-- TABLE: category
CREATE TABLE category (
                          id INTEGER PRIMARY KEY DEFAULT nextval('category_id_seq'),
                          name VARCHAR(255) NOT NULL,
                          description TEXT
);

-- TABLE: exhibit
CREATE TABLE exhibit (
                         id INTEGER PRIMARY KEY DEFAULT nextval('exhibit_id_seq'),
                         name VARCHAR(255) NOT NULL,
                         category_id INTEGER NOT NULL,
                         description TEXT,
                         acquisition_date DATE,
                         multimedia_id INTEGER,
                         FOREIGN KEY (category_id) REFERENCES category(id),
                         FOREIGN KEY (multimedia_id) REFERENCES multimedia(id)
);

-- TABLE: multimedia
CREATE TABLE multimedia (
                            id INTEGER PRIMARY KEY DEFAULT nextval('multimedia_id_seq'),
                            exhibit_id INTEGER,
                            type VARCHAR(50) NOT NULL,
                            file_path VARCHAR(255) NOT NULL,
                            FOREIGN KEY (exhibit_id) REFERENCES exhibit(id)
);

-- TABLE: report
CREATE TABLE report (
                        id INTEGER PRIMARY KEY DEFAULT nextval('report_id_seq'),
                        type VARCHAR(50) NOT NULL,
                        generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        content TEXT
);

-- TABLE: users
CREATE TABLE users (
                       id INTEGER PRIMARY KEY DEFAULT nextval('users_id_seq'),
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL CHECK (role IN ('USER', 'ADMIN'))
);
