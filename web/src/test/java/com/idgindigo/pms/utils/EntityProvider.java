package com.idgindigo.pms.utils;

import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.repository.BaseRepository;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * @author vomel
 * @since 29.10.13 15:02
 */
public abstract class EntityProvider<T extends BaseIdentifiable> {
    private static final Random RANDOM = new SecureRandom("OLOLOSEN'KI".getBytes());
    public static final int ALPHABETIC_DEFAULT_LENGTH = 10;

    public abstract T createAndFill();

    public static long randomPositiveLong() {
        return Math.abs(randomInt());
    }

    public static int randomPositiveInt() {
        return Math.abs(randomInt());
    }

    public abstract BaseRepository<T> getRepository();

    public T getTransientEntity(Visitor<T>... visitor) {
        T entity = createAndFill();
        for (Visitor<T> tVisitor : visitor) {
            tVisitor.visit(entity);
        }
        return entity;
    }

    public T getByIndex(int index, Visitor<T>... visitors) {
        List<T> existing = getRepository().findAll();
        return existing.size() <= index ? getPersistentEntity(visitors) : existing.get(index);
    }

    public T getFirst(Visitor<T>... visitors) {
        return getByIndex(0);
    }

    public T getPersistentEntity(Visitor<T>... visitor) {
        return getRepository().saveAndFlush(getTransientEntity(visitor));
    }

    public void updateEntity(T entity) {

    }

    public static String randomString() {
        return RandomStringUtils.random(10, true, true);
    }

    public static String randomAlphabeticString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static String randomAlphabeticString() {
        return RandomStringUtils.randomAlphabetic(ALPHABETIC_DEFAULT_LENGTH);
    }

    protected float randomFloat() {
        return RANDOM.nextFloat();
    }

    public static long randomLong() {
        return RANDOM.nextLong();
    }

    public static int randomInt() {
        return RANDOM.nextInt();
    }

}
