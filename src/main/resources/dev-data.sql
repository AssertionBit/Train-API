INSERT INTO public.routes (id, start, "end", estimated_time) VALUES
        (0, 'Moscow', 'Lipetsk', 60 * 6),
        (1, 'Lipetsk', 'Moscow', 60 * 6)
;

INSERT INTO public.trains (id, name) VALUES
    (0, 'Pobeda'),
    (1, 'Sapsan');

INSERT INTO public.wagon (id, code) VALUES
    (0, 'A001'),
    (1, 'A00B');

INSERT INTO public.sits (id, number, is_top, is_taken) VALUES
    (0, 1, false, false),
    (1, 2, true, true);

INSERT INTO public.route_trains (id, route_id, train_id, begin_time, end_time) VALUES
    (0, 0, 0, now(), now() + interval '6 hours');

INSERT INTO public.train_wagons (id, train_id, wagon_id) VALUES
    (0, 0, 0),
    (1, 0, 1);

INSERT INTO public.wagon_sits (id, wagon_id, sit_id, is_taken) VALUES
    (0, 0, 0, false),
    (1, 0, 1, false);
