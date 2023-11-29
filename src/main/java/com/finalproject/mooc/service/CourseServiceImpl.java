package com.finalproject.mooc.service;

import com.finalproject.mooc.entity.Course;
import com.finalproject.mooc.entity.Subject;
import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.model.requests.CreateCourseRequest;
import com.finalproject.mooc.model.requests.CreateSubjectRequest;
import com.finalproject.mooc.model.responses.*;
import com.finalproject.mooc.repository.CourseRepository;
import com.finalproject.mooc.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    SubjectRepository subjectRepository;


    //sementara create course sama subject dipisahkan, menunggu info dari FE
    @Override
    public CourseResponseNoSubject createCourse(CreateCourseRequest courseRequest, String username) {
        Course course = Course.builder()
                .courseName(courseRequest.getCourseFor())
                .courseCategory(courseRequest.getCourseCategory())
                .idCourse(courseRequest.getCourseCode())
                .TypePremium(courseRequest.getTypePremium())
                .courseLevel(courseRequest.getCourseLevel())
                .coursePrice(courseRequest.getCoursePrice())
                .courseAbout(courseRequest.getCourseAbout())
                .courseFor(courseRequest.getCourseFor())
                .urlTele(courseRequest.getUrlTele())
                .build();

        courseRepository.save(course);
        return toCourseResponseNoSubject(course);
    }

    @Override
    public SubjectResponse createSubject(CreateSubjectRequest subjectRequest, String username, String courseCode) {
        Subject subject = Subject.builder()
                .idSubject(subjectRequest.getSubjectCode())
                .title(subjectRequest.getTitle())
                .url(subjectRequest.getUrl())
                .chapter(subjectRequest.getChapter())
                .sequence(subjectRequest.getSequence())
                .TypePremium(subjectRequest.getTypePremium())
                .course(courseRepository.findById(courseCode)
                        .orElseThrow(()->
                                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Tidak Ditemukan")))
                .build();

        subjectRepository.save(subject);
        return toSubjectResponse(subject);
    }

    @Transactional(readOnly = true)
    @Override
    public CoursePaginationResponse showCourse(Integer page, String username) {
        page -= 1;
        Pageable halaman = PageRequest.of(page, 3);
        Page<Course> coursePage = courseRepository.findAll(halaman);

        return toCoursePaginationResponse(coursePage);
    }

    @Transactional(readOnly = true)
    @Override
    public CoursePaginationResponse showCourseByCategory(Integer page, List<CourseCategory> categories, String username) {
        log.info("CoursePagination bejalan");
        page -= 1; //halaman asli dari index 0
        //sementara size nya 3
        Pageable halaman = PageRequest.of(page, 3);
        Page<Course> coursePage = courseRepository.findCourseByCategory(categories, halaman)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Data tidak ditemukan"));

        if (coursePage.isEmpty() || coursePage == null){
            log.info("validate data empty");
            return this.showCourse(1, username);
        }

        if (coursePage.getContent().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Data not found");

        return toCoursePaginationResponse(coursePage);
    }

    @Transactional(readOnly = true)
    @Override
    public CoursePaginationResponse showCourseBySearch(Integer page, String title, String username) {

        page -= 1;
        Pageable halaman = PageRequest.of(page, 3);
        Page<Course> coursePage = courseRepository.searchCourse(title, halaman);

        if (coursePage.isEmpty() || coursePage == null){
            log.info("validate data empty");
            return this.showCourse(1, username);
        }

        return toCoursePaginationResponse(coursePage);
    }

    @Override
    public CourseResponseWithSubject showDetailCourse(String courseCode) {
        Course course = courseRepository.findCourseJoinSubject(courseCode)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Tidak Ditemukan"));
        //pakek fungsi CourseResponseWithSubject, maka keajaiban akan terjadi uehe

        List<Subject> subjects = course.getSubjects();
        CourseResponseWithSubject courseResponseWithSubject = toCourseResponseWithSubject(course, subjects);
        return courseResponseWithSubject;
    }


    private SubjectResponse toSubjectResponse(Subject subject) {
        return SubjectResponse.builder()
                .subjectCode(subject.getIdSubject())
                .title(subject.getTitle())
                .url(subject.getUrl())
                .chapter(subject.getChapter())
                .sequence(subject.getSequence())
                .TypePremium(subject.getTypePremium())
                .build();
    }


    private CourseResponseWithSubject toCourseResponseWithSubject(Course course, List<Subject> subjects) {
        //mapping course nya pakek fungsi konversi ke subjectResponse
        List<SubjectResponse> subjectResponseList = subjects.stream()
                .map(this::toSubjectResponse)
                .collect(Collectors.toList());

        return CourseResponseWithSubject.builder()
                .courseCode(course.getIdCourse())
                .courseName(course.getCourseName())
                .courseCategory(course.getCourseCategory())
                .TypePremium(course.getTypePremium())
                .coursePrice(course.getCoursePrice())
                .courseAbout(course.getCourseAbout())
                .courseFor(course.getCourseFor())
                .courseLevel(course.getCourseLevel())
                .urlTele(course.getUrlTele())
                .subjects(subjectResponseList)
                .build();
    }

    private CourseResponseNoSubject toCourseResponseNoSubject(Course course) {
        return CourseResponseNoSubject.builder()
                .courseCode(course.getIdCourse())
                .courseName(course.getCourseName())
                .courseCategory(course.getCourseCategory())
                .TypePremium(course.getTypePremium())
                .coursePrice(course.getCoursePrice())
                .courseAbout(course.getCourseAbout())
                .courseFor(course.getCourseFor())
                .courseLevel(course.getCourseLevel())
                .urlTele(course.getUrlTele())
                .build();
    }

    private CoursePaginationResponse toCoursePaginationResponse(Page<Course> coursePage){
        List<Course> courseResponses = coursePage.getContent();

        //mapping course nya pakek fungsi konversi ke courseResponse
        List<CourseResponseNoSubject> courseResponseNoSubjectList = courseResponses.stream()
                .map(this::toCourseResponseNoSubject)
                .collect(Collectors.toList());

        return CoursePaginationResponse.builder()
                .courseResponseNoSubject(courseResponseNoSubjectList)
                .productCurrentPage(coursePage.getNumber() + 1)
                .productTotalPage(coursePage.getTotalPages())
                .build();
    }

    /**
     * Funsgi ini ternyata gabisa untuk pagination, karena harus showAll
     */
    private List<Course> getUniqueFromJoinResult(List<Course> courses) {
        return new ArrayList<>(courses.stream()
                // Membuat perulangan hanya sekali bila id sama
                .collect(Collectors.toMap(Course::getIdCourse, course -> course,
                        (existing, replacement) -> existing))
                .values());
    }

}
