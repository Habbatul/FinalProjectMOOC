package com.finalproject.mooc.Service;

//import com.finalproject.mooc.enums.CourseCategory;
//import com.finalproject.mooc.model.responses.CoursePaginationResponse;
//import com.finalproject.mooc.service.CourseService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@SpringBootTest
//public class CourseServiceImplTest {
//
//    @Autowired
//    private CourseService courseService;
//
//    @Test
//    public void testShowCourseByCategory() {
//        Integer page = 1;
//        List<CourseCategory> categories = Arrays.asList(CourseCategory.WEB_DEVELOPMENT, CourseCategory.PRODUCT_MANAGEMENT);
//        String username = "testUser";
//
//        CoursePaginationResponse result = courseService.showCourseByCategory(page, categories, username);
//
//        assertEquals(1, result.getProductCurrentPage());
//         assertEquals(4, result.getCourseResponseWithSubject().size());
//    }
//
//}
