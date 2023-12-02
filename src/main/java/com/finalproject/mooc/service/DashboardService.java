package com.finalproject.mooc.service;

import com.finalproject.mooc.model.responses.DasboardResponse;
import com.finalproject.mooc.repository.CourseProgressRepository;
import com.finalproject.mooc.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {
    @Autowired
    CourseProgressRepository courseProgressRepository;
    @Autowired
    CourseRepository courseRepository;

    @Transactional
    public void cekhasilQuery(String userAdmin){
        Integer activeUser = courseProgressRepository.countActiveUser(userAdmin);
        Integer activeClass =courseRepository.countActiveClass(userAdmin);
        Integer activePremium =courseRepository.countPremiumClass(userAdmin);
        System.out.println("Active user : "+activeUser+"\nActive Class : "+activeClass+"\nPremium Class : "+activePremium);

        DasboardResponse.builder()
                .activeUser(activeUser)
                .activeClass(activeClass)
                .premiumClass(activePremium)
                .build();
    }
}
