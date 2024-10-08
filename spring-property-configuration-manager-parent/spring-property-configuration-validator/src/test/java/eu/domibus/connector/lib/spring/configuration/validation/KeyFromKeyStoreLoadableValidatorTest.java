package eu.domibus.connector.lib.spring.configuration.validation;

import static eu.domibus.connector.lib.spring.configuration.validation.ConstraintViolationSetHelper.printSet;
import static org.assertj.core.api.Assertions.assertThat;

import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreConfigurationProperties;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

public class KeyFromKeyStoreLoadableValidatorTest {
    private static Validator validator;
    private KeyAndKeyStoreConfigurationProperties props;

    @BeforeAll
    public static void beforeClass() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @BeforeEach
    public void setUp() {
        props = new KeyAndKeyStoreConfigurationProperties();
        props.setKeyStore(ConstraintViolationSetHelper.generateTestStore());
        props.setPrivateKey(ConstraintViolationSetHelper.generateTestKeyConfig());
    }

    @Test
    void isValid() {
        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate =
            validator.validate(props);
        printSet(validate);
        assertThat(validate).isEmpty();
    }

    @Test
    void isValid_wrongStorePath_shouldNotValidate() {
        props.getKeyStore().setPath(new ClassPathResource("/does/not/exist"));

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate =
            validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(3);
    }

    @Test
    void isValid_wrongKeyAlias_shouldNotValidate() {
        props.getPrivateKey().setAlias("WRONG_ALIAS");

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate =
            validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(1);
    }

    @Test
    void isValid_wrongKeyPassword_shouldNotValidate() {
        props.getPrivateKey().setPassword("WRONG_PASSWORD");

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate =
            validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(1);
    }

    @Test
    void isValid_keyInformationIsNull() {
        props.setPrivateKey(null);

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate =
            validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(2);
    }

    @Test
    void isValid_keyStoreInformationIsNull() {
        props.setKeyStore(null);

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate =
            validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(2);
    }
}
