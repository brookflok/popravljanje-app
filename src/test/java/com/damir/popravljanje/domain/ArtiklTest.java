package com.damir.popravljanje.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.damir.popravljanje.web.rest.TestUtil;

public class ArtiklTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Artikl.class);
        Artikl artikl1 = new Artikl();
        artikl1.setId(1L);
        Artikl artikl2 = new Artikl();
        artikl2.setId(artikl1.getId());
        assertThat(artikl1).isEqualTo(artikl2);
        artikl2.setId(2L);
        assertThat(artikl1).isNotEqualTo(artikl2);
        artikl1.setId(null);
        assertThat(artikl1).isNotEqualTo(artikl2);
    }
}
