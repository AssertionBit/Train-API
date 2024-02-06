-- Will sanitize database each period of time
CREATE OR REPLACE FUNCTION sanitize_routes() RETURNS VOID AS $$
BEGIN
    RAISE NOTICE 'hello world';
END;

$$ LANGUAGE plpgsql;

-- Will run function in background
CREATE OR REPLACE FUNCTION schedule_sanitazation_routes() RETURNS VOID AS $$
DECLARE
    data public.route_trains;
BEGIN
    -- NOTE: This doesn't work, module missing?
--     SELECT bgw_start_recurring_job (
--         'sanitize-routes',
--         'sanitize_routes',
--         '1 minute'
--     ) INTO job_id;

    -- Actual code
    LOOP
        PERFORM pg_sleep(60);
        BEGIN;
            FOR data IN SELECT * FROM public.route_trains
            LOOP
                IF now() <= SELECT routes.estimated_time THEN
                    
                END IF;
            END LOOP;
        END;
    END LOOP;
END;

$$ LANGUAGE plpgsql;