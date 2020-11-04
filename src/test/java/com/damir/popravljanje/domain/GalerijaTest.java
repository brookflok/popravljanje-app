package com.damir.popravljanje.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.damir.popravljanje.web.rest.TestUtil;

public class GalerijaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Galerija.class);
        Galerija galerija1 = new Galerija();
        galerija1.setId(1L);
        Galerija galerija2 = new Galerija();
        galerija2.setId(galerija1.getId());
        assertThat(galerija1).isEqualTo(galerija2);
        galerija2.setId(2L);
        assertThat(galerija1).isNotEqualTo(galerija2);
        galerija1.setId(null);
        assertThat(galerija1).isNotEqualTo(galerija2);
    }
}
