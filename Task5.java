import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationService {

    private static final Logger log =
            LoggerFactory.getLogger(ValidationService.class);

    public ValidationResult validate(Document doc) {

        if (doc == null) {
            return ValidationResult.failure("Document is null");
        }

        try {

            String content = doc.extractContent();

            if (content == null || content.isEmpty()) {
                return ValidationResult.failure("Empty content");
            }

            return runValidationRules(content);

        } catch (Exception e) {

            // Log unexpected system failure only
            log.error("Validation processing failed", e);

            return ValidationResult.failure(
                    "Internal validation error"
            );
        }
    }

    public void validateBatch(List<Document> docs) {

        for (Document doc : docs) {

            try {

                ValidationResult r = validate(doc);

                if (r != null && r.isValid()) {
                    saveResult(r);
                } else {
                    log.warn("Validation failed for document");
                }

            } catch (Exception e) {

                // Never swallow exceptions silently
                log.error("Batch validation failed", e);
            }
        }
    }

    private ValidationResult runValidationRules(String content) {
        return ValidationResult.success();
    }

    private void saveResult(ValidationResult r) {
    }
}