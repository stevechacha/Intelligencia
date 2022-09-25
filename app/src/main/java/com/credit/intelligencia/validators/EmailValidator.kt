package com.credit.intelligencia.validators

import android.text.TextUtils
import com.credit.intelligencia.validators.base.BaseValidator
import com.credit.intelligencia.validators.base.ValidateResult
import com.credit.intelligencia.R

class EmailValidator(val email: String) : BaseValidator() {
    override fun validate(): ValidateResult {
        val isValid =
            !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        return ValidateResult(
            isValid,
            if (isValid) R.string.text_validation_success else R.string.text_validation_error_email
        )
    }
}