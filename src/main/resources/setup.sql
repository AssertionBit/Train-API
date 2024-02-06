CREATE TABLE IF NOT EXISTS routes (
    id int UNIQUE NOT NULL,
    start varchar(256) NOT NULL,
    "end" varchar(256) NOT NULL,
    estimated_time int NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS trains (
    id int UNIQUE NOT NULL,
    name varchar(256) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS wagon (
    id int UNIQUE NOT NULL,
    code varchar(256) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS sits (
    id int UNIQUE NOT NULL,
    number varchar(256) NOT NULL,
    is_top boolean NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS tickets (
    id int UNIQUE NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS tickets_group (
    id int UNIQUE NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS route_trains (
    id int UNIQUE NOT NULL,
    route_id int NOT NULL,
    train_id int NOT NULL,
    begin_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    is_gone bool NOT NULL,
    FOREIGN KEY(route_id) REFERENCES routes(id),
    FOREIGN KEY(train_id) REFERENCES trains(id),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS public.train_wagons (
    id int UNIQUE NOT NULL,
    train_id int NOT NULL,
    wagon_id int NOT NULL,
    FOREIGN KEY(train_id) REFERENCES trains(id),
    FOREIGN KEY(wagon_id) REFERENCES wagon(id),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.wagon_sits (
    id int UNIQUE NOT NULL,
    wagon_id int NOT NULL,
    sit_id int NOT NULL,
    is_taken boolean NOT NULL,
    FOREIGN KEY(wagon_id) REFERENCES trains(id),
    FOREIGN KEY(sit_id) REFERENCES sits(id),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS public.route_tickets (
    id int NOT NULL,
    route_id int NOT NULL,
    ticket_id int NOT NULL,
    sit_id int NOT NULL,
    group_id int,
    FOREIGN KEY(route_id) REFERENCES routes(id),
    FOREIGN KEY(ticket_id) REFERENCES tickets(id),
    FOREIGN KEY(sit_id) REFERENCES sits(id),
    FOREIGN KEY(group_id) REFERENCES tickets_group(id),
    PRIMARY KEY(id)
);
