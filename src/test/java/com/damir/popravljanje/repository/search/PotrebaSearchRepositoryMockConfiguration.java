package com.damir.popravljanje.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PotrebaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PotrebaSearchRepositoryMockConfiguration {

    @MockBean
    private PotrebaSearchRepository mockPotrebaSearchRepository;

}
