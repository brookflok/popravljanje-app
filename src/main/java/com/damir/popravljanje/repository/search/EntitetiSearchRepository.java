package com.damir.popravljanje.repository.search;

import com.damir.popravljanje.domain.Entiteti;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Entiteti} entity.
 */
public interface EntitetiSearchRepository extends ElasticsearchRepository<Entiteti, Long> {
}
