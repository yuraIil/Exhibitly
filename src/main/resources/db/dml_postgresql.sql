
-- Categories
INSERT INTO category (name, description) VALUES
                                             ('Painting', 'Painting category'),
                                             ('Sculpture', 'Sculpture category'),
                                             ('Photograph', 'Photograph category'),
                                             ('Document', 'Document category'),
                                             ('Artifact', 'Artifact category'),
                                             ('Jewelry', 'Jewelry category'),
                                             ('Textile', 'Textile category'),
                                             ('Tool', 'Tool category'),
                                             ('Weapon', 'Weapon category'),
                                             ('Other', 'Other category');

-- Users
INSERT INTO users (username, password, role) VALUES
                                                 ('admin', 'admin123', 'ADMIN'),
                                                 ('user1', 'user123', 'USER');

-- Reports
INSERT INTO report (type, content) VALUES
                                       ('stats', 'Initial statistics report'),
                                       ('error', 'Example error report');
