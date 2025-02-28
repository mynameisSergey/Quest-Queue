package service;

import org.junit.jupiter.api.BeforeEach;

import static service.Managers.getDefaultInMemory;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        manager = getDefaultInMemory();
    }

}




