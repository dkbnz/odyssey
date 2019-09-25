/*
 * Script to emulate user interaction. This is the forth level for population.
 */
# --- !Ups

INSERT INTO `achievement_tracker` (`id`, `points`, `owner_id`, `current_streak`) VALUES
(1, 0, 1, 0),
(2, 0, 2, 0),
(3, 0, 3, 0),
(4, 0, 4, 0),
(5, 0, 5, 0),
(6, 0, 6, 0),
(7, 99, 7, 3),
(8, 999, 8, 6),
(9, 9999, 9, 30),
(10, 0, 10, 30),
(11, 0, 11, 30);


INSERT INTO `badge_progress` (`id`, `badge_id`, `achievement_tracker_id`, `progress`) VALUES
(1, 1, 7, 0),
(2, 1, 8, 9),
(3, 1, 9, 29),
(4, 2, 7, 0),
(5, 2, 8, 9),
(6, 2, 9, 49),
(7, 3, 7, 0),
(8, 3, 8, 14),
(9, 3, 9, 49),
(10, 4, 7, 0),
(11, 4, 8, 19),
(12, 4, 9, 49),
(13, 5, 7, 0),
(14, 5, 8, 4),
(15, 5, 9, 9),
(16, 6, 7, 0),
(17, 6, 8, 9),
(18, 6, 9, 29),
(19, 7, 7, 99999),
(20, 7, 8, 499999),
(21, 7, 9, 999999),
(22, 8, 7, 99),
(23, 8, 8, 999),
(24, 8, 9, 9999),
(25, 9, 7, 3),
(26, 9, 8, 6),
(27, 9, 9, 30),
(28, 5, 11, 0);


INSERT INTO `destination` (`id`, `name`, `type_id`, `district`, `latitude`, `longitude`, `country`, `is_public`, `owner_id`) VALUES
(119, 'Angus Flat', 31, 'Canterbury', -43.65598, 170.48378, 'New Zealand', true, 5),
(325, 'Baylys Beach Post Office', 10, 'North Auckland', -35.953527, 173.74573, 'New Zealand', false, 2),
(567, 'Bow Alley Creek', 89, 'Otago', -45.239576, 170.851946, 'New Zealand', true, 2),
(733, 'Cameron Stream', 89, 'Canterbury', -42.23865, 173.046403, 'New Zealand', true, 1),
(858, 'Cerberus', 38, 'Wellington', -40.512442, 176.213727, 'New Zealand', true, 1),
(1031, 'Courtenay Peak', 38, 'Otago', -44.529264, 168.195041, 'New Zealand', true, 1),
(1155, 'Demon Gap Icefall', 42, 'Otago', -44.392145, 168.361981, 'New Zealand', true, 1),
(1465, 'Feldspar Stream', 89, 'Southland', -45.03797, 167.347403, 'New Zealand', true, 1),
(1526, 'Flat Stream', 89, 'Marlborough', -41.940145, 173.111533, 'New Zealand', true, 1),
(1797, 'Greenstone Stream', 89, 'Canterbury', -42.590148, 172.745206, 'New Zealand', true, 1),
(1834, 'Haines Stream', 89, 'Nelson', -41.295971, 172.664481, 'New Zealand', true, 1),
(1894, 'Hart Creek', 89, 'Wellington', -40.729439, 175.430851, 'New Zealand', true, 1),
(1940, 'Headlong Peak', 38, 'Otago', -44.539752, 168.591617, 'New Zealand', true, 1),
(2035, 'Hitchin Range', 66, 'Westland', -43.111555, 170.824539, 'New Zealand', true, 1),
(2194, 'Iris Stream', 89, 'North Auckland', -36.970301, 174.530236, 'New Zealand', true, 1),
(2275, 'Johnstone Mount', 38, 'Canterbury', -43.37552, 170.857361, 'New Zealand', true, 1),
(2426, 'Kaurimu Stream', 89, 'North Auckland', -36.911035, 174.623382, 'New Zealand', true, 1),
(2439, 'Kawaunui Stream', 89, 'South Auckland', -38.353659, 176.310498, 'New Zealand', true, 1),
(2461, 'Kelleher', 38, 'Wellington', -40.784535, 175.376598, 'New Zealand', true, 1),
(2593, 'Krushen Stream', 89, 'Marlborough', -41.825296, 173.260128, 'New Zealand', true, 1),
(2631, 'Lake Rotoroa (Hamilton Lake)', 46, 'South Auckland', -37.798629, 175.27484, 'New Zealand', true, 1),
(2657, 'Lake Donne', 46, 'Canterbury', -43.608439, 171.115709, 'New Zealand', true, 1),
(2775, 'Lathrop Saddle', 54, 'Westland', -42.91523, 171.277535, 'New Zealand', true, 1),
(2822, 'Lily Creek', 89, 'Westland', -44.032015, 169.474478, 'New Zealand', true, 1),
(2921, 'Long Spur Stream', 89, 'Canterbury', -43.060244, 172.219981, 'New Zealand', true, 1),
(2962, 'Lumber Flat', 31, 'Westland', -44.224114, 168.659968, 'New Zealand', true, 1),
(3218, 'Maraeweka Stream', 89, 'Otago', -45.150038, 170.741188, 'New Zealand', true, 1),
(3338, 'Maungawhiorangi', 93, 'Gisborne', -38.174833, 177.243242, 'New Zealand', true, 1),
(3360, 'McCallum Stream', 89, 'Marlborough', -41.797334, 173.260076, 'New Zealand', true, 1),
(3558, 'Morgan Stream', 89, 'Canterbury', -43.59628, 171.339142, 'New Zealand', true, 1),
(3577, 'Bern', 18, 'Bern', 46.947832, 7.447618, 'Switzerland', true, 1),
(3580, 'Mother Millers Spring', 87, 'Canterbury', -43.358825, 171.288873, 'New Zealand', true, 1),
(3594, 'Motukauatirahi/Cass Bay', 6, 'Canterbury', -43.607459, 172.692363, 'New Zealand', true, 1),
(3607, 'Motuoapa Peninsula', 55, 'South Auckland', -38.924214, 175.859163, 'New Zealand', true, 1),
(3769, 'Mount Meehan', 38, 'Canterbury', -42.919966, 172.300892, 'New Zealand', true, 1),
(3852, 'Mount William Grant', 38, 'Canterbury', -43.704591, 170.32112, 'New Zealand', true, 1),
(3973, 'Nga Tamahineapani', 75, 'Nelson', -40.689108, 173.948723, 'New Zealand', true, 1),
(4087, 'Nym Peak', 38, 'Canterbury', -43.34196, 170.843819, 'New Zealand', true, 1),
(4109, 'Ogilvie Creek', 89, 'Westland', -42.559882, 171.326201, 'New Zealand', true, 1),
(4186, 'Omahuri', 59, 'North Auckland', -34.822269, 173.414253, 'New Zealand', true, 1),
(4216, 'Onetohunga Stream', 89, 'Gisborne', -38.114435, 178.219536, 'New Zealand', true, 1),
(4239, 'Orau Gorge', 95, 'North Auckland', -36.182429, 175.084831, 'New Zealand', true, 1),
(4357, 'Pacific Bay', 6, 'North Auckland', -35.618672, 174.536016, 'New Zealand', true, 1),
(4515, 'Patuki Mountain', 38, 'Southland', -44.669468, 168.021972, 'New Zealand', true, 1),
(4634, 'Pioke', 38, 'Taranaki', -39.167798, 173.967902, 'New Zealand', true, 1),
(4659, 'Plumbago Stream', 89, 'Wellington', -41.390123, 174.895805, 'New Zealand', true, 1),
(4741, 'Poututu Rural Sections', 2, 'Hawke''s Bay', -39.056581, 177.309005, 'New Zealand', true, 1),
(4775, 'Puffer Saddle', 54, 'Wellington', -41.073802, 175.242171, 'New Zealand', true, 1),
(4873, 'Putataua Bay', 6, 'North Auckland', -35.026401, 173.913905, 'New Zealand', true, 1),
(4977, 'Rat Island', 43, 'Southland', -47.133218, 167.567966, 'New Zealand', true, 1),
(5041, 'Refuge Island', 43, 'Southland', -46.949355, 168.127885, 'New Zealand', true, 1),
(5084, 'Ribbonwood Stream', 89, 'Canterbury', -43.136267, 172.227991, 'New Zealand', true, 1),
(5137, 'Rocky Knob', 38, 'Canterbury', -43.808197, 170.089933, 'New Zealand', true, 1),
(5150, 'Rollover Glacier', 36, 'Canterbury', -43.375889, 170.726508, 'New Zealand', true, 1),
(5201, 'Ruapake Stream', 89, 'Marlborough', -41.297087, 173.697105, 'New Zealand', true, 1),
(5234, 'Ryde Stream', 89, 'Canterbury', -44.846356, 170.942726, 'New Zealand', true, 1),
(5321, 'Seagull Lake', 46, 'Canterbury', -43.51051, 171.246743, 'New Zealand', true, 1),
(5375, 'Sherwood Range', 66, 'Canterbury', -43.796768, 170.798736, 'New Zealand', true, 1),
(5405, 'Sisters Stream', 89, 'Canterbury', -42.69284, 173.260088, 'New Zealand', true, 1),
(5418, 'Slip Gully', 95, 'Canterbury', -43.685145, 170.49254, 'New Zealand', true, 1),
(5439, 'Smylies Arm', 6, 'Nelson', -40.864706, 173.825993, 'New Zealand', true, 1),
(5543, 'Stag Pool', 60, 'Wellington', -39.013089, 175.8162, 'New Zealand', true, 1),
(5645, 'Sunshine', 90, 'Otago', -45.895673, 170.518723, 'New Zealand', true, 1),
(5909, 'Te Apu', 38, 'South Auckland', -38.577813, 176.771558, 'New Zealand', true, 1),
(5938, 'Te Henga (Bethells Beach)', 49, 'North Auckland', -36.882985, 174.452852, 'New Zealand', true, 1),
(6002, 'Te Moenga Bay', 6, 'South Auckland', -38.702123, 176.036604, 'New Zealand', true, 1),
(6011, 'Te Nunuhe Rock', 70, 'North Auckland', -35.18988, 174.20015, 'New Zealand', true, 1),
(6024, 'Te Pari o Te Mataahua', 59, 'Otago', -45.79335, 170.742656, 'New Zealand', true, 1),
(6087, 'Te Waha Point', 59, 'North Auckland', -36.93456, 174.453718, 'New Zealand', true, 1),
(6154, 'The Cathedrals', 75, 'Canterbury', -42.868175, 173.299436, 'New Zealand', true, 1),
(6304, 'Tiriwa Point', 59, 'North Auckland', -37.008989, 174.485523, 'New Zealand', true, 1),
(6447, 'Tui Stream', 89, 'Canterbury', -42.580916, 172.342799, 'New Zealand', true, 1),
(6611, 'Waiari Settlement', 2, 'South Auckland', -37.831616, 176.325227, 'New Zealand', true, 1),
(6723, 'Waingaro Road', 73, 'South Auckland', -37.660886, 175.014631, 'New Zealand', true, 1),
(6747, 'Waiopehu Stream', 89, 'Wellington', -40.741124, 175.364559, 'New Zealand', true, 1),
(6783, 'Waipaua Stream', 89, 'South Auckland', -38.315448, 174.717256, 'New Zealand', true, 1),
(6918, 'Webb Ridge', 72, 'Nelson', -41.476017, 172.218452, 'New Zealand', true, 1),
(6969, 'Whakapapa', 93, 'Wellington', -40.820868, 175.54942, 'New Zealand', true, 1),
(7159, 'Woodlands Stream', 89, 'North Auckland', -36.954632, 174.63239, 'New Zealand', true, 1),
(7267, 'Otiria-Okaihau Industrial Railway', 64, 'North Auckland', -35.44978, 173.811428, 'New Zealand', true, 1),
(7336, 'Mount Herbert/Te Ahu Patiki', 38, 'Canterbury', -43.689391, 172.741594, 'New Zealand', true, 1),
(7347, 'Kotukutuku Bay', 6, 'South Auckland', -38.205466, 176.381278, 'New Zealand', true, 1),
(7419, 'Punawhakareia Bay', 6, 'South Auckland', -38.053039, 176.442401, 'New Zealand', true, 1),
(7435, 'Selwyn River/Waikirikiri', 89, 'Canterbury', -43.615271, 172.126066, 'New Zealand', true, 1),
(7443, 'Stewart Island/Rakiura', 43, 'Southland', -47.000818, 167.999849, 'New Zealand', true, 1),
(7628, 'Awakino Government Purpose Wildlife Management Reserve', 37, 'North Auckland', -35.878333, 173.855833, 'New Zealand', true, 1),
(7899, 'Elaine Bay Recreation Reserve', 69, 'Nelson', -41.055, 173.769444, 'New Zealand', true, 1),
(8096, 'Hokonui Scenic Reserve', 79, 'Southland', -46.152222, 168.556389, 'New Zealand', true, 1),
(8115, 'Howdens Bush Scenic Reserve', 79, 'Marlborough', -41.09, 174.198889, 'New Zealand', true, 1),
(8133, 'Hutchinson Scenic Reserve', 79, 'Hawke''s Bay', -39.271111, 176.546111, 'New Zealand', true, 1),
(8279, 'Kerikeri Basin Recreation Reserve', 69, 'North Auckland', -35.215833, 173.959722, 'New Zealand', true, 1),
(8451, 'Long Bay Scenic Reserve', 79, 'Canterbury', -43.859722, 172.871389, 'New Zealand', true, 1),
(8519, 'Makuri Gorge Scenic Reserve', 79, 'Wellington', -40.546389, 175.978056, 'New Zealand', true, 1),
(8776, 'Motutangi Scenic Reserve', 79, 'North Auckland', -34.885833, 173.157778, 'New Zealand', true, 1),
(8923, 'Okaharau Road Scenic Reserve', 79, 'North Auckland', -35.713889, 173.820556, 'New Zealand', true, 1),
(8966, 'Onaero River Scenic Reserve', 79, 'Taranaki', -38.998611, 174.365556, 'New Zealand', true, 1),
(9241, 'Pukerau Red Tussock Scientific Reserve', 80, 'Southland', -46.09692, 169.077353, 'New Zealand', true, 1),
(9293, 'Raincliff Historic Reserve', 39, 'Canterbury', -44.1625, 170.993056, 'New Zealand', true, 1),
(9355, 'Ripapa Island Historic Reserve', 39, 'Canterbury', -43.620528, 172.754173, 'New Zealand', false, 1),
(9376, 'Rotokahu Scenic Reserve', 79, 'Wellington', -39.154167, 175.188056, 'New Zealand', true, 1),
(9487, 'Station Creek Scenic Reserve', 79, 'Nelson', -42.211389, 172.2625, 'New Zealand', false, 2),
(9488, 'Station Creek Scenic Reserve', 79, 'Nelson', -42.211389, 172.2625, 'New Zealand', false, 3),
(9489, 'Station Creek Scenic Reserve', 79, 'Nelson', -42.211389, 172.2625, 'New Zealand', false, 4),
(9490, 'Station Creek Scenic Reserve', 79, 'Nelson', -42.211389, 172.2625, 'New Zealand', false, 5),
(9491, 'Station Creek Scenic Reserve', 79, 'Nelson', -42.211389, 172.2625, 'New Zealand', false, 6),
(9001, 'Private Glade', 39, 'Canterbury', -44.1625, 170.993056, 'New Zealand', false, 3),
(9000, 'Japan', 39, 'Japan', -44.1625, 170.993056, 'Japan', true, 1),
(10000, 'Canterbury University', 3, 'Christchurch', -43.523434, 172.581681, 'New Zealand', false, 3);


INSERT INTO `photo` (`id`, `main_filename`, `thumbnail_filename`, `upload_date`, `upload_profile_id`, `content_type`) VALUES
(1, 'temp/935330b4-1adb-4d4c-9b2b-2e2a5638e315', 'temp/935330b4-1adb-4d4c-9b2b-2e2a5638e315', '2019-05-25', 1, 'image/png'),
(2, 'temp/935330b4-1adb-4d4c-9b2b-2e2a5638e317', 'temp/935330b4-1adb-4d4c-9b2b-2e2a5638e317', '2019-05-25', 2, 'image/png'),
(3, 'temp/935330b4-1adb-4d4c-9b2b-2e2a5638e318', 'temp/935330b4-1adb-4d4c-9b2b-2e2a5638e318', '2019-05-25', 2, 'image/png'),
(4, 'temp/935330b4-1adb-4d4c-9b2b-2e2a5638e319', 'temp/935330b4-1adb-4d4c-9b2b-2e2a5638e319', '2019-05-25', 3, 'image/png'),
(5, 'temp/935330b4-1adb-4d4c-9b2b-2e2a5638e316', 'temp/935330b4-1adb-4d4c-9b2b-2e2a5638e316', '2019-05-25', 2, 'image/png'),
(6, 'temp/935330b4-1adb-4d4c-9b2b-2e2a5638e316', 'temp/935330b4-1adb-4d4c-9b2b-2e2a5638e316', '2019-05-25', 3, 'image/png');


INSERT INTO `personal_photo` (`id`, `photo_id`, `profile_id`, `is_public`) VALUES
(1, 1, 1, true),
(2, 2, 2, true),
(3, 3, 2, true),
(4, 4, 1, true),
(5, 5, 2, true),
(6, 6, 3, true);


INSERT INTO `destination_personal_photo` (`destination_id`, `personal_photo_id`) VALUES
(119, 3),
(119, 4),
(119, 5);


INSERT INTO `quest` (`id`, `title`, `start_date`, `end_date`, `owner_id`) VALUES
(1, 'Journey to the centre of the Earth', '2019-08-16 03:02:00', '9999-08-16 11:59:00', 1),
(2, 'My new quest', '2019-08-15 22:47:00', '9999-08-16 11:59:00', 6),
(3, 'I am your father', '2019-08-16 03:02:00', '9999-08-16 11:59:00', 2),
(4, 'Use the force Luke', '2019-08-15 04:04:00', '9999-08-16 11:59:00', 2),
(5, 'Energise', '2019-08-15 04:09:00', '9999-08-16 11:59:00', 1),
(6, 'Urlaub in Europa', '1998-05-21 12:00:01', '2100-06-21 23:59:59', 1),
(7, 'International Quest', '1998-05-21 12:00:01', '2100-06-21 23:59:59', 10),
(8, 'Quest with objective to delete', '1998-05-21 12:00:01', '2100-06-21 23:59:59', 2),
(9, 'Odyssey Quest', '1998-05-21 12:00:01', '2100-06-21 23:59:59', 2);


INSERT INTO `objective` (`id`, `destination_id`, `riddle`, `radius`, `owner_id`, `quest_using_id`) VALUES
(1, 567, 'Gully that *may* have killed its family', 1, 2, 1),
(12, 1834, 'I am the capital of Australia', 1, 2, 1),
(2, 733, 'The hive of activity in NZ', 1, 2, 1),
(3, 858, 'Second Home of Computer Scientists', 1, 1, 2),
(13, 1834, 'I like to move it, move it!', 0.02, 2, 2),
(4, 1031, 'What Rhymes with "sniff cream file week"?', 1, 2, 3),
(5, 1465, 'What is your favourite colour', 1, 2, 3),
(6, 1526, 'Where does santa live?', 1, 2, 3),
(7, 1797, 'I am the place of government in russia', 1, 2, 3),
(8, 1834, 'My radius riddle', 1, 2, 4),
(9, 1834, 'Under the seeeeeeeeaaaaaaaa', 1, 2, 4),
(10, 1834, 'In german I am named Koeln', 1, 2, 5),
(11, 119, 'I am the capital of Switzerland', 1, 2, 5),
(14, 1834, 'The better engineering university', 1, 2, null),
(15, 1834, 'One small step for man, one giant leap for mankind', 1, 2, null),
(16, 1834, 'Gully that *may* have killed its family', 1, 2, null),
(17, 1834, 'What rhymes with smangus fat?', 0.005, 2, null),
(18, 9000, 'Earthquake prone country that has had a nuclear bomb.', 10, 10, 7),
(19, 9000, 'Earthquake 3.', 10, 2, 8),
(20, 9000, 'Earthquake 4', 10, 2, 9),
(21, 9000, 'Earthquake 5', 10, 2, 9),
(22, 9000, 'Earthquake 6', 10, 2, 9),
(23, 9000, 'Earthquake 7', 10, 2, 9),
(24, 9000, 'Earthquake 8', 10, 2, 9),
(25, 9000, 'Earthquake 9', 10, 2, 9),
(26, 9000, 'Earthquake 10', 10, 2, 9),
(27, 9000, 'Earthquake 11', 10, 2, 9),
(28, 9000, 'Earthquake 12', 10, 2, 9),
(29, 9000, 'Earthquake 13', 10, 2, 9),
(30, 1031, 'What Rhymes with "sniff cream file week"?', 1, 2, 7),
(31, 119, 'I am the capital of Switzerland', 1, 2, 6),
(32, 119, 'I am the capital of Switzerland', 1, 2, 7);


INSERT INTO `quest_attempt` (`id`, `attempted_by_id`, `quest_attempted_id`, `solved_current`, `checked_in_index`, `completed`) VALUES
(1, 1, 1, 0, 0, 0),
(2, 1, 3, 1, 3, 0),
(3, 2, 3, 1, 1, 0),
(4, 2, 4, 0, 1, 0),
(5, 3, 5, 0, 2, 1),
(6, 4, 2, 0, 2, 1),
(7, 7, 6, 1, 0, 0),
(8, 7, 5, 1, 1, 0),
(9, 8, 5, 1, 1, 0),
(10, 9, 5, 1, 1, 0),
(11, 7, 7, 1, 2, 0),
(12, 8, 7, 1, 2, 0),
(13, 9, 7, 1, 2, 0),
(14, 7, 9, 1, 9, 0),
(15, 8, 9, 1, 9, 0),
(16, 9, 9, 1, 9, 0),
(17, 8, 6, 1, 0, 0),
(18, 9, 6, 1, 0, 0),
(19, 5, 4, 0, 1, 0),
(20, 2, 8, 0, 1, 1);


INSERT INTO `hint` (`id`, `message`, `up_votes`, `down_votes`, `objective_id`, `creator_id`) VALUES
(1, 'This is a hint', 10, 3, 18, 7),
(2, 'This is also a hint', 4, 10, 24, 7),
(3, 'Think about the first word carefully', 45, 38, 31, 8),
(4, 'The most up-voted hint', 100, 1, 18, 7),
(5, 'The second most up-voted hint', 99, 1, 18, 7),
(6, 'The second-equal up-voted hint', 100, 2, 18, 7),
(7, 'First requested hint', 70, 2, 9, 7),
(8, 'Second requested hint', 60, 2, 9, 7),
(9, 'Third requested hint', 50, 2, 9, 7),
(10, 'Fourth requested hint', 40, 2, 9, 7),
(11, 'Fourth requested hint', 40, 2, 29, 7),
(12, 'Fourth requested hint', 40, 2, 29, 7);


INSERT INTO `vote` (`id`, `voter_id`, `target_hint_id`, `is_up_vote`) VALUES
(1, 9, 2, false),
(2, 7, 3, true);

INSERT INTO `hint_profile` (`hint_id`, `profile_id`) VALUES
(2, 2),
(2, 3),
(4, 8),
(4, 7),
(3, 3);

# --- !Downs