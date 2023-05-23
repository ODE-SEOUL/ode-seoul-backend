package kr.njw.odeseoul.common.config;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class P6SpyConfig {
    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(P6spyPrettySqlFormatter.class.getName());
    }

    public static class P6spyPrettySqlFormatter implements MessageFormattingStrategy {
        public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared,
                                    String sql, String url) {
            sql = this.formatSql(category, sql);
            return category + " completed in " + elapsed + " ms" + sql;
        }

        private String formatSql(String category, String sql) {
            if (sql == null || sql.trim().equals("")) return "";

            if (Category.STATEMENT.getName().equals(category)) {
                String tmpsql = sql.trim().toLowerCase(Locale.ROOT);
                if (tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {
                    sql = FormatStyle.DDL.getFormatter().format(sql);
                } else {
                    sql = FormatStyle.BASIC.getFormatter().format(sql);
                }
            }

            return sql;
        }
    }
}
