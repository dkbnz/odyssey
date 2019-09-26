/*
 * Script used for profile creation. This is the third level for population.
 */
# --- !Ups

INSERT INTO `profile` (`id`, `username`, `password`, `first_name`, `middle_name`, `last_name`, `gender`, `date_of_birth`, last_seen_date, `date_of_creation`, `is_admin`, `profile_picture_id`) VALUES
(1, 'admin@travelea.com', '25F43B1486AD95A1398E3EEB3D83BC4010015FCC9BEDB35B432E00298D5021F7', 'Default', '', 'Admin', 'Male', '2018-01-01', '2019-01-01', '2019-01-01 13:00:00.000000', 1, NULL),
(2, 'guestUser@travelea.com', '6B93CCBA414AC1D0AE1E77F3FAC560C748A6701ED6946735A49D463351518E16', 'Dave', 'John', 'McInloch', 'Other', '1998-10-18', '2019-04-17', '2019-04-17 15:31:19.579000', 0, NULL),
(3, 'testuser1@email.com', '6B93CCBA414AC1D0AE1E77F3FAC560C748A6701ED6946735A49D463351518E16', 'Test', '', 'UserOne', 'Other', '1973-02-18', '2019-01-05', '2019-01-05 15:31:19.579000', 0, NULL),
(4, 'testuser2@email.com', '6B93CCBA414AC1D0AE1E77F3FAC560C748A6701ED6946735A49D463351518E16', 'Test', '', 'UserTwo', 'Other', '1982-05-12', '2019-02-04', '2019-02-04 15:31:19.579000', 0, NULL),
(5, 'testuser3@email.com', '6B93CCBA414AC1D0AE1E77F3FAC560C748A6701ED6946735A49D463351518E16', 'Test', '', 'UserThree', 'Other', '1971-01-23', '2019-03-03', '2019-03-03 15:31:19.579000', 0, NULL),
(6, 'testuser4@email.com', '6B93CCBA414AC1D0AE1E77F3FAC560C748A6701ED6946735A49D463351518E16', 'Test', '', 'UserFour', 'Other', '1986-11-03', '2019-04-02', '2019-04-02 15:31:19.579000', 0, NULL),
(7, 'testuser5@email.com', '6B93CCBA414AC1D0AE1E77F3FAC560C748A6701ED6946735A49D463351518E16', 'Test', '', 'UserFour', 'Other', '1986-11-03', '2019-04-02', '2019-04-02 15:31:19.579000', 0, NULL),
(8, 'testuser6@email.com', '6B93CCBA414AC1D0AE1E77F3FAC560C748A6701ED6946735A49D463351518E16', 'Test', '', 'UserFour', 'Other', '1986-11-03', '2019-04-02', '2019-04-02 15:31:19.579000', 0, NULL),
(9, 'testuser7@email.com', '6B93CCBA414AC1D0AE1E77F3FAC560C748A6701ED6946735A49D463351518E16', 'Test', '', 'UserFour', 'Other', '1986-11-03', '2019-04-02', '2019-04-02 15:31:19.579000', 0, NULL),
(10, 'testuser8@email.com', '6B93CCBA414AC1D0AE1E77F3FAC560C748A6701ED6946735A49D463351518E16', 'Test', '', 'UserFour', 'Other', '1986-11-03', '2019-04-02', '2019-04-02 15:31:19.579000', 0, NULL),
(11, 'testuser9@email.com', '6B93CCBA414AC1D0AE1E77F3FAC560C748A6701ED6946735A49D463351518E16', 'Test', '', 'UserFour', 'Other', '1986-11-03', '2019-04-02', '2019-04-02 15:31:19.579000', 0, NULL);


INSERT INTO `nationality_profile` (`profile_id`, `nationality_id`) VALUES
(1, 67),
(2, 4),
(2, 5),
(3, 65),
(3, 42),
(4, 34),
(5, 23),
(6, 49),
(7, 49),
(8, 49),
(9, 49),
(10, 49),
(11, 49);


INSERT INTO `traveller_type_profile` (`profile_id`, `traveller_type_id`) VALUES
(1, 6),
(2, 1),
(2, 2),
(3, 3),
(3, 5),
(4, 2),
(4, 6),
(5, 1),
(6, 7),
(6, 2),
(6, 4),
(7, 4),
(8, 4),
(9, 4),
(10, 4),
(11, 4);


INSERT INTO `passport_profile` (`profile_id`, `passport_id`) VALUES
(2, 3),
(2, 4),
(2, 5),
(3, 65),
(5, 23),
(6, 49);


# --- !Downs