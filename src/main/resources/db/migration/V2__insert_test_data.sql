INSERT INTO players (name)
VALUES ('Novak Djokovic'),
       ('Alexander Zverev'),
       ('Carlos Alcaraz'),
       ('Jannik Sinner'),
       ('Daniil Medvedev'),
       ('Andrey Rublev'),
       ('Taylor Fritz'),
       ('Hubert Hurkacz'),
       ('Casper Ruud');

DO $$
BEGIN
    FOR first_player_id IN 1..9 LOOP
        FOR second_player_id IN first_player_id + 1 .. 9 LOOP
            INSERT INTO matches (first_player_id, second_player_id, winner_id)
            VALUES (first_player_id, second_player_id, first_player_id);

            INSERT INTO matches (first_player_id, second_player_id, winner_id)
            VALUES (first_player_id, second_player_id, second_player_id);
            END LOOP;
        END LOOP;
END $$;
