DROP FUNCTION delete_study_groups_with_average_mark;

CREATE OR REPLACE FUNCTION delete_study_groups_with_average_mark(target_value INTEGER)
RETURNS bool
LANGUAGE plpgsql
AS
$$
BEGIN
    DELETE
    FROM study_group
    WHERE study_group.average_mark = target_value;

    RETURN true;
END;
$$;
