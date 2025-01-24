package com.eMartix.commons.id;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@AttributeOverride(name = "GenerratedID", column = @Column(name = "id"))
//@IdGeneratorType(NanoIDGenerator.class)
public @interface GeneratedID {
}
