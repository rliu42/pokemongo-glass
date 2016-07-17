package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import java.io.Serializable;

public abstract class PropertyNamingStrategy implements Serializable {
    public static final PropertyNamingStrategy CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES;
    public static final PropertyNamingStrategy LOWER_CASE;
    public static final PropertyNamingStrategy PASCAL_CASE_TO_CAMEL_CASE;

    public static abstract class PropertyNamingStrategyBase extends PropertyNamingStrategy {
        public abstract String translate(String str);

        public String nameForField(MapperConfig<?> mapperConfig, AnnotatedField field, String defaultName) {
            return translate(defaultName);
        }

        public String nameForGetterMethod(MapperConfig<?> mapperConfig, AnnotatedMethod method, String defaultName) {
            return translate(defaultName);
        }

        public String nameForSetterMethod(MapperConfig<?> mapperConfig, AnnotatedMethod method, String defaultName) {
            return translate(defaultName);
        }

        public String nameForConstructorParameter(MapperConfig<?> mapperConfig, AnnotatedParameter ctorParam, String defaultName) {
            return translate(defaultName);
        }
    }

    public static class LowerCaseStrategy extends PropertyNamingStrategyBase {
        public String translate(String input) {
            return input.toLowerCase();
        }
    }

    public static class LowerCaseWithUnderscoresStrategy extends PropertyNamingStrategyBase {
        public String translate(String input) {
            if (input == null) {
                return input;
            }
            int length = input.length();
            StringBuilder result = new StringBuilder(length * 2);
            int resultLength = 0;
            boolean wasPrevTranslated = false;
            for (int i = 0; i < length; i++) {
                char c = input.charAt(i);
                if (i > 0 || c != '_') {
                    if (Character.isUpperCase(c)) {
                        if (!(wasPrevTranslated || resultLength <= 0 || result.charAt(resultLength - 1) == '_')) {
                            result.append('_');
                            resultLength++;
                        }
                        c = Character.toLowerCase(c);
                        wasPrevTranslated = true;
                    } else {
                        wasPrevTranslated = false;
                    }
                    result.append(c);
                    resultLength++;
                }
            }
            return resultLength > 0 ? result.toString() : input;
        }
    }

    public static class PascalCaseStrategy extends PropertyNamingStrategyBase {
        public String translate(String input) {
            if (input == null || input.length() == 0) {
                return input;
            }
            char c = input.charAt(0);
            char uc = Character.toUpperCase(c);
            if (c == uc) {
                return input;
            }
            StringBuilder sb = new StringBuilder(input);
            sb.setCharAt(0, uc);
            return sb.toString();
        }
    }

    static {
        CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES = new LowerCaseWithUnderscoresStrategy();
        PASCAL_CASE_TO_CAMEL_CASE = new PascalCaseStrategy();
        LOWER_CASE = new LowerCaseStrategy();
    }

    public String nameForField(MapperConfig<?> mapperConfig, AnnotatedField field, String defaultName) {
        return defaultName;
    }

    public String nameForGetterMethod(MapperConfig<?> mapperConfig, AnnotatedMethod method, String defaultName) {
        return defaultName;
    }

    public String nameForSetterMethod(MapperConfig<?> mapperConfig, AnnotatedMethod method, String defaultName) {
        return defaultName;
    }

    public String nameForConstructorParameter(MapperConfig<?> mapperConfig, AnnotatedParameter ctorParam, String defaultName) {
        return defaultName;
    }
}
