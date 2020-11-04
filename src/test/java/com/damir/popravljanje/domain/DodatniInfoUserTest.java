package com.damir.popravljanje.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.damir.popravljanje.web.rest.TestUtil;

public class DodatniInfoUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DodatniInfoUser.class);
        DodatniInfoUser dodatniInfoUser1 = new DodatniInfoUser();
        dodatniInfoUser1.setId(1L);
        DodatniInfoUser dodatniInfoUser2 = new DodatniInfoUser();
        dodatniInfoUser2.setId(dodatniInfoUser1.getId());
        assertThat(dodatniInfoUser1).isEqualTo(dodatniInfoUser2);
        dodatniInfoUser2.setId(2L);
        assertThat(dodatniInfoUser1).isNotEqualTo(dodatniInfoUser2);
        dodatniInfoUser1.setId(null);
        assertThat(dodatniInfoUser1).isNotEqualTo(dodatniInfoUser2);
    }
}
