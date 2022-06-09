package ru.mirea.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Properties specific to Provider.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    List<String> managerDataAccessRw;
    List<String> managerDataAccessRo;
    List<String> executorDataAccessRw;
    List<String> executorDataAccessRo;

    public List<String> getManagerDataAccessRw() {
        return managerDataAccessRw;
    }

    public void setManagerDataAccessRw(List<String> managerDataAccessRw) {
        this.managerDataAccessRw = managerDataAccessRw;
    }

    public List<String> getManagerDataAccessRo() {
        return managerDataAccessRo;
    }

    public void setManagerDataAccessRo(List<String> managerDataAccessRo) {
        this.managerDataAccessRo = managerDataAccessRo;
    }

    public List<String> getExecutorDataAccessRw() {
        return executorDataAccessRw;
    }

    public void setExecutorDataAccessRw(List<String> executorDataAccessRw) {
        this.executorDataAccessRw = executorDataAccessRw;
    }

    public List<String> getExecutorDataAccessRo() {
        return executorDataAccessRo;
    }

    public void setExecutorDataAccessRo(List<String> executorDataAccessRo) {
        this.executorDataAccessRo = executorDataAccessRo;
    }
}
