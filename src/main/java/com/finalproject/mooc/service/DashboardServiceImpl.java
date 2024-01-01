package com.finalproject.mooc.service;

import com.finalproject.mooc.model.responses.DasboardResponse;
import com.finalproject.mooc.repository.CourseProgressRepository;
import com.finalproject.mooc.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    CourseProgressRepository courseProgressRepository;

    @Autowired
    CourseRepository courseRepository;

    @Transactional
    public DasboardResponse cekhasilQuery(String userAdmin){
        Integer activeUser = courseProgressRepository.countActiveUser(userAdmin);
        Integer activeClass =courseRepository.countActiveClass(userAdmin);
        Integer activePremium =courseRepository.countPremiumClass(userAdmin);

        return DasboardResponse.builder()
                .activeUser(activeUser)
                .activeClass(activeClass)
                .premiumClass(activePremium)
                .build();
    }
}
