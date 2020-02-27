package com.ang.acb.baking.util

import org.mockito.ArgumentCaptor
import org.mockito.Mockito

/**
 * A Kotlin friendly mock that handles generics.
 *
 * See: https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample
 */
inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

inline fun <reified T> argumentCaptor(): ArgumentCaptor<T> = ArgumentCaptor.forClass(T::class.java)