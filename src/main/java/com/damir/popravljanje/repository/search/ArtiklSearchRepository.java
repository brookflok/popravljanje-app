package com.damir.popravljanje.repository.search;

import com.damir.popravljanje.domain.Artikl;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Artikl} entity.
 */
public interface ArtiklSearchRepository extends ElasticsearchRepository<Artikl, Long> {
}
