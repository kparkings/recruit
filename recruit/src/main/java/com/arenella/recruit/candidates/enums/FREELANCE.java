package com.arenella.recruit.candidates.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
* Valid options for a Candidates availability 
* to perfom freelance roles
* @author K Parkings
*/
@JsonFormat(shape=JsonFormat.Shape.STRING)
public enum FREELANCE {
	 TRUE
	,FALSE
	,UNKNOWN
}
