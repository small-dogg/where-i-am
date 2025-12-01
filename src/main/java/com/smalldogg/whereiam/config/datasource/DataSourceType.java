package com.smalldogg.whereiam.config.datasource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DataSourceType {
    MASTER("master"),
    SLAVE("slave");

    private final String name;
}
