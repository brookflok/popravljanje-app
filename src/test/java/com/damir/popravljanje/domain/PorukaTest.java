package com.damir.popravljanje.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.damir.popravljanje.web.rest.TestUtil;

public class PorukaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Poruka.class);
        Poruka poruka1 = new Poruka();
        poruka1.setId(1L);
        Poruka poruka2 = new Poruka();
        poruka2.setId(poruka1.getId());
        assertThat(poruka1).isEqualTo(poruka2);
        poruka2.setId(2L);
        assertThat(poruka1).isNotEqualTo(poruka2);
        poruka1.setId(null);
        assertThat(poruka1).isNotEqualTo(poruka2);
    }
}
