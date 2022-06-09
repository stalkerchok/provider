package ru.mirea.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ru.mirea.web.rest.TestUtil;

public class TemporaryAccessTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TemporaryAccess.class);
        TemporaryAccess temporaryAccess1 = new TemporaryAccess();
        temporaryAccess1.setId(1L);
        TemporaryAccess temporaryAccess2 = new TemporaryAccess();
        temporaryAccess2.setId(temporaryAccess1.getId());
        assertThat(temporaryAccess1).isEqualTo(temporaryAccess2);
        temporaryAccess2.setId(2L);
        assertThat(temporaryAccess1).isNotEqualTo(temporaryAccess2);
        temporaryAccess1.setId(null);
        assertThat(temporaryAccess1).isNotEqualTo(temporaryAccess2);
    }
}
