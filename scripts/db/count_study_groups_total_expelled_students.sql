DROP FUNCTION count_study_groups_total_expelled_students;

CREATE OR REPLACE FUNCTION count_study_groups_total_expelled_students()
RETURNS INTEGER
LANGUAGE plpgsql
AS
$$
DECLARE
    total_expelled_student_count INTEGER;
BEGIN
    SELECT
        COALESCE(SUM(study_group.expelled_students), 0) INTO total_expelled_student_count
    FROM study_group;

    RETURN total_expelled_student_count;
END;
$$;
