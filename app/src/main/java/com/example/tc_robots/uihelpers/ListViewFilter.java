package com.example.tc_robots.uihelpers;

import com.example.tc_robots.backend.Alert;
import com.example.tc_robots.backend.ErrorType;

import java.util.ArrayList;
import java.util.List;

public class ListViewFilter {

    public List<Alert> filterListByErrorType(List<Alert> listToFilter, ErrorType errorType) {
        List<Alert> resultList = new ArrayList<>();
        for (Alert alert : listToFilter) {
            if(alert.getErrorType().equals(errorType)) {
                resultList.add(alert);
            }
        }
        return resultList;
    }

}
