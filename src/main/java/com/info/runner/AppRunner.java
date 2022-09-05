package com.info.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.info.entity.EligibilityDetails;

import com.info.repo.IEligibilityDetailsRepo;
@Component
public class AppRunner implements ApplicationRunner {

	@Autowired
	 private IEligibilityDetailsRepo repo;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		EligibilityDetails entity1 = new EligibilityDetails();
		entity1.setEligId(1);
		entity1.setName("Aman");
		entity1.setPlanStatus("Approved");
		entity1.setEmail("aman@gmail.com");
		entity1.setMobile(9870852851l);
		entity1.setGender('M');
		entity1.setSsn(68686868l);
		repo.save(entity1);
		
		EligibilityDetails entity2 = new EligibilityDetails();
		entity2.setEligId(2);
		entity2.setName("Kavita");
		entity2.setPlanStatus("Denied");
		entity2.setEmail("kavita@gmail.com");
		entity2.setMobile(9870413585l);
		entity2.setGender('M');
		entity2.setSsn(68689868l);
		repo.save(entity2);
		
		EligibilityDetails entity3 = new EligibilityDetails();
		entity3.setEligId(3);
		entity3.setName("Sunita");
		entity3.setPlanStatus("ABC");
		entity3.setEmail("sunita@gmail.com");
		entity3.setMobile(9870899285l);
		entity3.setGender('M');
		entity3.setSsn(68686983l);
		repo.save(entity3);
	}

}
