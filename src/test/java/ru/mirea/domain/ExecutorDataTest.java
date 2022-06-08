package ru.mirea.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ru.mirea.web.rest.TestUtil;

public class ExecutorDataTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExecutorData.class);
        ExecutorData executorData1 = new ExecutorData();
        executorData1.setId(1L);
        ExecutorData executorData2 = new ExecutorData();
        executorData2.setId(executorData1.getId());
        assertThat(executorData1).isEqualTo(executorData2);
        executorData2.setId(2L);
        assertThat(executorData1).isNotEqualTo(executorData2);
        executorData1.setId(null);
        assertThat(executorData1).isNotEqualTo(executorData2);
    }
}
