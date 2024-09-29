CREATE TABLE IF NOT EXISTS players (
    id   SERIAL      PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS matches (
    id               SERIAL PRIMARY KEY,
    first_player_id  INT    NOT NULL,
    second_player_id INT    NOT NULL,
    winner_id        INT    NOT NULL,
    FOREIGN KEY (first_player_id)  REFERENCES players(id),
    FOREIGN KEY (second_player_id) REFERENCES players(id),
    FOREIGN KEY (winner_id)        REFERENCES players(id),
    CONSTRAINT check_players_unique CHECK (first_player_id <> second_player_id)
);