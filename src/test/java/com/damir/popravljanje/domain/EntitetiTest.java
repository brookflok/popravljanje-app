package com.damir.popravljanje.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.damir.popravljanje.web.rest.TestUtil;

public class EntitetiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entiteti.class);
        Entiteti entiteti1 = new Entiteti();
        entiteti1.setId(1L);
        Entiteti entiteti2 = new Entiteti();
        entiteti2.setId(entiteti1.getId());
        assertThat(entiteti1).isEqualTo(entiteti2);
        entiteti2.setId(2L);
        assertThat(entiteti1).isNotEqualTo(entiteti2);
        entiteti1.setId(null);
        assertThat(entiteti1).isNotEqualTo(entiteti2);
    }
}
