DROP FUNCTION IF EXISTS count_study_groups_with_average_mark;

CREATE OR REPLACE FUNCTION count_study_groups_with_average_mark(target_value INTEGER)
RETURNS INTEGER
LANGUAGE plpgsql
AS
$$
DECLARE
    count INTEGER;
BEGIN
    SELECT COUNT(*) INTO count
    FROM study_group
    WHERE study_group.average_mark = target_value;

    RETURN count;
END;
$$;
