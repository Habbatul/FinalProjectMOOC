package com.finalproject.mooc.service;

import com.finalproject.mooc.entity.Course;
import com.finalproject.mooc.entity.Subject;
import com.finalproject.mooc.entity.User;
import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.ERole;
import com.finalproject.mooc.enums.TypePremium;
import com.finalproject.mooc.model.requests.CreateCourseRequest;
import com.finalproject.mooc.model.requests.CreateSubjectRequest;
import com.finalproject.mooc.model.responses.*;
import com.finalproject.mooc.repository.CourseRepository;
import com.finalproject.mooc.repository.OrderRepository;
import com.finalproject.mooc.repository.SubjectRepository;
import com.finalproject.mooc.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;

    //sementara create course sama subject dipisahkan, menunggu info dari FE
    @Transactional
    @Override
    public CourseCreateResponse createCourse(CreateCourseRequest courseRequest, String username) {

        User user = userRepository.findUserByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "username tidak ditemukan"));

        Course course = Course.builder()
                .courseName(courseRequest.getCourseName())
                .courseCategory(courseRequest.getCourseCategory())
                .TypePremium(courseRequest.getTypePremium())
                .courseLevel(courseRequest.getCourseLevel())
                .coursePrice(courseRequest.getCoursePrice())
                .courseAbout(courseRequest.getCourseAbout())
                .courseFor(courseRequest.getCourseFor())
                .urlTele(courseRequest.getUrlTele())
                .user(user)
                .build();

        courseRepository.save(course);
        return toCourseCreateResponse(course);
    }

    @Transactional
    @Override
    public SubjectResponse createSubject(CreateSubjectRequest subjectRequest, String username, String courseCode) {
        Subject subject = Subject.builder()
                .title(subjectRequest.getTitle())
                .url(subjectRequest.getUrl())
                .chapter(subjectRequest.getChapter())
                .sequence(subjectRequest.getSequence())
                .TypePremium(subjectRequest.getTypePremium())
                .course(courseRepository.findById(courseCode)
                        .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Tidak Ditemukan")))
                .build();

        subjectRepository.save(subject);
        return toSubjectResponse(subject);
    }

    @Transactional(readOnly = true)
    @Override
    public CoursePaginationResponse showCourseByCategoryOrLevelOrPremiumAndSearch(Integer page, List<CourseCategory> categories,
                                                                                  List<CourseLevel> courseLevel,
                                                                                  List<TypePremium> typePremium,
                                                                                  String keyword, String username) {

        log.info("CoursePagination bejalan");
        page -= 1; //halaman asli dari index 0
        //sementara size nya 3
        Pageable halaman = PageRequest.of(page, 3);
        Page<Course> coursePage = courseRepository.findCourseByCategoryAndLevel(categories, courseLevel, typePremium, keyword, halaman)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Data tidak ditemukan"));

        if (coursePage.getContent().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Data tidak ditemukan");

        return toCoursePaginationResponse(coursePage);
    }

    @Transactional(readOnly = true)
    @Override
    public CourseResponseWithSubject showDetailCourse(String courseCode, String username) {
        Course course = courseRepository.findCourseJoinSubject(courseCode)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Tidak Ditemukan"));
        //pakek fungsi CourseResponseWithSubject, maka keajaiban akan terjadi uehe

        List<Subject> subjects = course.getSubjects();

        return toCourseResponseWithSubject(course, subjects,
                userRepository.findRoleNamesByUsername(username).orElseThrow(()->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anda belum login")),
                username);
    }


    //semua helper ada dibawah ini
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

    //pembatasan untuk limit url modul bila user belum membeli course premium
    private SubjectResponse toSubjectResponseLimitUrl(Subject subject, String courseCode, String username) {
        //cek apakah tipe module premium
        if(subject.getTypePremium().equals(TypePremium.PREMIUM)){
            //cek apakah sudah membayar
            if(orderRepository.findIsPremiumPaid(username, courseCode))
                return SubjectResponse.builder()
                        .subjectCode(subject.getIdSubject())
                        .title(subject.getTitle())
                        .url(subject.getUrl())
                        .chapter(subject.getChapter())
                        .sequence(subject.getSequence())
                        .TypePremium(subject.getTypePremium())
                        .build();
            else
                return SubjectResponse.builder()
                        .subjectCode(subject.getIdSubject())
                        .title(subject.getTitle())
                        .url("ANDA BELUM MEMBELI KELAS INI (Beri Url untuk redirect ke video dibatasi)")
                        .chapter(subject.getChapter())
                        .sequence(subject.getSequence())
                        .TypePremium(subject.getTypePremium())
                        .build();
        } else{
            return SubjectResponse.builder()
                    .subjectCode(subject.getIdSubject())
                    .title(subject.getTitle())
                    .url(subject.getUrl())
                    .chapter(subject.getChapter())
                    .sequence(subject.getSequence())
                    .TypePremium(subject.getTypePremium())
                    .build();
        }
    }


    private CourseResponseWithSubject toCourseResponseWithSubject(Course course, List<Subject> subjects, Set<ERole> role, String username) {
        List<SubjectResponse> subjectResponseList = new ArrayList<>();

        //jika role admin ijinkan lihat semua
        if(role.contains(ERole.ADMIN)){
            subjectResponseList = subjects.stream()
                    .sorted(Comparator.comparingInt(Subject::getSequence)) //sorted berdasarkan sequence subject/module
                    .map(this::toSubjectResponse)
                    .collect(Collectors.toList());
        }
        //jika role bukan admin cek apakah sudah order
        else{
            subjectResponseList = subjects.stream()
                    .sorted(Comparator.comparingInt(Subject::getSequence)) //sorted berdasarkan sequence subject/module
                    .map(subject -> toSubjectResponseLimitUrl(subject, course.getIdCourse(),username))
                    .collect(Collectors.toList());
        }

        //mapping course nya pakek fungsi konversi ke subjectResponse
        return CourseResponseWithSubject.builder()
                .teacher(course.getUser().getUsername())
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
                .numberOfModule(courseRepository.findTotalModule(course.getIdCourse()))
                .teacher(course.getUser().getUsername())
                .build();
    }

    private CoursePaginationResponse toCoursePaginationResponse(Page<Course> coursePage){
        List<Course> courseResponses = coursePage.getContent();

        //mapping course nya pakek fungsi konversi ke courseResponse
        //toCourseResponseNoSubject (course, totalModul)
        List<CourseResponseNoSubject> courseResponseNoSubjectList = courseResponses.stream()
                .map(this::toCourseResponseNoSubject)
                .collect(Collectors.toList());

        return CoursePaginationResponse.builder()
                .courseResponseNoSubject(courseResponseNoSubjectList)
                .productCurrentPage(coursePage.getNumber() + 1)
                .productTotalPage(coursePage.getTotalPages())
                .build();
    }

    private CourseCreateResponse toCourseCreateResponse(Course course) {
        return CourseCreateResponse.builder()
                .courseCode(course.getIdCourse())
                .courseName(course.getCourseName())
                .courseCategory(course.getCourseCategory())
                .TypePremium(course.getTypePremium())
                .coursePrice(course.getCoursePrice())
                .courseAbout(course.getCourseAbout())
                .courseFor(course.getCourseFor())
                .courseLevel(course.getCourseLevel())
                .urlTele(course.getUrlTele())
                .teacher(course.getUser().getUsername())
                .build();
    }
}

//    /**
//     * Funsgi ini ternyata gabisa untuk pagination, karena harus showAll
//     */
//    private List<Course> getUniqueFromJoinResult(List<Course> courses) {
//        return new ArrayList<>(courses.stream()
//                // Membuat perulangan hanya sekali bila id sama
//                .collect(Collectors.toMap(Course::getIdCourse, course -> course,
//                        (existing, replacement) -> existing))
//                .values());
//    }

//    @Transactional(readOnly = true)
//    @Override
//    public CoursePaginationResponse showCourse(Integer page, String username) {
//        page -= 1;
//        Pageable halaman = PageRequest.of(page, 3);
//        Page<Course> coursePage = courseRepository.findAll(halaman);
//
//        return toCoursePaginationResponse(coursePage);
//    }

//
//    @Transactional(readOnly = true)
//    @Override
//    public CoursePaginationResponse showCourseBySearch(Integer page, String title, String username) {
//        page -= 1;
//        Pageable halaman = PageRequest.of(page, 3);
//        Page<Course> coursePage = courseRepository.searchCourse(title, halaman)
//                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Data tidak ditemukan"));
//
//        return toCoursePaginationResponse(coursePage);
//    }


