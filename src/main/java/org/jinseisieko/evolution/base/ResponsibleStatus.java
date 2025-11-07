// src/main/java/org/jinseisieko/evolution/base/ResponsibleStatus.java
package org.jinseisieko.evolution.base;

import org.jinseisieko.evolution.bindingcomponents.Status;

public interface ResponsibleStatus extends Status {
    void applyThisStatus(Agent agent);
}