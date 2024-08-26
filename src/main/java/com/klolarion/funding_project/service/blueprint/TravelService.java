package com.klolarion.funding_project.service.blueprint;

import com.klolarion.funding_project.domain.entity.Travel;
import com.klolarion.funding_project.dto.travel.TravelDto;

import java.util.List;

public interface TravelService {

    List<Travel> allTravels();
    List<Travel> myTravels();
    Travel getTravel(Long travelId);

    Travel createTravel(TravelDto travelDto);
    boolean modifyTravel(TravelDto travelDto);
    boolean deleteTravel(Long travelId);
}
