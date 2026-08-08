package io.github.archetom.common.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ErrorContextTest {

    @Test
    void preservesAppendOrderAndDistinguishesRootFromCurrentError() {
        CommonError rootError = new CommonError(new ErrorCode("1", "2", "0001", "001"), "root", "service-a");
        CommonError currentError = new CommonError(new ErrorCode("1", "2", "0002", "002"), "current", "service-b");
        ErrorContext context = new ErrorContext();

        context.addError(rootError);
        context.addError(currentError);

        assertAll(
                () -> assertEquals(2, context.getErrorStack().size()),
                () -> assertSame(rootError, context.getErrorStack().get(0)),
                () -> assertSame(currentError, context.getErrorStack().get(1)),
                () -> assertSame(rootError, context.fetchRootError()),
                () -> assertSame(currentError, context.fetchCurrentError())
        );
    }
}
