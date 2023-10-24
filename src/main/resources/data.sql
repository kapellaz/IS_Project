

-- Inserir 10 proprietários
INSERT INTO owner (name, telephone_number) VALUES ('José', '123-456-7891');
INSERT INTO owner (name, telephone_number) VALUES ('Miguel', '234-567-8912');
INSERT INTO owner (name, telephone_number) VALUES ('Luis', '345-678-9123');
INSERT INTO owner (name, telephone_number) VALUES ('Bruno', '456-789-1234');
INSERT INTO owner (name, telephone_number) VALUES ('Rui', '567-891-2345');
INSERT INTO owner (name, telephone_number) VALUES ('Tomás', '678-912-3456');
INSERT INTO owner (name, telephone_number) VALUES ('Ricardo', '789-123-4567');
INSERT INTO owner (name, telephone_number) VALUES ('Nuno', '962-312-1234');
INSERT INTO owner (name, telephone_number) VALUES ('João', '789-123-4567');
INSERT INTO owner (name, telephone_number) VALUES ('Samuel', '891-234-5678');
INSERT INTO owner (name, telephone_number) VALUES ('David', '912-345-6789');
INSERT INTO owner (name, telephone_number) VALUES ('Anibal', '123-456-7890');

-- Proprietário 1
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Max', 'Dog', '2023-03-15', 11, 1);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Bella', 'Cat', '2022-07-20', 6, 1);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Lucy', 'Dog', '2023-05-12', 12, 1);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Cooper', 'Dog', '2022-11-28', 13, 1);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Daisy', 'Cat', '2023-02-10', 10, 1);

-- Proprietário 2
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Oliver', 'Dog', '2023-04-05', 14, 2);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Luna', 'Cat', '2022-08-16', 4, 2);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Tucker', 'Dog', '2023-06-22', 15, 2);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Sophie', 'Dog', '2022-12-10', 16, 2);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Leo', 'Cat', '2023-01-18', 10, 2);

-- Proprietário 3
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Molly', 'Dog', '2023-03-08', 17, 3);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Simba', 'Cat', '2022-07-05', 8, 3);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Buddy', 'Dog', '2023-05-20', 18, 3);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Misty', 'Cat', '2022-11-15', 9, 3);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Rocky', 'Dog', '2023-01-30', 19, 3);

-- Proprietário 4
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Baxter', 'Dog', '2023-03-12', 13, 4);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Sasha', 'Cat', '2022-06-18', 10, 4);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('LolaD', 'Dog', '2023-05-25', 14, 4);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Mittens', 'Cat', '2022-11-28', 11, 4);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Sammy', 'Dog', '2023-02-14', 9, 4);

-- Proprietário 5
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Teddy', 'Dog', '2023-04-10', 16, 5);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Milo', 'Cat', '2022-08-22', 9, 5);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Oscar', 'Dog', '2023-06-05', 9, 5);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Whiskers', 'Cat', '2022-12-15', 5, 5);

-- Proprietário 6
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Rocky', 'Dog', '2023-03-25', 17, 6);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Mittens', 'Cat', '2022-07-30', 9, 6);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Bentley', 'Dog', '2023-05-08', 12, 6);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Smokey', 'Cat', '2022-11-12', 13, 6);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Ruby', 'Dog', '2023-01-24', 5, 6);

-- Proprietário 7
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Maximus', 'Dog', '2023-03-18', 11, 7);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('LolaC', 'Cat', '2022-07-25', 14, 7);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Duke', 'Dog', '2023-05-10', 13, 7);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Muffin', 'Cat', '2022-11-30', 12, 7);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Roxy', 'Dog', '2023-02-05', 16, 7);

-- Proprietário 8 (um único animal de estimação)
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Misty', 'Cat', '2023-04-02', 14, 8);

-- Proprietário 9 
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Rusty', 'Dog', '2023-03-30', 6, 9);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Sasha', 'Cat', '2022-08-10', 13, 9);

-- Proprietário 10 (sete animais de estimação)
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Buddy', 'Dog', '2023-04-15', 6, 10);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Luna', 'Cat', '2022-07-20', 17, 10);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Rosie', 'Dog', '2023-05-12', 7, 10);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Simba', 'Cat', '2022-11-28', 19, 10);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Maddie', 'Dog', '2023-02-10', 13, 10);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Ziggy', 'Cat', '2023-03-20', 6, 10);
INSERT INTO pet (name, species, birthdate, weight, owner_id) VALUES ('Olive', 'Dog', '2023-04-05', 20, 10);
