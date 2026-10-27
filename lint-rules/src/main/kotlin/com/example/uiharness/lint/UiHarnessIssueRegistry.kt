package com.example.uiharness.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class UiHarnessIssueRegistry : IssueRegistry() {

    override val issues: List<Issue>
        get() = listOf(HardcodedDesignValueDetector.ISSUE)

    override val api: Int
        get() = CURRENT_API
}
