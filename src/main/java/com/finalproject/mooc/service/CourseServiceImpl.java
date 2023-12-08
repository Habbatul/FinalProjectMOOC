package com.finalproject.mooc.service;

import com.finalproject.mooc.entity.*;
import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.ERole;
import com.finalproject.mooc.enums.TypePremium;
import com.finalproject.mooc.model.requests.CreateCourseRequest;
import com.finalproject.mooc.model.requests.CreateSubjectRequest;
import com.finalproject.mooc.model.requests.UpdateCourseRequest;
import com.finalproject.mooc.model.requests.UpdateSubjectRequest;
import com.finalproject.mooc.model.responses.*;
import com.finalproject.mooc.repository.*;
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

    //kebutuhan intergrasi course yang diambil
    @Autowired
    SubjectProgressRepository subjectProgressRepository;
    @Autowired
    CourseProgressRepository courseProgressRepository;

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
    public CourseResponseNoSubject updateCourse(UpdateCourseRequest updateCourseRequest, String username, String courseCode) {
        User user = userRepository.findUserByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "username tidak ditemukan"));

        Course oldCourseId = courseRepository.findById(courseCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course tidak ditemukan"));

        Course courseUpdate = Course.builder()
                .idCourse(courseCode)
                .courseName((updateCourseRequest.getCourseName() != null && !updateCourseRequest.getCourseName().isEmpty()) ? updateCourseRequest.getCourseName() : oldCourseId.getCourseName())
                .courseCategory((updateCourseRequest.getCourseCategory() != null) ? updateCourseRequest.getCourseCategory() : oldCourseId.getCourseCategory())
                .courseLevel((updateCourseRequest.getCourseLevel() != null) ? updateCourseRequest.getCourseLevel() : oldCourseId.getCourseLevel())
                .coursePrice((updateCourseRequest.getCoursePrice() != null) ? updateCourseRequest.getCoursePrice() : oldCourseId.getCoursePrice())
                .courseAbout((updateCourseRequest.getCourseAbout() != null && !updateCourseRequest.getCourseAbout().isEmpty()) ? updateCourseRequest.getCourseAbout() : oldCourseId.getCourseAbout())
                .courseFor((updateCourseRequest.getCourseFor() != null && !updateCourseRequest.getCourseFor().isEmpty()) ? updateCourseRequest.getCourseFor() : oldCourseId.getCourseFor())
                .TypePremium((updateCourseRequest.getTypePremium() != null) ? updateCourseRequest.getTypePremium() : oldCourseId.getTypePremium())
                .urlTele((updateCourseRequest.getUrlTele() != null && !updateCourseRequest.getUrlTele().isEmpty()) ? updateCourseRequest.getUrlTele() : oldCourseId.getUrlTele())
                .user(user)
                .build();

        courseRepository.updateIdCourse(courseUpdate.getTypePremium(), courseUpdate.getCourseName(), courseUpdate.getCoursePrice(), courseUpdate.getCourseAbout(), courseUpdate.getCourseFor(), courseUpdate.getUrlTele(), courseUpdate.getCourseCategory(), courseUpdate.getCourseLevel(), courseCode);

        return toCourseResponseNoSubject(courseUpdate);
    }

    @Transactional
    @Override
    public void deleteCourse(String courseCode, String username) {
        userRepository.findUserByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "username tidak ditemukan"));

        log.debug("Running service deleteCourse, kode");
        if (courseRepository.existsById(courseCode)){
            courseRepository.deleteById(courseCode);
            log.info("Is success delete");
        } else {
            log.error("Course not found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "course not found");
        }
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

        //tambahkan ke bagian progress subject (tabel dimana user mengambil course beserta subject) untuk integrasi ke user
        subjectProgressRepository.saveAll(subjectToSubjectProgressList(subject, courseCode));


        return toSubjectResponse(subject);
    }

    @Transactional
    @Override
    public SubjectResponse updateSubject(UpdateSubjectRequest updateSubjectRequest, String username, String courseCode, String subjectCode) {

        userRepository.findUserByUsername(username).orElseThrow(()->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "username tidak ditemukan"));

        // Mengambil data subjek lama dari repository berdasarkan kode subjek
        Subject oldSubject = subjectRepository.findById(subjectCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subjek tidak ditemukan"));

        // Memeriksa apakah kode kursus tidak kosong atau null
        if (courseCode != null && !courseCode.isEmpty()) {
            // Jika kode kursus tidak kosong, maka mengambil data kursus baru dari repository
            Course course = courseRepository.findById(courseCode)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kursus tidak ditemukan"));

            oldSubject.setCourse(course);
        }

        // Membuat objek subjek baru berdasarkan data yang diterima dari permintaan pembaruan
        Subject subjectUpdate = Subject.builder()
                .idSubject(subjectCode)
                .title((updateSubjectRequest.getTitle() != null && !updateSubjectRequest.getTitle().isEmpty()) ? updateSubjectRequest.getTitle() : oldSubject.getTitle())
                .url((updateSubjectRequest.getUrl() != null && !updateSubjectRequest.getUrl().isEmpty()) ? updateSubjectRequest.getUrl() : oldSubject.getUrl())
                .chapter((updateSubjectRequest.getChapter() != null) ? updateSubjectRequest.getChapter() : oldSubject.getChapter())
                .sequence((updateSubjectRequest.getSequence() != null) ? updateSubjectRequest.getSequence() : oldSubject.getSequence())
                .TypePremium(updateSubjectRequest.getTypePremium() != null ? updateSubjectRequest.getTypePremium() : oldSubject.getTypePremium())
                .build();

        // Menyimpan subjek yang diperbarui ke repository
        subjectRepository.updateIdSubject(subjectUpdate.getTypePremium(),subjectUpdate.getChapter(),subjectUpdate.getSequence(),subjectUpdate.getTitle(),subjectUpdate.getUrl(),oldSubject.getCourse(), subjectCode);


        // Mengonversi subjek yang diperbarui ke respons subjek dan mengembalikannya
        return toSubjectResponse(subjectUpdate);
    }


    @Transactional(readOnly = true)
    @Override
    public CoursePaginationResponse<CourseResponseNoSubject> showCourseByCategoryOrLevelOrPremiumAndSearch(Integer page, List<CourseCategory> categories,
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

    @Override
    public ManageCoursePaginationResponse showManageCourseByFilterSearchPagination(String username, Integer page, List<CourseCategory> category, List<CourseLevel> courseLevel, List<TypePremium> typePremium, String keyword) {
        log.info("ManageCoursePagination bejalan");
        page -= 1; //halaman asli dari index 0
        //sementara size nya 3
        Pageable halaman = PageRequest.of(page, 3);
        Page<Course> coursePage = courseRepository.findCourseByAdmin(category, courseLevel, typePremium, keyword, username, halaman)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Data tidak ditemukan"));

        if (coursePage.getContent().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Data tidak ditemukan");

        return toManageCoursePaginationResponse(coursePage);
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
            log.info("Module ini PREMIUM");
            //cek apakah sudah membayar
            if(orderRepository.findIsPremiumPaid(username, courseCode)){
                log.info("User ini sudah membayar");
                return SubjectResponse.builder()
                        .subjectCode(subject.getIdSubject())
                        .title(subject.getTitle())
                        .url(subject.getUrl())
                        .chapter(subject.getChapter())
                        .sequence(subject.getSequence())
                        .TypePremium(subject.getTypePremium())
                        .build();
            }
            else{
                log.info("User ini belum membayar");
                return SubjectResponse.builder()
                        .subjectCode(subject.getIdSubject())
                        .title(subject.getTitle())
                        .url("ANDA BELUM MEMBELI KELAS INI (Beri Url untuk redirect ke video dibatasi)")
                        .chapter(subject.getChapter())
                        .sequence(subject.getSequence())
                        .TypePremium(subject.getTypePremium())
                        .build();
            }

        } else{
            log.info("Module ini tidak PREMIUM");
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

    private CoursePaginationResponse<CourseResponseNoSubject> toCoursePaginationResponse(Page<Course> coursePage){
        List<Course> courseResponses = coursePage.getContent();

        //mapping course nya pakek fungsi konversi ke courseResponse
        //toCourseResponseNoSubject (course, totalModul)
        List<CourseResponseNoSubject> courseResponseNoSubjectList = courseResponses.stream()
                .map(this::toCourseResponseNoSubject)
                .collect(Collectors.toList());

        return CoursePaginationResponse.<CourseResponseNoSubject>builder()
                .courseList(courseResponseNoSubjectList)
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

    private ManageCourseResponse toManageCourseResponse(Course course){
        return ManageCourseResponse.builder()
                .courseCode(course.getIdCourse())
                .category(course.getCourseCategory().name())
                .courseName(course.getCourseName())
                .courseType(course.getTypePremium().name())
                .level(course.getCourseLevel().name())
                .coursePrice(course.getCoursePrice())
                .build();
    }

    private ManageCoursePaginationResponse toManageCoursePaginationResponse(Page<Course> coursePage) {
        List<Course> courseResponse = coursePage.getContent();

        List<ManageCourseResponse> manageCourseResponses = courseResponse.stream()
                .map(this::toManageCourseResponse)
                .collect(Collectors.toList());

        return ManageCoursePaginationResponse.builder()
                .manageCourseResponse(manageCourseResponses)
                .productCurrentPage(coursePage.getNumber() + 1)
                .productTotalPage(coursePage.getTotalPages())
                .build();
    }


    //ini untuk kebutuhan update data pada tabel subject progress (subject yang diambil oleh user) (Integrasi proses bisnis)
    private List<SubjectProgress> subjectToSubjectProgressList(Subject subject, String courseCode){

        List<CourseProgress> courseProgresses = courseProgressRepository.showCourseProgressByCourseCode(courseCode)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Tidak dapat update ke progress user"));

        return courseProgresses.stream()
                .map(courseProgress -> SubjectProgress.builder()
                        .courseProgress(courseProgress)
                        .subject(subject)
                        .isDone(false)
                        .build())
                .collect(Collectors.toList());
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


