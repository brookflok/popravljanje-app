package com.damir.popravljanje.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.damir.popravljanje.web.rest.TestUtil;

public class MainSlikaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MainSlika.class);
        MainSlika mainSlika1 = new MainSlika();
        mainSlika1.setId(1L);
        MainSlika mainSlika2 = new MainSlika();
        mainSlika2.setId(mainSlika1.getId());
        assertThat(mainSlika1).isEqualTo(mainSlika2);
        mainSlika2.setId(2L);
        assertThat(mainSlika1).isNotEqualTo(mainSlika2);
        mainSlika1.setId(null);
        assertThat(mainSlika1).isNotEqualTo(mainSlika2);
    }
}
