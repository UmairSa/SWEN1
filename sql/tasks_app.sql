CREATE DATABASE mzcgdb;

CREATE TABLE IF NOT EXISTS task (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    done BOOLEAN
);

CREATE TABLE Users (
    UserID SERIAL PRIMARY KEY,
    Username VARCHAR(255) UNIQUE NOT NULL,
    Password VARCHAR(255) NOT NULL,
    Coins INT DEFAULT 0,
    ELO INT DEFAULT 100
);

INSERT INTO users (username, password, coins, elo) VALUES ('testuser', 'testpass', 100, 1000);

TRUNCATE users;