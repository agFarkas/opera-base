CREATE FUNCTION check_artist_not_conductor(perf_id int8, artist_id int8)
    RETURNS BOOLEAN
    language plpgsql AS '
    DECLARE
        cond_id int8;
    BEGIN
        select prf.conductor_id
        into cond_id
        from performance prf
        where prf.id = perf_id;

        RETURN (cond_id <> artist_id);
    END;
';