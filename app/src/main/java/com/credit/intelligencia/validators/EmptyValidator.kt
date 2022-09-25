package com.credit.intelligencia.validators


import com.credit.intelligencia.validators.base.BaseValidator
import com.credit.intelligencia.validators.base.ValidateResult
import com.credit.intelligencia.R



class EmptyValidator(val input: String) : BaseValidator() {
    override fun validate(): ValidateResult {
        val isValid = input.isNotEmpty()
        return ValidateResult(
            isValid,
            if (isValid) R.string.text_validation_success else R.string.text_validation_error_empty_field
        )
    }
}