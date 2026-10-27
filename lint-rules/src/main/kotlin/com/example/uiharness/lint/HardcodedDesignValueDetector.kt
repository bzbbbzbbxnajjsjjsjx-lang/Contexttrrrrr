package com.example.uiharness.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULiteralExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression

// ui/theme dışında sabit renk (Color(...)) ve sabit dp (16.dp) kullanımını yakalar.
// Tema dosyaları bu kuralın dışındadır, token'lar orada tanımlanır.
class HardcodedDesignValueDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> =
        listOf(UCallExpression::class.java, USimpleNameReferenceExpression::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        if (context.file.path.replace('\\', '/').contains("/ui/theme/")) return null

        return object : UElementHandler() {
            override fun visitCallExpression(node: UCallExpression) {
                if (node.methodName == "Color" && node.valueArgumentCount > 0) {
                    context.report(
                        ISSUE,
                        node,
                        context.getLocation(node),
                        "Tema dışında sabit Color. MaterialTheme.colorScheme kullan.",
                    )
                }
            }

            override fun visitSimpleNameReferenceExpression(node: USimpleNameReferenceExpression) {
                // 16.dp gibi bir ifadede "dp" adı, üst ifadenin seçicisi ve alıcısı sayısal bir literaldir.
                if (node.identifier != "dp") return
                val parent = node.uastParent as? UQualifiedReferenceExpression ?: return
                if (parent.selector == node && parent.receiver is ULiteralExpression) {
                    context.report(
                        ISSUE,
                        parent,
                        context.getLocation(parent),
                        "Tema dışında sabit dp. Spacing veya Radius kullan.",
                    )
                }
            }
        }
    }

    companion object {
        val ISSUE: Issue = Issue.create(
            id = "HardcodedDesignValue",
            briefDescription = "Tema dışında sabit renk veya dp",
            explanation = "Renk yalnızca MaterialTheme.colorScheme, boşluk Spacing, köşe Radius ile verilir. " +
                "ui/theme dışında Color(...) veya sayısal .dp kullanılmaz.",
            category = Category.CORRECTNESS,
            priority = 8,
            severity = Severity.ERROR,
            implementation = Implementation(
                HardcodedDesignValueDetector::class.java,
                Scope.JAVA_FILE_SCOPE,
            ),
        )
    }
}
