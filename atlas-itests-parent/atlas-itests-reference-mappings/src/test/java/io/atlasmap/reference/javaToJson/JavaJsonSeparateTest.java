/**
 * Copyright (C) 2017 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atlasmap.reference.javaToJson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import org.junit.Test;
import io.atlasmap.api.AtlasContext;
import io.atlasmap.api.AtlasSession;
import io.atlasmap.java.test.BaseContact;
import io.atlasmap.java.test.SourceContact;
import io.atlasmap.json.test.AtlasJsonTestUnrootedMapper;
import io.atlasmap.reference.AtlasMappingBaseTest;
import io.atlasmap.reference.AtlasTestUtil;

public class JavaJsonSeparateTest extends AtlasMappingBaseTest {

    @Test
    public void testProcessSeparateSimple() throws Exception {
        AtlasContext context = atlasContextFactory
                .createContext(new File("src/test/resources/javaToJson/atlasmapping-separate-simple.xml").toURI());
        AtlasSession session = context.createSession();
        BaseContact sourceContact = AtlasTestUtil.generateContact(SourceContact.class);
        sourceContact.setFirstName("Ozzie Smith");
        sourceContact.setLastName(null);
        session.setInput(sourceContact);
        context.process(session);

        Object object = session.getOutput();
        assertNotNull(object);

        assertTrue(object instanceof String);
        AtlasJsonTestUnrootedMapper mapper = new AtlasJsonTestUnrootedMapper();
        io.atlasmap.json.test.TargetContact targetContact = mapper.readValue((String) object,
                io.atlasmap.json.test.TargetContact.class);
        AtlasTestUtil.validateJsonContact(targetContact);
        assertFalse(session.hasErrors());
    }

    @Test
    public void testProcessSeparateSkip() throws Exception {
        AtlasContext context = atlasContextFactory
                .createContext(new File("src/test/resources/javaToJson/atlasmapping-separate-skip.xml").toURI());
        AtlasSession session = context.createSession();
        BaseContact sourceContact = AtlasTestUtil.generateContact(SourceContact.class);
        sourceContact.setFirstName("Dr. Mr. Ozzie L. Smith Jr.");
        sourceContact.setLastName(null);
        session.setInput(sourceContact);
        context.process(session);

        Object object = session.getOutput();
        assertNotNull(object);
        assertTrue(object instanceof String);
        AtlasJsonTestUnrootedMapper mapper = new AtlasJsonTestUnrootedMapper();
        io.atlasmap.json.test.TargetContact targetContact = mapper.readValue((String) object,
                io.atlasmap.json.test.TargetContact.class);
        AtlasTestUtil.validateJsonContact(targetContact);
        assertFalse(session.hasErrors());
    }

    @Test
    public void testProcessSeparateOutOfOrder() throws Exception {
        AtlasContext context = atlasContextFactory
                .createContext(new File("src/test/resources/javaToJson/atlasmapping-separate-outoforder.xml").toURI());
        AtlasSession session = context.createSession();
        BaseContact sourceContact = AtlasTestUtil.generateContact(SourceContact.class);
        sourceContact.setFirstName("Dr. Mr. Ozzie L. Smith Jr.");
        sourceContact.setLastName(null);
        session.setInput(sourceContact);
        context.process(session);

        Object object = session.getOutput();
        assertNotNull(object);
        assertTrue(object instanceof String);
        AtlasJsonTestUnrootedMapper mapper = new AtlasJsonTestUnrootedMapper();
        io.atlasmap.json.test.TargetContact targetContact = mapper.readValue((String) object,
                io.atlasmap.json.test.TargetContact.class);
        AtlasTestUtil.validateJsonContact(targetContact);
        assertFalse(session.hasErrors());
    }

    @Test
    public void testProcessSeparateNotEnoughInput() throws Exception {
        AtlasContext context = atlasContextFactory
                .createContext(new File("src/test/resources/javaToJson/atlasmapping-separate-inputshort.xml").toURI());
        AtlasSession session = context.createSession();
        BaseContact sourceContact = AtlasTestUtil.generateContact(SourceContact.class);
        sourceContact.setFirstName("Dr. Mr. Ozzie");
        sourceContact.setLastName(null);
        session.setInput(sourceContact);
        context.process(session);

        Object object = session.getOutput();
        assertNotNull(object);

        assertTrue(object instanceof String);
        AtlasJsonTestUnrootedMapper mapper = new AtlasJsonTestUnrootedMapper();
        io.atlasmap.json.test.TargetContact targetContact = mapper.readValue((String) object,
                io.atlasmap.json.test.TargetContact.class);
        assertNotNull(targetContact);
        assertEquals("Ozzie", targetContact.getFirstName());
        assertEquals(null, targetContact.getLastName());
        assertTrue(session.hasErrors());
        assertEquals("Separate returned fewer segements count=3 when outputField.path=/lastName requested index=4",
                session.getAudits().getAudit().get(0).getMessage());
    }

    @Test
    public void testProcessSeparateNullInput() throws Exception {
        AtlasContext context = atlasContextFactory
                .createContext(new File("src/test/resources/javaToJson/atlasmapping-separate-inputnull.xml").toURI());
        AtlasSession session = context.createSession();
        BaseContact sourceContact = AtlasTestUtil.generateContact(SourceContact.class);
        sourceContact.setFirstName(null);
        sourceContact.setLastName(null);
        session.setInput(sourceContact);
        context.process(session);

        Object object = session.getOutput();
        assertNotNull(object);
        assertTrue(object instanceof String);
        AtlasJsonTestUnrootedMapper mapper = new AtlasJsonTestUnrootedMapper();
        io.atlasmap.json.test.TargetContact targetContact = mapper.readValue((String) object,
                io.atlasmap.json.test.TargetContact.class);
        assertFalse(session.hasErrors());

        assertEquals(null, targetContact.getFirstName());
        assertEquals(null, targetContact.getLastName());
        assertEquals("5551212", targetContact.getPhoneNumber());
        assertEquals("81111", targetContact.getZipCode());
        assertFalse(session.hasErrors());
    }
}
