package com.fnproject.fn.runtime;

import com.fnproject.fn.runtime.testfns.TestFnConstructors;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end tests for function configuration methods
 */
public class FunctionConstructionTests {

    @Rule
    public final FnTestHarness fn = new FnTestHarness();

    @Test
    public void shouldConstructWithDefaultConstructor() {
        fn.givenDefaultEvent().enqueue();
        fn.thenRun(TestFnConstructors.DefaultEmptyConstructor.class, "invoke");
        assertThat(fn.exitStatus()).isEqualTo(0);
        assertThat(fn.getStdOutAsString()).isEqualTo("OK");
    }

    @Test
    public void shouldConstructWithExplicitConstructor() {
        fn.givenDefaultEvent().enqueue();
        fn.thenRun(TestFnConstructors.ExplicitEmptyConstructor.class, "invoke");
        assertThat(fn.exitStatus()).isEqualTo(0);
        assertThat(fn.getStdOutAsString()).isEqualTo("OK");
    }

    @Test
    public void shouldInjectConfigIntoConstructor() {
        fn.givenDefaultEvent().enqueue();
        fn.thenRun(TestFnConstructors.ConfigurationOnConstructor.class, "invoke");
        assertThat(fn.exitStatus()).isEqualTo(0);
        assertThat(fn.getStdOutAsString()).isEqualTo("OK");
    }

    @Test
    public void shouldFailWithInaccessibleConstructor() {
        fn.givenDefaultEvent().enqueue();
        fn.thenRun(TestFnConstructors.BadConstructorNotAccessible.class, "invoke");
        assertThat(fn.exitStatus()).isEqualTo(2);
        assertThat(fn.getStdErrAsString()).contains("cannot be instantiated as it has no public constructors");
    }

    @Test
    public void shouldFailFunctionWithTooManyConstructorArgs() {
        fn.givenDefaultEvent().enqueue();
        fn.thenRun(TestFnConstructors.BadConstructorTooManyArgs.class, "invoke");
        assertThat(fn.exitStatus()).isEqualTo(2);
        assertThat(fn.getStdErrAsString()).contains("cannot be instantiated as its constructor takes more than one argument");
    }

    @Test
    public void shouldFailFunctionWithAmbiguousConstructors() {
        fn.givenDefaultEvent().enqueue();
        fn.thenRun(TestFnConstructors.BadConstructorAmbiguousConstructors.class, "invoke");
        assertThat(fn.exitStatus()).isEqualTo(2);
        assertThat(fn.getStdErrAsString()).contains("cannot be instantiated as it has multiple public constructors");
    }

    @Test
    public void shouldFailFunctionWithErrorInConstructor() {
        fn.givenDefaultEvent().enqueue();
        fn.thenRun(TestFnConstructors.BadConstructorThrowsException.class, "invoke");
        assertThat(fn.exitStatus()).isEqualTo(2);
        assertThat(fn.getStdErrAsString()).contains("An error occurred in the function constructor while instantiating class");
    }

    @Test
    public void shouldFailFunctionWithBadSingleConstructConstructorArg() {
        fn.givenDefaultEvent().enqueue();
        fn.thenRun(TestFnConstructors.BadConstructorUnrecognisedArg.class, "invoke");
        assertThat(fn.exitStatus()).isEqualTo(2);
        assertThat(fn.getStdErrAsString()).contains("cannot be instantiated as its constructor takes an unrecognized argument of type int");
    }


    @Test
    public void shouldFailNonStaticInnerClassWithANiceMessage(){
        fn.givenDefaultEvent().enqueue();
        fn.thenRun(TestFnConstructors.NonStaticInnerClass.class, "invoke");
        assertThat(fn.exitStatus()).isEqualTo(2);
        assertThat(fn.getStdErrAsString()).contains("cannot be instantiated as it is a non-static inner class");
    }
}
