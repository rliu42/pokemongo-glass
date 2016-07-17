package com.fasterxml.jackson.annotation;

import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import spacemadness.com.lunarconsole.C1391R;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@JacksonAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonAutoDetect {

    /* renamed from: com.fasterxml.jackson.annotation.JsonAutoDetect.1 */
    static /* synthetic */ class C01251 {
        static final /* synthetic */ int[] f26x23d16a11;

        static {
            f26x23d16a11 = new int[Visibility.values().length];
            try {
                f26x23d16a11[Visibility.ANY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f26x23d16a11[Visibility.NONE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f26x23d16a11[Visibility.NON_PRIVATE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f26x23d16a11[Visibility.PROTECTED_AND_PUBLIC.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f26x23d16a11[Visibility.PUBLIC_ONLY.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public enum Visibility {
        ANY,
        NON_PRIVATE,
        PROTECTED_AND_PUBLIC,
        PUBLIC_ONLY,
        NONE,
        DEFAULT;

        public boolean isVisible(Member m) {
            switch (C01251.f26x23d16a11[ordinal()]) {
                case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    return true;
                case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                    return false;
                case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                    if (Modifier.isPrivate(m.getModifiers())) {
                        return false;
                    }
                    return true;
                case Place.TYPE_AQUARIUM /*4*/:
                    if (Modifier.isProtected(m.getModifiers())) {
                        return true;
                    }
                    break;
                case Place.TYPE_ART_GALLERY /*5*/:
                    break;
                default:
                    return false;
            }
            return Modifier.isPublic(m.getModifiers());
        }
    }

    Visibility creatorVisibility() default Visibility.DEFAULT;

    Visibility fieldVisibility() default Visibility.DEFAULT;

    Visibility getterVisibility() default Visibility.DEFAULT;

    Visibility isGetterVisibility() default Visibility.DEFAULT;

    Visibility setterVisibility() default Visibility.DEFAULT;
}
