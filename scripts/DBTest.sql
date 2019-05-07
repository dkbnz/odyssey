create table destination (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  type_id                       bigint,
  district                      varchar(255),
  latitude                      double not null,
  longitude                     double not null,
  country                       varchar(255),
  constraint pk_destination primary key (id)
);

create table destination_type (
  id                            bigint auto_increment not null,
  destination_type              varchar(255),
  constraint pk_destination_type primary key (id)
);

create table nationality (
  id                            bigint auto_increment not null,
  nationality                   varchar(255),
  country                       varchar(255),
  constraint pk_nationality primary key (id)
);

create table passport (
  id                            bigint auto_increment not null,
  country                       varchar(255),
  constraint pk_passport primary key (id)
);

create table profile (
  id                            bigint auto_increment not null,
  username                      varchar(255),
  password                      varchar(255),
  first_name                    varchar(255),
  middle_name                   varchar(255),
  last_name                     varchar(255),
  gender                        varchar(255),
  date_of_birth                 date,
  is_admin                      tinyint(1),
  date_of_creation              datetime(6),
  constraint pk_profile primary key (id)
);

create table profile_nationality (
  profile_id                    bigint not null,
  nationality_id                bigint not null,
  constraint pk_profile_nationality primary key (profile_id,nationality_id)
);

create table profile_traveller_type (
  profile_id                    bigint not null,
  traveller_type_id             bigint not null,
  constraint pk_profile_traveller_type primary key (profile_id,traveller_type_id)
);

create table profile_passport (
  profile_id                    bigint not null,
  passport_id                   bigint not null,
  constraint pk_profile_passport primary key (profile_id,passport_id)
);

create table traveller_type (
  id                            bigint auto_increment not null,
  traveller_type                varchar(255),
  description                   varchar(255),
  img_url                       varchar(255),
  constraint pk_traveller_type primary key (id)
);

create table trip (
  id                            bigint auto_increment not null,
  profile_id                    bigint not null,
  name                          varchar(255),
  constraint pk_trip primary key (id)
);

create table trip_destination (
  id                            bigint auto_increment not null,
  start_date                    date,
  end_date                      date,
  list_order                    integer not null,
  trip_id                       bigint,
  destination_id                bigint,
  constraint pk_trip_destination primary key (id)
);

create index ix_destination_type_id on destination (type_id);
alter table destination add constraint fk_destination_type_id foreign key (type_id) references destination_type (id) on delete restrict on update restrict;

create index ix_profile_nationality_profile on profile_nationality (profile_id);
alter table profile_nationality add constraint fk_profile_nationality_profile foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_profile_nationality_nationality on profile_nationality (nationality_id);
alter table profile_nationality add constraint fk_profile_nationality_nationality foreign key (nationality_id) references nationality (id) on delete restrict on update restrict;

create index ix_profile_traveller_type_profile on profile_traveller_type (profile_id);
alter table profile_traveller_type add constraint fk_profile_traveller_type_profile foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_profile_traveller_type_traveller_type on profile_traveller_type (traveller_type_id);
alter table profile_traveller_type add constraint fk_profile_traveller_type_traveller_type foreign key (traveller_type_id) references traveller_type (id) on delete restrict on update restrict;

create index ix_profile_passport_profile on profile_passport (profile_id);
alter table profile_passport add constraint fk_profile_passport_profile foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_profile_passport_passport on profile_passport (passport_id);
alter table profile_passport add constraint fk_profile_passport_passport foreign key (passport_id) references passport (id) on delete restrict on update restrict;

create index ix_trip_profile_id on trip (profile_id);
alter table trip add constraint fk_trip_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;

create index ix_trip_destination_trip_id on trip_destination (trip_id);
alter table trip_destination add constraint fk_trip_destination_trip_id foreign key (trip_id) references trip (id) on delete restrict on update restrict;

create index ix_trip_destination_destination_id on trip_destination (destination_id);
alter table trip_destination add constraint fk_trip_destination_destination_id foreign key (destination_id) references destination (id) on delete restrict on update restrict;

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

INSERT INTO traveller_type (traveller_type, description) VALUES
('Groupie', "You love following a band/artist around while they're on tour.", "/assets/images/traveller_types/groupie.png"),
('Thrillseeker', 'You''re an adrenaline junkie and love taking part in extreme sport that put you at physical risk.', '/assets/images/traveller_types/thrillseeker.png'),
('Gap Year', 'You''ve just graduated and are ready to see the world before your get into the working sector.', '/assets/images/traveller_types/gapYear.png'),
('Frequent Weekender', 'You''re a hard worker during the weekdays, but enjoy a quick weekend away to escape.', '/assets/images/traveller_types/frequentWeekender.png'),
('Holidaymaker', 'You''re the stereotypical tourist.', '/assets/images/traveller_types/holidayMaker.png'),
('Functional/Business', 'You travel for work, often spending short periods of time in one place.', '/assets/images/traveller_types/business.png'),
('Backpacker', 'You don''t mind going rough and love seeing the outdoors.', '/assets/images/traveller_types/backpacker.png');

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

INSERT INTO `destination` (`id`, `name`, `type_id`, `district`, `latitude`, `longitude`, `country`) VALUES
(119, 'Angus Flat', 31, 'Canterbury', -43.65598, 170.48378, 'New Zealand'),
(325, 'Baylys Beach Post Office', 10, 'North Auckland', -35.953527, 173.74573, 'New Zealand'),
(567, 'Bow Alley Creek', 89, 'Otago', -45.239576, 170.851946, 'New Zealand'),
(733, 'Cameron Stream', 89, 'Canterbury', -42.23865, 173.046403, 'New Zealand'),
(858, 'Cerberus', 38, 'Wellington', -40.512442, 176.213727, 'New Zealand'),
(1031, 'Courtenay Peak', 38, 'Otago', -44.529264, 168.195041, 'New Zealand'),
(1155, 'Demon Gap Icefall', 42, 'Otago', -44.392145, 168.361981, 'New Zealand'),
(1465, 'Feldspar Stream', 89, 'Southland', -45.03797, 167.347403, 'New Zealand'),
(1526, 'Flat Stream', 89, 'Marlborough', -41.940145, 173.111533, 'New Zealand'),
(1797, 'Greenstone Stream', 89, 'Canterbury', -42.590148, 172.745206, 'New Zealand'),
(1834, 'Haines Stream', 89, 'Nelson', -41.295971, 172.664481, 'New Zealand'),
(1894, 'Hart Creek', 89, 'Wellington', -40.729439, 175.430851, 'New Zealand'),
(1940, 'Headlong Peak', 38, 'Otago', -44.539752, 168.591617, 'New Zealand'),
(2035, 'Hitchin Range', 66, 'Westland', -43.111555, 170.824539, 'New Zealand'),
(2194, 'Iris Stream', 89, 'North Auckland', -36.970301, 174.530236, 'New Zealand'),
(2275, 'Johnstone Mount', 38, 'Canterbury', -43.37552, 170.857361, 'New Zealand'),
(2426, 'Kaurimu Stream', 89, 'North Auckland', -36.911035, 174.623382, 'New Zealand'),
(2439, 'Kawaunui Stream', 89, 'South Auckland', -38.353659, 176.310498, 'New Zealand'),
(2461, 'Kelleher', 38, 'Wellington', -40.784535, 175.376598, 'New Zealand'),
(2593, 'Krushen Stream', 89, 'Marlborough', -41.825296, 173.260128, 'New Zealand'),
(2631, 'Lake Rotoroa (Hamilton Lake)', 46, 'South Auckland', -37.798629, 175.27484, 'New Zealand'),
(2657, 'Lake Donne', 46, 'Canterbury', -43.608439, 171.115709, 'New Zealand'),
(2775, 'Lathrop Saddle', 54, 'Westland', -42.91523, 171.277535, 'New Zealand'),
(2822, 'Lily Creek', 89, 'Westland', -44.032015, 169.474478, 'New Zealand'),
(2921, 'Long Spur Stream', 89, 'Canterbury', -43.060244, 172.219981, 'New Zealand'),
(2962, 'Lumber Flat', 31, 'Westland', -44.224114, 168.659968, 'New Zealand'),
(3218, 'Maraeweka Stream', 89, 'Otago', -45.150038, 170.741188, 'New Zealand'),
(3338, 'Maungawhiorangi', 93, 'Gisborne', -38.174833, 177.243242, 'New Zealand'),
(3360, 'McCallum Stream', 89, 'Marlborough', -41.797334, 173.260076, 'New Zealand'),
(3558, 'Morgan Stream', 89, 'Canterbury', -43.59628, 171.339142, 'New Zealand'),
(3580, 'Mother Millers Spring', 87, 'Canterbury', -43.358825, 171.288873, 'New Zealand'),
(3594, 'Motukauatirahi/Cass Bay', 6, 'Canterbury', -43.607459, 172.692363, 'New Zealand'),
(3607, 'Motuoapa Peninsula', 55, 'South Auckland', -38.924214, 175.859163, 'New Zealand'),
(3769, 'Mount Meehan', 38, 'Canterbury', -42.919966, 172.300892, 'New Zealand'),
(3852, 'Mount William Grant', 38, 'Canterbury', -43.704591, 170.32112, 'New Zealand'),
(3973, 'Nga Tamahineapani', 75, 'Nelson', -40.689108, 173.948723, 'New Zealand'),
(4087, 'Nym Peak', 38, 'Canterbury', -43.34196, 170.843819, 'New Zealand'),
(4109, 'Ogilvie Creek', 89, 'Westland', -42.559882, 171.326201, 'New Zealand'),
(4186, 'Omahuri', 59, 'North Auckland', -34.822269, 173.414253, 'New Zealand'),
(4216, 'Onetohunga Stream', 89, 'Gisborne', -38.114435, 178.219536, 'New Zealand'),
(4239, 'Orau Gorge', 95, 'North Auckland', -36.182429, 175.084831, 'New Zealand'),
(4357, 'Pacific Bay', 6, 'North Auckland', -35.618672, 174.536016, 'New Zealand'),
(4515, 'Patuki Mountain', 38, 'Southland', -44.669468, 168.021972, 'New Zealand'),
(4634, 'Pioke', 38, 'Taranaki', -39.167798, 173.967902, 'New Zealand'),
(4659, 'Plumbago Stream', 89, 'Wellington', -41.390123, 174.895805, 'New Zealand'),
(4741, 'Poututu Rural Sections', 2, 'Hawke''s Bay', -39.056581, 177.309005, 'New Zealand'),
(4775, 'Puffer Saddle', 54, 'Wellington', -41.073802, 175.242171, 'New Zealand'),
(4873, 'Putataua Bay', 6, 'North Auckland', -35.026401, 173.913905, 'New Zealand'),
(4977, 'Rat Island', 43, 'Southland', -47.133218, 167.567966, 'New Zealand'),
(5041, 'Refuge Island', 43, 'Southland', -46.949355, 168.127885, 'New Zealand'),
(5084, 'Ribbonwood Stream', 89, 'Canterbury', -43.136267, 172.227991, 'New Zealand'),
(5137, 'Rocky Knob', 38, 'Canterbury', -43.808197, 170.089933, 'New Zealand'),
(5150, 'Rollover Glacier', 36, 'Canterbury', -43.375889, 170.726508, 'New Zealand'),
(5201, 'Ruapake Stream', 89, 'Marlborough', -41.297087, 173.697105, 'New Zealand'),
(5234, 'Ryde Stream', 89, 'Canterbury', -44.846356, 170.942726, 'New Zealand'),
(5321, 'Seagull Lake', 46, 'Canterbury', -43.51051, 171.246743, 'New Zealand'),
(5375, 'Sherwood Range', 66, 'Canterbury', -43.796768, 170.798736, 'New Zealand'),
(5405, 'Sisters Stream', 89, 'Canterbury', -42.69284, 173.260088, 'New Zealand'),
(5418, 'Slip Gully', 95, 'Canterbury', -43.685145, 170.49254, 'New Zealand'),
(5439, 'Smylies Arm', 6, 'Nelson', -40.864706, 173.825993, 'New Zealand'),
(5543, 'Stag Pool', 60, 'Wellington', -39.013089, 175.8162, 'New Zealand'),
(5645, 'Sunshine', 90, 'Otago', -45.895673, 170.518723, 'New Zealand'),
(5909, 'Te Apu', 38, 'South Auckland', -38.577813, 176.771558, 'New Zealand'),
(5938, 'Te Henga (Bethells Beach)', 49, 'North Auckland', -36.882985, 174.452852, 'New Zealand'),
(6002, 'Te Moenga Bay', 6, 'South Auckland', -38.702123, 176.036604, 'New Zealand'),
(6011, 'Te Nunuhe Rock', 70, 'North Auckland', -35.18988, 174.20015, 'New Zealand'),
(6024, 'Te Pari o Te Mataahua', 59, 'Otago', -45.79335, 170.742656, 'New Zealand'),
(6087, 'Te Waha Point', 59, 'North Auckland', -36.93456, 174.453718, 'New Zealand'),
(6154, 'The Cathedrals', 75, 'Canterbury', -42.868175, 173.299436, 'New Zealand'),
(6304, 'Tiriwa Point', 59, 'North Auckland', -37.008989, 174.485523, 'New Zealand'),
(6447, 'Tui Stream', 89, 'Canterbury', -42.580916, 172.342799, 'New Zealand'),
(6611, 'Waiari Settlement', 2, 'South Auckland', -37.831616, 176.325227, 'New Zealand'),
(6723, 'Waingaro Road', 73, 'South Auckland', -37.660886, 175.014631, 'New Zealand'),
(6747, 'Waiopehu Stream', 89, 'Wellington', -40.741124, 175.364559, 'New Zealand'),
(6783, 'Waipaua Stream', 89, 'South Auckland', -38.315448, 174.717256, 'New Zealand'),
(6918, 'Webb Ridge', 72, 'Nelson', -41.476017, 172.218452, 'New Zealand'),
(6969, 'Whakapapa', 93, 'Wellington', -40.820868, 175.54942, 'New Zealand'),
(7159, 'Woodlands Stream', 89, 'North Auckland', -36.954632, 174.63239, 'New Zealand'),
(7267, 'Otiria-Okaihau Industrial Railway', 64, 'North Auckland', -35.44978, 173.811428, 'New Zealand'),
(7336, 'Mount Herbert/Te Ahu Patiki', 38, 'Canterbury', -43.689391, 172.741594, 'New Zealand'),
(7347, 'Kotukutuku Bay', 6, 'South Auckland', -38.205466, 176.381278, 'New Zealand'),
(7419, 'Punawhakareia Bay', 6, 'South Auckland', -38.053039, 176.442401, 'New Zealand'),
(7435, 'Selwyn River/Waikirikiri', 89, 'Canterbury', -43.615271, 172.126066, 'New Zealand'),
(7443, 'Stewart Island/Rakiura', 43, 'Southland', -47.000818, 167.999849, 'New Zealand'),
(7628, 'Awakino Government Purpose Wildlife Management Reserve', 37, 'North Auckland', -35.878333, 173.855833, 'New Zealand'),
(7899, 'Elaine Bay Recreation Reserve', 69, 'Nelson', -41.055, 173.769444, 'New Zealand'),
(8096, 'Hokonui Scenic Reserve', 79, 'Southland', -46.152222, 168.556389, 'New Zealand'),
(8115, 'Howdens Bush Scenic Reserve', 79, 'Marlborough', -41.09, 174.198889, 'New Zealand'),
(8133, 'Hutchinson Scenic Reserve', 79, 'Hawke''s Bay', -39.271111, 176.546111, 'New Zealand'),
(8279, 'Kerikeri Basin Recreation Reserve', 69, 'North Auckland', -35.215833, 173.959722, 'New Zealand'),
(8451, 'Long Bay Scenic Reserve', 79, 'Canterbury', -43.859722, 172.871389, 'New Zealand'),
(8519, 'Makuri Gorge Scenic Reserve', 79, 'Wellington', -40.546389, 175.978056, 'New Zealand'),
(8776, 'Motutangi Scenic Reserve', 79, 'North Auckland', -34.885833, 173.157778, 'New Zealand'),
(8923, 'Okaharau Road Scenic Reserve', 79, 'North Auckland', -35.713889, 173.820556, 'New Zealand'),
(8966, 'Onaero River Scenic Reserve', 79, 'Taranaki', -38.998611, 174.365556, 'New Zealand'),
(9241, 'Pukerau Red Tussock Scientific Reserve', 80, 'Southland', -46.09692, 169.077353, 'New Zealand'),
(9293, 'Raincliff Historic Reserve', 39, 'Canterbury', -44.1625, 170.993056, 'New Zealand'),
(9355, 'Ripapa Island Historic Reserve', 39, 'Canterbury', -43.620528, 172.754173, 'New Zealand'),
(9376, 'Rotokahu Scenic Reserve', 79, 'Wellington', -39.154167, 175.188056, 'New Zealand'),
(9486, 'Station Creek Scenic Reserve', 79, 'Nelson', -42.211389, 172.2625, 'New Zealand');

INSERT INTO `profile` (`id`, `username`, `password`, `first_name`, `middle_name`, `last_name`, `gender`, `date_of_birth`, `date_of_creation`, `is_admin`) VALUES
(1, 'admin@travelea.com', '25F43B1486AD95A1398E3EEB3D83BC4010015FCC9BEDB35B432E00298D5021F7', 'Default', '', 'Admin', 'male', '2019-01-01', '2019-01-01 13:00:00.000000', 1),
(2, 'guestUser@travelea.com', '6B93CCBA414AC1D0AE1E77F3FAC560C748A6701ED6946735A49D463351518E16', 'Dave', '', 'McInloch', 'Other', '1998-10-18', '2019-04-17 15:31:19.579000', 0);

INSERT INTO `profile_nationality` (`profile_id`, `nationality_id`) VALUES
(1, 67),
(2, 4),
(2, 5);

INSERT INTO `profile_traveller_type` (`profile_id`, `traveller_type_id`) VALUES
(1, 6),
(2, 1),
(2, 2);

INSERT INTO `profile_passport` (`profile_id`, `passport_id`) VALUES
(2, 3),
(2, 4),
(2, 5);