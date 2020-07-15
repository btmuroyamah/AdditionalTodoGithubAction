package com.example.common.validation;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateCheckValidator implements ConstraintValidator<DateCheck, LocalDate> {

    @Override
    public void initialize(DateCheck constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return isDateValid(value);
    }

    //
    static boolean isDateValid(LocalDate date) {

		//TODO 期限が過去のものでないかチェック
		LocalDate now = LocalDate.now();
		if(!date.isBefore(now)) {
			//　期限が現在から1年を超えるものかチェック
			if(date.isBefore(now.plusYears(1))) {
				return true;
			}
		}
        return false;
    }
}