package com.nborba.vocalize.core.permission.model

/**
 * Value object holding title and description text for permission prompts.
 *
 * @property title The header text displayed in rationale or settings prompts.
 * @property description The explanatory body text displayed in rationale or settings prompts.
 */
data class PermissionPromptContent(
    val title: String,
    val description: String,
)
