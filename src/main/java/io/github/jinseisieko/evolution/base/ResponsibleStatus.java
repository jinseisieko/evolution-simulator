// src/main/java/io.github/jinseisieko/evolution/base/ResponsibleStatus.java
package io.github.jinseisieko.evolution.base;

import io.github.jinseisieko.evolution.bindingcomponents.Status;

public interface ResponsibleStatus extends Status {
    void applyThisStatus(Agent agent, double dt);
}