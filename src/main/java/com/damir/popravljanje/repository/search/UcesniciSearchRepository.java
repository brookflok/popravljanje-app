package com.damir.popravljanje.repository.search;

import com.damir.popravljanje.domain.Ucesnici;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Ucesnici} entity.
 */
public interface UcesniciSearchRepository extends ElasticsearchRepository<Ucesnici, Long> {
}
