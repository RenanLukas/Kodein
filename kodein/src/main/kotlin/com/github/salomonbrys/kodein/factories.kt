package com.github.salomonbrys.kodein

import java.lang.reflect.Type

/**
 * Base class that knows how to construct an instance.
 *
 * All scopes must return a factory.
 */
public interface Factory<in A, out T : Any> {
    /**
     * Get an instance of type T function argument A.
     *
     * Whether it's a new instance or not entirely depends on implementation (scope).
     */
    public fun getInstance(kodein: Kodein, arg: A): T

    /**
     * The name of the scope this factory represents.
     * For debug only.
     */
    public fun scopeName(): String;

    /**
     * The type of the argument this factory will function for.
     */
    public val argType: Type
}

/**
 * Concrete implementation of factory that delegates `getInstance` to a factory method.
 */
public class CFactory<A, out T : Any>(private val _scopeName: () -> String, private val _provider: Kodein.(A) -> T, override val argType: Type) : Factory<A, T> {
    override fun getInstance(kodein: Kodein, arg: A) = kodein._provider(arg)
    override fun scopeName(): String = _scopeName()
}

/**
 * Concrete implementation of factory that delegates `getInstance` to a provider method.
 * A provider is a factory that takes no argument (Unit as it's argument type).
 */
public class CProvider<out T : Any>(private val _scopeName: () -> String, private val _provider: Kodein.() -> T) : Factory<Unit, T> {
    override fun getInstance(kodein: Kodein, arg: Unit) = kodein._provider()
    override fun scopeName(): String = _scopeName()
    override val argType: Type = Unit.javaClass
}
