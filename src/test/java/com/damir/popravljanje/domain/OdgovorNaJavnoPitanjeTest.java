package com.damir.popravljanje.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.damir.popravljanje.web.rest.TestUtil;

public class OdgovorNaJavnoPitanjeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OdgovorNaJavnoPitanje.class);
        OdgovorNaJavnoPitanje odgovorNaJavnoPitanje1 = new OdgovorNaJavnoPitanje();
        odgovorNaJavnoPitanje1.setId(1L);
        OdgovorNaJavnoPitanje odgovorNaJavnoPitanje2 = new OdgovorNaJavnoPitanje();
        odgovorNaJavnoPitanje2.setId(odgovorNaJavnoPitanje1.getId());
        assertThat(odgovorNaJavnoPitanje1).isEqualTo(odgovorNaJavnoPitanje2);
        odgovorNaJavnoPitanje2.setId(2L);
        assertThat(odgovorNaJavnoPitanje1).isNotEqualTo(odgovorNaJavnoPitanje2);
        odgovorNaJavnoPitanje1.setId(null);
        assertThat(odgovorNaJavnoPitanje1).isNotEqualTo(odgovorNaJavnoPitanje2);
    }
}
