DROP FUNCTION change_study_group_form_of_education_to;

CREATE OR REPLACE FUNCTION change_study_group_form_of_education_to(target_id INTEGER, new_form_of_education TEXT)
RETURNS study_group
LANGUAGE plpgsql
AS
$$
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
        RAISE NOTICE 'StudyGroup with ID % not found', p_id;
        RETURN NULL;
END;
$$;
