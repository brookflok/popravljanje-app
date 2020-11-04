package com.damir.popravljanje.repository.search;

import com.damir.popravljanje.domain.Galerija;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Galerija} entity.
 */
public interface GalerijaSearchRepository extends ElasticsearchRepository<Galerija, Long> {
}
