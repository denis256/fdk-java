package com.fnproject.fn.api;

import java.lang.annotation.*;

/**
 * Annotation to be used in user function classes to enable runtime-wide feature.
 *
 * Runtime features are initialized at the point that the function class is loaded but prior to the call chain.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(FnFeatures.class)
public @interface FnFeature {
    /**
     * The feature class to load this must have a zero-arg public constructor
     * @return feature class
     */
    Class<? extends  RuntimeFeature> value();
}
