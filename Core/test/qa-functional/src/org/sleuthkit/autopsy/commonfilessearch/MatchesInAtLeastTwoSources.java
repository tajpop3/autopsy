/*
 * 
 * Autopsy Forensic Browser
 * 
 * Copyright 2018 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.commonfilessearch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbTestCase;
import org.openide.util.Exceptions;
import org.python.icu.impl.Assert;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.casemodule.ImageDSProcessor;
import org.sleuthkit.autopsy.casemodule.NoCurrentCaseException;
import org.sleuthkit.autopsy.commonfilesearch.AllDataSourcesCommonFilesAlgorithm;
import org.sleuthkit.autopsy.commonfilesearch.CommonFilesMetadata;
import org.sleuthkit.autopsy.commonfilesearch.CommonFilesMetadataBuilder;
import static org.sleuthkit.autopsy.commonfilessearch.IntraCaseUtils.*;
import org.sleuthkit.autopsy.ingest.IngestJobSettings;
import org.sleuthkit.autopsy.ingest.IngestModuleTemplate;
import org.sleuthkit.autopsy.modules.filetypeid.FileTypeIdModuleFactory;
import org.sleuthkit.autopsy.modules.hashdatabase.HashLookupModuleFactory;
import org.sleuthkit.autopsy.testutils.IngestUtils;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.TskCoreException;

/**
 * Ensures that matches only are found for files which appear in at least two
 * data sources.
 *
 * The two datasources used here have no common files. One of the data sources
 * has two identical files within it. This should not count as a match.
 *
 * None of the test files should be found in the results of this test.
 */
public class MatchesInAtLeastTwoSources extends NbTestCase {

    public static Test suite() {
        NbModuleSuite.Configuration conf = NbModuleSuite.createConfiguration(MatchesInAtLeastTwoSources.class).
                clusters(".*").
                enableModules(".*");
        return conf.suite();
    }

    private final IntraCaseUtils utils;

    public MatchesInAtLeastTwoSources(String name) {
        super(name);

        this.utils = new IntraCaseUtils(this, "MatchesInAtLeastTwoSources");
    }

    @Override
    public void setUp() {
        this.utils.createAsCurrentCase();

        final ImageDSProcessor imageDSProcessor = new ImageDSProcessor();

        this.utils.addImageOne(imageDSProcessor);
        this.utils.addImageFour(imageDSProcessor);

        IngestModuleTemplate hashLookupTemplate = IngestUtils.getIngestModuleTemplate(new HashLookupModuleFactory());
        IngestModuleTemplate mimeTypeLookupTemplate = IngestUtils.getIngestModuleTemplate(new FileTypeIdModuleFactory());

        ArrayList<IngestModuleTemplate> templates = new ArrayList<>();
        templates.add(hashLookupTemplate);
        templates.add(mimeTypeLookupTemplate);

        IngestJobSettings ingestJobSettings = new IngestJobSettings(IngestedWithHashAndFileType.class.getCanonicalName(), IngestJobSettings.IngestType.FILES_ONLY, templates);

        try {
            IngestUtils.runIngestJob(Case.getCurrentCaseThrows().getDataSources(), ingestJobSettings);
        } catch (NoCurrentCaseException | TskCoreException ex) {
            Exceptions.printStackTrace(ex);
            Assert.fail(ex);
        }
    }

    @Override
    public void tearDown() {
        this.utils.tearDown();
    }

    public void testOne() {
        try {
            Map<Long, String> dataSources = this.utils.getDataSourceMap();

            CommonFilesMetadataBuilder allSourcesBuilder = new AllDataSourcesCommonFilesAlgorithm(dataSources, false, false);
            CommonFilesMetadata metadata = allSourcesBuilder.findCommonFiles();

            Map<Long, String> objectIdToDataSource = IntraCaseUtils.mapFileInstancesToDataSources(metadata);

            List<AbstractFile> files = IntraCaseUtils.getFiles(objectIdToDataSource.keySet());

            assertTrue(IntraCaseUtils.verifyFileExistanceAndCount(files, dataSources, IMG, SET1, 0));
            assertTrue(IntraCaseUtils.verifyFileExistanceAndCount(files, dataSources, IMG, SET4, 0));

            assertTrue(IntraCaseUtils.verifyFileExistanceAndCount(files, dataSources, DOC, SET1, 0));
            assertTrue(IntraCaseUtils.verifyFileExistanceAndCount(files, dataSources, DOC, SET4, 0));

            assertTrue(IntraCaseUtils.verifyFileExistanceAndCount(files, dataSources, EMPTY, SET1, 0));
            assertTrue(IntraCaseUtils.verifyFileExistanceAndCount(files, dataSources, EMPTY, SET4, 0));

        } catch (NoCurrentCaseException | TskCoreException | SQLException ex) {
            Exceptions.printStackTrace(ex);
            Assert.fail(ex);
        }
    }
}
