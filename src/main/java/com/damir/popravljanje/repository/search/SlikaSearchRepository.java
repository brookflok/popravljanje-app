package com.damir.popravljanje.repository.search;

import com.damir.popravljanje.domain.Slika;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Slika} entity.
 */
public interface SlikaSearchRepository extends ElasticsearchRepository<Slika, Long> {
}
