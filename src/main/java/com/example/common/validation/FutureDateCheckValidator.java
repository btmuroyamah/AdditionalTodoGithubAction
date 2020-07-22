package com.example.common.validation;

import java.time.LocalDate;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.terasoluna.gfw.common.date.ClassicDateFactory;

public class FutureDateCheckValidator implements ConstraintValidator<FutureDateCheck, LocalDate> {

	@Inject
	ClassicDateFactory dateFactory;

	@Override
	public void initialize(FutureDateCheck constraintAnnotation) {
	}

	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		return isDateValid(value);
	}

	private boolean isDateValid(LocalDate date) {

		// 期限が過去のものでないかチェック・期限が現在から1年を超えるものかチェック
		LocalDate now = dateFactory.newSqlDate().toLocalDate();
		if (!date.isBefore(now) && date.isBefore(now.plusYears(1))) {
			return true;
		}
		return false;
	}
}