package com.damir.popravljanje.repository.search;

import com.damir.popravljanje.domain.Usluga;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Usluga} entity.
 */
public interface UslugaSearchRepository extends ElasticsearchRepository<Usluga, Long> {
}
