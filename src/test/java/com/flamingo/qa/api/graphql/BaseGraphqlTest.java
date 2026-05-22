package com.flamingo.qa.api.graphql;

import com.flamingo.qa.client.HygraphApiClient;
import com.flamingo.qa.support.TestInjector;
import com.flamingo.qa.support.AllureListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

@Tag("api")
@Tag("graphql")
@ExtendWith(AllureListener.class)
public abstract class BaseGraphqlTest extends HygraphApiClient {

    @BeforeEach
    void injectDependencies() {
        TestInjector.injectMembers(this);
    }
}
