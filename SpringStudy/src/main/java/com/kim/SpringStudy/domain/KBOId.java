package com.kim.SpringStudy.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class KBOId implements Serializable {
    private String teamName;
    private LocalDate recordDate;

    // equals & hashCode 필수!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KBOId)) return false;
        KBOId kboId = (KBOId) o;
        return teamName.equals(kboId.teamName) &&
                recordDate.equals(kboId.recordDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamName, recordDate);
    }
}
