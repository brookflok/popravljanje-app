package com.damir.popravljanje.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.damir.popravljanje.web.rest.TestUtil;

public class UslugaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Usluga.class);
        Usluga usluga1 = new Usluga();
        usluga1.setId(1L);
        Usluga usluga2 = new Usluga();
        usluga2.setId(usluga1.getId());
        assertThat(usluga1).isEqualTo(usluga2);
        usluga2.setId(2L);
        assertThat(usluga1).isNotEqualTo(usluga2);
        usluga1.setId(null);
        assertThat(usluga1).isNotEqualTo(usluga2);
    }
}
