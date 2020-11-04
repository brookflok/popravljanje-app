package com.damir.popravljanje.repository.search;

import com.damir.popravljanje.domain.JavnoPitanje;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link JavnoPitanje} entity.
 */
public interface JavnoPitanjeSearchRepository extends ElasticsearchRepository<JavnoPitanje, Long> {
}
