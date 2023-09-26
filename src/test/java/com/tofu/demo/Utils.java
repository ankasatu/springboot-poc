package com.tofu.demo;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.UUID;

public class Utils {

    private Utils() {}

    public static TypeSafeMatcher<String> matchUuidFormat = new TypeSafeMatcher<>() {
        @Override
        protected boolean matchesSafely(String s) {
            try {
                UUID.fromString(s);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a valid UUID");
        }
    };
}
