package com.damir.popravljanje.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.damir.popravljanje.web.rest.TestUtil;

public class JavnoPitanjeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JavnoPitanje.class);
        JavnoPitanje javnoPitanje1 = new JavnoPitanje();
        javnoPitanje1.setId(1L);
        JavnoPitanje javnoPitanje2 = new JavnoPitanje();
        javnoPitanje2.setId(javnoPitanje1.getId());
        assertThat(javnoPitanje1).isEqualTo(javnoPitanje2);
        javnoPitanje2.setId(2L);
        assertThat(javnoPitanje1).isNotEqualTo(javnoPitanje2);
        javnoPitanje1.setId(null);
        assertThat(javnoPitanje1).isNotEqualTo(javnoPitanje2);
    }
}
