package eu.domibus.connector.lib.spring.configuration.validation;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FolderWriteableValidatorTest {
    private static Validator validator;

    @BeforeAll
    public static void beforeClass() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testDirectoryExists() {
        FilePathTestClass pathTestClass =
            new FilePathTestClass(Paths.get("./" + UUID.randomUUID()));
        Set<ConstraintViolation<FilePathTestClass>> validate = validator.validate(pathTestClass);

        assertThat(validate).hasSize(2);
    }

    @Data
    private static class FilePathTestClass {
        @CheckFolderWriteable
        private Path filePath;

        public FilePathTestClass(Path f) {
            this.filePath = f;
        }
    }
}
