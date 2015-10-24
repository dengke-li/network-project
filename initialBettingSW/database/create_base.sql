

-- Drop tables

drop table if exists result_competitions_winner;
drop table if exists result_competitions_podium;
drop table if exists play_competitions_winner;
drop table if exists play_competitions_podium;
drop table if exists bets_winner;
drop table if exists bets_podium;
drop table if exists competitions_winner;
drop table if exists competitions_podium;
drop table if exists competitors;
drop table if exists subscribers;



-- Create tables

create table subscribers
(
  id        serial      primary key,
  firstname varchar(15) not null,
  lastname  varchar(15) not null,
  username  varchar(15) not null,
  password  varchar(8)  not null,
  borndate	varchar(10) not null,
  account	integer		not null
);


create table competitors
(
  id        serial      primary key,
  firstname varchar(15) not null,
  lastname  varchar(15) not null,
  borndate  varchar(12) not null
);


create table competitions_winner
(
  id               serial  primary key,
  name varchar(15) not null,
  c_type varchar(15) not null,
  close_date date not null
);

create table competitions_podium
(
  id               serial  primary key,
  name varchar(15) not null,
  c_type varchar(15) not null,
  close_date date not null  
);

create table play_competitions_winner
(
  id               serial  primary key,  
  id_competitor integer not null references competitors,
  competition_winner_id integer not null references competitions_winner
);

create table play_competitions_podium
(
  id               serial  primary key,  
  id_competitor integer not null references competitors,
  competition_podium_id integer not null references competitions_podium
);
  
  
create table bets_winner
(
  id               serial  primary key,
  number_of_tokens integer not null,
  id_subscriber integer not null references subscribers ,
  id_competition integer not null references competitions_winner,
  result_id_competitor integer not null references competitors
);

create table bets_podium
(
  id               serial  primary key,
  number_of_tokens integer not null,
  id_subscriber integer not null references subscribers,
  id_competition integer not null references competitions_podium,
  id_winner integer not null references competitors,
  id_second integer not null references competitors,
  id_third integer not null references competitors
);

create table result_competitions_winner
(
  id               serial  primary key,
  competition_winner_id integer not null references competitions_winner,
  result_id_competitor integer not null references competitors
);

create table result_competitions_podium
(
  id               serial  primary key,
  competition_podium_id integer not null references competitions_podium,
  result1_id_competitor integer not null references competitors,
  result2_id_competitor integer not null references competitors,
  result3_id_competitor integer not null references competitors
);




-- Sample data
--insert into subscribers(firstname, lastname)  values ('Mayte',    'SEGARRA');
--insert into subscribers(firstname, lastname)  values ('Issam',    'REBAI');
--insert into subscribers(firstname, lastname)  values ('Philippe', 'TANGUY');