CREATE FUNCTION check_role_in_play(perf_id int8, rl_id int8)
    RETURNS BOOLEAN
    language plpgsql AS '
    DECLARE
        cnt_roles int;
    BEGIN
        select count(r.id) AS cnt
        into cnt_roles
        from role r
                 right join play p on r.play_id = p.id
        where r.id = rl_id
          and p.id = (select prf.play_id
                      from performance prf
                      where prf.id = perf_id);

        RETURN (cnt_roles = 1);
    END;
';