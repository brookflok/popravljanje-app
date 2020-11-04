package com.damir.popravljanje.repository.search;

import com.damir.popravljanje.domain.Lokacija;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Lokacija} entity.
 */
public interface LokacijaSearchRepository extends ElasticsearchRepository<Lokacija, Long> {
}
