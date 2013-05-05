/*
 * Copyright (C) Qatar Computing Research Institute, 2013.
 * All rights reserved.
 */

package qa.qcri.nadeef.test.core;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import qa.qcri.nadeef.core.datamodel.Cell;
import qa.qcri.nadeef.core.datamodel.Fix;
import qa.qcri.nadeef.core.datamodel.Violation;
import qa.qcri.nadeef.core.operator.FixDecisionMaker;
import qa.qcri.nadeef.test.TestDataRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Test for FixDecisionMaker.
 */
public class FixDecisionMakerTest {
    private List<Fix> loadFix(File path) throws IOException {
        List<Fix> result = Lists.newArrayList();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = reader.readLine();
        Fix.Builder fixBuilder = new Fix.Builder().vid(Violation.UnknownId);
        Cell.Builder cellBuilder = new Cell.Builder();
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(";");
            Cell leftCell = cellBuilder.tid(Integer.parseInt(tokens[0]))
                .column(tokens[1])
                .value(tokens[2])
                .build();
            Cell rightCell = cellBuilder.tid(Integer.parseInt(tokens[3]))
                .column(tokens[4])
                .value(tokens[5])
                .build();
            Fix fix = fixBuilder.left(leftCell).right(rightCell).build();
            result.add(fix);
        }
        return result;
    }

    @Test
    public void test1() {
        try {
            List<Fix> fixes = loadFix(TestDataRepository.getFixTestData1());
            FixDecisionMaker eq = new FixDecisionMaker();
            Collection<Fix> result = eq.execute(fixes);
            Assert.assertEquals(3, result.size());
            for (Fix fix : result) {
                Cell left = fix.getLeft();
                String value = fix.getRightValue();
                switch (left.getTupleId()) {
                    case 2:
                    case 1:
                        Assert.assertEquals("a3", value);
                        break;
                    case 6:
                        Assert.assertEquals("b1", value);
                        break;
                    default:
                        Assert.fail("Incorrect changed tuple.");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
}
