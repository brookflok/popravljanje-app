package com.damir.popravljanje.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.damir.popravljanje.web.rest.TestUtil;

public class ProfilnaSlikaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfilnaSlika.class);
        ProfilnaSlika profilnaSlika1 = new ProfilnaSlika();
        profilnaSlika1.setId(1L);
        ProfilnaSlika profilnaSlika2 = new ProfilnaSlika();
        profilnaSlika2.setId(profilnaSlika1.getId());
        assertThat(profilnaSlika1).isEqualTo(profilnaSlika2);
        profilnaSlika2.setId(2L);
        assertThat(profilnaSlika1).isNotEqualTo(profilnaSlika2);
        profilnaSlika1.setId(null);
        assertThat(profilnaSlika1).isNotEqualTo(profilnaSlika2);
    }
}
