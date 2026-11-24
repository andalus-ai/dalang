package tech.kayys.dalang.timeline;

import java.util.List;

public record ValidationResult(List<ValidationIssue> issues) {
    public ValidationResult {
        issues = List.copyOf(issues);
    }

    public boolean isValid() {
        return issues.stream().noneMatch(issue -> issue.severity() == ValidationSeverity.ERROR);
    }

    public List<ValidationIssue> errors() {
        return issues.stream().filter(issue -> issue.severity() == ValidationSeverity.ERROR).toList();
    }
}
