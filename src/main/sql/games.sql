-- SET GLOBAL time_zone = '-6:00';

DROP DATABASE IF EXISTS games;
CREATE DATABASE games;
USE games;

CREATE OR REPLACE TABLE game (
id INT UNSIGNED AUTO_INCREMENT,
numOfPlayers INT UNSIGNED NOT NULL,
PRIMARY KEY (id)
);

CREATE OR REPLACE TABLE move (
game INT UNSIGNED NOT NULL,
moveNumber INT UNSIGNED NOT NULL,
oldX INT UNSIGNED NOT NULL,
oldY INT UNSIGNED NOT NULL,
newX INT UNSIGNED NOT NULL,
newY INT UNSIGNED NOT NULL,
FOREIGN KEY (game) REFERENCES  game(id),
PRIMARY KEY (game, moveNumber)
);

DELIMITER $$

-- looks like wee don't use this procedures, just doing regular sql
-- so do we leave it like this?
-- idk, we can delete them at the end ig, I'll leave a TODO so we don't forget about it
CREATE OR REPLACE PROCEDURE getGameMoves(IN id INT)
BEGIN
    SELECT move.oldX AS oldX, move.oldY AS oldY, move.newX AS newX, move.newY AS newY
    FROM move
    WHERE move.game = id;
END; $$

CREATE OR REPLACE PROCEDURE addMove(IN agame INT, IN moveNum INT, IN aoldX INT, IN aoldY INT, IN anewX INT, IN anewY INT, OUT success BOOL)
BEGIN
    IF (EXISTS(SELECT * FROM game WHERE id = agame)) THEN
        INSERT INTO move (game, moveNumber, oldX, oldY, newX, newY)
        VALUES(agame, moveNum, aoldX, aoldY, anewX, anewY);
        SELECT TRUE INTO success;
    ELSE
        SELECT FALSE INTO success;
    END IF;
END; $$

CREATE OR REPLACE PROCEDURE createNewGame(IN anumOfPlayers INT, OUT result INT)
BEGIN
    INSERT INTO game (numOfPlayers)
    VALUES (anumOfPlayers);
    SELECT LAST_INSERT_ID() INTO result;
END; $$

DELIMITER ;

-- testy
/*
SET @out_value = -1 ;
CALL createNewGame(2, @out_value);
SELECT @out_value;
SET @moveAdded = FALSE;
CALL addMove(1, 1, 1, 1, 1, 1, @moveAdded);
SELECT @moveAdded;
CALL getGameMoves(1);
CALL getGameMoves(2);
*/