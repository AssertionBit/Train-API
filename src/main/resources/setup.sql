CREATE TABLE IF NOT EXISTS public.trains (
    id int UNIQUE NOT NULL,
    name varchar(256) NOT NULL,
    PRIMARY KEY(id)
);
