package com.example.common.validation;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.joda.time.LocalDate;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;

public class DateCheckValidator implements ConstraintValidator<DateCheck, LocalDate> {
	
	@Inject
	JodaTimeDateFactory dateFactory;

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
    
    private boolean isDateValid(LocalDate date) {

		//TODO 期限が過去のものでないかチェック・期限が現在から1年を超えるものかチェック
		LocalDate now = dateFactory.newDateTime().toLocalDate();
		if(!date.isBefore(now) && date.isBefore(now.plusYears(1))) {
			return true;
		}
        return false;
    }
}