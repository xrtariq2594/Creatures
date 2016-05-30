/*
 * Copyright 2016 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.creatures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.registry.In;
import org.terasology.world.block.BlockComponent;

/*
 * Based on Portals module.
 */
@RegisterSystem(RegisterMode.AUTHORITY)
public class CreatureSpawnerSystem extends BaseComponentSystem {

    private static final Logger logger = LoggerFactory.getLogger(CreatureSpawnerSystem.class);

    @In
    private EntityManager entityManager;

    @Override
    public void initialise() {
    }

    @Override
    public void shutdown() {
    }

    @ReceiveEvent(components = {CreatureSpawnerComponent.class})
    public void onActivate(ActivateEvent event, EntityRef entity) {
        logger.info("Activating CreatureSpawnerSystem!");
        // Not sure if this will be needed, but handy to remember how it is done
        CreatureSpawnerComponent portal = entity.getComponent(CreatureSpawnerComponent.class);
        BlockComponent block = entity.getComponent(BlockComponent.class);

        EntityRef ref = entityManager.create("WildAnimals:Deer");
        ref.send(new OnCreatureSpawnedEvent());
    }
}
