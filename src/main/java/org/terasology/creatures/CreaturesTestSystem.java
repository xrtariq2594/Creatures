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
import org.terasology.anatomy.AnatomyComponent;
import org.terasology.anatomy.AnatomyPartComponent;
import org.terasology.anatomy.events.DoAnatomyDamageEvent;
import org.terasology.anatomy.events.DoAnatomyDeadEvent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.characters.CharacterComponent;
import org.terasology.logic.delay.DelayManager;
import org.terasology.logic.health.DoDamageEvent;
import org.terasology.logic.health.HealthComponent;
import org.terasology.registry.In;
import org.terasology.utilities.random.FastRandom;
import org.terasology.utilities.random.Random;

/**
 * A test class for testing the creatures.
 */
@RegisterSystem
public class CreaturesTestSystem extends BaseComponentSystem {
    private static final Logger logger = LoggerFactory.getLogger(CreaturesTestSystem.class);

    @In
    private EntityManager entityManager;

    @In
    private DelayManager delayManager;

    // USED only for TESTING, with the assumption that ONLY the local player has the AnatomyComponent attached.
    @In
    private EntityRef playerRef;

    // Create the creature's anatomy parts.
    @ReceiveEvent(components = {CharacterComponent.class, HealthComponent.class, AnatomyComponent.class})
    public void onCreate(OnCreatureSpawnedEvent event, EntityRef entity, AnatomyComponent anatomy) {
        createAnatomyParts(entity, anatomy);
    }

    // Randomly damage one of the anatomical parts of this creature.
    @ReceiveEvent(components = {CharacterComponent.class, HealthComponent.class, AnatomyComponent.class})
    public void onDamage(DoDamageEvent event, EntityRef entity, AnatomyComponent anatomy) {
        logger.info(entity.toString() + " has taken " + event.getAmount() + " points of damage!");

        // If the anatomy parts don't exist yet, add them to the creature.
        if (anatomy.aParts.size() == 0) {
            createAnatomyParts(entity, anatomy);
        }

        // Apply damage to one of the creature's anatomical parts.
        FastRandom random = new FastRandom();
        anatomy.aParts.get(random.nextInt(0, anatomy.aParts.size() - 1)).send(new DoAnatomyDamageEvent(event.getAmount()));
    }

    private void createAnatomyParts(EntityRef entity, AnatomyComponent anatomy) {
        for (int i = 0; i < anatomy.aPrefabNames.size(); i++) {
            anatomy.aParts.add(entityManager.create(anatomy.aPrefabNames.get(i)));
        }

        entity.saveComponent(anatomy);
    }
}
