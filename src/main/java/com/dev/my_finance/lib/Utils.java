package com.dev.my_finance.lib;

import java.time.LocalDateTime;

public class Utils {

    public static LocalDateTime addTenWeeks(LocalDateTime startDate) {
        if (startDate == null) {
            startDate = LocalDateTime.now();
        }
        return startDate.plusWeeks(10);
    }

}
