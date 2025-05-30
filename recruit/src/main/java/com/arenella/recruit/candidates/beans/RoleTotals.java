package com.arenella.recruit.candidates.beans;

import com.arenella.recruit.candidates.enums.COUNTRY;

public record RoleTotals(COUNTRY id, Long available, Long unavailable) {

}
