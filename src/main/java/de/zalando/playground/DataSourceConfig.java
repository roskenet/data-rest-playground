package de.zalando.playground;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opentable.db.postgres.embedded.EmbeddedPostgreSQL;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() throws IOException {
        return EmbeddedPostgreSQL.start().getPostgresDatabase();
    }
}
