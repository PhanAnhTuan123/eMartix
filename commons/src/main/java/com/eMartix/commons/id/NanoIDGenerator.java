package com.eMartix.commons.id;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.PrePersist;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.security.SecureRandom;

public class NanoIDGenerator implements IdentifierGenerator {
    
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final int ID_LENGTH = 16; // Adjust length as needed

    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object) {
        return NanoIdUtils.randomNanoId(RANDOM, ALPHABET, ID_LENGTH);
    }

}
