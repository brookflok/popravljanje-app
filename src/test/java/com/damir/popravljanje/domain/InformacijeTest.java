package com.damir.popravljanje.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.damir.popravljanje.web.rest.TestUtil;

public class InformacijeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Informacije.class);
        Informacije informacije1 = new Informacije();
        informacije1.setId(1L);
        Informacije informacije2 = new Informacije();
        informacije2.setId(informacije1.getId());
        assertThat(informacije1).isEqualTo(informacije2);
        informacije2.setId(2L);
        assertThat(informacije1).isNotEqualTo(informacije2);
        informacije1.setId(null);
        assertThat(informacije1).isNotEqualTo(informacije2);
    }
}
