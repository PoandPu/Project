SET NAMES utf8;
SET CHARACTER SET UTF8;
-- ===============================================================
-- Create script DB for MySQL
-- ==============================================================
DROP DATABASE IF EXISTS TestScreeningDB;
CREATE DATABASE IF NOT EXISTS `TestScreeningDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
USE `TestScreeningDB`;

SET @old_unique_checks=@@unique_checks, @@unique_checks=0;
SET @old_foreign_key_checks = @@foreign_key_checks, @@foreign_key_checks = 0;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS difficulty_level;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS tests;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS results;
SET @@foreign_key_checks = @old_foreign_key_checks;
SET @@unique_checks = @old_unique_checks;
-- -----------------------------------------------------
-- Table `ScreeningDB`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TestScreeningDB`.`roles` (
	id INTEGER NOT NULL PRIMARY KEY,
	name VARCHAR(16) NOT NULL UNIQUE
);
-- insert data into roles table
INSERT INTO roles VALUES(0, 'admin');
INSERT INTO roles VALUES(1, 'client');


-- -----------------------------------------------------
-- Table `screeningdb`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TestScreeningDB`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(20) NOT NULL,
  `password` VARCHAR(256) NOT NULL,
  `password_key` VARCHAR(45) NOT NULL,
  `language` ENUM('ru', 'en') NOT NULL DEFAULT 'ru',
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(128) NOT NULL,
  `role_id` INT NOT NULL,
  `isBlocked` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
  INDEX `fk_users_roles_idx` (`role_id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  CONSTRAINT `fk_users_roles`
    FOREIGN KEY (`role_id`)
    REFERENCES `TestScreeningDB`.`roles` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT);

-- -----------------------------------------------------
-- Table `ScreeningDB`.`subjects`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TestScreeningDB`.`subjects` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name_ru` VARCHAR(45) NOT NULL,
  `name_en` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_ru_UNIQUE` (`name_ru` ASC) VISIBLE,
  UNIQUE INDEX `name_en_UNIQUE` (`name_en` ASC) VISIBLE);

-- -----------------------------------------------------
-- Table `ScreeningDB`.`difficulty_level`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TestScreeningDB`.`difficulty_level` (
  `id` INT NOT NULL,
  `name_ru` VARCHAR(45) NOT NULL,
  `name_en` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
   UNIQUE INDEX `name_ru_UNIQUE` (`name_ru` ASC) VISIBLE,
  UNIQUE INDEX `name_en_UNIQUE` (`name_en` ASC) VISIBLE);
  
INSERT INTO difficulty_level VALUES(1,'очень легко', 'very easy');
INSERT INTO difficulty_level VALUES(2,'легко', 'easy');
INSERT INTO difficulty_level VALUES(3,'нормально', 'medium');
INSERT INTO difficulty_level VALUES(4,'сложно', 'difficult');
INSERT INTO difficulty_level VALUES(5,'очень сложно', 'very difficult');

-- -----------------------------------------------------
-- Table `ScreeningDB`.`tests`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TestScreeningDB`.`tests` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name_ru` VARCHAR(128) NOT NULL,
  `name_en` VARCHAR(128) NOT NULL,
  `time_minutes` INT UNSIGNED NOT NULL,
  `numb_of_requests` INT UNSIGNED NOT NULL DEFAULT '0',
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `subject_id` INT NOT NULL,
  `difficulty_level_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_ru_UNIQUE` (`name_ru` ASC) VISIBLE,
  UNIQUE INDEX `name_en_UNIQUE` (`name_en` ASC) VISIBLE,
  INDEX `fk_tests_subject1_idx` (`subject_id` ASC) VISIBLE,
  INDEX `fk_tests_difficulty_level1_idx` (`difficulty_level_id` ASC) VISIBLE,
  CONSTRAINT `fk_tests_subject1`
    FOREIGN KEY (`subject_id`)
    REFERENCES `TestScreeningDB`.`subjects` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_tests_difficulty_level1`
    FOREIGN KEY (`difficulty_level_id`)
    REFERENCES `TestScreeningDB`.`difficulty_level` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT);

-- -----------------------------------------------------
-- Table `screeningdb`.`questions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TestScreeningDB`.`questions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title_ru` VARCHAR(1024) NOT NULL,
  `title_en` VARCHAR(1024) NOT NULL,
  `test_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_questions_tests1_idx` (`test_id` ASC) VISIBLE,
  CONSTRAINT `fk_questions_tests1`
    FOREIGN KEY (`test_id`)
    REFERENCES `TestScreeningDB`.`tests` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT);

-- -----------------------------------------------------
-- Table `screeningdb`.`answers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TestScreeningDB`.`answers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `option_ru` VARCHAR(128) NOT NULL,
  `option_en` VARCHAR(128) NOT NULL,
  `isCorrect` TINYINT(1) NULL DEFAULT NULL,
  `question_id` INT NOT NULL,
  PRIMARY KEY (`id`),
 -- UNIQUE INDEX `option_UNIQUE` (`option_ru` ASC) VISIBLE,
  INDEX `fk_answer_questions1_idx` (`question_id` ASC) VISIBLE,
 -- UNIQUE INDEX `option_en_UNIQUE` (`option_en` ASC) VISIBLE,
  CONSTRAINT `fk_answer_questions1`
    FOREIGN KEY (`question_id`)
    REFERENCES `TestScreeningDB`.`questions` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT);

-- -----------------------------------------------------
-- Table `screeningdb`.`results`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TestScreeningDB`.`results` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `mark` DECIMAL(5,2) UNSIGNED NOT NULL,
  `test_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `test_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_results_tests1_idx` (`test_id` ASC) VISIBLE,
  INDEX `fk_results_users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_results_tests1`
    FOREIGN KEY (`test_id`)
    REFERENCES `TestScreeningDB`.`tests` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_results_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `TestScreeningDB`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT);


INSERT INTO `users` (`login`, `password`, `password_key`, `first_name`, `last_name`, `email`, `role_id`) VALUES ("admin","hZZc126QKCHyugLiEAn9ZFGOP1r59CWBQX3kMKUM0is=","KPKaJ153m4I4GpQHPrI0eb8wio0Lji", "Алексей", "Павельчук", "alex80395@mail.ru", 0);

INSERT INTO `users` (`login`, `password`, `password_key`, `first_name`, `last_name`, `email`, `role_id`) VALUES ("student1","AArQDRSpDs+R81ysAa5tbZmcyA5B46SnxIoXAaegY/o=","CafvvpKw4RNDAsF7kZ3RhyIDWN82pO", "Алексей", "Оленич", "student1@mail.ru", 1);
INSERT INTO `users` (`login`, `password`,`password_key`, `first_name`, `last_name`, `email`, `role_id`) VALUES ("student2","VD61+34xG7psytB/I8gbfAZh2UFiNwvfKoTm9156I2s=", "R4KETSKwS0ocW8uEhxYqUyzkdXn6po","Роман", "Авраменко", "student2@mail.ru", 1);
INSERT INTO `users` (`login`, `password`,`password_key`, `first_name`, `last_name`, `email`, `role_id`) VALUES ("student3","cEb08HuOSroDbdp9ua/ctdWZHBGp7nir6GDRGpS3FYk=","IWFVTEwUI7oTMsEw0b6WqG2kRutxdW",  "Андрей", "Харченко", "student3@mail.ru", 1);
INSERT INTO `users` (`login`, `password`,`password_key`, `first_name`, `last_name`, `email`, `role_id`) VALUES ("student4","O20htAGgghzhhr3YO+/Fg28wGHgl+eRKVlIGksgJSuc=","UeMYbHGPxUbpPJTTnp8FgD2DHUNm8V",  "Нам", "Тран", "student4@mail.ru" , 1);
INSERT INTO `users` (`login`, `password`,`password_key`, `first_name`, `last_name`, `email`, `role_id`) VALUES ("student5","YVrQNz9AKP49Qs+iDMHGzSD+C0XtPylA/fqxbhuPWp0=","5ZatfaeQVE5myFX7gVumcYIYVeeuhZ",  "Ярослава", "Голенко", "student5@mail.ru", 1);


INSERT INTO subjects (name_ru, name_en) VALUE ('История','History');
INSERT INTO subjects (name_ru, name_en) VALUE ('Математика','Mathematics');
INSERT INTO subjects (name_ru, name_en) VALUE ('Информатика','Computer science');
INSERT INTO subjects (name_ru, name_en) VALUE ('Физика','Physics');
INSERT INTO subjects (name_ru, name_en) VALUE ('Иностранный язык','Foreign language');
INSERT INTO subjects (name_ru, name_en) VALUE ('Музыка','Music');
INSERT INTO subjects (name_ru, name_en) VALUE ('Литература','Literature');
INSERT INTO subjects (name_ru, name_en) VALUE ('Русский язык','Russian language');
INSERT INTO subjects (name_ru, name_en) VALUE ('География','Geography');

-- INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE ('История Украины','History of Ukraine', '1', '3', '10');

-- INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE ('История Древней Греции','Ancient Greek history', '1', '2', '8');
-- INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE ('География Украины','Geography of Ukraine', '10', '2', '5');


	INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE ('История Древнего Мира','Ancient World History', '1', '3', '5');

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('В каком ответе хронологически правильно указана последовательность событий древней истории?','Which answer chronologically correctly indicates the sequence of events in ancient history?', '1');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('Первые наскальные рисунки, изобретение лука и стрел', 'First cave paintings, invention of the bow and arrow', 1, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('Изготовление керамики, изобретение лука и стрел', 'The making of ceramics, the invention of the bow and arrow', 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('Появление наскальных рисунков, первые захоронения умерших', 'The appearance of cave paintings, the first burials of the dead', 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('Домостроительство, захоронение умерших', 'Construction, burial of the dead', 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('Изобретение лука и стрел, первые наскальные рисунки', 'Invention of the bow and arrow, the first cave paintings', 1);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Определите основные занятия древних людей, живших 30-14 тыс. лет тому назад.','Identify the main occupations of ancient people who lived 30-14 thousand years ago.', '1');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('охота, собирательство', 'hunting, gathering', 2);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('охота, собирательство, рыболовство', 'hunting, gathering, fishing', 2, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('собирательство, приручение животных, мотыжное земледелие', 'gathering, animal domestication, hoe farming', 2);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('ремесло, земледелие, скотоводство', 'craft, agriculture, cattle breeding', 2);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('охота, мотыжное земледелие, приручение животных', 'hunting, hoe farming, domestication', 2);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Кто из античных скульпторов создал прославленную статую Зевса Олимпийского?','Which of the ancient sculptors created the famous statue of Olympian Zeus?', '1');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('Поликлет', 'Polyclet', 3);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('Фидий', 'Phidias', 3, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('Мирон', 'Myron', 3);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('Гесиод', 'Hesiod', 3);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('Аполлодор', 'Apollodorus', 3);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Как древние греки представляли жизнь богов на горе Олимп? ','How did the ancient Greeks imagine the life of the gods on Mount Olympus?', '1');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('боги живут во дворцах, носят нарядные одежды, часто пируют', 'the gods live in palaces, wear smart clothes, often feast', 4, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('одни из богов богатые и знатные, другие - бедные, но так же, как и первые, могущественные', 'some of the gods are rich and noble, others are poor, but just like the first, powerful', 4);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('они, как многие знатные люди, властолюбивы, жестоки и мстительны', 'they, like many noble people, are power-hungry, cruel and vindictive', 4, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('боги ведут суровый образ жизни, следят за порядком и всегда готовы наказать людей', 'the gods lead a harsh lifestyle, keep order and are always ready to punish people', 4);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('всё из выше перечисленного', 'all of the above', 4);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Определите причины, способствовавшие разорению древнего Египетского царства? (3 правильных ответа)','Identify the reasons that contributed to the ruin of the ancient Egyptian kingdom? (3 correct answers)', '1');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('ослабление торговли', 'weakening of trade;', 5);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('высокие поборы', 'high fees', 5);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('разорение земледельцев', 'the ruin of farmers', 5, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('голод, вызванный неурожаями', 'hunger caused by crop failures', 5);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('восстание рабов и бедноты', 'the uprising of slaves and the poor', 5, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('нападение кочевых племен', 'attack by nomadic tribes', 5);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('захватнические войны египетских фараонов.', 'the wars of conquest of the Egyptian pharaohs.', 5, 1);


	INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE ('Всемирная История','The World History', '1', '5', '10');

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('В каком городе ежегодно проходит Всемирный экономический форум?','In which city does the World Economic Forum take place annually?', '2');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('Страсбурге', 'Strasbourg', 6);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('Брюсселе', 'Brussels', 6);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('Маастрихте', 'Maastricht', 6);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('Давосе', 'Davos', 6, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('Женеве', 'Geneva', 6);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Последствиями крестовых походов на Восток были: (3 правильных ответа)','The consequences of the Crusades to the East were:  (3 correct answers)', '2');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('оживление торговли по Средиземному морю', 'revitalization of trade in the Mediterranean', 7, 1);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('знакомство европейцев с новыми земледельческими культурами и ремеслом, изменения в быту', 'acquaintance of Europeans with new agricultural cultures and crafts, changes in everyday life', 7, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('образование самостоятельных государств', 'the formation of independent states', 7);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('возникновение торговых поселений в Сирии и Палестине', 'the emergence of trading settlements in Syria and Palestine', 7);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('усиление эксплуатации зависимых крестьян', 'increased exploitation of dependent peasants', 7, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('тяга феодалов к роскоши и рост эксплуатации крестьян.', 'the craving of the feudal lords for luxury and the growth of the exploitation of the peasants.', 7);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('В 1830 г в мире было 332 км железных дорог. Где находилась большая часть из них?','In 1830, there were 332 km of railways in the world. Where were most of them located?', '2');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('в Англии', 'in England', 8, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('во Франции', 'in France', 8);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('в США', 'in the USA', 8);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('в Германии', 'in Germany', 8);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('в Австрии', 'in Austria', 8);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Где впервые были обнаружены останки неандертальца?','Where were the remains of a Neanderthal first discovered?', '2');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('во Франции', 'in England', 9);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('в Испании', 'in France', 9);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('в Италии', 'in the USA', 9);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('в Германии', 'in Germany', 9, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('в Китае', 'in Austria', 9);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Определите основные задачи феодального государства в период раннего средневековья (2 правильных ответа)','Identify the main tasks of the feudal state in the early Middle Ages (2 correct answers)', '2');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('усиление народной власти', "strengthening the people's power", 10);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('подавление сопротивлений низших слоев общества', 'suppression of the resistance of the lower strata of society', 10, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('развитие ремесла', 'craft development', 10);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('ведение войн', 'warfare', 10, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('создание различных форм управления', 'creation of various forms of management', 10);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('В каком из ответов правильно отражена хронологическая последовательность возникновения мировых религий?','Which of the answers correctly reflects the chronological sequence of the emergence of world religions?', '2');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('буддизм, ислам, христианство', 'Buddhism, Islam, Christianity', 11);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('буддизм, христианство, ислам', 'Buddhism, Christianity, Islam', 11, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('зороастризм, ислам, христианство', 'Zoroastrianism, Islam, Christianity', 11);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('зороастризм, христианство, буддизм', 'Zoroastrianism, Christianity, Buddhism', 11);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('буддизм, зороастризм, христианство', 'Buddhism, Zoroastrianism, Christianity', 11);


	INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE ('История Азии','Asia History', '1', '4', '4');

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Какая знаменитая игра, возникшая в Индии, называется "четыре рода войск"?','What famous game that originated in India is called the "four branches of the army"?', '3');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('игральные кости', 'dice', 12);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('шахматы', 'chess', 12, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('домино', 'dominoes', 12);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('нарды', 'backgammon', 12);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('лото', 'lotto', 12);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Определите территорию, которая располагалась на пересечении стратегически важных морских путей в Азию.','Identify the area that was located at the intersection of strategically important sea routes to Asia.', '3');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('Гавайские острова', 'Hawaiian Islands', 13, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('Филиппины', 'Philippines', 13);
INSERT INTO answers (option_ru, option_en, question_id) VALUE (' Андаманские острова', 'Andaman Islands', 13);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('Индонезия', 'Indonesia', 13);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('Мальдивские острова', 'Maldives', 13);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('В каком году Филиппины были провозглашены независимой республикой?','In what year was the Philippines declared an independent republic?', '3');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('1945', '1945', 14);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('1946', '1946', 14, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE (' 1956', '1956', 14);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('1981', '1981', 14);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('1994', '1994', 14);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Какую религию исповедовали монголы при Чингисхане?','What religion did the Mongols profess under Genghis Khan?', '3');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('даосизм', 'Taoism', 15);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('конфуцианство', 'Confucianism', 15);
INSERT INTO answers (option_ru, option_en, question_id) VALUE (' буддизм', 'Buddhism', 15);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('шаманизм', 'Shamanism', 15, 1);


	INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE ('Элементарная математика','Elementary mathematics', '2', '3', '10');

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Сколько будет 2+2*2?','What is 2+2*2?', '4');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('10','10', 16);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('8', '8', 16);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('6','6', 16, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('4','4', 16);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Сколько секунд содержится в 1 часе 160 минутах и 2 секундах?','How many seconds are there in 1 hour 160 minutes and 2 seconds?', '4');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('13202','13202', 17, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('12202', '12202', 17);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('14202','14202', 17);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('106002','106002', 17);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Какое из высказываний относительно натуральных чисел ложное?','Which statement about natural numbers is false?', '4');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('на 5 делятся все числа, которые оканчиваются на цифры 0 или 5','All numbers that end in digits 0 or 5 are divisible by 5', 18);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('на 4 делятся все числа, которые оканчиваются цифрой 0 или 4', 'All numbers ending in 0 or 4 are divisible by 4', 18, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('чтобы число делилось на 12, достаточно, чтобы оно делилось на 3 и на 4','for a number to be divisible by 12, it is sufficient that it is divisible by 3 and 4', 18);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('наименьшим кратным данных чисел будет наименьшее число, которое делится без остатка на эти числа','the smallest multiple of these numbers will be the smallest number that is evenly divisible by these numbers', 18);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Вычислите:3,8 * (2,01 – 3,81)','Calculate: 3.8 * (2.01 - 3.81)', '4');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('-6,84','-6,84', 19, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('5,82', '5,82', 19);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('6,84','6,84', 19);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('-5,82','-5,82', 19);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Найдите число, обратное числу 0,8','Find the reciprocal of 0.8', '4');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('-5/4','-5/4', 20);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('8', '8', 20);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('-0,8','-0,8', 20);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('1,25','1,25', 20, 1);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Какая пара состоит из взаимно простых чисел?','Which pair is made up of coprime numbers?', '4');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('(11; 22)','(11; 22)', 21);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('(12; 35)', '(12; 35)', 21, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('(8; 14)','(8; 14)', 21);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('(12; 34)','(12; 34)', 21);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Разбейте число 434 на части, обратно пропорциональным числам 13 и 18.','Divide the number 434 into parts inversely proportional to the numbers 13 and 18.', '4');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('252 и 182','252 and 182', 22, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('238 и 196', '238 and 196', 22);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('192 и 242','192 and 242', 22);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('150 и 284','150 and 284', 22);


	INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE ('Алгебра','Algebra', '2', '4', '10');
    
INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Какие из нижеприведенных свойств для неравенств правильные? (3 правильных ответа)','Which of the following properties are correct for inequalities? (3 correct answers)', '5');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('если а > b и b > с, то а - с > 0','if a> b and b> c, then a - c> 0', 23, 1);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE (' если а > b и с > 0, то ас - bс > 0', 'if a> b and c> 0, then ac - bc> 0', 23, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('если а > b и с < 0, то ас - bс > 0','if a> b and c <0, then ac - bc> 0', 23);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('если а > b, то с - а > с - b','if a> b, then c - a> c - b', 23);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('если а > b > 0 и m > 0, то m/a – m/b < 0','if a> b> 0 and m> 0, then m / a - m / b <0', 23, 1);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Найдите произведение корней уравнения 4|х - 2| = 3 + (х - 2)2','Find the product of the roots of the equation 4 | x - 2 | = 3 + (x - 2) 2', '5');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('-3','-3', 24);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('3', '3', 24);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('15','15', 24);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('-15','-15', 24, 1);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Решите неравенство: |х - 1| ≥ 2.','Solve the inequality: | x - 1 | ≥ 2', '5');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE (' (-∞; -1] U [3; ∞)',' (-∞; -1] U [3; ∞)', 25, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('(-∞; -1]', '(-∞; -1]', 25);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('[-1; 3]','[-1; 3]', 25);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('[1; 3]','[1; 3]', 25);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Решите систему неравенств: (3x - 2)/4 > (1 - 5x)/6 | и | (3x - 1) ≤ 3 + 4x','Solve the system of inequalities:(3x - 2) / 4> (1 - 5x) / 6 | and |  (3x - 1) ≤ 3 + 4x', '5');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('(8/19; 4/5]','(8/19; 4/5]', 26);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('(-∞; 4/5]', '(-∞; 4/5]', 26);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('(8/19; ∞)','(8/19; ∞)', 26, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('x є R','x є R', 26);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Упростите выражение: |х — у| — |z — у| — |z — х|, если х < z < у.','Simplify the expression:| x - y | - | z - y | - | z - x |, if x <z <y.', '5');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('2z-2y','2z-2y', 27);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('2y-2z', '2y-2z', 27);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('0','0', 27, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('2у-2х','2у-2х', 27);


	INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE ('Тригонометрия','Trigonometry', '2', '2', '8');

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Упростите: (sin4a – sin6a) : (cos5a*sina)','Simplify: (sin4a - sin6a): (cos5a * sina)', '6');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('-2','-2', 28, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('2sina', '2sina', 28);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('-2sina','-2sina', 28);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('-2cosa','-2cosa', 28);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Упростите: 4 / (ctga – tga)','Simplify: 4 / (ctga - tga)', '6');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('tg2a','tg2a', 29);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('ctg2a', 'ctg2a', 29);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('2tg2a','2tg2a', 29, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('sin2a','sin2a', 29);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Вычислите: cos30°sin75°- cos60°sin15°.','Calculate: cos30° sin75° - cos60° sin15° ', '6');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('0','0', 30);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('√3/2', '√3/2', 30);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('1/2','1/2', 30);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('√2/2','√2/2', 30, 1);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Найдите ctgа, если tg(π/4 - а) = -5/3.','Find ctga if tan (π / 4 - a) = -5/3.', '6');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('1/3','1/3', 31);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('-1/4', '-1/4', 31, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('-1/3','-1/3', 31);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('-4','-4', 31);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('В каком ответе tg(—790)°, cos280° и sin510° знаки приведены в порядке их написания?','In which answer tg (-790)°, cos280° and sin510° are the signs in the order of their writing?', '6');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('+,-,+','+,-,+', 32);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('-,+,+', '-,+,+', 32, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('-,+,-','-,+,-', 32);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('+,-,-','+,-,-', 32);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Упростите: sin2a + sin2β - sin2a·sin2β + cos2a·cos2β','Simplify: sin2a + sin2β - sin2a sin2β + cos2a cos2β', '6');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('0','0', 33);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('-1', '-1', 33);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('-2','-2', 33);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('1','1', 33, 1);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('При каких значениях х верно неравенство sin2х – 5/2 sinx + 1 < 0, если х Є [0; 2π]?','For what values of х is the inequality sin2х - 5/2 sinx + 1 <0 true if х Є [0; 2π]?', '6');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('(π/6; 5π/6)','(π/6; 5π/6)', 34, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('[0; π/6) U (5π/6; 2π]', '[0; π/6) U (5π/6; 2π]', 34);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('(0; π/3) U (2π/3; 2π]','(0; π/3) U (2π/3; 2π]', 34);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('[0; π/3] U [2π/3; 2π]','[0; π/3] U [2π/3; 2π]', 34);


	INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE ('Основы алгоритмизации и языки программирования','Fundamentals of Algorithmization and Programming Languages', '3', '3', '15');

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Алгоритм — это:','The algorithm is:', '7');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('указание на выполнение действий','indication to take action', 35, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('система правил, описывающая последовательность действий, которые необходимо выполнить для решения задачи', 'system of rules describing the sequence of actions that must be performed to solve the problem', 35);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('процесс выполнения вычислений, приводящих к решению задачи','the process of performing computations leading to the solution of the problem.', 35);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Свойствами алгоритма являются: (4 правильных ответа)','The properties of the algorithm are: (4 correct answers) ', '7');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('информативность ','informativeness', 36);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('дискретность', 'discreteness', 36, 1);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('массовость','massness', 36, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('оперативность','efficiency', 36);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('определенность','certainty', 36, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('цикличность','cyclicality', 36);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('результативность','performance', 36, 1);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Алгоритм может быть задан следующими способами: (4 правильных ответа)','The algorithm can be set in the following ways: (4 correct answers)', '7');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('словесным ','verbal', 37, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('словесно-графическим', 'discreteness', 37);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE (' графическим','graphic', 37, 1);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('формально-словесным','formally verbal', 37, 1);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('на алгоритмическом языке','in algorithmic language', 37, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('последовательностью байтов','a sequence of bytes', 37);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Программа — это:','The program is:', '7');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('система правил, описывающая последовательность действий, которые необходимо выполнить для решения задачи ','system of rules describing the sequence of actions that must be performed to solve the problem', 38);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('указание на выполнение действий из заданного набора', 'indication to perform actions from a given set', 38);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('область внешней памяти для хранения текстовых, числовых данных и другой информации','External memory area for storing text, numeric data and other information', 38);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('последовательность команд, реализующая алгоритм решения задачи.','sequence of commands that implements the algorithm for solving the problem.', 38, 1);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Программа-интерпретатор выполняет:','The interpreter program does:', '7');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('поиск файлов на диске','search files on disk', 39);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('пооператорное выполнение программы', 'operative execution of the program', 39);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('полное выполнение программы','complete execution of the program', 39, 1);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Программа-компилятор выполняет:','The compiler program does:', '7');
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('переводит исходный текст в машинный код','translates the source code into machine code', 40);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('формирует текстовый файл', 'forms a text file', 40);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('записывает машинный код в форме загрузочного файла.','Writes machine code in the form of a boot file.', 40, 1);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Числовые данные могут быть представлены как: (3 правильных ответа)','Numerical data can be represented as: (3 correct answers)', '7');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('целые','integral', 41, 1);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('с фиксированной точкой', 'fixed point', 41, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('в виде строк','as strings', 41);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('с плавающей точкой.','floating point.', 41, 1);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Если тип данных несет текстовую информацию, то он должен быть заключен в кавычки:','If the data type carries textual information, then it must be enclosed in quotation marks:', '7');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('верно','true', 42, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('не верно', 'false', 42);

INSERT INTO questions (title_ru, title_en, test_id) VALUE ('Арифметические выражения состоят из: (5 правильных ответов)','Arithmetic expressions consist of: (5 correct answers)', '7');
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('чисел','numbers', 43, 1);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('констант','constants', 43, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('команд MS-DOS', 'MS-DOS commands', 43);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('машинных команд', 'machine commands', 43);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('переменных','variables', 43, 1);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('функций','Functions', 43, 1);
INSERT INTO answers (option_ru, option_en, question_id, isCorrect) VALUE ('круглых скобок','parentheses', 43, 1);
INSERT INTO answers (option_ru, option_en, question_id) VALUE ('квадратных скобок', 'square brackets', 43);


-- 	INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE ('Пустой тест1','Empty test1', '2', '1', '3');
-- 	INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE ('Пустой тест2','Empty test2', '2', '2', '12');
--  INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE ('Пустой тест3','Empty test3', '2', '3', '5');
-- 	INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE ('Пустой тест4','Empty test4', '2', '4', '15');
-- 	INSERT INTO tests (name_ru, name_en, subject_id, difficulty_level_id, time_minutes) VALUE ('Пустой тест5','Empty test5', '2', '5', '10');

-- INSERT INTO `results` (`mark`, `entrant_id`, `test_id`) VALUES (33.33, 2, 1);

-- UPDATE tests SET numb_of_requests = numb_of_requests + 1 WHERE id = 1;
-- SELECT * FROM tests;
-- SELECT * FROM questions;
SELECT * FROM users;
-- SELECT * FROM results WHERE entrant_id = 2;
-- SELECT results.mark, results.test_date, tests.name_Ru, tests.name_En, subjects.name_Ru, subjects.name_En FROM tests 
-- 	JOIN results ON tests.id = results.test_id 
 --    JOIN subjects ON tests.subject_id = subjects.id
 --    WHERE results.entrant_id = 2;
-- SELECT users.login, users.password, users.language, entrants.* FROM users JOIN entrants ON entrants.id = users.id WHERE users.id=2;
-- SELECT * FROM subjects WHERE id = 1;
-- SELECT * FROM difficulty_level WHERE id = 3;
-- UPDATE tests SET name_ru="As", name_en="EN", time_minutes = "5", difficulty_level_id = "2" WHERE id=1;
-- SELECT * FROM answers;
-- SELECT users.login, users.password, users.language, entrants.* FROM users JOIN entrants ON entrants.id = users.id ORDER BY first_name DESC LIMIT 0, 10;
-- SELECT * FROM results ORDER BY test_date;
-- SELECT * FROM difficulty_level;
-- DELETE FROM users WHERE id = 7;
-- SELECT * FROM questions WHERE test_id = 1;
-- SELECT * FROM difficulty_level WHERE id = 3;
-- SELECT * FROM tests WHERE subject_id = 2;
-- SELECT * FROM questions WHERE test_id = 3;
-- SELECT * FROM answers  Join questions ON test_id = 2 WHERE question_id = 1;
-- SELECT * FROM answers WHERE question_id = 1;
-- SELECT * FROM answers, questions Where questions.test_id=1 AND answers.question_id=1;
-- SELECT id FROM answers Where question_id=4 AND isCorrect=1;
-- SELECT time_minutes FROM tests Where id=1;
-- SELECT users.login, users.password, users.language, entrants.* FROM users JOIN entrants ON entrants.id = users.id WHERE users.login='student1';
-- SELECT results.mark, results.test_date, tests.name_Ru, tests.name_En, subjects.name_Ru, subjects.name_En FROM tests JOIN results ON tests.id = results.test_id JOIN subjects ON tests.subject_id = subjects.id WHERE results.entrant_id = 1;
-- SELECT results.mark, results.test_date, tests.name_Ru, tests.name_En FROM tests JOIN results ON tests.id = results.test_id WHERE results.entrant_id = 1;
-- SELECT subjects.name_Ru, subjects.name_En FROM tests JOIN results ON tests.id = results.test_id JOIN subjects ON tests.subject_id = subjects.id WHERE results.entrant_id = 1;
-- SELECT subjects.name_Ru, subjects.name_En FROM tests JOIN subjects ON tests.subject_id = subjects.id WHERE tests.name_Ru = "История Древнего Мира";
-- DELETE FROM questions WHERE id = 1;

