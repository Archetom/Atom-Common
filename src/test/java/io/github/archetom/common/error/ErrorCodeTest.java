package io.github.archetom.common.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ErrorCodeTest {

    @Test
    void assemblesParsesAndRendersDefaultVersionCodeWithoutLoss() {
        ErrorCode assembled = new ErrorCode("1", "2", "3456", "789");
        String errorCode = assembled.toString();
        ErrorCode parsed = new ErrorCode(errorCode, "0");

        assertAll(
                () -> assertEquals("DE0123456789", errorCode),
                () -> assertEquals(errorCode, parsed.toString()),
                () -> assertEquals("DE", parsed.getErrorPrefix()),
                () -> assertEquals("0", parsed.getVersion()),
                () -> assertEquals("1", parsed.getErrorLevel()),
                () -> assertEquals("2", parsed.getErrorType()),
                () -> assertEquals("3456", parsed.getErrorScene()),
                () -> assertEquals("789", parsed.getErrorSpecific())
        );
    }

    @Test
    void defaultVersionFormatUsesTheDocumentedFieldWidths() {
        String errorCode = new ErrorCode("1", "2", "3456", "789").toString();

        assertAll(
                () -> assertEquals(12, errorCode.length()),
                () -> assertEquals("DE", errorCode.substring(0, 2)),
                () -> assertEquals(1, errorCode.substring(2, 3).length()),
                () -> assertEquals(1, errorCode.substring(3, 4).length()),
                () -> assertEquals(1, errorCode.substring(4, 5).length()),
                () -> assertEquals(4, errorCode.substring(5, 9).length()),
                () -> assertEquals(3, errorCode.substring(9, 12).length())
        );
    }

    @Test
    void rejectsInvalidLengthInsteadOfReplacingTheCodeWithAReservedCode() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ErrorCode("DE012345678")
        );

        assertTrue(exception.getMessage().contains("DE012345678"));
    }

    @Test
    void rejectsUnsupportedConstructorAndEncodedVersionsExplicitly() {
        IllegalArgumentException constructorVersionException = assertThrows(
                IllegalArgumentException.class,
                () -> new ErrorCode("DE0123456789", "1")
        );
        IllegalArgumentException encodedVersionException = assertThrows(
                IllegalArgumentException.class,
                () -> new ErrorCode("DE1123456789")
        );

        assertAll(
                () -> assertTrue(constructorVersionException.getMessage().contains("1")),
                () -> assertTrue(encodedVersionException.getMessage().contains("1"))
        );
    }
}
