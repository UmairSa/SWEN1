CREATE DATABASE mtcgdb;

DROP TABLE users CASCADE;
DROP TABLE packages cascade;
drop table cards cascade;
drop table battle cascade;
drop table trade cascade;


CREATE TABLE IF NOT EXISTS users
(
    userid   SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    coins    INT DEFAULT 20,
    elo      INT DEFAULT 100,
    name     varchar(255),
    bio      varchar(255),
    image    varchar(255),
    wins     INT DEFAULT 0,
    losses   INT DEFAULT 0
);


CREATE TABLE IF NOT EXISTS packages
(
    packageid INT PRIMARY KEY,
    price     INT DEFAULT 5 NOT NULL
);


CREATE TABLE IF NOT EXISTS cards
(
    cardid      UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    damage      DECIMAL NOT NULL,
    elementtype VARCHAR(255) CHECK (elementtype IN ('Fire', 'Water', 'Normal')),
    cardtype    VARCHAR(255) CHECK (cardtype IN ('Monster', 'Spell')),
    indeck      BOOLEAN DEFAULT FALSE,
    ownerid     INT REFERENCES users (userid) ON DELETE SET NULL,
    packageid   INT REFERENCES packages (packageid) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS trade
(
    tradeid          UUID PRIMARY KEY,
    cardtotradeid    UUID NOT NULL REFERENCES cards (cardid),
    cardtype         VARCHAR(255),
    minimumdamage    DECIMAL,
    user1id          INT NOT NULL REFERENCES users (userid),
    user2id          INT REFERENCES users (userid)
);

CREATE TABLE IF NOT EXISTS battle
(
    battleid     SERIAL PRIMARY KEY,
    player1id    INT NOT NULL REFERENCES users (userid),
    player2id    INT NOT NULL REFERENCES users (userid),
    battlelog    TEXT,
    battleoutcome VARCHAR(255),
    createdat    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);