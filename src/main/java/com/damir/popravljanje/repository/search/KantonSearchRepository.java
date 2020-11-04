package com.damir.popravljanje.repository.search;

import com.damir.popravljanje.domain.Kanton;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Kanton} entity.
 */
public interface KantonSearchRepository extends ElasticsearchRepository<Kanton, Long> {
}
