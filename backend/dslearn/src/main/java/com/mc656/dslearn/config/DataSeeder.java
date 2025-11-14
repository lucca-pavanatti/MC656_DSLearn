package com.mc656.dslearn.config;

import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.InputStreamReader;
import java.sql.Connection;

@Component
@Slf4j
public class DataSeeder {

    private final DataSource dataSource;

    public DataSeeder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void init() {
        try (Connection connection = dataSource.getConnection()) {
            org.springframework.jdbc.datasource.init.ScriptUtils.executeSqlScript(connection,
                    new org.springframework.core.io.ClassPathResource("data.sql"));

            var resource = new ClassPathResource("csv/leetcode.csv");
            try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
                String[] row;
                boolean header = true;
                var stmt = connection.prepareStatement(
                        "INSERT INTO exercise (title, difficulty, url, companies, related_topics) VALUES (?, ?, ?, ?, ?)"
                );
                while ((row = reader.readNext()) != null) {
                    if (header) { header = false; continue; }

                    String title = row.length > 0 ? row[0] : null;
                    String difficulty = row.length > 1 ? row[1] : null;
                    String url = row.length > 2 ? row[2] : null;
                    String companies = row.length > 3 ? row[3] : null;
                    String related = row.length > 4 ? row[4] : null;

                    stmt.setString(1, title);
                    stmt.setString(2, difficulty);
                    stmt.setString(3, url);
                    stmt.setString(4, companies);
                    stmt.setString(5, related);
                    stmt.addBatch();
                }
                stmt.executeBatch();
                log.info("CSV importado com sucesso!");
            }
        } catch (Exception e) {
            log.error("Erro ao executar data.sql: {}", e);
        }
    }
}
