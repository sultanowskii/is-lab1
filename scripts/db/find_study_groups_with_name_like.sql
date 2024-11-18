DROP FUNCTION IF EXISTS find_study_groups_with_name_like;

CREATE OR REPLACE FUNCTION find_study_groups_with_name_like(substr TEXT)
RETURNS SETOF study_group
LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY
    SELECT * FROM study_group
    WHERE study_group.name LIKE '%' || substr || '%';
END;
$$;
