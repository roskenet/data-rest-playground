package de.zalando.playground;

import java.io.IOException;

import javax.persistence.Embedded;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;


@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() throws IOException {
        return EmbeddedPostgres.start().getPostgresDatabase();
    }
}
