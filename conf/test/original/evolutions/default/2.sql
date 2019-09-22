/*
 * Script to populate static tables that the users do not insert into. This is the second level of population.
 */
# --- !Ups

INSERT INTO `destination_type` (`id`, `destination_type`) VALUES
(1, 'Amenity Area'),
(2, 'Appellation'),
(3, 'Area'),
(4, 'Bank'),
(5, 'Basin'),
(6, 'Bay'),
(7, 'Beach'),
(8, 'Bend'),
(9, 'Bridge'),
(10, 'Building'),
(11, 'Bush'),
(12, 'Canal'),
(13, 'Canyon'),
(14, 'Cape'),
(15, 'Cave'),
(16, 'Channel'),
(17, 'Chasm'),
(18, 'City'),
(19, 'Clearing'),
(20, 'Cliff'),
(21, 'Coast Feature'),
(22, 'Conservation Park'),
(23, 'Crater'),
(24, 'Crown Protected Area'),
(25, 'Desert'),
(26, 'District'),
(27, 'Ecological Area'),
(28, 'Estuary'),
(29, 'Facility'),
(30, 'Fan'),
(31, 'Flat'),
(32, 'Ford'),
(33, 'Forest'),
(34, 'Fork'),
(35, 'Gap'),
(36, 'Glacier'),
(37, 'Government Purpose Reserve'),
(38, 'Hill'),
(39, 'Historic Reserve'),
(40, 'Historic Site'),
(41, 'Hole'),
(42, 'Ice Feature'),
(43, 'Island'),
(44, 'Isthmus'),
(45, 'Knoll'),
(46, 'Lake'),
(47, 'Ledge'),
(48, 'Local Government'),
(49, 'Locality'),
(50, 'Marine Feature'),
(51, 'Marine Reserve'),
(52, 'National Park'),
(53, 'Nature Reserve'),
(54, 'Pass'),
(55, 'Peninsula'),
(56, 'Pinnacle'),
(57, 'Place'),
(58, 'Plateau'),
(59, 'Point'),
(60, 'Pool'),
(61, 'Port'),
(62, 'Railway Crossing'),
(63, 'Railway Junction'),
(64, 'Railway Line'),
(65, 'Railway Station'),
(66, 'Range'),
(67, 'Rapid'),
(68, 'Recreation'),
(69, 'Recreation Reserve'),
(70, 'Reef'),
(71, 'Reserve (non-CPA)'),
(72, 'Ridge'),
(73, 'Road'),
(74, 'Roadstead'),
(75, 'Rock'),
(76, 'Saddle'),
(77, 'Sanctuary Area'),
(78, 'Scarp'),
(79, 'Scenic Reserve'),
(80, 'Scientific Reserve'),
(81, 'Seachannel'),
(82, 'Shelf'),
(83, 'Shoal'),
(84, 'Sill'),
(85, 'Site'),
(86, 'Spit'),
(87, 'Spring'),
(88, 'Spur'),
(89, 'Stream'),
(90, 'Suburb'),
(91, 'Town'),
(92, 'Track'),
(93, 'Trig Station'),
(94, 'Trough'),
(95, 'Valley'),
(96, 'Village'),
(97, 'Waterfall'),
(98, 'Wetland'),
(99, 'Wilderness Area'),
(100, 'Wildlife Management Area');


INSERT INTO nationality (nationality, country) VALUES
('Afghan', 'Afghanistan'),
('Albanian', 'Albania'),
('Algerian', 'Algeria'),
('Argentinian', 'Argentina'),
('Australian', 'Australia'),
('Austrian', 'Austria'),
('Belgian', 'Belgium'),
('Bolivian', 'Bolivia'),
('Batswana', 'Botswana'),
('Brazilian', 'Brazil'),
('Bulgarian', 'Bulgaria'),
('Cambodian', 'Cambodia'),
('Cameroonian', 'Cameroon'),
('Canadian', 'Canada'),
('Chilean', 'Chile'),
('Chinese', 'China'),
('Costa Rican', 'Costa Rica'),
('Croatian', 'Croatia'),
('Cuban', 'Cuba'),
('Czech', 'Czech Republic'),
('Danish', 'Denmark'),
('Dominican', 'Dominican Republic'),
('Ecuadorian', 'Ecuador'),
('Egyptian', 'Egypt'),
('Salvadorian', 'El Salvador'),
('English', 'England'),
('Estonian', 'Estonia'),
('Ethiopian', 'Ethiopia'),
('Fijian', 'Fiji'),
('Finnish', 'Finland'),
('French', 'France'),
('German', 'Germany'),
('Ghanaian', 'Ghana'),
('Greek', 'Greece'),
('Guatemalan', 'Guatemala'),
('Haitian', 'Haiti'),
('Honduran', 'Honduras'),
('Hungarian', 'Hungary'),
('Icelandic', 'Iceland'),
('Indian', 'India'),
('Indonesian', 'Indonesia'),
('Iranian', 'Iran'),
('Iraqi', 'Iraq'),
('Irish', 'Ireland'),
('Israeli', 'Israel'),
('Italian', 'Italy'),
('Jamaican', 'Jamaica'),
('Japanese', 'Japan'),
('Jordanian', 'Jordan'),
('Kenyan', 'Kenya'),
('Kuwaiti', 'Kuwait'),
('Lao', 'Laos'),
('Latvian', 'Latvia'),
('Lebanese', 'Lebanon'),
('Libyan', 'Libya'),
('Lithuanian', 'Lithuania'),
('Malaysian', 'Malaysia'),
('Malian', 'Mali'),
('Maltese', 'Malta'),
('Mexican', 'Mexico'),
('Mongolian', 'Mongolia'),
('Moroccan', 'Morocco'),
('Mozambican', 'Mozambique'),
('Namibian', 'Namibia'),
('Nepalese', 'Nepal'),
('Dutch', 'Netherlands'),
('New Zealand', 'New Zealand'),
('Nicaraguan', 'Nicaragua'),
('Nigerian', 'Nigeria'),
('Norwegian', 'Norway'),
('Pakistani', 'Pakistan'),
('Panamanian', 'Panama'),
('Paraguayan', 'Paraguay'),
('Peruvian', 'Peru'),
('Philippine', 'Philippines'),
('Polish', 'Poland'),
('Portuguese', 'Portugal'),
('Romanian', 'Romania'),
('Russian', 'Russia'),
('Saudi', 'Saudi Arabia'),
('Scottish', 'Scotland'),
('Senegalese', 'Senegal'),
('Serbian', 'Serbia'),
('Singaporean', 'Singapore'),
('Slovak', 'Slovakia'),
('South African', 'South Africa'),
('Korean', 'South Korea'),
('Spanish', 'Spain'),
('Sri Lankan', 'Sri Lanka'),
('Sudanese', 'Sudan'),
('Swedish', 'Sweden'),
('Swiss', 'Switzerland'),
('Syrian', 'Syria'),
('Taiwanese', 'Taiwan'),
('Thai', 'Thailand'),
('Tongan', 'Tonga'),
('Tunisian', 'Tunisia'),
('Turkish', 'Turkey'),
('Ukrainian', 'Ukraine'),
('Emirati', 'United Arab Emirates'),
('British', 'United Kingdom'),
('American', 'United States'),
('Uruguayan', 'Uruguay'),
('Venezuelan', 'Venezuela'),
('Vietnamese', 'Vietnam'),
('Welsh', 'Wales'),
('Zambian', 'Zambia'),
('Zimbabwean', 'Zimbabwe');


INSERT INTO `traveller_type` (`id`, `traveller_type`, `description`, `img_url`) VALUES
(1, 'Groupie', 'You love following a band/artist around while they''re on tour.', '../../../static/traveller_types/groupie.png'),
(2, 'Thrillseeker', 'You''re an adrenaline junkie and love taking part in extreme sport that put you at physical risk.', '../../../static/traveller_types/thrillseeker.png'),
(3, 'Gap Year', 'You''ve just graduated and are ready to see the world before your get into the working sector.', '../../../static/traveller_types/gapYear.png'),
(4, 'Frequent Weekender', 'You''re a hard worker during the weekdays, but enjoy a quick weekend away to escape.', '../../../static/traveller_types/frequentWeekender.png'),
(5, 'Holidaymaker', 'You''re the stereotypical tourist.', '../../../static/traveller_types/holidayMaker.png'),
(6, 'Functional/Business', 'You travel for work, often spending short periods of time in one place.', '../../../static/traveller_types/business.png'),
(7, 'Backpacker', 'You don''t mind going rough and love seeing the outdoors.', '../../../static/traveller_types/backpacker.png');


INSERT INTO passport (`id`, `country`) VALUES
(1, 'Afghanistan'),
(2, 'Albania'),
(3, 'Algeria'),
(4, 'Argentina'),
(5, 'Australia'),
(6, 'Austria'),
(7, 'Belgium'),
(8, 'Bolivia'),
(9, 'Botswana'),
(10, 'Brazil'),
(11, 'Bulgaria'),
(12, 'Cambodia'),
(13, 'Cameroon'),
(14, 'Canada'),
(15, 'Chile'),
(16, 'China'),
(17, 'Costa Rica'),
(18, 'Croatia'),
(19, 'Cuba'),
(20, 'Czech Republic'),
(21, 'Denmark'),
(22, 'Dominican Republic'),
(23, 'Ecuador'),
(24, 'Egypt'),
(25, 'El Salvador'),
(26, 'England'),
(27, 'Estonia'),
(28, 'Ethiopia'),
(29, 'Fiji'),
(30, 'Finland'),
(31, 'France'),
(32, 'Germany'),
(33, 'Ghana'),
(34, 'Greece'),
(35, 'Guatemala'),
(36, 'Haiti'),
(37, 'Honduras'),
(38, 'Hungary'),
(39, 'Iceland'),
(40, 'India'),
(41, 'Indonesia'),
(42, 'Iran'),
(43, 'Iraq'),
(44, 'Ireland'),
(45, 'Israel'),
(46, 'Italy'),
(47, 'Jamaica'),
(48, 'Japan'),
(49, 'Jordan'),
(50, 'Kenya'),
(51, 'Kuwait'),
(52, 'Laos'),
(53, 'Latvia'),
(54, 'Lebanon'),
(55, 'Libya'),
(56, 'Lithuania'),
(57, 'Malaysia'),
(58, 'Mali'),
(59, 'Malta'),
(60, 'Mexico'),
(61, 'Mongolia'),
(62, 'Morocco'),
(63, 'Mozambique'),
(64, 'Namibia'),
(65, 'Nepal'),
(66, 'Netherlands'),
(67, 'New Zealand'),
(68, 'Nicaragua'),
(69, 'Nigeria'),
(70, 'Norway'),
(71, 'Pakistan'),
(72, 'Panama'),
(73, 'Paraguay'),
(74, 'Peru'),
(75, 'Philippines'),
(76, 'Poland'),
(77, 'Portugal'),
(78, 'Romania'),
(79, 'Russia'),
(80, 'Saudi Arabia'),
(81, 'Scotland'),
(82, 'Senegal'),
(83, 'Serbia'),
(84, 'Singapore'),
(85, 'Slovakia'),
(86, 'South Africa'),
(87, 'South Korea'),
(88, 'Spain'),
(89, 'Sri Lanka'),
(90, 'Sudan'),
(91, 'Sweden'),
(92, 'Switzerland'),
(93, 'Syria'),
(94, 'Taiwan'),
(95, 'Thailand'),
(96, 'Tonga'),
(97, 'Tunisia'),
(98, 'Turkey'),
(99, 'Ukraine'),
(100, 'United Arab Emirates'),
(101, 'United Kingdom'),
(102, 'United States'),
(103, 'Uruguay'),
(104, 'Venezuela'),
(105, 'Vietnam'),
(106, 'Wales'),
(107, 'Zambia'),
(108, 'Zimbabwe');


INSERT INTO `point_reward`(`id`, `name`, `value`) VALUES
(1, 'RIDDLE_SOLVED', 5),
(2, 'CHECKED_IN', 10),
(3, 'DESTINATION_CREATED', 2),
(4, 'QUEST_CREATED', 3),
(5, 'TRIP_CREATED', 3),
(6, 'QUEST_COMPLETED', 20),
(7, 'HINT_CREATED', 1),
(8, 'HINT_UPVOTED', 1),
(9, 'HINT_UPVOTE_REMOVED', -1);


INSERT INTO `badge` (`id`, `action_to_achieve`, `name`, `bronze_breakpoint`, `silver_breakpoint`, `gold_breakpoint`, `how_to_progress`) VALUES
(1,'TRIP_CREATED', 'Planner', 1, 10, 30, 'You need to create %s more trips to achieve %s'),
(2,'DESTINATION_CREATED', 'Cartographer', 1, 10, 50, 'You need to create %s more destinations to achieve %s'),
(3,'QUEST_CREATED', 'Writer', 1, 15, 50, 'You need to create %s more quests to achieve %s'),
(4,'QUEST_COMPLETED', 'Solver', 1, 20, 50, 'You need to complete %s more quests to achieve %s'),
(5,'INTERNATIONAL_QUEST_COMPLETED', 'Explorer', 1, 5, 10, 'You need to complete %s more international quests to achieve %s'),
(6,'LARGE_QUEST_COMPLETED', 'Adventurer', 1, 10, 30, 'You need to complete %s more large quests to achieve %s'),
(7,'DISTANCE_QUEST_COMPLETED', 'Wayfarer', 100000, 500000, 1000000, 'You need to travel %s more metres in quests to achieve %s'),
(8,'POINTS_GAINED', 'Overachiever', 100, 1000, 10000, 'You need to earn %s more points to achieve %s'),
(9,'LOGIN_STREAK', 'Streaker', 1, 7, 31, 'You need to login each day %s more times to achieve %s');


# --- !Downs