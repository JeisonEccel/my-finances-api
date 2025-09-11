package com.jeisoneccel.my_finances.core.entities;

import java.time.ZonedDateTime;

public interface BasicEntity {

    String getId();

    ZonedDateTime getCreatedDate();

}