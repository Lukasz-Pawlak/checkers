DROP DATABASE IF EXISTS games;
CREATE DATABASE games;
USE games;

CREATE OR REPLACE TABLE games (
id INT UNSIGNED AUTO_INCREMENT,
numOfPlayers INT UNSIGNED NOT NULL,
PRIMARY KEY (id)
);

CREATE OR REPLACE TABLE moves (
game INT UNSIGNED NOT NULL,
moveNumber INT UNSIGNED NOT NULL,
oldX INT UNSIGNED NOT NULL,
oldY INT UNSIGNED NOT NULL,
newX INT UNSIGNED NOT NULL,
newY INT UNSIGNED NOT NULL,
FOREIGN KEY (game) REFERENCES  games(id),
PRIMARY KEY (game, moveNumber)
);

DELIMITER $$

CREATE OR REPLACE PROCEDURE getGameMoves(IN id INT)
BEGIN
    SELECT moves.oldX AS oldX, moves.oldY AS oldY, moves.newX AS newX, moves.newY AS newY
    FROM moves
    WHERE moves.game = id;
END; $$

CREATE OR REPLACE PROCEDURE addMove(IN agame INT, IN moveNum INT, IN aoldX INT, IN aoldY INT, IN anewX INT, IN anewY INT, OUT success BOOL)
BEGIN
    IF (EXISTS(SELECT * FROM games WHERE id = agame)) THEN
        INSERT INTO moves (game, moveNumber, oldX, oldY, newX, newY)
        VALUES(agame, moveNum, aoldX, aoldY, anewX, anewY);
        SELECT TRUE INTO success;
    ELSE
        SELECT FALSE INTO success;
    END IF;
END; $$

CREATE OR REPLACE PROCEDURE createNewGame(IN anumOfPlayers INT, OUT result INT)
BEGIN
    INSERT INTO games (numOfPlayers)
    VALUES (anumOfPlayers);
    SELECT LAST_INSERT_ID() INTO result;
END; $$

DELIMITER ;

-- testy
SET @out_value = -1 ;
CALL createNewGame(2, @out_value);
SELECT @out_value;
SET @moveAdded = FALSE;
CALL addMove(1, 1, 1, 1, 1, 1, @moveAdded);
SELECT @moveAdded;
CALL getGameMoves(1);
CALL getGameMoves(2);