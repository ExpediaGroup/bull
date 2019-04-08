package com.hotels.beans.sample.builder;

import com.hotels.beans.sample.FromFooWithBuilder;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Test;

import static org.testng.AssertJUnit.assertSame;

public class BuilderTest {


    /**
     * Test that we can get the Builder from a class (with a builder) created manually
     */

    @Test
    public void checkDeclaredClassInManualBuilder(){
        // WHEN
        Class<?>[] declaredClasses = ToFoo.class.getDeclaredClasses();
        Class clazz  = declaredClasses[0];

        // THEN
        assertSame(clazz,ToFoo.Builder.class);
    }


    /**
     * Test that we can get the Builder from a class created with Lombok
     * Expected name for the Builder created from Lombok  is "OriginalClassName" + "Builder" without any character in between
     * see https://projectlombok.org/features/Builder
     */

    @Test
    public void checkDeclaredClassInBuilderFromLombok(){
        // WHEN
        Class<?>[] declaredClasses = FromFooWithBuilder.class.getDeclaredClasses();
        Class clazz  = declaredClasses[0];

        // THEN
        assertSame(clazz,FromFooWithBuilder.FromFooWithBuilderBuilder.class);
    }


    /*
        NONJAVADOC!!! TODO : check if useful!!!
        Here some chat about builder constructed WITHOUT STATIC CLASS
        https://softwareengineering.stackexchange.com/questions/225207/why-should-a-builder-be-an-inner-class-instead-of-in-its-own-class-file


        Anyway buinder constructed with lombok use static class
        https://projectlombok.org/features/Builder
     */
    @Test
    public void checkInnerClass(){
        throw new  NotImplementedException("Check builder with Inner (non static) class");
    }

    }
