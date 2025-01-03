package com.eMartix.commons.id;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.PrePersist;

public class NanoIDGenerator {
    @PrePersist
    public String generate() {
        return NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR,
                NanoIdUtils.DEFAULT_ALPHABET,21);
    }
}
