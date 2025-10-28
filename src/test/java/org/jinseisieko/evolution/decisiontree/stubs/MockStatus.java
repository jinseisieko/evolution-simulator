// src/test/java/org/jinseisieko/evolution/decisiontree/stubs/MockStatus.java
package org.jinseisieko.evolution.decisiontree.stubs;

import java.util.Objects;

import org.jinseisieko.evolution.bindingcomponents.Status;

/**
 * A simple mock implementation of the {@link Status} interface for testing purposes.
 * <p>
 * This class allows tests to create specific status objects with known properties
 * to verify the behavior of other components that depend on or interact with {@code Status}.
 * </p>
 *
 * @author jinseisieko
 */
public class MockStatus implements Status {

    private final String description;

    /**
     * Constructs a new {@code MockStatus} with the specified description.
     *
     * @param description A string describing this status instance. Can be used for assertions.
     */
    public MockStatus(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MockStatus{" + description + "}";
    }
    /**
    * Equals and hashCode might be optionally overridden if needed for specific tests
    * For basic verification, the default Object implementations might suffice.
    * Example optional override:
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MockStatus that = (MockStatus) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}