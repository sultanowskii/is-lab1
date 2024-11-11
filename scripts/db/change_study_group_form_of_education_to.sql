DROP FUNCTION change_study_group_form_of_education_to;

CREATE OR REPLACE FUNCTION change_study_group_form_of_education_to(target_id INTEGER, new_form_of_education TEXT)
RETURNS bool
LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE study_group
    SET form_of_education = new_form_of_education
    WHERE study_group.id = target_id;

    RETURN true;
END;
$$;
