package com.arenella.recruit.authentication.controllers;

/**
* Summary of a Users login behaviour
*/
public record UserLoginSummary(
		long loginsThisWeeek,
		long loginsLast30Days,
		long loginsLast60Days,
		long loginsLats90Days) {
}
