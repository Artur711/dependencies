package com.java17.dependencies.annotations.util;

import org.passay.CharacterRule;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Arrays.asList;
import static org.passay.EnglishCharacterData.Digit;
import static org.passay.EnglishCharacterData.LowerCase;
import static org.passay.EnglishCharacterData.Special;
import static org.passay.EnglishCharacterData.UpperCase;
import static org.passay.EnglishSequenceData.Alphabetical;
import static org.passay.EnglishSequenceData.Numerical;
import static org.passay.EnglishSequenceData.USQwerty;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator passwordValidator = new PasswordValidator(asList(
                new LengthRule(8, 30),
                new CharacterRule(UpperCase, 1),
                new CharacterRule(LowerCase, 1),
                new CharacterRule(Digit, 1),
                new CharacterRule(Special, 1),
                new IllegalSequenceRule(Alphabetical, 3, false),
                new IllegalSequenceRule(Numerical, 3, false),
                new IllegalSequenceRule(USQwerty, 3, false),
                new WhitespaceRule()));
        RuleResult result = passwordValidator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                        String.join(",", passwordValidator.getMessages(result)))
                .addConstraintViolation();
        return false;
    }
}
