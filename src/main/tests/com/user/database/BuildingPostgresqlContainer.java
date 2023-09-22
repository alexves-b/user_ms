package com.user.database;
import org.testcontainers.containers.PostgreSQLContainer;

public class BuildingPostgresqlContainer extends PostgreSQLContainer<BuildingPostgresqlContainer> {

    private static final String IMAGE_VERSION = "postgres:11.1";
    private static BuildingPostgresqlContainer container;

    private BuildingPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static BuildingPostgresqlContainer getInstance() {
        if (container == null) {
            container = new BuildingPostgresqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
        System.setProperty("DB_DRIVER_CLASS_NAME", container.getDriverClassName());
    }

    @Override
    public void stop(){

    }

}
