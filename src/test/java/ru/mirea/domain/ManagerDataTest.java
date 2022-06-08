package ru.mirea.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ru.mirea.web.rest.TestUtil;

public class ManagerDataTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManagerData.class);
        ManagerData managerData1 = new ManagerData();
        managerData1.setId(1L);
        ManagerData managerData2 = new ManagerData();
        managerData2.setId(managerData1.getId());
        assertThat(managerData1).isEqualTo(managerData2);
        managerData2.setId(2L);
        assertThat(managerData1).isNotEqualTo(managerData2);
        managerData1.setId(null);
        assertThat(managerData1).isNotEqualTo(managerData2);
    }
}
