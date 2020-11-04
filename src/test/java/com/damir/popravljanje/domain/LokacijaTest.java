package com.damir.popravljanje.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.damir.popravljanje.web.rest.TestUtil;

public class LokacijaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lokacija.class);
        Lokacija lokacija1 = new Lokacija();
        lokacija1.setId(1L);
        Lokacija lokacija2 = new Lokacija();
        lokacija2.setId(lokacija1.getId());
        assertThat(lokacija1).isEqualTo(lokacija2);
        lokacija2.setId(2L);
        assertThat(lokacija1).isNotEqualTo(lokacija2);
        lokacija1.setId(null);
        assertThat(lokacija1).isNotEqualTo(lokacija2);
    }
}
