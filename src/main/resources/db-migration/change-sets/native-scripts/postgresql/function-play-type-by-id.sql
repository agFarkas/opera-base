CREATE FUNCTION get_play_type_by_id(play_id int8)
    RETURNS varchar
    language plpgsql AS '
    DECLARE
        play_type VARCHAR(5);
    BEGIN
        select p.type
        into play_type
        from play p
        where p.id = play_id;

        RETURN play_type;
    END;
';