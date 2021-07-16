package com.skaggsm.compose

import arrow.optics.optics

@optics
data class CounterState(val counter1: Int = 0, val counter2: Int = 1) {
    companion object
}

//val aaaa = CounterState.counter1
