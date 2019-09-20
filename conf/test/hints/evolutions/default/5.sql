# --- !Ups

INSERT INTO `objective` (`id`, `destination_id`, `riddle`, `radius`, `owner_id`) VALUES
(30, 9000, 'Objective 1 in solved quest', 1, 10),
(31, 9000, 'Objective 2 in solved quest', 1, 10);



INSERT INTO `quest` (`id`, `title`, `start_date`, `end_date`, `owner_id`) VALUES
(10, 'Quest completed by user 2', '1998-05-21 12:00:01', '2100-06-21 23:59:59', 10);


INSERT INTO `quest_objective` (`quest_id`, `objective_id`) VALUES
(10, 30),
(10, 31);


INSERT INTO `quest_attempt` (`id`, `attempted_by_id`, `quest_attempted_id`, `solved_current`, `checked_in_index`, `completed`) VALUES
(19, 2, 10, 0, 2, 1),
(20, 2, 2, 1, 0, 0),
(21, 2, 1, 0, 1, 0);

# --- !Downs