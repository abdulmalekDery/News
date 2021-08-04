package com.beinmedia.test.utils

fun <T> androidLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)
