CREATE TABLE IF NOT EXISTS `task` (
  `task_id` VARCHAR(10) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `description` TEXT,
  `parent_task_id` VARCHAR(10),
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;