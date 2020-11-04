package com.damir.popravljanje.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.damir.popravljanje.web.rest.TestUtil;

public class KantonTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Kanton.class);
        Kanton kanton1 = new Kanton();
        kanton1.setId(1L);
        Kanton kanton2 = new Kanton();
        kanton2.setId(kanton1.getId());
        assertThat(kanton1).isEqualTo(kanton2);
        kanton2.setId(2L);
        assertThat(kanton1).isNotEqualTo(kanton2);
        kanton1.setId(null);
        assertThat(kanton1).isNotEqualTo(kanton2);
    }
}
