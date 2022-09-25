package com.credit.intelligencia.validators


import com.credit.intelligencia.validators.base.BaseValidator
import com.credit.intelligencia.validators.base.ValidateResult
import com.credit.intelligencia.R



class PasswordValidator(val password: String) : BaseValidator() {
    private val minPasswordLength = 5
    private val maxPasswordLength = 10

    override fun validate(): ValidateResult {
        if (password.length < minPasswordLength)
            return ValidateResult(false, R.string.text_validation_error_min_pass_length)
        if (password.length > maxPasswordLength)
            return ValidateResult(false, R.string.text_validation_error_max_pass_length)
        return ValidateResult(true, R.string.text_validation_success)
    }
}