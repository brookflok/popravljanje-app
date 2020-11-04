package com.damir.popravljanje.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.damir.popravljanje.web.rest.TestUtil;

public class SlikaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Slika.class);
        Slika slika1 = new Slika();
        slika1.setId(1L);
        Slika slika2 = new Slika();
        slika2.setId(slika1.getId());
        assertThat(slika1).isEqualTo(slika2);
        slika2.setId(2L);
        assertThat(slika1).isNotEqualTo(slika2);
        slika1.setId(null);
        assertThat(slika1).isNotEqualTo(slika2);
    }
}
