DROP FUNCTION IF EXISTS change_study_group_form_of_education_to;
CREATE OR REPLACE FUNCTION change_study_group_form_of_education_to(target_id INTEGER, new_form_of_education TEXT)
RETURNS study_group
LANGUAGE plpgsql
AS
'
DECLARE
    updated_study_group study_group;
BEGIN
    UPDATE study_group
    SET form_of_education = new_form_of_education
    WHERE study_group.id = target_id
    RETURNING * INTO updated_study_group;

    RETURN updated_study_group;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE NOTICE ''StudyGroup with ID % not found'', p_id;
        RETURN NULL;
END;
';

DROP FUNCTION IF EXISTS count_study_groups_total_expelled_students;
CREATE OR REPLACE FUNCTION count_study_groups_total_expelled_students()
RETURNS INTEGER
LANGUAGE plpgsql
AS
'
DECLARE
    total_expelled_student_count INTEGER;
BEGIN
    SELECT
        COALESCE(SUM(study_group.expelled_students), 0) INTO total_expelled_student_count
    FROM study_group;

    RETURN total_expelled_student_count;
END;
';

DROP FUNCTION IF EXISTS count_study_groups_with_average_mark;
CREATE OR REPLACE FUNCTION count_study_groups_with_average_mark(target_value INTEGER)
RETURNS INTEGER
LANGUAGE plpgsql
AS
'
DECLARE
    count INTEGER;
BEGIN
    SELECT COUNT(*) INTO count
    FROM study_group
    WHERE study_group.average_mark = target_value;

    RETURN count;
END;
';

DROP FUNCTION IF EXISTS delete_study_groups_with_average_mark;
CREATE OR REPLACE FUNCTION delete_study_groups_with_average_mark(target_value INTEGER)
RETURNS bool
LANGUAGE plpgsql
AS
'
BEGIN
    DELETE
    FROM study_group
    WHERE study_group.average_mark = target_value;

    RETURN true;
END;
';

DROP FUNCTION IF EXISTS find_study_groups_with_name_like;
CREATE OR REPLACE FUNCTION find_study_groups_with_name_like(substr TEXT)
RETURNS SETOF study_group
LANGUAGE plpgsql
AS
'
BEGIN
    RETURN QUERY
    SELECT * FROM study_group
    WHERE study_group.name LIKE ''%'' || substr || ''%'';
END;
';
