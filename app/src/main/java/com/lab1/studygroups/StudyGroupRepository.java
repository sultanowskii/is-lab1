package com.lab1.studygroups;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lab1.common.CRUDRepository;

@Repository
public interface StudyGroupRepository extends CRUDRepository<StudyGroup> {
    @Query(value = "SELECT delete_study_groups_with_average_mark(:average_mark)", nativeQuery = true)
    boolean deleteWithAverageMark(@Param("average_mark") int averageMark);

    @Query(value = "SELECT count_study_groups_with_average_mark(:average_mark)", nativeQuery = true)
    int countWithAverageMark(@Param("average_mark") int averageMark);

    @Query(value = "SELECT * FROM find_study_groups_with_name_like(:partal_name)", nativeQuery = true)
    List<StudyGroup> findWithNameLike(@Param("partal_name") String partialName);

    @Query(value = "SELECT count_study_groups_total_expelled_students()", nativeQuery = true)
    int countTotalExpelledStudents();

    @Query(value = "SELECT * FROM change_study_group_form_of_education_to(:id, :form_of_education)", nativeQuery = true)
    StudyGroup changeFormOfEducationTo(@Param("id") int id, @Param("form_of_education") String formOfEducation);
}
