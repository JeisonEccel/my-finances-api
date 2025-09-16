package com.jeisoneccel.my_finances.utils.date_time;

import com.jeisoneccel.my_finances.exceptions.ErrorCode;

import java.time.temporal.Temporal;
import java.util.function.Function;

public record DateTimeParse(Function<String, Temporal> parseMethod, ErrorCode parseErrorCode) {

}
