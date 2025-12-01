package com.smalldogg.whereiam.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class RoutingSource extends AbstractRoutingDataSource {

    @Override
    protected @Nullable Object determineCurrentLookupKey() {
        return DataSourceType.MASTER.getName();
    }
}
