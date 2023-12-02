package com.finalproject.mooc.service;

import com.finalproject.mooc.entity.*;
import com.finalproject.mooc.enums.CourseCategory;
import com.finalproject.mooc.enums.CourseLevel;
import com.finalproject.mooc.enums.TypePremium;
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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseProgressService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    CourseProgressRepository courseProgressRepository;
    @Autowired
    SubjectProgressRepository subjectProgressRepository;

    @Transactional(readOnly = true)
    public ProgressCourseResponse showProgressCourseByUsernameAndCourseCode(String username, String courseCode){
        CourseProgress courseProgress= courseProgressRepository.showCourseProgress(username, courseCode).orElseThrow(()->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "User belum mengambil course ini"));


        return toProgressCourseResponse(courseProgress);
    }

    @Transactional(readOnly = true)
    public CoursePaginationResponse<ProgressCourseNoSubject> showProgressCourseListByUsername(String username,
                                                                                              List<CourseCategory> categories,
                                                                                              List<CourseLevel> courseLevels,
                                                                                              List<TypePremium> typePremiums,
                                                                                              String keyword,
                                                                                              Integer page){
        log.info("CoursePagination bejalan");
        page -= 1; //halaman asli dari index 0
        //sementara size nya 3
        Pageable halaman = PageRequest.of(page, 8);


        Page<CourseProgress> courseProgress= courseProgressRepository.showCourseProgressPagination(username, categories, courseLevels,
                                                                                                    typePremiums, keyword, halaman).
                orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User belum mengambil course ini"));

        if (courseProgress.getContent().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Data tidak ditemukan");


        return toCoursePaginationResponse(courseProgress);
    }


    @Transactional
    public ProgressCourseNoSubject startCourse(String username, String courseCode){

        Course course = courseRepository.findById(courseCode).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course yang dipilih tidak ada"));

        //validasi apakah sudah membayar jika tipe course nya premium
        if(course.getTypePremium().equals(TypePremium.PREMIUM)){
            if(!orderRepository.findIsPremiumPaid(username, courseCode)){
                log.info("Belum membayar kelas, seharusnya melakukan throw dan tidak menyimpan course");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tidak dapat mengikuti kelas, anda belum membayar course ini");
            }
        }

        User user = userRepository.findUserByUsername(username).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anda belum login atau user tidak ditemukan"));

        if(courseProgressRepository.findCourseProgressExist(user, course)){
            log.info("apakah Progress sudah pernah didaftarkan : {}", courseProgressRepository.findCourseProgressExist(user, course));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Sudah didaftarkan sebelumnya");
        }



        CourseProgress courseProgress = courseToCourseProgress(course, user);
        courseProgressRepository.save(courseProgress);
        subjectProgressRepository.saveAll(courseProgress.getSubjectProgresses());

        return toProgressCourseNoSubject(courseProgress);
    }




    //helper konversi
    private CoursePaginationResponse<ProgressCourseNoSubject> toCoursePaginationResponse(Page<CourseProgress> coursePage){
        List<CourseProgress> courseResponses = coursePage.getContent();

        //mapping course nya pakek fungsi konversi ke courseResponse
        //toCourseResponseNoSubject (course, totalModul)
        List<ProgressCourseNoSubject> courseProgressList= courseResponses.stream()
                .map(this::toProgressCourseNoSubject)
                .collect(Collectors.toList());

        return CoursePaginationResponse.<ProgressCourseNoSubject>builder()
                .courseList(courseProgressList)
                .productCurrentPage(coursePage.getNumber() + 1)
                .productTotalPage(coursePage.getTotalPages())
                .build();
    }

    private ProgressCourseResponse toProgressCourseResponse(CourseProgress courseProgress){
        long progressBar = subjectProgressRepository.totalPercentageSubjectIsDone(courseProgress.getId())/subjectProgressRepository.totalSubject(courseProgress.getId());
        Course courseFollowed = courseProgress.getCourse();
        return ProgressCourseResponse.builder()
                .courseCode(courseFollowed.getIdCourse())
                .courseName(courseFollowed.getCourseName())
                .coursePrice(courseFollowed.getCoursePrice())
                .courseLevel(courseFollowed.getCourseLevel())
                .courseAbout(courseFollowed.getCourseAbout())
                .courseFor(courseFollowed.getCourseFor())
                .urlTele(courseFollowed.getUrlTele())
                .teacher(courseFollowed.getUser().getUsername())
                .TypePremium(courseFollowed.getTypePremium())
                .courseCategory(courseFollowed.getCourseCategory())
                .progressSubject(toProgressSubjectResponseList(courseProgress.getSubjectProgresses()))
                .progressBar(progressBar + "%")
                .build();
    }

    private ProgressCourseNoSubject toProgressCourseNoSubject(CourseProgress courseProgress){
        //cek apabila ada arithmetic exception maka progress bar nya 0, biasanya terjadi karena tidak ada subject nya
        long progressBar;
        try {
            progressBar = subjectProgressRepository.totalPercentageSubjectIsDone(courseProgress.getId())/subjectProgressRepository.totalSubject(courseProgress.getId());
        }catch (ArithmeticException a){
            progressBar = 0;
        }

        Course courseFollowed = courseProgress.getCourse();
        return ProgressCourseNoSubject.builder()
                .courseCode(courseFollowed.getIdCourse())
                .courseName(courseFollowed.getCourseName())
                .coursePrice(courseFollowed.getCoursePrice())
                .courseLevel(courseFollowed.getCourseLevel())
                .courseAbout(courseFollowed.getCourseAbout())
                .courseFor(courseFollowed.getCourseFor())
                .urlTele(courseFollowed.getUrlTele())
                .teacher(courseFollowed.getUser().getUsername())
                .TypePremium(courseFollowed.getTypePremium())
                .courseCategory(courseFollowed.getCourseCategory())
                .progressBar(progressBar + "%")
                .build();
    }

    private List<ProgressSubjectResponse> toProgressSubjectResponseList(List<SubjectProgress> subjectProgressList){
        return subjectProgressList.stream()
                .sorted(Comparator.comparingInt(v-> v.getSubject().getSequence()))
                .map(this::toProgressSubjectResponse)
                .collect(Collectors.toList());
    }

    private ProgressSubjectResponse toProgressSubjectResponse(SubjectProgress subjectProgress) {
        Subject subject = subjectProgress.getSubject();
        return ProgressSubjectResponse.builder()
                .subjectCode(subject.getIdSubject())
                .title(subject.getTitle())
                .url(subject.getUrl())
                .chapter(subject.getChapter())
                .sequence(subject.getSequence())
                .TypePremium(subject.getTypePremium())
                .isDone(subjectProgress.getIsDone())
                .build();
    }


    private CourseProgress courseToCourseProgress(Course course, User user){
        CourseProgress courseProgress = CourseProgress.builder()
                .course(course)
                .user(user)
                .build();

        courseProgress.setSubjectProgresses(subjectToSubjectProgress(course.getSubjects(), courseProgress));

        return courseProgress;
    }

    private List<SubjectProgress> subjectToSubjectProgress(List<Subject> subjects, CourseProgress courseProgress){

        return subjects.stream()
                .map(subject -> SubjectProgress.builder()
                        .courseProgress(courseProgress)
                        .subject(subject)
                        .isDone(false)
                        .build())
                .collect(Collectors.toList());
    }

}
