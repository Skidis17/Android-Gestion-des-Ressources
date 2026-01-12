import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DbDebug {

    private final DataSource dataSource;

    public DbDebug(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void showDbInfo() throws Exception {
        var conn = dataSource.getConnection();
        System.out.println(">>> DB URL utilisée = " + conn.getMetaData().getURL());
        System.out.println(">>> DB USER = " + conn.getMetaData().getUserName());
        conn.close();
    }
}
